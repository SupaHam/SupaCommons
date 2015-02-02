package com.supaham.commons.yaml.comments;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.supaham.commons.utils.StringUtils.checkNotNullOrEmpty;

import com.supaham.commons.yaml.YAMLProcessor;

import java.util.Map;

import javax.annotation.Nonnull;

import pluginbase.config.SerializationRegistrar;
import pluginbase.config.field.Field;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;

public class CommentsMapper {

    @Nonnull
    private String currentPath;
    private Map<?, ?> configMap = null;
    private FieldMap fieldMap = null;
    @Nonnull
    private YamlFileCommentInstrumenter commentInstrumenter;

    public static YamlFileCommentInstrumenter getCommentInstrumenter(@Nonnull YAMLProcessor config) {
        checkNotNull(config, "config cannot be null.");
        return new CommentsMapper("", config.getMap(), YamlFileCommentInstrumenter
            .createCommentInstrumenter(config.getOptions().getIndent())).getCommentInstrumenter();
    }

    private CommentsMapper(@Nonnull String currentPath, @Nonnull Map<?, ?> configMap,
                           @Nonnull YamlFileCommentInstrumenter commentInstrumenter) {
        checkNotNullOrEmpty(currentPath, "currentPath");
        checkNotNull(configMap, "configMap cannot be null.");
        checkNotNull(commentInstrumenter, "commentInstrumenter cannot be null.");
        this.currentPath = currentPath;
        this.configMap = configMap;
        this.commentInstrumenter = commentInstrumenter;
    }
  
    private CommentsMapper(@Nonnull String currentPath, @Nonnull FieldMap fieldMap,
                           @Nonnull YamlFileCommentInstrumenter commentInstrumenter) {
        checkNotNullOrEmpty(currentPath, "currentPath");
        checkNotNull(fieldMap, "fieldMap cannot be null.");
        checkNotNull(commentInstrumenter, "commentInstrumenter cannot be null.");
        this.currentPath = currentPath;
        this.fieldMap = fieldMap;
        this.commentInstrumenter = commentInstrumenter;
    }

    private YamlFileCommentInstrumenter getCommentInstrumenter() {
        if (configMap != null) {
            processConfigMap();
        } else if (fieldMap != null) {
            processFieldMap();
        }
        return commentInstrumenter;
    }

    private String getNewPath(Object key) {
        return currentPath + (currentPath.isEmpty() ? "" : ".") + key.toString();
    }

    private String getNewPath(Field field) {
        return currentPath + (currentPath.isEmpty() ? "" : ".") + field.getName();
    }

    private YamlFileCommentInstrumenter processConfigMap() {
        for (Map.Entry<?, ?> entry : configMap.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            Class entryClass = entry.getValue().getClass();
            if (Map.class.isAssignableFrom(entryClass)) {
                Map<?, ?> configMap = (Map<?, ?>) entry.getValue();
                commentInstrumenter = new CommentsMapper(getNewPath(entry.getKey()), configMap, commentInstrumenter).getCommentInstrumenter();
            } else if (SerializationRegistrar.isClassRegistered(entryClass)) {
                FieldMap fieldMap = FieldMapper.getFieldMap(entryClass);
                commentInstrumenter = new CommentsMapper(getNewPath(entry.getKey()), fieldMap, commentInstrumenter).getCommentInstrumenter();
            }
        }
        return commentInstrumenter;
    }

    private YamlFileCommentInstrumenter processFieldMap() {
        for (Field field : fieldMap) {
            String newPath = null;
            if (SerializationRegistrar.isClassRegistered(field.getType())) {
                newPath = getNewPath(field);
                commentInstrumenter = new CommentsMapper(newPath, field, commentInstrumenter).getCommentInstrumenter();
            }
            String[] comments = field.getComments();
            if (comments != null) {
                if (newPath == null) {
                    newPath = getNewPath(field);
                }
                commentInstrumenter.setCommentsForPath(newPath, comments);
            }
        }
        return commentInstrumenter;
    }

}
