package com.lighty.dndclothesapi.utils;

import com.google.gson.JsonObject;

import com.lighty.dndclothesapi.API;
import com.lighty.dndclothesapi.DNDClothesAPI;
import lombok.SneakyThrows;
import net.skinsrestorer.api.PlayerWrapper;
import net.skinsrestorer.api.property.IProperty;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class MojangAPI {

    @SneakyThrows
    public static void update(Player p, API.SkinType skinType) {
        new BukkitRunnable(){
            @SneakyThrows
            @Override
            public void run() {

                ArrayList<JsonObject> orderedClothes = DNDClothesAPI.getApi().getOrderedClothes(p);
                BufferedImage baseBody = DNDClothesAPI.getApi().getBaseBodyImage(p);

                for(int i = 0; i < orderedClothes.size(); i++) {
                    BufferedImage cloth = ImageIO.read(DNDClothesAPI.getApi().getClothFile(orderedClothes.get(i)));

                    Graphics graphics = baseBody.getGraphics();
                    graphics.drawImage(cloth,0, 0, null);
                    graphics.dispose();
                }

                String cacheUUID = UUID.randomUUID().toString();
                File file = new File(DNDClothesAPI.getCache() + cacheUUID + ".png");
                ImageIO.write(baseBody, "png", file);

                String skinUrl = DNDClothesAPI.getClient().generateUpload(file).get().data.texture.url;
                IProperty skin = DNDClothesAPI.getSkinsRestorerAPI().genSkinUrl(skinUrl, skinType.getSkinVariant());

                DNDClothesAPI.getSkinsRestorerAPI().applySkin(new PlayerWrapper(p), skin);
                file.delete();
            }
        }.runTaskAsynchronously(DNDClothesAPI.getPlugin());
    }
}
