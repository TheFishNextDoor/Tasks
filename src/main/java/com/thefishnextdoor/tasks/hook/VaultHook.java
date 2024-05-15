package com.thefishnextdoor.tasks.hook;

import org.bukkit.Server;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.thefishnextdoor.tasks.TasksPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class VaultHook {

    private static boolean usingVault = false;

    private static Economy economy = null;
    private static Permission permissions = null;
    private static Chat chat = null;

    public static boolean isUsingVault() {
        return usingVault;
    }

    public static Economy getEconomy() {
        if (!usingVault) {
            throw new IllegalStateException("Vault not found");
        }
        return economy;
    }

    public static Permission getPermissions() {
        if (!usingVault) {
            throw new IllegalStateException("Vault not found");
        }
        return permissions;
    }

    public static Chat getChat() {
        if (!usingVault) {
            throw new IllegalStateException("Vault not found");
        }
        return chat;
    }

    public static boolean hook(TasksPlugin plugin) {
        Server server = plugin.getServer();
        if (server.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> economyService = server.getServicesManager().getRegistration(Economy.class);
        if (economyService == null) {
            return false;
        }
        economy = economyService.getProvider();

        RegisteredServiceProvider<Permission> permissionService = server.getServicesManager().getRegistration(Permission.class);
        if (permissionService == null) {
            return false;
        }
        permissions = permissionService.getProvider();

        RegisteredServiceProvider<Chat> chatService = server.getServicesManager().getRegistration(Chat.class);
        if (chatService == null) {
            return false;
        }
        chat = chatService.getProvider();

        return true;
    }
}