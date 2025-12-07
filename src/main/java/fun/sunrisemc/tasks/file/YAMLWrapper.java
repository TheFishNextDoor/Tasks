package fun.sunrisemc.tasks.file;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import org.jetbrains.annotations.NotNull;

public abstract class YAMLWrapper {

    // Fields

    protected final @NotNull YamlConfiguration config;

    // Constructor

    protected YAMLWrapper(@NotNull YamlConfiguration config) {
        this.config = config;
    }

    // Config

    @NotNull
    public YamlConfiguration getConfig() {
        return this.config;
    }

    // Keys

    public boolean hasKey(@NotNull String path) {
        return this.config.contains(path);
    }

    @NotNull
    public HashSet<String> getKeys() {
        return new HashSet<>(this.config.getKeys(false));
    }

    @NotNull
    public HashSet<String> getKeys(@NotNull String path) {
        if (!this.config.contains(path)) {
            return new HashSet<>();
        }

        ConfigurationSection section = this.config.getConfigurationSection(path);
        if (section == null) {
            return new HashSet<>();
        }

        return new HashSet<>(section.getKeys(false));
    }

    public boolean renameKey(String oldKey, String newKey) {
        if (!this.config.contains(oldKey) || this.config.contains(newKey)) {
            return false;
        }

        Object value = this.config.get(oldKey);

        config.set(newKey, value);
        config.set(oldKey, null);

        return true;
    }

    public boolean moveKey(String oldKey, String newKey) {
        if (!this.config.contains(oldKey) || this.config.contains(newKey)) {
            return false;
        }

        Object value = this.config.get(oldKey);

        config.set(newKey, value);
        config.set(oldKey, null);

        return true;
    }

    // Reading Values

    public Optional<Boolean> getBoolean(@NotNull String path) {
        if (!this.config.contains(path)) {
            return Optional.empty();
        }

        boolean value = this.config.getBoolean(path);

        return Optional.of(value);
    }

    public Optional<Integer> getInt(@NotNull String path) {
        if (!this.config.contains(path)) {
            return Optional.empty();
        }

        int value = this.config.getInt(path);

        return Optional.of(value);
    }

    public Optional<Integer> getIntClamped(@NotNull String path, int min, int max) {
        if (!this.config.contains(path)) {
            return Optional.empty();
        }

        int value = this.config.getInt(path);

        return Optional.of(Math.clamp(value, min, max));
    }

    public Optional<Double> getDouble(@NotNull String path) {
        if (!this.config.contains(path)) {
            return Optional.empty();
        }

        double value = this.config.getDouble(path);

        return Optional.of(value);
    }

    public Optional<Double> getDoubleClamped(@NotNull String path, double min, double max) {
        if (!this.config.contains(path)) {
            return Optional.empty();
        }

        double value = this.config.getDouble(path);

        return Optional.of(Math.clamp(value, min, max));
    }

    public Optional<Long> getLong(@NotNull String path) {
        if (!this.config.contains(path)) {
            return Optional.empty();
        }

        long value = this.config.getLong(path);

        return Optional.of(value);
    }

    public Optional<Long> getLongClamped(@NotNull String path, long min, long max) {
        if (!this.config.contains(path)) {
            return Optional.empty();
        }

        long value = this.config.getLong(path);

        return Optional.of(Math.clamp(value, min, max));
    }

    public Optional<String> getString(@NotNull String path) {
        if (!this.config.contains(path)) {
            return Optional.empty();
        }

        String value = this.config.getString(path);

        return Optional.ofNullable(value);
    }

    @NotNull
    public List<String> getStringList(@NotNull String path) {
        if (!this.config.contains(path)) {
            return new ArrayList<String>();
        }

        List<String> StringList = this.config.getStringList(path);

        if (StringList.isEmpty()) {
            String singleString = this.config.getString(path);
            if (singleString != null) {
                StringList = List.of(singleString);
            }
        }

        return StringList;
    }

    public Optional<List<String>> getStringListIfPresent(@NotNull String path) {
        if (!this.config.contains(path)) {
            return Optional.empty();
        }

        List<String> StringList = this.config.getStringList(path);

        if (StringList.isEmpty()) {
            String singleString = this.config.getString(path);
            if (singleString != null) {
                StringList = List.of(singleString);
            }
        }

        return Optional.of(StringList);
    }

    // Writing Values

    public void set(@NotNull String path, @NotNull Object value) {
        this.config.set(path, value);
    }

    public void clear(@NotNull String path) {
        this.config.set(path, null);
    }
}