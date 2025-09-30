package fun.sunrisemc.tasks.utils;

import net.md_5.bungee.api.ChatColor;

public class Money {

    private static final String CURRENCY_PREFIX = "$";
    private static final String CURRENCY_SUFFIX = "";
    private static final String POSITIVE_PREFIX = ChatColor.GOLD + "";
    private static final String NEGATIVE_PREFIX = ChatColor.RED + "-";

    public static String format(double money) {
        String color = money < 0 ? NEGATIVE_PREFIX : POSITIVE_PREFIX;
        money = Math.abs(money);
        if (money >= 1000000000) {
            double billion = money / 1000000000;
            String formattedMoney = billion % 1 == 0 ? String.format("%.0f", billion) : String.format("%.1f", billion);
            return color + CURRENCY_PREFIX + formattedMoney + "b" + CURRENCY_SUFFIX;
        } 
        else if (money >= 1000000) {
            double million = money / 1000000;
            String formattedMoney = million % 1 == 0 ? String.format("%.0f", million) : String.format("%.1f", million);
            return color + CURRENCY_PREFIX + formattedMoney + "m" + CURRENCY_SUFFIX;
        } 
        else if (money >= 1000) {
            double thousand = money / 1000;
            String formattedMoney = thousand % 1 == 0 ? String.format("%.0f", thousand) : String.format("%.1f", thousand);
            return color + CURRENCY_PREFIX + formattedMoney + "k" + CURRENCY_SUFFIX;
        } 
        else {
            String formattedMoney = money % 1 == 0 ? String.format("%.0f", money) : String.format("%.2f", money);
            return color + CURRENCY_PREFIX + formattedMoney + CURRENCY_SUFFIX;
        } 
    }
}