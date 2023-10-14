package de.containercloud.database.handler;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.MongoCollection;
import de.containercloud.database.CloudMongoCollection;
import de.containercloud.database.Handler;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.env.EnvConfig;
import de.containercloud.impl.template.TemplateImpl;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MongoTemplateHandler extends Handler {

    private final MongoDatabaseHandler databaseHandler;

    public MongoTemplateHandler(MongoDatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public CompletableFuture<TemplateImpl> template(String name) {

        Publisher<Document> documentPublisher = this.collection().find(Filters.eq("name", name)).first();

        CompletableFuture<TemplateImpl> future = new CompletableFuture<>();

        documentPublisher.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1L);
            }

            @Override
            public void onNext(Document document) {
                future.complete(document == null ? null : new Gson().fromJson(document.toJson(), TemplateImpl.class));
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

    public CompletableFuture<Boolean> existTemplate(String name) {
        Publisher<Document> documentPublisher = this.collection().find(Filters.eq("name", name)).first();

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        documentPublisher.subscribe(new Subscriber<>() {
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

    public boolean createTask(TemplateImpl template) {

        try {

            if (this.existTemplate(template.name()).get())
                return false;

            this.collection().insertOne(this.getGson().fromJson(this.getGson().toJson(template), Document.class));

        } catch (InterruptedException | ExecutionException exception) {
            exception.fillInStackTrace();
        }

        return true;
    }

    public boolean deleteTask(String name) {

        try {

            if (!this.existTemplate(name).get())
                return false;

            this.collection().deleteOne(Filters.eq("name", name));

        } catch (InterruptedException | ExecutionException exception) {
            exception.fillInStackTrace();
        }


        return true;
    }

    public boolean updateTask(TemplateImpl template) {

        try {

            if (!this.existTemplate(template.name()).get())
                return false;

            this.collection().updateOne(Filters.eq("name", template.name()), List.of(
                    Updates.set("path", template.externalPath())
            ));

        } catch (InterruptedException | ExecutionException exception) {
            exception.fillInStackTrace();
        }

        return true;
    }

    protected MongoCollection<Document> collection() {
        return this.databaseHandler.collection(EnvConfig.getCollectionEnv(CloudMongoCollection.CollectionTypes.TEMPLATE));
    }

}
