package de.dasshorty.shortcloud.config;

import de.dasshorty.shortcloud.database.CloudMongoCollection;
import lombok.val;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {

    private final JsonFile jsonFile;

    public ConfigHandler(Logger logger) {

        val dir = new File("/cloud");
        val file = new File(dir, "settings.yml");

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                logger.error("Cloud can't create folders! [ConfigHandler:dir]");
                throw new RuntimeException("Cloud can't create folders! [ConfigHandler:e:dir]");
            }

            try {
                if (!file.createNewFile()) {
                    logger.error("Cloud can't create files! [ConfigHandler:file]");
                    throw new RuntimeException("Cloud can't create folders! [ConfigHandler:e:file]");
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
