package de.containercloud.database.handler;

import com.mongodb.client.model.Filters;
import com.mongodb.reactivestreams.client.MongoCollection;
import de.containercloud.api.security.Verification;
import de.containercloud.database.CloudMongoCollection;
import de.containercloud.database.Handler;
import de.containercloud.database.MongoDatabaseHandler;
import de.containercloud.env.EnvConfig;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.CompletableFuture;


public class MongoVerificationHandler extends Handler {

    private final MongoDatabaseHandler databaseHandler;

    public MongoVerificationHandler(MongoDatabaseHandler databaseHandler) {
        this.databaseHandler = databaseHandler;
    }

    public CompletableFuture<Boolean> isVerificationKeyValid(Verification verification) {
        Publisher<Document> documentPublisher = this.collection().find(Filters.eq("token", verification.validateToken())).first();

        CompletableFuture<Boolean> future = new CompletableFuture<>();
        documentPublisher.subscribe(new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(1L);
            }

            @Override
            public void onNext(Document document) {

                if (document == null)
                    future.complete(false);
                else
                    future.complete(true);

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
        return this.databaseHandler.collection(EnvConfig.getCollectionEnv(CloudMongoCollection.CollectionTypes.VERIFICATION));
    }

}
