package com.supaham.commons.bukkit.text;

import com.supaham.commons.bukkit.text.legacy.LegacyParser;
import com.supaham.commons.bukkit.text.xml.XmlParser;

public class TextParsers {
  public static final XmlParser XML = new XmlParser();
  public static final LegacyParser LEGACY = new LegacyParser();
}
