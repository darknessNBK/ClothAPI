package com.lighty.dndclothesapi.sql;

import com.lighty.dndclothesapi.DNDClothesAPI;

import java.util.logging.Level;

public class Error {
    public static void execute(DNDClothesAPI plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(DNDClothesAPI plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
