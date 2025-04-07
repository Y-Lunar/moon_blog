package com.moon.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.moon.entity.BlogFile;
import com.moon.entity.Talk;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.TalkDto;
import com.moon.entity.vo.*;
import com.moon.enums.FilePathEnum;
import com.moon.mapper.BlogFileMapper;
import com.moon.mapper.CommentMapper;
import com.moon.mapper.TagMapper;
import com.moon.service.RedisService;
import com.moon.service.TalkService;
import com.moon.mapper.TalkMapper;
import com.moon.strategy.context.UploadStrategyContext;
import com.moon.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.moon.constant.CommonConstant.FALSE;
import static com.moon.constant.RedisConstant.TALK_LIKE_COUNT;
import static com.moon.enums.ArticleStatusEnum.PUBLIC;
import static com.moon.enums.CommentTypeEnum.TALK;

/**
* @author Y.0
* @description 针对表【t_talk】的数据库操作Service实现
* @createDate 2023-10-08 16:19:12
*/
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk>
    implements TalkService{

    @Autowired(required = false)
    private TalkMapper talkMapper;

    @Autowired(required = false)
    private CommentMapper commentMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Autowired(required = false)
    private BlogFileMapper blogFileMapper;
    /**
     * 博客主界面说说
     * @return
     */
    @Override
    public List<String> talkList() {
        List<Talk> talkList = talkMapper.selectList(new LambdaQueryWrapper<Talk>()
                .select(Talk::getTalkContent)
                .eq(Talk::getStatus, PUBLIC.getStatus())
                .orderByDesc(Talk::getIsTop)
                .orderByDesc(Talk::getId)
                .last("limit 5"));
        return talkList.stream().map(talk -> talk.getTalkContent().length() > 200
                        ? HTMLUtils.deleteHtmlTag(talk.getTalkContent().substring(0, 200))
                        : HTMLUtils.deleteHtmlTag(talk.getTalkContent()))
                .collect(Collectors.toList());
    }

    /**
     * 查看说说列表
     * @return
     */
    @Override
    public PageResult<TalkVo> talkPageList() {
        // 查询说说总量
        Long count = talkMapper.selectCount((new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, PUBLIC.getStatus())));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说
        List<TalkVo> talkVOList = talkMapper.selectTalkList(PageUtils.getLimit(), PageUtils.getSize());
        // 查询说说评论量
        List<Integer> talkIdList = talkVOList.stream()
                .map(TalkVo::getId)
                .collect(Collectors.toList());
        List<CommentCountVo> commentCountVOList = commentMapper.selectCommentCountByTypeId(talkIdList, TALK.getType());
        Map<Integer, Integer> commentCountMap = commentCountVOList.stream()
                .collect(Collectors.toMap(CommentCountVo::getId, CommentCountVo::getCommentCount));
        // 查询说说点赞量
        Map<String, Integer> likeCountMap = redisService.getHashAll(TALK_LIKE_COUNT);
        // 封装说说
        talkVOList.forEach(item -> {
            item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0));
            item.setCommentCount(Optional.ofNullable(commentCountMap.get(item.getId())).orElse(0));
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkVOList, count);
    }

    /**
     * 根据id查询出对应的说说id
     *
     * @param talkId
     * @return
     */
    @Override
    public TalkVo getTalkById(Integer talkId) {
        TalkVo talkVo = talkMapper.selectTalkById(talkId);
        if (Objects.isNull(talkVo)){
            return null;
        }
        // 查询说说点赞量
        Integer likeCount = redisService.getHash(TALK_LIKE_COUNT, talkId.toString());
        talkVo.setLikeCount(Optional.ofNullable(likeCount).orElse(0));
        // 转换图片格式
        if (Objects.nonNull(talkVo.getImages())) {
            talkVo.setImgList(CommonUtils.castList(JSON.parseObject(talkVo.getImages(), List.class), String.class));
        }
        return talkVo;
    }

    /**
     * 查看后台说说列表
     *
     * @param condition 条件
     * @return 后台说说
     */
    @Override
    public PageResult<TalkBackVo> talkListBackVo(ConditionDto condition) {
        // 查询说说数量
        Long count = talkMapper.selectCount(new LambdaQueryWrapper<Talk>()
                .eq(Objects.nonNull(condition.getStatus()), Talk::getStatus, condition.getStatus()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询说说列表
        List<TalkBackVo> talkBackVOList = talkMapper.selectTalkBackVo(PageUtils.getLimit(), PageUtils.getSize(), condition.getStatus());
        talkBackVOList.forEach(item -> {
            // 转换图片格式
            if (Objects.nonNull(item.getImages())) {
                item.setImgList(CommonUtils.castList(JSON.parseObject(item.getImages(), List.class), String.class));
            }
        });
        return new PageResult<>(talkBackVOList, count);
    }

    /**
     * 上传说说图片
     *
     * @param file 文件
     * @return
     */
    @Override
    public String uploadTalkCover(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.TALK.getPath());
        try {
            // 获取文件md5值
            String md5 = FileUtils.getMd5(file.getInputStream());
            // 获取文件扩展名
            String extName = FileUtils.getExtension(file);
            BlogFile existFile = blogFileMapper.selectOne(new LambdaQueryWrapper<BlogFile>()
                    .select(BlogFile::getId)
                    .eq(BlogFile::getFileName, md5)
                    .eq(BlogFile::getFilePath, FilePathEnum.TALK.getFilePath()));
            if (Objects.isNull(existFile)) {
                // 保存文件信息
                BlogFile newFile = BlogFile.builder()
                        .fileUrl(url)
                        .fileName(md5)
                        .filePath(FilePathEnum.TALK.getFilePath())
                        .extendName(extName)
                        .fileSize((int) file.getSize())
                        .isDir(FALSE)
                        .build();
                blogFileMapper.insert(newFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 添加说说
     *
     * @param talk 说说信息
     * @return
     */
    @Override
    public void addTalk(TalkDto talk) {
        Talk newTalk = BeanCopyUtils.copyBean(talk, Talk.class);
        newTalk.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.insert(newTalk);
    }

    /**
     * 删除说说
     *
     * @param talkId 说说id
     * @return
     */
    @Override
    public void deleteTalk(Integer talkId) {
        talkMapper.deleteById(talkId);
    }

    /**
     * 修改说说
     *
     * @param talk 说说信息
     * @return
     */
    @Override
    public void updateTalk(TalkDto talk) {
        Talk newTalk = BeanCopyUtils.copyBean(talk, Talk.class);
        newTalk.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.updateById(newTalk);
    }

    /**
     * 编辑说说
     *
     * @param talkId 说说id
     * @return
     */
    @Override
    public TalkBackInfoVo editTalk(Integer talkId) {
        TalkBackInfoVo talkBackVO = talkMapper.selectTalkBackById(talkId);
        // 转换图片格式
        if (Objects.nonNull(talkBackVO.getImages())) {
            talkBackVO.setImgList(CommonUtils.castList(JSON.parseObject(talkBackVO.getImages(), List.class), String.class));
        }
        return talkBackVO;
    }
}




