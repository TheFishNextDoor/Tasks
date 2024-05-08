package com.thefishnextdoor.tasks.task;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;

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
        return taskConfiguration.toString() + " (" + progress + "/" + taskConfiguration.getAmount() + ")";
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
        return System.currentTimeMillis() > expires;
    }

    public void trigger(TriggerType triggerType, Entity entity, ItemStack item, Block block, int amount) {
        Optional<Player> player = playerProfile.getPlayer();
        if (!player.isPresent()) {
            return;
        }
        if (taskConfiguration.isValidFor(triggerType, player.get(), entity, item, block)) {
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
        completed = true;
        playerProfile.getCompletedTasks().add(taskConfiguration.getId());
        playerProfile.addXp(taskConfiguration.getRewardXp());
        // TODO: reward money
        playerProfile.getPlayer().ifPresent(player -> player.sendMessage(ChatColor.BLUE + "Task completed: " + ChatColor.WHITE + taskConfiguration.toString()));
    }
}
