package com.thefishnextdoor.tasks.task;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.thefishnextdoor.tasks.player.PlayerProfile;

public class PlayerTask {

    private TaskConfiguration taskConfiguration;

    private PlayerProfile playerProfile;

    private int progress;

    private long expires;

    private boolean finished;

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

    public long getExpires() {
        return expires;
    }

    public boolean isExpired() {
        return finished || System.currentTimeMillis() > expires;
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
        this.progress += progress;
        if (this.progress >= taskConfiguration.getAmount()) {
            finish();
        }
    }

    public void finish() {
        if (finished) {
            return;
        }
        finished = true;
        playerProfile.getCompletedTasks().add(taskConfiguration.getId());
        playerProfile.addXp(taskConfiguration.getRewardXp());
        // TODO: reward money
    }
}
