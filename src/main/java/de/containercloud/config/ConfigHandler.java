package de.containercloud.config;

import de.containercloud.ContainerCloud;
import de.containercloud.database.CloudMongoCollection;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class ConfigHandler {

    private final JsonFile jsonFile;

    public ConfigHandler(Logger logger) {

        val dir = new File("/home/cloud");
        val file = new File(dir, "settings.json");

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                logger.error("Cloud can't create folders! [ConfigHandler:dir]");
                throw new RuntimeException("Cloud can't create folders! [ConfigHandler:e:dir]");
            }

            try {
                if (!file.exists()) {
                    FileUtils.copyURLToFile(new URL("https://data.theccloud.de/settings.json"), file);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        this.jsonFile = new JsonFile("settings", dir);

        new ConfigEnvChecker(this);
    }

    private JSONObject dataBase() {
        return ((JSONObject) this.jsonFile.get("database"));
    }

    public String dataBaseHost() {
        return ((String) dataBase().get("host"));
    }

    public String dataBasePort() {
        return ((String) dataBase().get("port"));
    }

    public String dataBaseUser() {
        return ((String) dataBase().get("user"));
    }

    public String dataBaseDataBase() {
        return ((String) dataBase().get("database"));
    }

    public String dataBasePassword() {
        return ((String) dataBase().get("password"));
    }

    public boolean isSrvEnabled() {
        return ((boolean) dataBase().get("enableSrv"));
    }

    @SuppressWarnings("unchecked")
    public void setDataBase(String path, Object value) {
        val jsonObject = dataBase();
        jsonObject.put(path, value);
        this.jsonFile.set("database", jsonObject);
        this.jsonFile.saveFile();
    }

    public CloudMongoCollection getCollection(CloudMongoCollection.CollectionTypes collection) {
        return new CloudMongoCollection(collection, this.jsonFile.getString("database.collections." + collection.getType()));
    }
}
