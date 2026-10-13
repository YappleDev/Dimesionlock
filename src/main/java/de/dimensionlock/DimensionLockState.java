package de.dimensionlock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Haelt den Sperrstatus und speichert ihn in config/dimensionlock.json. */
public final class DimensionLockState {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("dimensionlock.json");

    private static Data data = new Data();

    private static final class Data {
        boolean nether = false;
        boolean end = false;
    }

    private DimensionLockState() {}

    public static void load() {
        if (Files.exists(FILE)) {
            try {
                Data loaded = GSON.fromJson(Files.readString(FILE), Data.class);
                if (loaded != null) data = loaded;
            } catch (Exception e) {
                DimensionLockMod.LOGGER.error("Konnte dimensionlock.json nicht lesen", e);
            }
        } else {
            save();
        }
    }

    private static void save() {
        try {
            Files.writeString(FILE, GSON.toJson(data));
        } catch (IOException e) {
            DimensionLockMod.LOGGER.error("Konnte dimensionlock.json nicht speichern", e);
        }
    }

    public static boolean isLocked(String name) {
        return switch (name) {
            case "nether" -> data.nether;
            case "end" -> data.end;
            default -> false;
        };
    }

    public static boolean isLocked(ResourceKey<Level> dim) {
        if (dim == Level.NETHER) return data.nether;
        if (dim == Level.END) return data.end;
        return false;
    }

    public static void setLocked(String name, boolean locked) {
        switch (name) {
            case "nether" -> data.nether = locked;
            case "end" -> data.end = locked;
            default -> { return; }
        }
        save();
    }
}
