package com.sulphate.chatcolor2.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * A simple utility class for easily loading & creating multiple YAML configuration files.
 */
public class ConfigUtils {

    private final JavaPlugin plugin;
    private final Consumer<String> errorLogger;

    /**
     * A {@link Map} storing loaded config instances for use if the config then needs to be saved later.
     */
    private final Map<String, YamlConfiguration> configCache;

    /**
     * Constructs a new instance of ConfigUtils, with the given Spigot {@link JavaPlugin} and error logging
     * {@link Consumer}.
     *
     * @param plugin      The Spigot JavaPlugin instance to use.
     * @param errorLogger A Consumer to use for logging any errors.
     */
    public ConfigUtils(JavaPlugin plugin, Consumer<String> errorLogger) {
        this.plugin = plugin;
        this.errorLogger = errorLogger;

        configCache = new HashMap<>();
    }

    /**
     * Clears the config cache. Any further requests to load configs will be read from disk.
     */
    public void clearCache() {
        configCache.clear();
    }

    /**
     * Prints a formatted error message to the console with a plugin identifier for clarity.
     *
     * @param message The error message to display in console.
     */
    private void printError(String message) {
        errorLogger.accept(colourise(String.format("&6%s &7| &cError: %s", plugin.getName(), message)));
    }

    /**
     * Saves a config with the given config name. Config instances are fetched from the cache.
     *
     * @param configName The name of the config to save.
     * @return           {@code true} if the config saved successfully or {@code false} if either the config failed to
     *                   save, or the config was never loaded.
     */
    public boolean saveConfig(String configName) {
        if (configCache.containsKey(configName)) {
            YamlConfiguration config = configCache.get(configName);
            File configFile = getFileOrCreateBlank(configName);

            try {
                config.save(configFile);
                return true;
            } catch (IOException ex) {
                printError(String.format("Failed to save config %s: %s", configName, ex.getMessage()));
                return false;
            }
        }
        else {
            printError(String.format("Tried to save an unloaded config: %s", configName));
            return false;
        }
    }

    /**
     * Gets or creates the data folder for the plugin.
     *
     * @return The plugin's data folder.
     */
    private File getDataFolder() {
        plugin.getDataFolder().mkdir();
        return plugin.getDataFolder();
    }

    /**
     * Gets or creates a blank YAML configuration file inside the plugin's data folder.
     *
     * @param configName The name of the config to get or create.
     * @return           The fetched or created config file.
     */
    public YamlConfiguration getConfigOrCreateBlank(String configName) {
        File file = getFileOrCreateBlank(configName);

        if (file == null) {
            return null;
        }
        else {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            configCache.put(configName, config);

            return config;
        }
    }

    /**
     * Gets or creates a blank file inside the plugin's data folder.
     *
     * @param fileName The name of the file to get or create.
     * @return         The fetched or created file.
     */
    private File getFileOrCreateBlank(String fileName) {
        File target = new File(getDataFolder(), fileName);

        if (!target.exists()) {
            try {
                target.createNewFile();
            }
            catch (IOException ex) {
                printError(String.format("Failed to create file %s: %s", fileName, ex.getMessage()));
                return null;
            }
        }

        return target;
    }

    /**
     * Gets or creates a YAML config file inside the plugin's data folder, copying content from an existing default
     * resource if it does not exist.
     *
     * @param configName The name of the config to get or create.
     * @return           The fetched or created config file.
     */
    public YamlConfiguration getConfigOrCopyDefault(String configName) {
        File file = getFileOrCopyResource(configName);

        if (file == null) {
            return null;
        }
        else {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            configCache.put(configName, config);

            return config;
        }
    }

    public InputStream getRawResourceStream(String configName) {
        return plugin.getResource(configName);
    }

    /**
     * Gets or creates a file inside the plugin's data folder, copying content from an existing default resource if it
     * does not exist.
     *
     * @param fileName The name of the file to get or create.
     * @return         The fetched or created file.
     */
    private File getFileOrCopyResource(String fileName) {
        File target = new File(getDataFolder(), fileName);

        if (!target.exists()) {
            // Create a new blank file.
            target = getFileOrCreateBlank(fileName);

            if (target == null) {
                return null;
            }

            try (InputStream in = plugin.getResource(fileName); OutputStream out = Files.newOutputStream(target.toPath())) {
                if (in == null) {
                    printError(String.format("Failed to find resource %s", fileName));
                    return null;
                }

                int nextByte;
                while ((nextByte = in.read()) != -1) {
                    out.write(nextByte);
                }
            } catch (IOException ex) {
                printError(String.format("Failed to write resource %s: %s", fileName, ex.getMessage()));
                return null;
            }
        }

        return target;
    }

    /**
     * Colourises a given message using the legacy colour API.
     *
     * @param message The message to colour.
     * @return        The coloured message.
     */
    private static String colourise(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
