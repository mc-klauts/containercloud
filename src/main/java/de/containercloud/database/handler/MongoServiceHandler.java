package de.containercloud.database.handler;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.containercloud.database.CloudMongoCollection;
import de.containercloud.database.Handler;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.env.EnvConfig;
import de.containercloud.impl.service.ServiceImpl;
import lombok.val;
import org.bson.Document;

public class MongoServiceHandler extends Handler {

    private final MongoDatabaseHandler databaseHandler;

    public MongoServiceHandler(MongoDatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public ServiceImpl serviceById(String serviceId) {
        val collection = this.collection();

        val document = collection.find(Filters.eq("serviceId", serviceId)).first();

        return document == null ? null : new Gson().fromJson(document.toJson(), ServiceImpl.class);
    }

    public ServiceImpl service(String name) {
        val collection = this.collection();

        val document = collection.find(Filters.eq("name", name)).first();

        return document == null ? null : new Gson().fromJson(document.toJson(), ServiceImpl.class);
    }

    protected MongoCollection<Document> collection() {
        return this.databaseHandler.collection(EnvConfig.getCollectionEnv(CloudMongoCollection.CollectionTypes.TASK));
    }

    public boolean existService(String serviceId) {
        val collection = this.collection();
        val document = collection.find(Filters.eq("serviceId", serviceId)).first();
        return document != null;
    }

    public boolean createService(ServiceImpl service) {
        val collection = this.collection();

        if (this.existService(service.serviceId()))
            return false;

        collection.insertOne(this.getGson().fromJson(this.getGson().toJson(service), Document.class));
        return true;
    }

    public boolean deleteService(String serviceId) {

        val collection = this.collection();

        if (!this.existService(serviceId))
            return false;

        this.collection().deleteOne(Filters.eq("serviceId", serviceId));

        return true;
    }

    //public boolean updateService(ServiceImpl service) {
//
    //    val collection = this.collection();
//
    //    if (!this.existService(service.serviceId()))
    //        return false;
//
    //    this.collection().updateOne(Filters.eq("serviceId", service.serviceId()), List.of(
    //            Updates.set("serviceConfiguration", service.configuration().toString())
    //    ));
//
    //    return true;
    //}

}
