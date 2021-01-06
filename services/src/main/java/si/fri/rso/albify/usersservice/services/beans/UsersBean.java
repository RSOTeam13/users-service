package si.fri.rso.albify.usersservice.services.beans;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import si.fri.rso.albify.usersservice.lib.GoogleAuthService;
import si.fri.rso.albify.usersservice.models.entities.UserEntity;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import java.util.Date;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@RequestScoped
public class UsersBean {

    private Logger log = Logger.getLogger(UsersBean.class.getName());

    private CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    private MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(System.getenv("DB_URL")))
                .codecRegistry(pojoCodecRegistry)
                .build();


    private MongoClient mongoClient = MongoClients.create(settings);
    private MongoDatabase database = mongoClient.getDatabase("albify");
    private MongoCollection<UserEntity> usersCollection = database.getCollection("users", UserEntity.class);

    @PreDestroy
    private void onDestroy() {
        try {
            mongoClient.close();
        } catch (Exception e) {
            log.severe("Error when closing user bean database connection.");
            e.printStackTrace();
        }
    }


    /**
     * Returns user by its ID.
     * @param userId User ID.
     * @return User entity.
     */
    public UserEntity getUser(String userId) {
        try {
            UserEntity entity = usersCollection.find(eq("_id", new ObjectId(userId))).first();
            if (entity != null && entity.getId() != null) {
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Handles user's authentication.
     * @param token Google Auth token.
     * @return Authenticated user.
     */
    public UserEntity authenticateUser(String token) {
        try {
            GoogleIdToken.Payload payload = GoogleAuthService.getPayload(token);

            if (payload != null) {
                String googleId = payload.getSubject();
                UserEntity entity = usersCollection.find(eq("googleId", googleId)).first();
                if (entity == null || entity.getId() == null) {
                    entity = new UserEntity();

                    entity.setFirstName((String) payload.get("given_name"));
                    entity.setLastName((String) payload.get("family_name"));
                    entity.setImageUrl((String) payload.get("picture"));
                    entity.setGoogleId(googleId);
                    entity.setEmail(payload.getEmail());
                    entity.setCreatedAt(new Date());

                    InsertOneResult result = usersCollection.insertOne(entity);
                    if (result.getInsertedId() != null) {
                        return entity;
                    }
                } else {
                    return entity;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
