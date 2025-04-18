package com.moon.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.*;
import com.moon.entity.dto.CheckDto;
import com.moon.entity.dto.CommentDto;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.MailDto;
import com.moon.entity.vo.*;
import com.moon.mapper.*;
import com.moon.service.CommentService;
import com.moon.service.RedisService;
import com.moon.service.SiteConfigService;
import com.moon.utils.HTMLUtils;
import com.moon.utils.PageUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.moon.constant.CommonConstant.*;
import static com.moon.constant.MqConstant.*;
import static com.moon.constant.RedisConstant.COMMENT_LIKE_COUNT;
import static com.moon.enums.CommentTypeEnum.*;

/**
* @author Y.0
* @description 针对表【t_comment】的数据库操作Service实现
* @createDate 2023-10-12 10:41:59
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Value("${blog.url}")
    private String websiteUrl;

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired
    private RedisService redisService;

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private ArticleMapper articleMapper;

    @Autowired(required = false)
    private TalkMapper talkMapper;

    @Autowired(required = false)
    private SiteConfigService siteConfigService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 查看最新评论（top 5）
     *
     * @return
     */
    @Override
    public List<RecentCommentVo> recentCommentList() {
        return commentMapper.selectRecentCommList();
    }

    /**
     * 查看回复评论
     *
     * @param commentId 评论id
     * @return
     */
    @Override
    public List<RelyVo> listResultById(Integer commentId) {
        List<RelyVo> relyVos= commentMapper.selectReplyByPid(PageUtils.getLimit(),PageUtils.getSize(),commentId);
        // 子评论点赞Map
        Map<String, Integer> likeCountMap = redisService.getHashAll(COMMENT_LIKE_COUNT);
        relyVos.forEach(item -> item.setLikeCount(likeCountMap.get(item.getId().toString())));
        return relyVos;
    }

    /**
     * 查看评论
     *
     * @param condition
     * @return
     */
    @Override
    public PageResult<CommentVo> CommentVoList(ConditionDto condition) {
        // 查询父评论数量
        Long count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.nonNull(condition.getTypeId()), Comment::getTypeId, condition.getTypeId())
                .eq(Comment::getCommentType, condition.getCommentType())
                .eq(Comment::getIsCheck, TRUE).isNull(Comment::getParentId));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询父评论
        List<CommentVo> commentVOList = commentMapper.selectParentComment(PageUtils.getLimit(), PageUtils.getSize(), condition);
        if (CollectionUtils.isEmpty(commentVOList)) {
            return new PageResult<>();
        }
        // 评论点赞
        Map<String, Integer> likeCountMap = redisService.getHashAll(COMMENT_LIKE_COUNT);
        // 父评论id集合
        List<Integer> parentCommentIdList = commentVOList.stream().map(CommentVo::getId).collect(Collectors.toList());
        // 分组查询每组父评论下的子评论前三条
        List<RelyVo> replyVOList = commentMapper.selectReplyByParentIdList(parentCommentIdList);
        // 封装子评论点赞量
        replyVOList.forEach(item -> item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0)));
        // 根据父评论id生成对应子评论的Map
        Map<Integer, List<RelyVo>> replyMap = replyVOList.stream().collect(Collectors.groupingBy(RelyVo::getParentId));
        // 父评论的回复数量
        List<ReplyCountVo> replyCountList = commentMapper.selectReplyCountByParentId(parentCommentIdList);
        // 转换Map
        Map<Integer, Integer> replyCountMap = replyCountList.stream().collect(Collectors.toMap(ReplyCountVo::getCommentId, ReplyCountVo::getReplyCount));
        // 封装评论数据
        commentVOList.forEach(item -> {
            item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0));
            item.setReplyVOList(replyMap.get(item.getId()));
            item.setReplyCount(Optional.ofNullable(replyCountMap.get(item.getId())).orElse(0));
        });
        return new PageResult<>(commentVOList, count);
    }

    /**
     * 查看后台评论列表
     *
     * @param condition 条件
     * @return 后台评论
     */
    @Override
    public PageResult<CommentBackVo> commentListVo(ConditionDto condition) {
        // 查询后台评论数量
        Long count = commentMapper.countComment(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台评论集合
        List<CommentBackVo> commentBackVOList = commentMapper.commentListVo(PageUtils.getLimit(),
                PageUtils.getSize(), condition);
        return new PageResult<>(commentBackVOList, count);
    }

    /**
     * 添加评论
     *
     * @param comment 评论信息
     * @return
     */
    @Override
    public void addComment(CommentDto comment) {
        // 校验评论参数
        verifyComment(comment);
        SiteConfig siteConfig = siteConfigService.getSiteConfig();
        Integer commentCheck = siteConfig.getCommentCheck();
        // 过滤标签
        comment.setCommentContent(HTMLUtils.filter(comment.getCommentContent()));
        Comment newComment = Comment.builder()
                .fromUid(StpUtil.getLoginIdAsInt())
                .toUid(comment.getToUid())
                .typeId(comment.getTypeId())
                .commentType(comment.getCommentType())
                .parentId(comment.getParentId())
                .replyId(comment.getReplyId())
                .commentContent(comment.getCommentContent())
                .isCheck(commentCheck.equals(FALSE) ? TRUE : FALSE)
                .build();
        // 保存评论
        commentMapper.insert(newComment);
        // 查询评论用户昵称
        String fromNickname = userMapper.selectOne(new LambdaQueryWrapper<User>()
                        .select(User::getNickname)
                        .eq(User::getId, StpUtil.getLoginIdAsInt()))
                .getNickname();
        // 通知用户
        if (siteConfig.getEmailNotice().equals(TRUE)) {
            CompletableFuture.runAsync(() -> {
                System.out.println("你好");
                notice(newComment, fromNickname);
            });
        }
    }

    /**
     * 审核评论
     *
     * @param check 审核信息
     * @return
     */
    @Override
    public void updateCommentCheck(CheckDto check) {
        // 修改评论审核状态
        List<Comment> commentList = check.getIdList().stream().map(id -> Comment.builder().id(id).isCheck(check.getIsCheck()).build()).collect(Collectors.toList());
        this.updateBatchById(commentList);
    }

    /**
     * 评论通知
     *
     * @param comment      评论
     * @param fromNickname 评论用户昵称
     */
    private void notice(Comment comment, String fromNickname) {
        // 自己回复自己不用提醒
        if (comment.getFromUid().equals(comment.getToUid())) {
            return;
        }
        // 邮件标题
        String title = "友链";
        // 回复用户id
        Integer toUid = BLOGGER_ID;
        // 父评论
        if (Objects.isNull(comment.getParentId())) {
            if (comment.getCommentType().equals(ARTICLE.getType())) {
                Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                        .select(Article::getArticleTitle, Article::getUserId)
                        .eq(Article::getId, comment.getTypeId()));
                title = article.getArticleTitle();
                toUid = article.getUserId();
            }
            if (comment.getCommentType().equals(TALK.getType())) {
                title = "说说";
                toUid = talkMapper.selectOne(new LambdaQueryWrapper<Talk>()
                                .select(Talk::getUserId)
                                .eq(Talk::getId, comment.getTypeId()))
                        .getUserId();
            }
            // 自己评论自己的作品，不用提醒
            if(comment.getFromUid().equals(toUid)){
                return;
            }
        } else {
            // 子评论
            toUid = comment.getToUid();
            if (comment.getCommentType().equals(ARTICLE.getType())) {
                title = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                                .select(Article::getArticleTitle)
                                .eq(Article::getId, comment.getTypeId()))
                        .getArticleTitle();
            }
            if (comment.getCommentType().equals(TALK.getType())) {
                title = "说说";
            }
        }
        // 查询回复用户邮箱、昵称、id
        User toUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getEmail, User::getNickname)
                .eq(User::getId, toUid));
        // 邮箱不为空
        if (StringUtils.hasText(toUser.getEmail())) {
            sendEmail(comment, toUser, title, fromNickname);
        }
    }

    /**
     * 发送邮箱
     *
     * @param comment 评论
     * @param toUser  回复用户信息
     * @param title   标题
     * @param fromNickname 评论用户昵称
     */
    private void sendEmail(Comment comment, User toUser, String title, String fromNickname) {
        MailDto mailDTO = new MailDto();
        if (comment.getIsCheck().equals(TRUE)) {
            Map<String, Object> contentMap = new HashMap<>(7);
            // 评论链接
            String typeId = Optional.ofNullable(comment.getTypeId())
                    .map(Object::toString)
                    .orElse("");
            String url = websiteUrl + getCommentPath(comment.getCommentType()) + typeId;
            // 父评论则提醒作者
            if (Objects.isNull(comment.getParentId())) {
                mailDTO.setToEmail(toUser.getEmail());
                mailDTO.setSubject(COMMENT_REMIND);
                mailDTO.setTemplate(AUTHOR_TEMPLATE);
                String createTime = DateUtil.formatLocalDateTime(comment.getCreateTime());
                contentMap.put("time", createTime);
                contentMap.put("url", url);
                contentMap.put("title", title);
                contentMap.put("nickname", fromNickname);
                contentMap.put("content", comment.getCommentContent());
                mailDTO.setContentMap(contentMap);
            } else {
                // 子评论则回复的是用户提醒该用户
                Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>()
                        .select(Comment::getCommentContent, Comment::getCreateTime)
                        .eq(Comment::getId, comment.getReplyId()));
                mailDTO.setToEmail(toUser.getEmail());
                mailDTO.setSubject(COMMENT_REMIND);
                mailDTO.setTemplate(USER_TEMPLATE);
                contentMap.put("url", url);
                contentMap.put("title", title);
                String createTime = DateUtil.formatLocalDateTime(comment.getCreateTime());
                contentMap.put("time", createTime);
                // 被回复用户昵称
                contentMap.put("toUser", toUser.getNickname());
                // 评论用户昵称
                contentMap.put("fromUser", fromNickname);
                // 被回复的评论内容
                contentMap.put("parentComment", parentComment.getCommentContent());
                // 回复评论内容
                contentMap.put("replyComment", comment.getCommentContent());
                mailDTO.setContentMap(contentMap);
            }
            // 发送HTML邮件
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, EMAIL_HTML_KEY, mailDTO);
        } else {
            // 审核提醒
            String adminEmail = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .select(User::getEmail)
                    .eq(User::getId, BLOGGER_ID)).getEmail();
            mailDTO.setToEmail(adminEmail);
            mailDTO.setSubject(CHECK_REMIND);
            mailDTO.setContent("您收到一条新的回复，请前往后台管理页面审核");
            // 发送普通邮件
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, EMAIL_SIMPLE_KEY, mailDTO);
        }
    }

    private void verifyComment(CommentDto comment) {
        if (comment.getCommentType().equals(ARTICLE.getType())) {
            Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().select(Article::getId).eq(Article::getId, comment.getTypeId()));
            Assert.notNull(article, "文章不存在");
        }
        if (comment.getCommentType().equals(TALK.getType())) {
            Talk talk = talkMapper.selectOne(new LambdaQueryWrapper<Talk>().select(Talk::getId).eq(Talk::getId, comment.getTypeId()));
            Assert.notNull(talk, "说说不存在");
        }
        // 评论为子评论，判断回复的评论和用户是否存在
        Optional.ofNullable(comment.getParentId()).ifPresent(parentId -> {
            // 判断父评论是否存在
            Comment parentComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getParentId, Comment::getCommentType).eq(Comment::getId, parentId));
            Assert.notNull(parentComment, "父评论不存在");
            Assert.isNull(parentComment.getParentId(), "当前评论为子评论，不能作为父评论");
            Assert.isTrue(comment.getCommentType().equals(parentComment.getCommentType()), "只能以同类型的评论作为父评论");
            // 判断回复的评论和用户是否存在
            Comment replyComment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>().select(Comment::getId, Comment::getParentId, Comment::getFromUid, Comment::getCommentType).eq(Comment::getId, comment.getReplyId()));
            User toUser = userMapper.selectOne(new LambdaQueryWrapper<User>().select(User::getId).eq(User::getId, comment.getToUid()));
            Assert.notNull(replyComment, "回复的评论不存在");
            Assert.notNull(toUser, "回复的用户不存在");
            Assert.isTrue(comment.getCommentType().equals(replyComment.getCommentType()), "只能回复同类型的下的评论");
            if (Objects.nonNull(replyComment.getParentId())) {
                Assert.isTrue(replyComment.getParentId().equals(parentId), "提交的评论parentId与当前回复评论parentId不一致");
            }
            Assert.isTrue(replyComment.getFromUid().equals(comment.getToUid()), "提交的评论toUid与当前回复评论fromUid不一致");
            // 只能回复当前父评论及其子评论
            List<Integer> replyIdList = commentMapper.selectCommentIdByParentId(parentId);
            replyIdList.add(parentId);
            Assert.isTrue(replyIdList.contains(parentId), "当前父评论下不存在该子评论");
        });
    }
}




