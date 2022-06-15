package com.lighty.dndclothesapi;

import com.lighty.dndclothesapi.sql.Database;
import com.lighty.dndclothesapi.sql.SQLite;
import lombok.Getter;
import net.skinsrestorer.api.SkinsRestorerAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mineskin.MineskinClient;

public final class DNDClothesAPI extends JavaPlugin {

    @Getter private static DNDClothesAPI plugin;
    @Getter private static API api;
    @Getter private static SkinsRestorerAPI skinsRestorerAPI;
    @Getter private static MineskinClient client;
    @Getter private static String cache = "plugins/DNDClothesAPI/cache/";
    @Getter private static Database database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        database = new SQLite(this);
        database.load();

        skinsRestorerAPI = SkinsRestorerAPI.getApi();
        client = new MineskinClient();

        api = new API(this, database);
        plugin.saveDefaultConfig();
        
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
