package com.supaham.commons.bukkit.text.legacy;

import com.google.common.base.Preconditions;

import com.supaham.commons.Enums;
import com.supaham.commons.bukkit.text.TextParser;
import com.supaham.commons.bukkit.utils.ChatUtils;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

public class LegacyParser implements TextParser {

  @Override public Component parse(String source) {
    return ComponentParser.from(source);
  }

  private static class ComponentParser {

    private String message;
    private int currentIndex;

    private final Pattern INCREMENTAL_PATTERN = Pattern.compile(
        "(" + ChatColor.COLOR_CHAR +
        "[0-9a-fk-or])|(\\n)|(?:(https?://[^ ][^ ]*?)(?=[\\.\\?!,;:]?(?:[ \\n]|$)))",
        Pattern.CASE_INSENSITIVE
    );

    public static Component from(@Nonnull String message) {
      Preconditions.checkNotNull(message, "message.");
      return new ComponentParser(message).build();
    }

    public ComponentParser(String message) {
      this.message = message;
    }

    private Component build() {
      Matcher matcher = INCREMENTAL_PATTERN.matcher(message);
      final TextComponent.Builder _builder = TextComponent.builder().content("");
      TextComponent.Builder currentBuilder = _builder;
      String match;
      while (matcher.find()) {
        int group = 0;
        while ((match = matcher.group(++group)) == null) {
        }

        appendMessage(currentBuilder, matcher.start(group));

        TextComponent.Builder processResult = null;
        switch (group) {
          case 1: // Color/format group
            ChatColor color = ChatColor.getByChar(match.charAt(1));
            processResult = processChatColor(currentBuilder, color);
            break;
          case 2: // New line
            TextComponent.Builder builderToChange = currentBuilder;
            if (!isEmpty(currentBuilder, false)) {
              builderToChange = processResult = TextComponent.builder();
            }
            builderToChange.content("\n");
            break;
          case 3: // URL
            processResult = appendMessage(currentBuilder, matcher.end(group));
            processResult.clickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, match));
            break;
        }
        if (processResult != null && currentBuilder != processResult) {
          if (currentBuilder != _builder) { // Will run only when not empty base.
            _builder.append(currentBuilder.build());
          }
          currentBuilder = processResult;
        }
//        appendIfNotEqual(builder, processResult);
        currentIndex = matcher.end(group);
      }
      if (currentIndex < message.length()) {
        TextComponent.Builder appendResult = appendMessage(currentBuilder, message.length());
        appendIfNotEqual(currentBuilder, appendResult);
      }
      if (currentBuilder != _builder) { // Will run only when not empty base.
        _builder.append(currentBuilder.build());
      }
      return _builder.build();
    }

    private TextComponent.Builder processChatColor(TextComponent.Builder builder, ChatColor color) {
      if (color.equals(ChatColor.RESET)) {
        TextComponent.Builder newBuilder = TextComponent.builder().content("");
        ChatUtils.forceResetStyles(newBuilder);
        return newBuilder;
      } else if (color.isFormat()) {
        switch (color) {
          case BOLD:
            builder.decoration(TextDecoration.BOLD, true);
            break;
          case ITALIC:
            builder.decoration(TextDecoration.ITALIC, true);
            break;
          case STRIKETHROUGH:
            builder.decoration(TextDecoration.STRIKETHROUGH, true);
            break;
          case UNDERLINE:
            builder.decoration(TextDecoration.UNDERLINE, true);
            break;
          case MAGIC:
            builder.decoration(TextDecoration.OBFUSCATED, true);
            break;
          default:
            throw new UnsupportedOperationException("Unexpected ChatColor format " + color);
        }
      } else {
        TextColor textColor = Enums.findByValue(TextColor.class, color.name());
        TextComponent.Builder builderToChange = builder;
        if (!isEmpty(builderToChange, false)) {
          builderToChange = TextComponent.builder().content("");
        }
        builderToChange.color(textColor);
        return builderToChange;
      }
      return builder;
    }

    private TextComponent.Builder appendMessage(TextComponent.Builder builder, int index) {
      if (index <= currentIndex) {
        return builder;
      }

      String message = this.message.substring(currentIndex, index);
      this.currentIndex = index;

      if (!isEmpty(builder, true)) {
        builder = TextComponent.builder();
      }
      builder.content(message);
      return builder;
    }
    
    private static void appendIfNotEqual(TextComponent.Builder parent, TextComponent.Builder builder) {
      if (builder != null && parent != builder) {
        parent.append(builder.build());
      }
    }

    private static boolean isEmpty(TextComponent.Builder builder, boolean contentOnly) {
      TextComponent component;
      try {
        component = builder.build();
      } catch (IllegalStateException e) { // content not set
        if (contentOnly) {
          return true;
        }
        // Hacky way to get around build exception.
        try {
          builder.content("");
          return isEmpty(builder, contentOnly);
        } finally {
          builder.content(null);
        }
      }
      if (component.content().isEmpty()) {
        if (contentOnly) return true;
        if (component.children().isEmpty()) {
          if (component.color() == null) {
            if (component.decorations().isEmpty()) {
              if (component.hoverEvent() == null) {
                if (component.clickEvent() == null) {
                  if (component.insertion() == null) {
                    return true;
                  }
                }
              }
            }
          }
        }
      }
      return false;
    }
  }
}
