package fun.sunrisemc.tasks.permission;

import org.jetbrains.annotations.NotNull;

public class Permissions {

    // Standard Permissions

    public static final @NotNull String COLOR_PERMISSION = "tasks.color"; // Allows the player to change the color of their tasks

    // Admin Permissions

    public static final @NotNull String ADMIN_RELOAD_PERMISSION = "tasks.admin.reload"; // Allows the player to reload the plugin
    public static final @NotNull String ADMIN_TASK_PERMISSION = "tasks.admin.task"; // Allows the player to alter players tasks
    public static final @NotNull String ADMIN_XP_PERMISSION = "tasks.admin.xp"; // Allows the player to alter players xp
    public static final @NotNull String ADMIN_UNLOCK_PERMISSION = "tasks.admin.unlock"; // Allows the player to alter players unlocks
    public static final @NotNull String ADMIN_SKIPS_PERMISSION = "tasks.admin.skips"; // Allows the player to alter players skips
    
}