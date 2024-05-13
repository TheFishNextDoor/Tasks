package com.thefishnextdoor.tasks.toolkit;

public class MoneyTools {

    public static String format(double money) {
        String currency = "$";
        if (money < 1) {
            return currency + String.format("%.2f", money);
        } 
        else if (money < 1000) {
            return currency + String.format("%.0f", money);
        } 
        else if (money < 1000000) {
            return currency + String.format("%.0fk", money / 1000);
        } 
        else {
            return currency + String.format("%.0fm", money / 1000000);
        }
    }   
}