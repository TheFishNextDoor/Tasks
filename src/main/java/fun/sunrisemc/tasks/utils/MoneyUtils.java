package fun.sunrisemc.tasks.utils;

import net.md_5.bungee.api.ChatColor;

public class MoneyUtils {

    private static final String currencyPrefix = "$";
    private static final String currencySuffix = "";

    private static final String positivePrefix = ChatColor.GOLD + "";
    private static final String negativePrefix = ChatColor.RED + "-";

    public static String format(double money) {
        String color = money < 0 ? negativePrefix : positivePrefix;
        money = Math.abs(money);
        if (money >= 1000000000) {
            double billion = money / 1000000000;
            String formattedMoney = billion % 1 == 0 ? String.format("%.0f", billion) : String.format("%.1f", billion);
            return color + currencyPrefix + formattedMoney + "b" + currencySuffix;
        } 
        else if (money >= 1000000) {
            double million = money / 1000000;
            String formattedMoney = million % 1 == 0 ? String.format("%.0f", million) : String.format("%.1f", million);
            return color + currencyPrefix + formattedMoney + "m" + currencySuffix;
        } 
        else if (money >= 1000) {
            double thousand = money / 1000;
            String formattedMoney = thousand % 1 == 0 ? String.format("%.0f", thousand) : String.format("%.1f", thousand);
            return color + currencyPrefix + formattedMoney + "k" + currencySuffix;
        } 
        else {
            String formattedMoney = money % 1 == 0 ? String.format("%.0f", money) : String.format("%.2f", money);
            return color + currencyPrefix + formattedMoney + currencySuffix;
        } 
    }
}