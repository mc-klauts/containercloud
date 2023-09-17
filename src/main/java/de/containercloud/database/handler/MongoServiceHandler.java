package de.containercloud.database.handler;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import de.containercloud.config.ConfigHandler;
import de.containercloud.database.CloudMongoCollection;
import de.containercloud.database.Handler;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.impl.service.ServiceImpl;
import de.containercloud.impl.task.TaskImpl;
import lombok.val;
import org.bson.Document;

import java.util.List;
import java.util.UUID;

public class MongoServiceHandler extends Handler {

    private final MongoDatabaseHandler databaseHandler;
    private final ConfigHandler configHandler;

    public MongoServiceHandler(MongoDatabaseHandler databaseHandler, ConfigHandler configHandler) {
        this.databaseHandler = databaseHandler;
        this.configHandler = configHandler;
    }

    public ServiceImpl service(UUID uid) {
        val collection = this.collection();

        val document = collection.find(Filters.eq("uid", uid.toString())).first();

        return document == null ? null : new Gson().fromJson(document.toJson(), ServiceImpl.class);
    }

    public ServiceImpl service(String name) {
        val collection = this.collection();

        val document = collection.find(Filters.eq("name", name)).first();

        return document == null ? null : new Gson().fromJson(document.toJson(), ServiceImpl.class);
    }

    protected MongoCollection<Document> collection() {
        return this.databaseHandler.collection(this.configHandler.getCollection(CloudMongoCollection.CollectionTypes.TASK));
    }

    public boolean existService(UUID uid) {
        val collection = this.collection();
        val document = collection.find(Filters.eq("uid", uid.toString())).first();
        return document != null;
    }

    public boolean createService(ServiceImpl service) {
        val collection = this.collection();

        if (this.existService(service.serviceId()))
            return false;

        collection.insertOne(this.getGson().fromJson(this.getGson().toJson(service), Document.class));
        return true;
    }

    public boolean deleteService(UUID uid) {

        val collection = this.collection();

        if (!this.existService(uid))
            return false;

        this.collection().deleteOne(Filters.eq("uid", uid.toString()));

        return true;
    }

    public boolean updateService(ServiceImpl service) {

        val collection = this.collection();

        if (!this.existService(service.serviceId()))
            return false;

        this.collection().updateOne(Filters.eq("uid", service.serviceId()), List.of(
                Updates.set("serviceConfiguration", service.configuration().toString())
        ));

        return true;
    }

}
