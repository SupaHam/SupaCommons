package com.supaham.commons.bukkit.text.xml.tags;

import com.supaham.commons.bukkit.text.FancyMessage;
import com.supaham.commons.bukkit.text.xml.Element;

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Color element.
 *
 * @since 0.2.4
 */
@XmlRootElement
public class Color extends Element {

  @XmlAttribute(required = true)
  private String id;

  @Override
  protected void modifyStyle(FancyMessage fancyMessage, Object... params) {
    if (id.isEmpty()) {
      throw new IllegalArgumentException("id can not be empty.");
    }
    super.modifyStyle(fancyMessage, params);
    ChatColor color =
        id.length() == 1 ? ChatColor.getByChar(id.toLowerCase())
                         : ChatColor.valueOf(id.toUpperCase());

    Validate.notNull(color, id + " is not a valid color");
    Validate.isTrue(!color.isFormat(), id + " is not a color, its a style.");

    fancyMessage.color(color);
  }
}
