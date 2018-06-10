package com.supaham.commons.bukkit.text.xml;

import com.google.common.base.Preconditions;

import com.supaham.commons.Enums;
import com.supaham.commons.utils.StringUtils;

import net.kyori.text.BuildableComponent;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import org.bukkit.ChatColor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.namespace.QName;

@XmlSeeAlso({
    Tags.A.class,
    Tags.B.class,
    Tags.Click.class,
    Tags.Color.class,
    Tags.Hover.class,
    Tags.I.class,
    Tags.Keybind.class,
    Tags.Obfuscated.class,
    Tags.Strike.class,
    Tags.Score.class,
    Tags.Selector.class,
    Tags.Span.class,
    Tags.Tl.class,
    Tags.U.class
})
public class Element {

  private static final Pattern FUNCTION_PATTERN = Pattern.compile("^([^(]+)\\([\"'](.*)[\"']\\)$");

  @XmlAttribute private String onClick;
  @XmlAttribute private String onHover;
  @XmlAttribute private String style;
  @XmlAttribute private String insertion;
  @XmlElementRef(type = Element.class) @XmlMixed private List<Object> mixedContent;
  @XmlAnyAttribute private Map<QName, String> attributes;

  public static <C extends BuildableComponent, B extends BuildableComponent.Builder<C, B>> void parseAndApplyStyle(
      BuildableComponent.Builder<C, B> builder, String style) {
    String[] styles = style.split("\\s*,\\s*");
    for (String _style : styles) {
      handleStyle(builder, _style);
    }
  }

  private static <C extends BuildableComponent, B extends BuildableComponent.Builder<C, B>> void handleStyle(
      BuildableComponent.Builder<C, B> builder, String style) {
    String[] stateSplit = style.split("\\s*:\\s*");
    String styleName = stateSplit[0];
    boolean stateSpecified = stateSplit.length > 1;
    TextDecoration.State state = stateSpecified ? Enums.findFuzzyByValue(TextDecoration.State.class, stateSplit[1])
                                                : TextDecoration.State.TRUE;
    if (stateSpecified && state == null) {
      throw new IllegalArgumentException("Invalid state: '" + stateSplit[1] + "'");
    }
    boolean found = false;
    for (TextDecoration decoration : TextDecoration.values()) {
      if (styleName.equals(decoration.name())) {
        builder.decoration(decoration, state);
        found = true;
        break;
      }
    }
    if (!found) {
      found = handleColor(builder, styleName, state, stateSpecified);
    }
    if (!found) {
      for (char _char : style.toCharArray()) {
        ChatColor chatColor = ChatColor.getByChar(_char);
        Preconditions.checkArgument(chatColor != null, "Invalid style '" + _char + "'");
        TextDecoration textDeco;
        if (chatColor == ChatColor.MAGIC) {
          textDeco = TextDecoration.OBFUSCATED;
        } else {
          textDeco = Enums.findByValue(TextDecoration.class, chatColor.name());
        }

        if (textDeco != null) {
          builder.decoration(textDeco, state);
          found = true;
          continue;
        }
        TextColor textColor = Enums.findFuzzyByValue(TextColor.class, chatColor.name());
        if (textColor != null) {
          builder.color(textColor);
          found = true;
          continue;
        }
      }
    }
    Preconditions.checkArgument(found, "Invalid style '" + styleName + "'");
  }

  private static <C extends BuildableComponent, B extends BuildableComponent.Builder<C, B>> boolean handleColor(
      BuildableComponent.Builder<C, B> builder, String value, TextDecoration.State state, boolean stateSpecified) {
    if (value.equalsIgnoreCase("color")) {
      TextDecoration.State foundState = (!stateSpecified && state == TextDecoration.State.TRUE)
                                    ? TextDecoration.State.NOT_SET : state;
      Preconditions.checkArgument(state != TextDecoration.State.TRUE,
                                  "color style only accepts FALSE OR NOT_SET");
      builder.color(null);
      return true;
    } else {
      TextColor textColor = Enums.findFuzzyByValue(TextColor.class, value);
      if (textColor != null) {
        builder.color(textColor);
        return true;
      }
    }
    return false;
  }

  public <C extends BuildableComponent, B extends BuildableComponent.Builder<C, B>> void apply(
      BuildableComponent.Builder<C, B> builder) {
    if (style != null) {
      parseAndApplyStyle(builder, style);
    }
    if (insertion != null) {
      builder.insertion(insertion);
    }
    if (onClick != null) {
      Matcher matcher = FUNCTION_PATTERN.matcher(onClick);
      Preconditions.checkArgument(matcher.matches(), "onClick syntax invalid \"" + onClick + "\"");
      ClickEvent.Action action = ClickEvent.Action.valueOf(matcher.group(1).toUpperCase());
      String data = String.format(matcher.group(2));
      builder.clickEvent(new ClickEvent(action, data));
    }
    if (onHover != null) {
      Matcher matcher = FUNCTION_PATTERN.matcher(onHover);
      Preconditions.checkArgument(matcher.matches(), "onHover syntax invalid \"" + onHover + "\"");
      HoverEvent.Action action = HoverEvent.Action.valueOf(matcher.group(1).toUpperCase());
      String data = String.format(matcher.group(2));
      builder.hoverEvent(new HoverEvent(action, TextComponent.of(data)));
    }
  }

  public <C extends BuildableComponent, B extends BuildableComponent.Builder<C, B>> void loop(
      BuildableComponent.Builder<C, B> builder) {
    boolean elementAppended = false; // Maintains order of string content when attempting to optimise.
    if (mixedContent != null) {
      for (Object o : mixedContent) {
        if (o instanceof String) {
          if (builder instanceof TextComponent.Builder) {
            TextComponent component = ((TextComponent.Builder) builder).build();
            if (!elementAppended && StringUtils.stripToNull(component.content()) == null) {
              ((TextComponent.Builder) builder).content(o.toString());
            } else {
              builder.append(TextComponent.of(o.toString()));
            }
          } else {
            builder.append(TextComponent.of(o.toString()));
          }
        } else if (o instanceof Element) {
          Element el = (Element) o;
          elementAppended = true;
          BuildableComponent.Builder elBuilder;
          if (el instanceof Tags.ComponentCreator) {
            elBuilder = ((Tags.ComponentCreator) el).createBuilder();
          } else {
            elBuilder = builder;
          }
          el.apply(elBuilder);
          el.loop(elBuilder);
          builder.append(elBuilder.build());
        } else {
          throw new IllegalStateException("Unknown mixed content of type " + o.getClass().getCanonicalName());
        }
      }
    }
  }

  protected Map<QName, String> getAttributes() {
    return this.attributes == null ? Collections.emptyMap() : Collections.unmodifiableMap(this.attributes);
  }
}
