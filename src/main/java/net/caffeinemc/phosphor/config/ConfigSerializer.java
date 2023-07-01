package net.caffeinemc.phosphor.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.type.ImBoolean;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.function.Supplier;

@Slf4j
public class ConfigSerializer<T> {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ImBoolean.class, new ImBoolAdapter())
            .setPrettyPrinting()
            .create();

    private final Class<T> configClass;
    private final File file;
    private final Supplier<T> defaultConfig;

    /**
     * Creates a serializer.
     *
     * @param configClass   The class of the config
     * @param file          The file to save the config into
     * @param defaultConfig The default config
     */
    public ConfigSerializer(Class<T> configClass, File file, Supplier<T> defaultConfig) {
        this.configClass = configClass;
        this.file = file;
        this.defaultConfig = defaultConfig;
    }

    /**
     * Reads the config from the file.
     * If the file isn't found or is corrupted, the file is overwritten by the default config.
     *
     * @return The deserialized config
     */
    public T deserialize() {
        if (!Files.isRegularFile(file.toPath())) {
            File parent = file.getParentFile();

            if (!parent.mkdirs() && !Files.isDirectory(parent.toPath())) {
                log.warn("Could not create directory {}", parent.getAbsolutePath());
            } else {
                serialize(defaultConfig.get());
            }

            return defaultConfig.get();
        }

        try (Reader r = new BufferedReader(new FileReader(file))) {
            return GSON.fromJson(r, configClass);
        } catch (Exception e) {
            log.warn("A corrupted configuration file was found, overwriting it with the default config", e);
            serialize(defaultConfig.get());
            return defaultConfig.get();
        }
    }

    public void serialize(T config) {
        try (Writer w = new BufferedWriter(new FileWriter(file))) {
            GSON.toJson(config, w);
        } catch (IOException e) {
            log.warn("Could not write config", e);
        }
    }
}
