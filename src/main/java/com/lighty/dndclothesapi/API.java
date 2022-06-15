package com.lighty.dndclothesapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lighty.dndclothesapi.sql.Database;
import com.lighty.dndclothesapi.utils.Base64Utils;
import com.lighty.dndclothesapi.utils.MojangAPI;
import lombok.Getter;
import lombok.Setter;
import net.skinsrestorer.api.SkinVariant;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.ToIntFunction;

public class API {

    @Getter @Setter private static DNDClothesAPI plugin;
    @Getter @Setter private static Database database;

    public API(DNDClothesAPI plugin, Database database) {
        setPlugin(plugin); setDatabase(database);
    }

    public void setBaseBody(Player player, String base64) {
        database.setValue(player, base64, clothesToJSON(getClothes(player)).toString());
    }

    public void setBaseBody(Player player, BufferedImage skinImage) {
        String base64 = Base64Utils.imgToBase64String(skinImage, "png");
        database.setValue(player, base64, clothesToJSON(getClothes(player)).toString());
    }

    public enum SkinType {
        STEVE(SkinVariant.CLASSIC),
        ALEX(SkinVariant.SLIM);

        @Getter SkinVariant skinVariant;

        SkinType(SkinVariant variant) {
            skinVariant = variant;
        }
    };

    public BufferedImage getBaseBodyImage(Player player) {
        String base64 = DNDClothesAPI.getDatabase().getValue(player, "basebody");
        return Base64Utils.base64StringToImg(base64);
    }

    public String getBaseBodyBase64(Player player) {
        String base64 = DNDClothesAPI.getDatabase().getValue(player, "basebody");
        return base64;
    }

    public ArrayList<JsonObject> getClothes(Player player) {
        String clothesJSONString = DNDClothesAPI.getDatabase().getValue(player, "clothes");
        JsonObject clothesJSON = (JsonObject) new JsonParser().parse(clothesJSONString);
        JsonArray clothesJSONArray = clothesJSON.getAsJsonArray("clothes");
        ArrayList<JsonObject> clothes = new ArrayList<>();

        clothesJSONArray.forEach(cloth -> {
            clothes.add(cloth.getAsJsonObject());
        });

        return clothes;
    }

    public JsonObject clothesToJSON(ArrayList<JsonObject> clothes) {
        JsonObject clothesJson = new JsonObject();
        JsonArray clothesJsonArray = new JsonArray();

        clothes.forEach(clothesJsonArray::add);

        clothesJson.add("clothes", clothesJsonArray);
        return clothesJson;
    }

    public void addCloth(Player player, String clothName, String category, Integer priority) {
        ArrayList<JsonObject> clothes = getClothes(player);
        JsonObject newCloth = new JsonObject();

        newCloth.addProperty("name", clothName);
        newCloth.addProperty("priority", priority);
        newCloth.addProperty("category", category);
        clothes.add(newCloth);

        database.setValue(player, getBaseBodyBase64(player), clothesToJSON(clothes).toString());
    }

    public void removeCloth(Player player, String clothName) {
        ArrayList<JsonObject> clothes = getClothes(player);

        clothes.removeIf(jsonObject -> jsonObject.get("name").getAsString().equalsIgnoreCase(clothName));

        database.setValue(player, getBaseBodyBase64(player), clothesToJSON(clothes).toString());
    }

    public File getClothFile(JsonObject cloth) {
        String category = cloth.get("category").toString();
        String clothName = cloth.get("name").toString();
        return new File("plugins/clothAPI/clothes/" + category + "/" + clothName + ".png");
    }

    public ArrayList<JsonObject> getOrderedClothes(Player player) {
        ArrayList<JsonObject> clothes = getClothes(player);
        ArrayList<JsonObject> orderedClothes = new ArrayList<>();

        for(int i = 0; i < clothes.size(); i++) {
            if(clothes.get(i).get("category").getAsString().equalsIgnoreCase("face")) {
                prioritize(clothes, orderedClothes, i);
            }
            if(clothes.get(i).get("category").getAsString().equalsIgnoreCase("shoulder")) {
                prioritize(clothes, orderedClothes, i);
            }
            else if(clothes.get(i).get("category").getAsString().equalsIgnoreCase("hands")) {
                prioritize(clothes, orderedClothes, i);
            }
            else if(clothes.get(i).get("category").getAsString().equalsIgnoreCase("chest")) {
                prioritize(clothes, orderedClothes, i);
            }
            else if(clothes.get(i).get("category").getAsString().equalsIgnoreCase("knees")) {
                prioritize(clothes, orderedClothes, i);
            }
            else if(clothes.get(i).get("category").getAsString().equalsIgnoreCase("feet")) {
                prioritize(clothes, orderedClothes, i);
            }
            else if(clothes.get(i).get("category").getAsString().equalsIgnoreCase("head")) {
                prioritize(clothes, orderedClothes, i);
            }
            else if(clothes.get(i).get("category").getAsString().equalsIgnoreCase("torso & arms")) {
                prioritize(clothes, orderedClothes, i);
            }
            else if(clothes.get(i).get("category").getAsString().equalsIgnoreCase("legs")) {
                prioritize(clothes, orderedClothes, i);
            }
        }

        return orderedClothes;
    }

    private void prioritize(ArrayList<JsonObject> clothes, ArrayList<JsonObject> orderedClothes, int i) {
        if(clothes.get(i).get("priority").getAsInt() == 1) orderedClothes.add(i, clothes.get(i));
        else if(clothes.get(i).get("priority").getAsInt() == 2) orderedClothes.add(i, clothes.get(i));
        else if(clothes.get(i).get("priority").getAsInt() == 3) orderedClothes.add(i, clothes.get(i));
        else orderedClothes.add(i, clothes.get(i));
    }

    public void applySkin(Player player, SkinType skinType) {
        MojangAPI.update(player, skinType);
    }

}
