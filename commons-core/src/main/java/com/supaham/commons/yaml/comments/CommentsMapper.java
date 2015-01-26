package com.supaham.commons.yaml.comments;

import com.supaham.commons.yaml.YAMLProcessor;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import pluginbase.config.SerializationRegistrar;
import pluginbase.config.field.Field;
import pluginbase.config.field.FieldMap;
import pluginbase.config.field.FieldMapper;

public class CommentsMapper {

    @NotNull
    private String currentPath;
    private Map<?, ?> configMap = null;
    private FieldMap fieldMap = null;
    @NotNull
    private YamlFileCommentInstrumenter commentInstrumenter;

    public static YamlFileCommentInstrumenter getCommentInstrumenter(@NotNull YAMLProcessor config) {
        return new CommentsMapper("", config.getMap(), YamlFileCommentInstrumenter
            .createCommentInstrumenter(config.getOptions().getIndent())).getCommentInstrumenter();
    }

    private CommentsMapper(@NotNull String currentPath, @NotNull Map<?, ?> configMap,
                           @NotNull YamlFileCommentInstrumenter commentInstrumenter) {
        this.currentPath = currentPath;
        this.configMap = configMap;
        this.commentInstrumenter = commentInstrumenter;
    }

    private CommentsMapper(@NotNull String currentPath, @NotNull FieldMap fieldMap,
                           @NotNull YamlFileCommentInstrumenter commentInstrumenter) {
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
