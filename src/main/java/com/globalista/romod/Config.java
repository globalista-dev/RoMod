package com.globalista.romod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Config {

    /*
    This config was created using the tutorial available on
    https://www.quiltservertools.net/ServerSideDevDocs/config/gson_config/,
    since I did not want to use an API just for a simple config.
    Huge thanks to the authors!
    */

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .disableHtmlEscaping()
            .create();

    public String slab = "Enable Reinforced Obsidian Slab (default: false)";
    public boolean slabEnable = false;
    public String stairs = "Enable Reinforced Obsidian Stairs (default: false)";
    public boolean stairsEnable = false;
    public String wall = "Enable Reinforced Obsidian Wall (default: false)";
    public boolean wallEnable = false;
    public String glass = "Enable Reinforced Glass (default: false)";
    public boolean glassEnable = false;
    public String tintedGlass = "Enable Reinforced Tinted Glass (default: false)";
    public boolean tintedGlassEnable = false;

    public static Config loadConfigFile(File file) {
        Config config = null;

        if (file.exists()) {
            try (BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
            )) {
                config = gson.fromJson(fileReader, Config.class);
            } catch (IOException e) {
                throw new RuntimeException("[Reinforced Obsidian] Problem occurred when trying to load config: ", e);
            }
        }
        if (config == null) {
            config = new Config();
        }
        config.saveConfigFile(file);
        return config;
    }

    public void saveConfigFile(File file) {
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
