package de.containercloud.database;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import de.containercloud.config.ConfigHandler;
import lombok.Getter;
import lombok.val;
import org.bson.Document;

public class MongoDatabaseHandler {

    private final MongoDatabase database;
    @Getter
    private final ConfigHandler configHandler;

    public MongoDatabaseHandler(ConfigHandler configHandler) {
        this.configHandler = configHandler;

        MongoClient client = MongoClients.create(setupConnectionString(configHandler));
        this.database = client.getDatabase(configHandler.dataBaseDataBase());

        setupCollections(configHandler);

        Runtime.getRuntime().addShutdownHook(new Thread(client::close));
    }

    public MongoCollection<Document> collection(CloudMongoCollection collection) {
        return this.database.getCollection(collection.collectionName());
    }

    private void setupCollections(ConfigHandler configHandler) {

        val collections = this.database.listCollectionNames();

        for (CloudMongoCollection.CollectionTypes cloudCollection : CloudMongoCollection.CollectionTypes.values()) {

            val collection = configHandler.getCollection(cloudCollection);

            if (!checkCollection(collection, collections))
                this.database.createCollection(collection.collectionName());

        }


    }

    private boolean checkCollection(CloudMongoCollection collection, MongoIterable<String> collections) {

        boolean exist = false;

        for (String mongoCollection : collections)
            if (mongoCollection.equals(collection.collectionName())) {
                exist = true;
                break;
            }


        return exist;
    }

    private ConnectionString setupConnectionString(ConfigHandler configHandler) {

        val user = configHandler.dataBaseUser();
        val password = configHandler.dataBasePassword();
        val dataBase = configHandler.dataBaseDataBase();
        val port = configHandler.dataBasePort();
        val host = configHandler.dataBaseHost();

        return new ConnectionString(
                "mongodb" +
                        (configHandler.isSrvEnabled() ? "+srv://" : "://") +
                        user + ":" + password + "@" + host + ":" + port + "/" + dataBase);

    }
}
