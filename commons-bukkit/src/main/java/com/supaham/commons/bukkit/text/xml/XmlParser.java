package com.supaham.commons.bukkit.text.xml;

import com.supaham.commons.bukkit.text.TextParser;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlParser implements TextParser {

  @Override public Component parse(String source) {
    try {
      source = "<span>" + source + "</span>";
      JAXBContext context = JAXBContext.newInstance(Element.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      Element tag = (Element) unmarshaller.unmarshal(new StringReader(source));
      TextComponent.Builder builder = TextComponent.builder().content("");
      tag.apply(builder);
      tag.loop(builder);
      return builder.build();
    } catch (Exception e) {
      throw new RuntimeException("Failed to parse: " + source, e);
    }
  }
}
