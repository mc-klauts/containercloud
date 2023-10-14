package de.containercloud.database.handler;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import de.containercloud.api.security.Verification;
import de.containercloud.database.CloudMongoCollection;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.env.EnvConfig;
import lombok.RequiredArgsConstructor;
import org.bson.Document;

@RequiredArgsConstructor
public class MongoVerificationHandler {

    private final MongoDatabaseHandler databaseHandler;

    public boolean isVerificationKeyValid(Verification verification) {
        return this.collection().find(Filters.eq("token", verification.validateToken())).first() != null;
    }

    protected MongoCollection<Document> collection() {
        return this.databaseHandler.collection(EnvConfig.getCollectionEnv(CloudMongoCollection.CollectionTypes.VERIFICATION));
    }

}
