package de.containercloud.database;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import de.containercloud.env.EnvConfig;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CompletableFuture;

public class MongoDatabaseHandler {

    private final MongoDatabase database;

    public MongoDatabaseHandler() {


        try (MongoClient client = MongoClients.create(setupConnectionString())) {
            this.database = client.getDatabase(EnvConfig.getEnv("DATABASE.DATABASE"));

            setupCollections();
            //ShutdownService.addShutdown(100, o -> client.close());
        }
    }

    public MongoCollection<Document> collection(CloudMongoCollection collection) {
        return this.database.getCollection(collection.collectionName());
    }

    private void setupCollections() {

        Publisher<String> collections = this.database.listCollectionNames();

        for (CloudMongoCollection.CollectionTypes cloudCollection : CloudMongoCollection.CollectionTypes.values()) {

            CloudMongoCollection collection = EnvConfig.getCollectionEnv(cloudCollection);

            if (!checkCollection(collection, collections).getNow(false))
                this.database.createCollection(collection.collectionName());

        }


    }

    private CompletableFuture<Boolean> checkCollection(CloudMongoCollection collection, Publisher<String> collections) {

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        collections.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(CloudMongoCollection.CollectionTypes.values().length);
            }

            @Override
            public void onNext(String dbCollection) {
                if (dbCollection.equals(collection.collectionName()))
                    future.complete(true);
            }

            @Override
            public void onError(Throwable t) {
                t.fillInStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });

        return future;
    }

    private ConnectionString setupConnectionString() {

        String user = EnvConfig.getEnv("DATABASE.USER");
        String password = EnvConfig.getEnv("DATABASE.PASSWORD");
        String dataBase = EnvConfig.getEnv("DATABASE.DATABASE");
        String port = EnvConfig.getEnv("DATABASE.PORT");
        String host = EnvConfig.getEnv("DATABASE.HOST");

        return new ConnectionString(
                "mongodb://" +
                        user + ":" + password + "@" + host + ":" + port + "/?authMechanism=SCRAM-SHA-256&authSource=" + dataBase);

    }
}
