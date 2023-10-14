package de.containercloud.database.handler;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoCollection;
import de.containercloud.database.CloudMongoCollection;
import de.containercloud.database.Handler;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.env.EnvConfig;
import de.containercloud.impl.service.ServiceImpl;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MongoServiceHandler extends Handler {

    private final MongoDatabaseHandler databaseHandler;

    public MongoServiceHandler(MongoDatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public CompletableFuture<ServiceImpl> serviceById(String serviceId) {
        MongoCollection<Document> collection = this.collection();

        Publisher<Document> document = collection.find(Filters.eq("serviceId", serviceId)).first();

        CompletableFuture<ServiceImpl> future = new CompletableFuture<>();

        document.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1);
            }

            @Override
            public void onNext(Document document) {
                future.complete(document == null ? null : new Gson().fromJson(document.toJson(), ServiceImpl.class));
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

    public CompletableFuture<ServiceImpl> service(String name) {
        MongoCollection<Document> collection = this.collection();

        Publisher<Document> document = collection.find(Filters.eq("name", name)).first();

        CompletableFuture<ServiceImpl> future = new CompletableFuture<>();
        document.subscribe(new Subscriber<Document>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1L);
            }

            @Override
            public void onNext(Document document) {
                future.complete(document == null ? null : new Gson().fromJson(document.toJson(), ServiceImpl.class));
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

    protected MongoCollection<Document> collection() {
        return this.databaseHandler.collection(EnvConfig.getCollectionEnv(CloudMongoCollection.CollectionTypes.TASK));
    }

    public CompletableFuture<Boolean> existService(String serviceId) {
        MongoCollection<Document> collection = this.collection();
        Publisher<Document> document = collection.find(Filters.eq("serviceId", serviceId)).first();

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        document.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1L);
            }

            @Override
            public void onNext(Document document) {
                future.complete(document != null);
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

    public boolean createService(ServiceImpl service) {

        try {

            if (this.existService(service.serviceId()).get())
                return false;

            this.collection().insertOne(this.getGson().fromJson(this.getGson().toJson(service), Document.class));

        } catch (InterruptedException | ExecutionException exception) {
            exception.fillInStackTrace();
        }

        return true;
    }

    public boolean deleteService(String serviceId) {

        try {

            if (!this.existService(serviceId).get())
                return false;


            this.collection().deleteOne(Filters.eq("serviceId", serviceId));

        } catch (InterruptedException | ExecutionException exception) {
            exception.fillInStackTrace();
        }


        return true;
    }

}
