//package com.supaham.commons.bukkit.utils;
//
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.enchantments.EnchantmentTarget;
//import org.bukkit.enchantments.EnchantmentWrapper;
//import org.bukkit.inventory.ItemStack;
//
//import java.lang.reflect.Field;
//
//public class EnchantmentUtils {
//
//    public static final Enchantment GLOW_ENCHANTMENT = new EnchantGlow();
//
//    static {
//        try {
//            Field f = Enchantment.class.getDeclaredField("acceptingNew");
//            f.setAccessible(true);
//            f.set(null, true);
//            Enchantment.registerEnchantment(new EnchantGlow());
//            f.set(null, false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static final class EnchantGlow extends EnchantmentWrapper {
//
//        public EnchantGlow() {
//            super("supa-commons-glow");
//        }
//
//        @Override public int getMaxLevel() {
//            return 100;
//        }
//
//        @Override public int getStartLevel() {
//            return 0;
//        }
//
//        @Override public EnchantmentTarget getItemTarget() {
//            return null;
//        }
//
//        @Override public boolean canEnchantItem(ItemStack item) {
//            return true;
//        }
//
//        @Override public String getName() {
//            return "Glow";
//        }
//
//        @Override public boolean isTreasure() {
//            return false;
//        }
//
//        @Override public boolean isCursed() {
//            return false;
//        }
//
//        @Override public boolean conflictsWith(Enchantment other) {
//            return false;
//        }
//    }
//}
