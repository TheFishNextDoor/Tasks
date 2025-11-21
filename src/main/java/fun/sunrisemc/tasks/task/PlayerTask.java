package fun.sunrisemc.tasks.task;

import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import fun.sunrisemc.tasks.player.PlayerProfile;
import fun.sunrisemc.tasks.unlock.Unlock;
import fun.sunrisemc.tasks.utils.Money;
import fun.sunrisemc.tasks.utils.StringUtils;

public class PlayerTask {

    private @NotNull TaskConfiguration taskConfiguration;

    private @NotNull PlayerProfile playerProfile;

    private int progress;

    private long expires;

    private boolean completed;

    public PlayerTask(@NotNull TaskConfiguration taskConfiguration, @NotNull PlayerProfile playerProfile, int progress, long expires) {
        this.taskConfiguration = taskConfiguration;
        this.playerProfile = playerProfile;
        this.progress = progress;
        this.expires = expires;
    }

    public PlayerTask(@NotNull TaskConfiguration taskConfiguration, @NotNull PlayerProfile playerProfile) {
        this(taskConfiguration, playerProfile, 0, taskConfiguration.getTimeLimitMS() == 0 ? 0 : System.currentTimeMillis() + taskConfiguration.getTimeLimitMS());
    }

    @Override
    @NotNull
    public String toString() {
        String progressSection;
        if (completed) {
            progressSection = ChatColor.WHITE + " (" + ChatColor.GREEN + "Completed" + ChatColor.WHITE + ")";
        } 
        else {
            String progressString;
            switch (taskConfiguration.getProgressDisplayType()) {
                case PERCENT:
                    progressString = StringUtils.formatPercent(this.progress, taskConfiguration.getAmount());
                    break;
                case TIME:
                    progressString = StringUtils.formatSecondsAbbreviated(this.progress);
                    break;
                default:
                    progressString = progress + "/" + taskConfiguration.getAmount();
            }
            progressSection = ChatColor.WHITE + " (" + playerProfile.getColor() + progressString + ChatColor.WHITE + ")";
        }

        String rewardMoneySection = "";
        double rewardMoney = taskConfiguration.getRewardMoney();
        if (rewardMoney > 0) {
            rewardMoneySection = ChatColor.WHITE + " (" + Money.format(rewardMoney) + ChatColor.WHITE + ")";
        }

        String rewardSkipsSection = "";
        int rewardSkips = taskConfiguration.getRewardSkips();
        if (rewardSkips > 0) {
            rewardSkipsSection = ChatColor.WHITE + " (" + ChatColor.LIGHT_PURPLE + "+" + rewardSkips + " skips" + ChatColor.WHITE + ")";
        }

        String expireSection = "";
        if (!completed && canExpire()) {
            long timeLeft = expires - System.currentTimeMillis();

            ChatColor color;
            if (timeLeft < 60000) {
                color = ChatColor.RED;
            } 
            else if (timeLeft < 3600000) {
                color = ChatColor.YELLOW;
            } 
            else if (timeLeft < 86400000) {
                color = ChatColor.GREEN;
            } 
            else {
                color = ChatColor.AQUA;
            }

            String formattedSeconds = StringUtils.formatSecondsAbbreviated((int) (timeLeft / 1000));

            expireSection = ChatColor.WHITE + " (" + color + formattedSeconds + " left" + ChatColor.WHITE + ")";
        }

        return taskConfiguration.toString() + progressSection + rewardMoneySection + rewardSkipsSection + expireSection;
    }

    @NotNull
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

    public void trigger(@NotNull TriggerType triggerType, @NotNull Location location, @Nullable Entity entity, @Nullable ItemStack item, @Nullable Block block, int amount) {
        Optional<Player> player = playerProfile.getPlayer();
        if (player.isEmpty()) {
            return;
        }

        if (taskConfiguration.isValidFor(triggerType, player.get(), location, entity, item, block)) {
            addProgress(amount);
        }

        if (triggerType == TriggerType.DEATH && taskConfiguration.resetOnDeath()) {
            progress = 0;
        }
    }

    public void addProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("Progress cannot be negative");
        }
        if (completed) {
            return;
        }
        this.progress += progress;
        if (this.progress >= taskConfiguration.getAmount()) {
            complete();
        }
        if (taskConfiguration.showActionbar()) {
            playerProfile.getPlayer().ifPresent(player -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(this.toString())));
        }
    }

    public void complete() {
        if (completed) {
            return;
        }

        Optional<Player> player = playerProfile.getPlayer();
        if (player.isEmpty()) {
            return;
        }

        completed = true;

        playerProfile.addCompletedTask(taskConfiguration.getId());

        playerProfile.sendNotification("Task Completed", taskConfiguration.toString());

        String name = player.get().getName();
        for (String message : taskConfiguration.getRewardMessages()) {
            player.get().sendMessage(message.replace("{player}", name));
        }
        
        playerProfile.addMoney(taskConfiguration.getRewardMoney());

        for (Unlock unlock : taskConfiguration.getRewardUnlocks()) {
            unlock.giveTo(playerProfile);
        }

        Server server = player.get().getServer();

        for (String consoleCommand : taskConfiguration.getRewardConsoleCommands()) {
            server.dispatchCommand(server.getConsoleSender(), consoleCommand.replace("{player}", name));
        }

        for (String playerCommand : taskConfiguration.getRewardPlayerCommands()) {
            player.get().performCommand(playerCommand.replace("{player}", name));
        }

        playerProfile.addXp(taskConfiguration.getRewardXp());

        playerProfile.addSkips(taskConfiguration.getRewardSkips());
    }
}