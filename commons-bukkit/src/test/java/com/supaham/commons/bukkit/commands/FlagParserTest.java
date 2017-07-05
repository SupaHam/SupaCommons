package com.supaham.commons.bukkit.commands;

import com.supaham.commons.bukkit.commands.flags.Flag;
import com.supaham.commons.bukkit.commands.flags.FlagParseResult;
import com.supaham.commons.bukkit.commands.flags.FlagParser;
import com.supaham.commons.bukkit.commands.flags.MissingFlagException;
import com.supaham.commons.bukkit.commands.flags.MissingFlagValueException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FlagParserTest {

  private static final String[] NO_ARGS = new String[0];
  private FlagParser parser;
  private Flag FOO_FLAG; 
  private Flag VAL_FOO_FLAG;
  private Flag OPT_BAR_FLAG;
  private Flag OPT_VAL_BAR_FLAG; 
  
  @Before
  public void setUp() throws Exception {
    parser = new FlagParser(FlagParser.SHORTHAND_PREFIX, FlagParser.LONGHAND_PREFIX);
    FOO_FLAG = new Flag('f', "foo", false, false);
    VAL_FOO_FLAG = new Flag('f', "foo", false, true);
    OPT_BAR_FLAG = new Flag('b', "bar", true, false);
    OPT_VAL_BAR_FLAG = new Flag('b', "bar", true, true);
  }

  @Test
  public void testEmpty() throws Exception {
    FlagParseResult result = parser.parse(NO_ARGS);
    Assert.assertNotNull(result);
  }

  @Test
  public void testNoFlags() throws Exception {
    String[] args = {"some", "args"};
    FlagParseResult result = parser.parse(args);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getArgs().length == 2);
    Assert.assertEquals("some", result.getArgs()[0]);
    Assert.assertEquals("args", result.getArgs()[1]);
  }

  @Test
  public void testFooFlag() throws Exception {
    parser.add(FOO_FLAG);
    String[] args = {"-f"};
    FlagParseResult result = parser.parse(args);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getArgs().length == 0);
  }

  @Test
  public void testFooFlagWithNormalArg() throws Exception {
    parser.add(FOO_FLAG);
    String[] args = {"-f", "foo"};
    FlagParseResult result = parser.parse(args);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getArgs().length == 1);
    Assert.assertEquals("foo", result.getArgs()[0]);
  }

  @Test
  public void testFooValueFlag() throws Exception {
    parser.add(VAL_FOO_FLAG);
    String[] args = {"-f", "foo"};
    FlagParseResult result = parser.parse(args);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getArgs().length == 0);
  }

  @Test
  public void testFooValueFlagWithNormalArg() throws Exception {
    parser.add(VAL_FOO_FLAG);
    String[] args = {"-f", "foo", "bar"};
    FlagParseResult result = parser.parse(args);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getArgs().length == 1);
    Assert.assertEquals("bar", result.getArgs()[0]);
  }

  @Test
  public void testOptionalBarFlagAbsent() throws Exception {
    parser.add(OPT_BAR_FLAG);
    String[] args = {"bar"};
    FlagParseResult result = parser.parse(args);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getArgs().length == 1);
    Assert.assertEquals("bar", result.getArgs()[0]);
  }

  @Test
  public void testOptionalBarFlagPresent() throws Exception {
    parser.add(OPT_BAR_FLAG);
    String[] args = {"-b", "bar"};
    FlagParseResult result = parser.parse(args);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getArgs().length == 1);
    Assert.assertEquals("bar", result.getArgs()[0]);
  }

  @Test
  public void testOptionalBarValueFlagAbsent() throws Exception {
    parser.add(OPT_VAL_BAR_FLAG);
    String[] args = {"bar"};
    FlagParseResult result = parser.parse(args);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getArgs().length == 1);
    Assert.assertEquals("bar", result.getArgs()[0]);
  }

  @Test
  public void testOptionalBarValueFlagPresent() throws Exception {
    parser.add(OPT_VAL_BAR_FLAG);
    String[] args = {"-b", "bar"};
    FlagParseResult result = parser.parse(args);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getArgs().length == 0);
    Assert.assertEquals("bar", result.get('b'));
  }

  @Test
  public void testOptionalBarValueFlagPresentWithNormalArg() throws Exception {
    parser.add(OPT_VAL_BAR_FLAG);
    String[] args = {"-b", "bar", "foo"};
    FlagParseResult result = parser.parse(args);
    Assert.assertNotNull(result);
    Assert.assertTrue(result.getArgs().length == 1);
    Assert.assertEquals("bar", result.get('b'));
    Assert.assertEquals("foo", result.getArgs()[0]);
  }

  @Test(expected = MissingFlagException.class)
  public void testMissingFlag() throws Exception {
    parser.add(FOO_FLAG);
    FlagParseResult parse = parser.parse(NO_ARGS);
  }

  @Test(expected = MissingFlagException.class)
  public void testMissingFlagForValueFlag() throws Exception {
    parser.add(VAL_FOO_FLAG);
    FlagParseResult parse = parser.parse(NO_ARGS);
  }

  @Test(expected = MissingFlagValueException.class)
  public void testMissingFlagValue() throws Exception {
    parser.add(VAL_FOO_FLAG);
    String[] args = {"-f"};
    FlagParseResult parse = parser.parse(args);
  }

  @Test(expected = MissingFlagValueException.class)
  public void testMissingOptionalFlagValue() throws Exception {
    parser.add(OPT_VAL_BAR_FLAG);
    String[] args = {"-b"};
    FlagParseResult parse = parser.parse(args);
  }
}
