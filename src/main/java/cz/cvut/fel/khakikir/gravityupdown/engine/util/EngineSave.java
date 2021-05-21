package cz.cvut.fel.khakikir.gravityupdown.engine.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class EngineSave {
    private static final Logger LOGGER = Logger.getLogger(EngineSave.class.getName());

    private Map<String, String> data = new HashMap<>();
    private String name;
    private String path;

    /**
     * Saves the data and associates it with a specified key.
     *
     * @param key    The key.
     * @param object The data.
     */
    public void put(String key, String object) {
        data.put(key, object);
    }

    /**
     * Returns the saved data to which the specified key is mapped.
     *
     * @param key the key whose associated data is to be returned.
     * @return the data associated with the key, or null if no key is found.
     */
    public String get(String key) {
        return data.getOrDefault(key, null);
    }

    /**
     * Binds this `EngineSave` to a certain file and reads the data.
     *
     * @param fileName A file name.
     */
    public void bind(String fileName) {
        this.name = fileName;
        this.path = new File(".").getPath() + "/" + this.name + ".json";
        var file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                LOGGER.severe("Can't create a new save file");
            }
        }

        load();
    }

    /**
     * Save everything stored in the object to file.
     *
     * @return Returns false if the save object is not bound yet.
     */
    public boolean flush() {
        if (name != null) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                var writer = mapper.writerWithDefaultPrettyPrinter();
                writer.writeValue(new File(path), data);
            } catch (JsonProcessingException e) {
                LOGGER.severe("Can't serialize the EngineSave data");
            } catch (IOException e) {
                LOGGER.severe("Can't write the serialized EngineSave to file");
            }

            return true;
        }

        return false;
    }

    /**
     * Loads data from the file to the object.
     *
     * @return Returns false if the save object is not bound yet.
     */
    public boolean load() {
        if (name != null) {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<HashMap<String, String>> typeRef = new TypeReference<>() {};
            try {
                data = mapper.readValue(new File(path), typeRef);
            } catch (JsonProcessingException e) {
                LOGGER.severe("Can't deserialize the save file");
            } catch (IOException e) {
                LOGGER.severe("Can't read the save file");
            }

            return true;
        }

        return false;
    }

    /**
     * Erases everything stored in the object.
     * Data is immediately erased and the object is saved that way.
     *
     * @return Returns false if the save object is not bound yet.
     */
    public boolean erase() {
        if (name != null) {
            this.data.clear();
            return true;
        }

        return false;
    }
}
