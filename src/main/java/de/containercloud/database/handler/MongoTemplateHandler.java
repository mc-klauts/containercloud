package de.containercloud.database.handler;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import de.containercloud.database.CloudMongoCollection;
import de.containercloud.database.Handler;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.env.EnvConfig;
import de.containercloud.impl.template.TemplateImpl;
import lombok.val;
import org.bson.Document;

import java.util.List;

public class MongoTemplateHandler extends Handler {

    private final MongoDatabaseHandler databaseHandler;

    public MongoTemplateHandler(MongoDatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public TemplateImpl template(String name) {
        val collection = this.collection();

        val document = collection.find(Filters.eq("name", name)).first();

        return document == null ? null : new Gson().fromJson(document.toJson(), TemplateImpl.class);
    }

    public boolean existTemplate(String name) {
        val collection = this.collection();
        val document = collection.find(Filters.eq("name", name)).first();
        return document != null;
    }

    public boolean createTask(TemplateImpl template) {
        val collection = this.collection();

        if (this.existTemplate(template.name()))
            return false;

        collection.insertOne(this.getGson().fromJson(this.getGson().toJson(template), Document.class));
        return true;
    }

    public boolean deleteTask(String name) {

        val collection = this.collection();

        if (!this.existTemplate(name))
            return false;

        this.collection().deleteOne(Filters.eq("name", name));

        return true;
    }

    public boolean updateTask(TemplateImpl template) {

        val collection = this.collection();

        if (!this.existTemplate(template.name()))
            return false;

        this.collection().updateOne(Filters.eq("name", template.name()), List.of(
                Updates.set("path", template.externalPath())
        ));

        return true;
    }

    protected MongoCollection<Document> collection() {
        return this.databaseHandler.collection(EnvConfig.getCollectionEnv(CloudMongoCollection.CollectionTypes.TEMPLATE));
    }

}
