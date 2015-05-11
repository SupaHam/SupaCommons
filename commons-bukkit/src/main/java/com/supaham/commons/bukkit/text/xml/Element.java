package com.supaham.commons.bukkit.text.xml;

import com.supaham.commons.bukkit.text.FancyMessage;
import com.supaham.commons.bukkit.text.xml.tags.A;
import com.supaham.commons.bukkit.text.xml.tags.B;
import com.supaham.commons.bukkit.text.xml.tags.Color;
import com.supaham.commons.bukkit.text.xml.tags.I;
import com.supaham.commons.bukkit.text.xml.tags.Obfuscated;
import com.supaham.commons.bukkit.text.xml.tags.S;
import com.supaham.commons.bukkit.text.xml.tags.Span;
import com.supaham.commons.bukkit.text.xml.tags.U;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Element serialized through {@link XmlParser}.
 *
 * @since 0.2.4
 */
@XmlSeeAlso({A.class,
             B.class,
             Color.class,
             I.class,
             Obfuscated.class,
             S.class,
             Span.class,
             U.class})
public class Element {

  @XmlAttribute
  private String onClick = null;
  @XmlAttribute
  private String onHover = null;

  @XmlElementRef(type = Element.class)
  @XmlMixed
  public List<Object> mixedContent = new ArrayList<Object>();

  /**
   * This regex pattern validates the syntax for onClick/onHover chat events. <p/>
   * Click <a href="http://www.regexr.com/38pjh">here</a> for fun.
   */
  private static final Pattern FUNCTION_PATTERN = Pattern.compile("^([^(]+)\\('(.*)'\\)$");

  protected void modifyStyle(FancyMessage fancyMessage, Object... params) {

    if (onClick != null) {
      Matcher matcher = FUNCTION_PATTERN.matcher(onClick);
      if (!matcher.matches()) {
        throw new IllegalArgumentException("onClick syntax is invalid ('" + onClick + "')");
      }
      String event = matcher.group(1);
      String data = String.format(matcher.group(2), params);
      fancyMessage.onClick(event, data);
    }

    if (onHover != null) {
      Matcher matcher = FUNCTION_PATTERN.matcher(onHover);
      if (!matcher.matches()) {
        throw new IllegalArgumentException("onHover syntax is invalid ('" + onHover + "')");
      }
      String event = matcher.group(1);
      String data = String.format(matcher.group(2), params);
      fancyMessage.onHover(event, data);
    }
  }

  public FancyMessage getFancyMessage() {
    FancyMessage fancyMessage = new FancyMessage();
    modifyStyle(fancyMessage);
    loop(fancyMessage);
    return fancyMessage;
  }

  protected void loop(FancyMessage fancyMessage) {
    for (Object o : mixedContent) {
      if (fancyMessage.hasText()) {
        fancyMessage.then();
      }
      if (o instanceof String) {
        fancyMessage.text(o.toString());
      } else if (o instanceof Element) {
        Element element = (Element) o;
        element.modifyStyle(fancyMessage);
        element.loop(fancyMessage);
      }
    }
  }
}
