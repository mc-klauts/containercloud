package de.dasshorty.shortcloud.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFile {

    private final File file;
    private final JSONObject jsonObject;

    public JsonFile(String name, File dir) {
        String name1 = name + ".json";

        file = new File(dir, name1);
        this.jsonObject = new JSONObject(readFile());
    }

    /**
     * @param path  the path to the key
     * @param field the key that stores the value
     * @return the Object from key
     */
    public Object get(String path, String field) {
        return translatePath(path).get(field);
    }

    private JSONObject translatePath(String path) {
        String[] paths = path.split("\\.");
        List<JSONObject> objectList = new ArrayList<>();
        for (String pathString : paths) {
            if (objectList.isEmpty()) {
                objectList.add((JSONObject) jsonObject.get(pathString));
            } else {
                JSONObject jsonObject = objectList.get(objectList.size() - 1);
                objectList.add((JSONObject) jsonObject.get(pathString));
            }
        }
        return objectList.get(objectList.size() - 1);
    }

    /**
     * @param key for the value
     * @return the object from key
     */
    public Object get(String key) {
        return jsonObject.get(key);
    }

    /**
     * @param key for the value
     * @return the String from key
     * @see JsonFile#get(String) get the object
     */
    public String getString(String key) {
        return (String) jsonObject.get(key);
    }

    /**
     * @param key that stores the value
     * @param value that contains the value
     */
    public void set(String key, Object value) {
        jsonObject.put(key, value);
        saveFile();
    }

    private JSONObject readFile() {
        try {
            return (JSONObject) new JSONParser().parse(new FileReader(file));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeFile() {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveFile() {
        writeFile();
    }

}
