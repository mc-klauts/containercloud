package de.containercloud.database.handler;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import de.containercloud.database.CloudMongoCollection.CollectionTypes;
import de.containercloud.database.Handler;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.env.EnvConfig;
import de.containercloud.impl.task.TaskImpl;
import lombok.val;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoTaskHandler extends Handler {

    private final MongoDatabaseHandler databaseHandler;

    public MongoTaskHandler(MongoDatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public List<TaskImpl> tasks() {

        val documents = collection().find();


        val list = new ArrayList<TaskImpl>();

        documents.forEach(document -> list.add(getGson().fromJson(document.toJson(), TaskImpl.class)));

        return list;
    }

    public TaskImpl task(String taskId) {
        val collection = this.collection();

        val document = collection.find(Filters.eq("taskId", taskId)).first();

        return document == null ? null : new Gson().fromJson(document.toJson(), TaskImpl.class);
    }

    protected MongoCollection<Document> collection() {
        return this.databaseHandler.collection(EnvConfig.getCollectionEnv(CollectionTypes.TASK));
    }

    public boolean existTask(String taskId) {
        val collection = this.collection();
        val document = collection.find(Filters.eq("taskId", taskId)).first();
        return document != null;
    }

    public boolean createTask(TaskImpl task) {
        val collection = this.collection();

        if (this.existTask(task.taskId()))
            return false;

        collection.insertOne(this.getGson().fromJson(this.getGson().toJson(task), Document.class));
        return true;
    }

    public boolean deleteTask(String taskId) {

        if (!this.existTask(taskId))
            return false;

        this.collection().deleteOne(Filters.eq("taskId", taskId));

        return true;
    }

    public boolean updateTask(TaskImpl task) {

        if (!this.existTask(task.taskId()))
            return false;

        this.collection().updateOne(Filters.eq("taskId", task.taskId()), List.of(
                Updates.set("type", task.configuration().version().type().name()),
                Updates.set("template", task.getTemplate())
        ));

        return true;
    }


}
