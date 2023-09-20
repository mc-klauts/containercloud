package de.containercloud.config;

import de.containercloud.ContainerCloud;
import de.containercloud.database.CloudMongoCollection;
import lombok.val;
import org.apache.commons.io.FileUtils;
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

    public String dataBaseHost() {
        return this.jsonFile.getString("database.host");
    }

    public String dataBasePort() {
        return this.jsonFile.getString("database.port");
    }

    public String dataBaseUser() {
        return this.jsonFile.getString("database.user");
    }

    public String dataBaseDataBase() {
        return this.jsonFile.getString("database.database");
    }

    public String dataBasePassword() {
        return this.jsonFile.getString("database.password");
    }

    public boolean isSrvEnabled() {
        return ((boolean) this.jsonFile.get("database.enableSrv"));
    }

    public void setDataBase(String path, Object value) {
        this.jsonFile.set("database." + path, value);
        this.jsonFile.saveFile();
    }

    public CloudMongoCollection getCollection(CloudMongoCollection.CollectionTypes collection) {
        return new CloudMongoCollection(collection, this.jsonFile.getString("database.collections." + collection.getType()));
    }
}
