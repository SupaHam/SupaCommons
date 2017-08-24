package com.supaham.commons.bukkit.text.legacy;

import com.supaham.commons.bukkit.text.TextParsers;

import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

import org.bukkit.ChatColor;
import org.junit.Assert;
import org.junit.Test;

public class LegacyParserTest {

  @Test
  public void testStringOnly() {
    Component component = TextParsers.LEGACY.parse("foo");
    TextComponent expected = TextComponent.of("foo");
    Assert.assertEquals(expected, component);
  }

  @Test
  public void testColor() {
    Component component = TextParsers.LEGACY.parse(ChatColor.COLOR_CHAR + "afoo");
    TextComponent expected = TextComponent.of("foo").color(TextColor.GREEN);
    Assert.assertEquals(expected, component);
  }

  @Test
  public void testAnchor() {
    String url = "http://example.com";
    Component component = TextParsers.LEGACY.parse("foo " + url);
    TextComponent expected = TextComponent.of("foo ")
        .append(TextComponent.of(url).clickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
    Assert.assertEquals(expected, component);
  }

  @Test
  public void testSingleStyle() {
    Component component = TextParsers.LEGACY.parse(ChatColor.COLOR_CHAR + "lfoo");
    TextComponent expected = TextComponent.of("foo").decoration(TextDecoration.BOLD, true);
    Assert.assertEquals(expected, component);
  }

  @Test
  public void testMultipleStyle() {
    Component component = TextParsers.LEGACY.parse(ChatColor.COLOR_CHAR + "l" + ChatColor.COLOR_CHAR + "mfoo");
    TextComponent expected = TextComponent.of("foo").decoration(TextDecoration.BOLD, true).decoration(TextDecoration.STRIKETHROUGH, true);
    Assert.assertEquals(expected, component);
  }

  @Test
  public void testColorWithEmptyBase() {
    Component component = TextParsers.LEGACY.parse(ChatColor.COLOR_CHAR + "a" + ChatColor.COLOR_CHAR + "lfoo");
    TextComponent expected = TextComponent.of("foo").color(TextColor.GREEN).decoration(TextDecoration.BOLD, true);
    Assert.assertEquals(expected, component);
  }

  @Test
  public void testColorWithTrailingText() {
    String spaces = "    ";
    Component component = TextParsers.LEGACY.parse(spaces + ChatColor.COLOR_CHAR + "a" + ChatColor.COLOR_CHAR + "lfoo");
    TextComponent expected = TextComponent.of(spaces).append(
        TextComponent.of("foo").color(TextColor.GREEN).decoration(TextDecoration.BOLD, true)
    );
    Assert.assertEquals(expected, component);
  }
}
