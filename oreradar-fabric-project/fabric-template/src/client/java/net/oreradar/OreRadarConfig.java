package net.oreradar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Ganz normale JSON-Config, wie sie viele kleine Fabric-Mods benutzen.
 * Wird unter .minecraft/config/oreradar.json gespeichert.
 *
 * Die Taste selbst muss hier NICHT gespeichert werden - Minecraft speichert
 * Keybindings automatisch selbst in options.txt, sobald man sie in
 * Optionen -> Steuerung ändert.
 */
public class OreRadarConfig {

    public int searchRadius = 24;        // wie weit (in Blöcken) um den Spieler herum gesucht wird
    public int searchIntervalTicks = 20; // alle wie viele Ticks neu gesucht wird (20 Ticks = 1 Sekunde)

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Path configPath;

    public static OreRadarConfig load() {
        configPath = FabricLoader.getInstance().getConfigDir().resolve("oreradar.json");

        if (Files.exists(configPath)) {
            try {
                String json = Files.readString(configPath);
                OreRadarConfig config = GSON.fromJson(json, OreRadarConfig.class);
                if (config != null) {
                    return config;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        OreRadarConfig fresh = new OreRadarConfig();
        fresh.save();
        return fresh;
    }

    public void save() {
        try {
            Files.writeString(configPath, GSON.toJson(this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
