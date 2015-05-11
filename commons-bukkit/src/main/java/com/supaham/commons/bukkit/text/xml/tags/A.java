package com.supaham.commons.bukkit.text.xml.tags;

import com.supaham.commons.bukkit.text.FancyMessage;
import com.supaham.commons.bukkit.text.xml.Element;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Hyperlink element.
 *
 * @since 0.2.4
 */
@XmlRootElement
public class A extends Element {

  @XmlAttribute(required = true)
  private String href;

  @Override
  protected void modifyStyle(FancyMessage fancyMessage, Object... params) {
    super.modifyStyle(fancyMessage, params);
    fancyMessage.link(href);
  }
}
