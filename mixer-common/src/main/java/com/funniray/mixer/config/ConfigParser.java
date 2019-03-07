package com.funniray.mixer.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigParser {
    public static Config loadConfig(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            Config config = new Config();
            saveConfig(path,config);
        }
        Gson gson = new Gson();
        return gson.fromJson(new FileReader(file), Config.class);
    }

    public static void saveConfig(String path, Config config) throws IOException {
        if (!config.shouldSaveToFile)
            return;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.createDirectory(Paths.get(path).getParent());
        Files.write(Paths.get(path),gson.toJson(config).getBytes());
    }
}
