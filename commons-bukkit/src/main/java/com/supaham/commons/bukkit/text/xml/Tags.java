package com.supaham.commons.bukkit.text.xml;

import com.google.common.base.Preconditions;

import com.supaham.commons.Enums;
import com.supaham.commons.bukkit.text.TextParsers;

import net.kyori.text.Component;
import net.kyori.text.KeybindComponent;
import net.kyori.text.ScoreComponent;
import net.kyori.text.SelectorComponent;
import net.kyori.text.TextComponent;
import net.kyori.text.TranslatableComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

public class Tags {

  // javac bug doesn't allow naming generic type definition B because there exists a class named B
  interface ComponentCreator<BB extends Component.Builder<BB, C>, C extends Component> {

    Component.Builder<BB, C> createBuilder();
  }

  @XmlRootElement
  public static class A extends Element implements ComponentCreator<TextComponent.Builder, TextComponent> {

    @XmlAttribute(required = true) private String href;

    @Override public Component.Builder<TextComponent.Builder, TextComponent> createBuilder() {
      return TextComponent.builder().content("").clickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, href));
    }
  }

  @XmlRootElement
  public static class B extends Element implements ComponentCreator<TextComponent.Builder, TextComponent> {

    @Override public Component.Builder<TextComponent.Builder, TextComponent> createBuilder() {
      return TextComponent.builder().content("").decoration(TextDecoration.BOLD, true);
    }
  }

  @XmlRootElement
  public static class Click extends Element implements ComponentCreator<TextComponent.Builder, TextComponent> {

    @XmlAttribute(required = true) private String action;
    @XmlAttribute(required = true) private String value;

    @Override public Component.Builder<TextComponent.Builder, TextComponent> createBuilder() {
      ClickEvent.Action action = Enums.findFuzzyByValue(ClickEvent.Action.class, this.action);
      Preconditions.checkArgument(action != null, "Invalid click action " + action);
      return TextComponent.builder().content("").clickEvent(new ClickEvent(action, value));
    }
  }

  @XmlRootElement
  public static class Color extends Element implements ComponentCreator<TextComponent.Builder, TextComponent> {

    @XmlAttribute(required = true) private String color;

    @Override public Component.Builder<TextComponent.Builder, TextComponent> createBuilder() {
      TextColor textColor = Enums.findFuzzyByValue(TextColor.class, this.color);
      if (textColor == null) {
        for (TextColor _textColor : TextColor.values()) {
          if (_textColor.toString().equalsIgnoreCase(this.color)) {
            textColor = _textColor;
          }
        }
      }
      Preconditions.checkArgument(textColor != null, "Invalid color " + this.color);
      return TextComponent.builder().content("").color(textColor);
    }
  }

  @XmlRootElement
  public static class Hover extends Element implements ComponentCreator<TextComponent.Builder, TextComponent> {

    @XmlAttribute(required = true) private String action;
    @XmlAttribute(required = true) private String value;

    @Override public Component.Builder<TextComponent.Builder, TextComponent> createBuilder() {
      HoverEvent.Action action = Enums.findFuzzyByValue(HoverEvent.Action.class, this.action);
      Preconditions.checkArgument(action != null, "Invalid hover action " + action);
      Component value;
      try {
        value = TextParsers.XML.parse(this.value);
      } catch (Exception e) {
        value = TextComponent.of(this.value);
      }
      return TextComponent.builder().content("").hoverEvent(new HoverEvent(action, value));
    }
  }

  @XmlRootElement
  public static class I extends Element implements ComponentCreator<TextComponent.Builder, TextComponent> {

    @Override public Component.Builder<TextComponent.Builder, TextComponent> createBuilder() {
      return TextComponent.builder().content("").decoration(TextDecoration.ITALIC, true);
    }
  }

  @XmlRootElement
  public static class Keybind extends Element implements ComponentCreator<KeybindComponent.Builder, KeybindComponent> {

    @XmlAttribute(required = true) private String key;

    @Override public Component.Builder<KeybindComponent.Builder, KeybindComponent> createBuilder() {
      return KeybindComponent.builder().keybind(this.key);
    }
  }

  @XmlRootElement
  public static class Obfuscated extends Element implements ComponentCreator<TextComponent.Builder, TextComponent> {

    @Override public Component.Builder<TextComponent.Builder, TextComponent> createBuilder() {
      return TextComponent.builder().content("").decoration(TextDecoration.OBFUSCATED, true);
    }
  }

  @XmlRootElement
  public static class Score extends Element implements ComponentCreator<ScoreComponent.Builder, ScoreComponent> {

    @XmlAttribute(required = true) private String name;
    @XmlAttribute(required = true) private String objective;
    @XmlAttribute private String value;

    @Override public Component.Builder<ScoreComponent.Builder, ScoreComponent> createBuilder() {
      return ScoreComponent.builder().name(this.name).objective(this.objective).value(this.value);
    }
  }

  @XmlRootElement
  public static class Selector extends Element
      implements ComponentCreator<SelectorComponent.Builder, SelectorComponent> {

    @XmlAttribute(required = true) private String pattern;

    @Override public Component.Builder<SelectorComponent.Builder, SelectorComponent> createBuilder() {
      return SelectorComponent.builder().pattern(this.pattern);
    }
  }


  @XmlRootElement
  public static class Span extends Element implements ComponentCreator<TextComponent.Builder, TextComponent> {

    @Override public Component.Builder<TextComponent.Builder, TextComponent> createBuilder() {
      return TextComponent.builder().content("");
    }
  }


  @XmlRootElement
  public static class Strike extends Element implements ComponentCreator<TextComponent.Builder, TextComponent> {

    @Override public Component.Builder<TextComponent.Builder, TextComponent> createBuilder() {
      return TextComponent.builder().content("").decoration(TextDecoration.STRIKETHROUGH, true);
    }
  }

  @XmlRootElement
  public static class Tl extends Element
      implements ComponentCreator<TranslatableComponent.Builder, TranslatableComponent> {

    @XmlAttribute(required = true) private String key;

    @Override public Component.Builder<TranslatableComponent.Builder, TranslatableComponent> createBuilder() {
      TranslatableComponent.Builder component = TranslatableComponent.builder().key(this.key);
      List<Component> args = getAttributes().entrySet().stream()
          .filter(attr -> attr.getKey().getLocalPart().startsWith("with-"))
          .sorted(Comparator.comparing(Object::toString))
          .map(attr -> TextParsers.XML.parse(attr.getValue())).collect(Collectors.toList());
      if (!args.isEmpty()) {
        component.args(args);
      }
      return component;
    }
  }


  @XmlRootElement
  public static class U extends Element implements ComponentCreator<TextComponent.Builder, TextComponent> {

    @Override public Component.Builder<TextComponent.Builder, TextComponent> createBuilder() {
      return TextComponent.builder().content("").decoration(TextDecoration.UNDERLINE, true);
    }
  }
}
