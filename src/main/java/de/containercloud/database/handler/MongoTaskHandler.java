package de.containercloud.database.handler;

import com.google.gson.Gson;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.reactivestreams.client.FindPublisher;
import com.mongodb.reactivestreams.client.MongoCollection;
import de.containercloud.database.CloudMongoCollection.CollectionTypes;
import de.containercloud.database.Handler;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.env.EnvConfig;
import de.containercloud.impl.task.TaskImpl;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MongoTaskHandler extends Handler {

    private final MongoDatabaseHandler databaseHandler;

    public MongoTaskHandler(MongoDatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public List<TaskImpl> tasks() {

        FindPublisher<Document> documentFindPublisher = collection().find();

        ArrayList<TaskImpl> list = new ArrayList<>();

        documentFindPublisher.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Document document) {
                list.add(getGson().fromJson(document.toJson(), TaskImpl.class));
            }

            @Override
            public void onError(Throwable t) {
                t.fillInStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });

        return list;
    }

    public CompletableFuture<TaskImpl> task(String taskId) {

        Publisher<Document> documentPublisher = this.collection().find(Filters.eq("taskId", taskId)).first();

        CompletableFuture<TaskImpl> future = new CompletableFuture<>();

        documentPublisher.subscribe(new Subscriber<Document>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1L);
            }

            @Override
            public void onNext(Document document) {
                future.complete(document == null ? null : new Gson().fromJson(document.toJson(), TaskImpl.class));
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
        return this.databaseHandler.collection(EnvConfig.getCollectionEnv(CollectionTypes.TASK));
    }

    public CompletableFuture<Boolean> existTask(String taskId) {
        Publisher<Document> documentPublisher = this.collection().find(Filters.eq("taskId", taskId)).first();

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        documentPublisher.subscribe(new Subscriber<Document>() {
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

    public boolean createTask(@NotNull TaskImpl task) {

        try {

            if (this.existTask(task.taskId()).get())
                return false;

            this.collection().insertOne(this.getGson().fromJson(this.getGson().toJson(task), Document.class));

        } catch (InterruptedException | ExecutionException exception) {
            exception.fillInStackTrace();
        }

        return true;
    }

    public boolean deleteTask(String taskId) {

        try {
            if (!this.existTask(taskId).get())
                return false;

            this.collection().deleteOne(Filters.eq("taskId", taskId));

        } catch (InterruptedException | ExecutionException exception) {
            exception.fillInStackTrace();
        }

        return true;
    }

    public boolean updateTask(@NotNull TaskImpl task) {

        try {
            if (!this.existTask(task.taskId()).get())
                return false;

            this.collection().updateOne(Filters.eq("taskId", task.taskId()), List.of(
                    Updates.set("type", task.configuration().version().type().name()),
                    Updates.set("template", task.getTemplate())
            ));

        } catch (InterruptedException | ExecutionException exception) {
            exception.fillInStackTrace();
        }



        return true;
    }


}
