package com.thefishnextdoor.tasks.task;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.TasksPlugin;
import com.thefishnextdoor.tasks.player.PlayerProfile;
import com.thefishnextdoor.tasks.unlock.Unlock;

import net.md_5.bungee.api.ChatColor;

public class PlayerTask {

    private TaskConfiguration taskConfiguration;

    private PlayerProfile playerProfile;

    private int progress;

    private long expires;

    private boolean completed;

    public PlayerTask(TaskConfiguration taskConfiguration, PlayerProfile playerProfile, int progress, long expires) {
        if (taskConfiguration == null) {
            throw new IllegalArgumentException("Task configuration cannot be null");
        }
        if (playerProfile == null) {
            throw new IllegalArgumentException("Player profile cannot be null");
        }
        this.taskConfiguration = taskConfiguration;
        this.playerProfile = playerProfile;
        this.progress = progress;
        this.expires = expires;
    }

    @Override
    public String toString() {
        String progressSection;
        if (completed) {
            progressSection = ChatColor.WHITE + " (" + ChatColor.GREEN + "Completed" + ChatColor.WHITE + ")";
        } else {
            progressSection = ChatColor.WHITE + " (" + ChatColor.BLUE + progress + "/" + taskConfiguration.getAmount() + ChatColor.WHITE + ")";
        }

        String rewardMoneySection = "";
        double rewardMoney = taskConfiguration.getRewardMoney();
        if (!completed && rewardMoney > 0) {
            rewardMoneySection = ChatColor.WHITE + " (" + ChatColor.GOLD + "$" + rewardMoney + ChatColor.WHITE + ")";
        }

        String expireSection = "";
        if (!completed && canExpire()) {
            long timeLeft = expires - System.currentTimeMillis();
            if (timeLeft <= 0) {
                expireSection = ChatColor.WHITE + " (" + ChatColor.RED + "0s left" + ChatColor.WHITE + ")";
            } 
            else if (timeLeft < 60000) {
                expireSection = ChatColor.WHITE + " (" + ChatColor.RED + timeLeft / 1000 + "s left" + ChatColor.WHITE + ")";
            } 
            else if (timeLeft < 3600000) {
                expireSection = ChatColor.WHITE + " (" + ChatColor.YELLOW + timeLeft / 60000 + "m left" + ChatColor.WHITE + ")";
            } 
            else if (timeLeft < 86400000) {
                expireSection = ChatColor.WHITE + " (" + ChatColor.GREEN + timeLeft / 3600000 + "h left" + ChatColor.WHITE + ")";
            } 
            else {
                expireSection = ChatColor.WHITE + " (" + ChatColor.AQUA + timeLeft / 86400000 + "d left" + ChatColor.WHITE + ")";
            }
        }
        else {
            expireSection = "";
        }

        return taskConfiguration.toString() + progressSection + rewardMoneySection + expireSection;
    }

    public TaskConfiguration getTaskConfiguration() {
        return taskConfiguration;
    }

    public int getProgress() {
        return progress;
    }

    public boolean isCompleted() {
        return completed;
    }

    public long getExpires() {
        return expires;
    }

    public boolean isExpired() {
        return canExpire() && System.currentTimeMillis() > expires;
    }

    public boolean canExpire() {
        return expires != 0;
    }

    public void trigger(TriggerType triggerType, Location location, Entity entity, ItemStack item, Block block, int amount) {
        Optional<Player> player = playerProfile.getPlayer();
        if (!player.isPresent()) {
            return;
        }
        if (taskConfiguration.isValidFor(triggerType, player.get(), location, entity, item, block)) {
            addProgress(amount);
        }
    }

    public void addProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress cannot be negative");
        }
        this.progress += progress;
        if (this.progress >= taskConfiguration.getAmount()) {
            complete();
        }
    }

    public void complete() {
        if (completed) {
            return;
        }

        Optional<Player> optionalPlayer = playerProfile.getPlayer();
        if (!optionalPlayer.isPresent()) {
            return;
        }
        Player player = optionalPlayer.get();

        completed = true;
        playerProfile.addCompletedTask(taskConfiguration.getId());
        player.sendMessage(ChatColor.BLUE + "" +  ChatColor.BOLD + "Task Completed: " + ChatColor.WHITE + taskConfiguration.toString());
    
        playerProfile.addXp(taskConfiguration.getRewardXp());
        TasksPlugin.getEconomy().ifPresent(economy -> economy.depositPlayer(player, taskConfiguration.getRewardMoney()));
        
        Unlock.checkUnlocks(playerProfile);
    }
}
