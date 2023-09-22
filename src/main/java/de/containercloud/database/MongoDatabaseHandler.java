package de.containercloud.database;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import de.containercloud.env.EnvConfig;
import lombok.val;
import org.bson.Document;

public class MongoDatabaseHandler {

    private final MongoDatabase database;

    public MongoDatabaseHandler() {


        MongoClient client = MongoClients.create(setupConnectionString());
        this.database = client.getDatabase(EnvConfig.getEnv("DATABASE.DATABASE"));

        setupCollections();

        Runtime.getRuntime().addShutdownHook(new Thread(client::close));
    }

    public MongoCollection<Document> collection(CloudMongoCollection collection) {
        return this.database.getCollection(collection.collectionName());
    }

    private void setupCollections() {

        val collections = this.database.listCollectionNames();

        for (CloudMongoCollection.CollectionTypes cloudCollection : CloudMongoCollection.CollectionTypes.values()) {

            val collection = EnvConfig.getCollectionEnv(cloudCollection);

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

    private ConnectionString setupConnectionString() {

        val user = EnvConfig.getEnv("DATABASE.USER");
        val password = EnvConfig.getEnv("DATABASE.PASSWORD");
        val dataBase = EnvConfig.getEnv("DATABASE.DATABASE");
        val port = EnvConfig.getEnv("DATABASE.PORT");
        val host = EnvConfig.getEnv("DATABASE.HOST");

        return new ConnectionString(
                "mongodb://" +
                        user + ":" + password + "@" + host + ":" + port + "/" + dataBase);

    }
}
