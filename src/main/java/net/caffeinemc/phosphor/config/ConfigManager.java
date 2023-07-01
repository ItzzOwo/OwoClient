package net.caffeinemc.phosphor.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * Manages a config, by holding it, saving it and loading it.
 *
 * @param <T> The type of the config
 */
@Slf4j
public class ConfigManager<T> {
    private final ConfigSerializer<T> serializer;

    /**
     * The class of the held config
     */
    @Getter
    private final Class<T> configClass;

    /**
     * The config held by the manager.
     */
    @Getter
    private final T config;

    /**
     * Creates a manager.
     *
     * @param configClass The class of the config
     * @param serializer  The serializer
     * @param config      The initial config
     */
    public ConfigManager(Class<T> configClass, ConfigSerializer<T> serializer, T config) {
        this.configClass = configClass;
        this.serializer = serializer;
        this.config = config;
    }

    /**
     * Creates a manager which provides an initial config by deserializing the file.
     *
     * @param configClass The class of the config
     * @param serializer  The serializer
     */
    public ConfigManager(Class<T> configClass, ConfigSerializer<T> serializer) {
        this(configClass, serializer, serializer.deserialize());
    }

    /**
     * Creates a default config manager, with a default config serializer.
     * The config will be saved to and read from <code>./config/[name].toml</code> (without the brackets).
     *
     * @param configClass The class of the config
     * @param name        The name of the config, used for the filename
     * @param <T>         The type of the config
     * @return The generated config manager
     */
    public static <T> ConfigManager<T> create(Class<T> configClass, String name) {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(name + ".json");
        Supplier<T> defaultConfig = () -> newInstance(configClass);
        return new ConfigManager<>(configClass, new ConfigSerializer<>(configClass, path.toFile(), defaultConfig));
    }

    /**
     * Saves the stored config.
     */
    public void saveConfig() {
        serializer.serialize(config);
    }

    private static <T> T newInstance(Class<T> klass) {
        try {
            Constructor<T> constructor = klass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InstantiationException e) {
            log.error("{} does not have a public no-arg constructor!", klass.getName());
            throw new NoSuchElementException(e);
        } catch (Exception e) {
            log.error("Could not instantiate class {}", klass.getName());
            throw new IllegalArgumentException(e);
        }
    }
}
