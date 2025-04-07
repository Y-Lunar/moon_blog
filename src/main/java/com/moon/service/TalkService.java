package com.moon.service;

import com.moon.entity.Talk;
import com.baomidou.mybatisplus.extension.service.IService;
import com.moon.entity.dto.ConditionDto;
import com.moon.entity.dto.TalkDto;
import com.moon.entity.vo.PageResult;
import com.moon.entity.vo.TalkBackInfoVo;
import com.moon.entity.vo.TalkBackVo;
import com.moon.entity.vo.TalkVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Y.0
* @description 针对表【t_talk】的数据库操作Service
* @createDate 2023-10-08 16:19:12
*/
public interface TalkService extends IService<Talk> {

    /**
     * 博客主界面说说
     * @return
     */
    List<String> talkList();

    /**
     * 查看说说列表
     *
     * @return
     */
    PageResult<TalkVo> talkPageList();

    /**
     * 根据talkId查询出对应的说说信息
     *
     * @param talkId
     * @return
     */
    TalkVo getTalkById(Integer talkId);

    /**
     * 查看后台说说列表
     *
     * @param condition 条件
     * @return 后台说说
     */
    PageResult<TalkBackVo> talkListBackVo(ConditionDto condition);

    /**
     * 上传说说图片
     *
     * @param file 文件
     * @return
     */
    String uploadTalkCover(MultipartFile file);

    /**
     * 添加说说
     *
     * @param talk 说说信息
     * @return
     */
    void addTalk(TalkDto talk);

    /**
     * 删除说说
     *
     * @param talkId 说说id
     * @return
     */
    void deleteTalk(Integer talkId);

    /**
     * 修改说说
     *
     * @param talk 说说信息
     * @return
     */
    void updateTalk(TalkDto talk);

    /**
     * 编辑说说
     *
     * @param talkId 说说id
     * @return
     */
    TalkBackInfoVo editTalk(Integer talkId);
}
