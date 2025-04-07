package com.moon.validator;

import com.moon.entity.dto.CommentDto;
import com.moon.validator.groups.ArticleTalk;
import com.moon.validator.groups.Link;
import com.moon.validator.groups.ParentIdNotNull;
import com.moon.validator.groups.ParentIdNull;
import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.moon.enums.CommentTypeEnum.*;

/**
 * 评论分组校验器
 *
 * @author:Y.0
 * @date:2023/10/13
 */
public class CommentProvider implements DefaultGroupSequenceProvider<CommentDto> {
    @Override
    public List<Class<?>> getValidationGroups(CommentDto commentDto) {
        List<Class<?>> defaultGroupSequence = new ArrayList<>();
        defaultGroupSequence.add(CommentDto.class);
        if (commentDto != null) {
            if (commentDto.getCommentType().equals(ARTICLE.getType()) || commentDto.getCommentType().equals(TALK.getType())) {
                defaultGroupSequence.add(ArticleTalk.class);
            }
            if (commentDto.getCommentType().equals(LINK.getType())) {
                defaultGroupSequence.add(Link.class);
            }
            if (Objects.isNull(commentDto.getParentId())) {
                defaultGroupSequence.add(ParentIdNull.class);
            }
            if (Objects.nonNull(commentDto.getParentId())) {
                defaultGroupSequence.add(ParentIdNotNull.class);
            }
        }
        return defaultGroupSequence;
    }
}
