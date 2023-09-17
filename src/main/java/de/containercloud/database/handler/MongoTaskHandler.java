package de.containercloud.database.handler;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import de.containercloud.config.ConfigHandler;
import de.containercloud.database.CloudMongoCollection.CollectionTypes;
import de.containercloud.database.Handler;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.impl.task.TaskImpl;
import lombok.val;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

public class MongoTaskHandler extends Handler {

    private final MongoDatabaseHandler databaseHandler;
    private final ConfigHandler configHandler;

    public MongoTaskHandler(MongoDatabaseHandler databaseHandler, ConfigHandler configHandler) {
        this.databaseHandler = databaseHandler;
        this.configHandler = configHandler;
    }

    public TaskImpl task(UUID uid) {
        val collection = this.collection();

        val document = collection.find(Filters.eq("uid", uid.toString())).first();

        return document == null ? null : new Gson().fromJson(document.toJson(), TaskImpl.class);
    }

    protected MongoCollection<Document> collection() {
        return this.databaseHandler.collection(this.configHandler.getCollection(CollectionTypes.TASK));
    }

    public boolean existTask(UUID uid) {
        val collection = this.collection();
        val document = collection.find(Filters.eq("uid", uid.toString())).first();
        return document != null;
    }

    public boolean createTask(TaskImpl task) {
        val collection = this.collection();

        if (this.existTask(task.uid()))
            return false;

        collection.insertOne(this.getGson().fromJson(this.getGson().toJson(task), Document.class));
        return true;
    }

    public boolean deleteTask(UUID uid) {

        val collection = this.collection();

        if (!this.existTask(uid))
            return false;

        this.collection().deleteOne(Filters.eq("uid", uid.toString()));

        return true;
    }

    public boolean updateTask(TaskImpl task) {

        val collection = this.collection();

        if (!this.existTask(task.uid()))
            return false;

        this.collection().updateOne(Filters.eq("uid", task.uid()), List.of(
                Updates.set("type", task.type().toString()),
                Updates.set("runningServices", task.runningServices()),
                Updates.set("template", task.template().toString())
        ));

        return true;
    }


}
