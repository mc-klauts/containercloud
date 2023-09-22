package de.containercloud.env;

import de.containercloud.database.CloudMongoCollection;

public class EnvConfig {

    public static String getEnv(String key) {

        if (!System.getenv().containsKey(key)) {
            throw new NullPointerException("Env Variable " + key + " has to contain data!");
        }

        return System.getenv(key);
    }

    public static CloudMongoCollection getCollectionEnv(CloudMongoCollection.CollectionTypes type) {

        if (!System.getenv().containsKey("DATABASE." + type.name().toUpperCase()))
            return new CloudMongoCollection(type, type.getType());

        return new CloudMongoCollection(type, System.getenv("DATABASE." + type.name().toUpperCase()));

    }

}
