package de.containercloud.database;

import de.containercloud.database.handler.MongoServiceHandler;
import de.containercloud.database.handler.MongoTaskHandler;
import de.containercloud.database.handler.MongoTemplateHandler;
import de.containercloud.database.handler.MongoVerificationHandler;

public class MongoProvider {

    private static MongoProvider INSTANCE;
    private final MongoServiceHandler serviceHandler;
    private final MongoTaskHandler taskHandler;
    private final MongoTemplateHandler templateHandler;
    private final MongoVerificationHandler verificationHandler;

    public MongoProvider(MongoDatabaseHandler databaseHandler) {
        INSTANCE = this;

        this.serviceHandler = new MongoServiceHandler(databaseHandler);
        this.taskHandler = new MongoTaskHandler(databaseHandler);
        this.templateHandler = new MongoTemplateHandler(databaseHandler);
        this.verificationHandler = new MongoVerificationHandler(databaseHandler);
    }

    public MongoServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    public MongoTaskHandler getTaskHandler() {
        return taskHandler;
    }

    public MongoTemplateHandler getTemplateHandler() {
        return templateHandler;
    }

    public MongoVerificationHandler getVerificationHandler() {
        return verificationHandler;
    }

    public static MongoProvider getINSTANCE() {
        return INSTANCE;
    }

}
