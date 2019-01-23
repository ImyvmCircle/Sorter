package com.imyvm.Sorter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.logging.Logger;

public class Sorter extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private FileConfiguration config = getConfig();
    private static String tool;
    public SorterListener sorterListener;

    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
    }

    @Override
    public void onEnable() {
        log.info(String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));

        config.addDefault("sorter_tool", "STICK");

        config.options().copyDefaults(true);
        saveConfig();

        tool = config.getString("sorter_tool");
        sorterListener = new SorterListener(this);
        Commands commands = new Commands(this);

        Bukkit.getServer().getPluginManager().registerEvents(sorterListener, this);
        getCommand("si").setExecutor(commands);
    }

    public static String getTool(){
        return tool;
    }
}
