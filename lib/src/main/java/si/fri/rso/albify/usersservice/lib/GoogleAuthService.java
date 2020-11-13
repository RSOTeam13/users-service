package si.fri.rso.albify.usersservice.lib;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.util.Collections;

public class GoogleAuthService {

    private static final String GOOGLE_CLIENT_ID = "";

    public static GoogleIdToken.Payload getPayload (String tokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                    .build();

            GoogleIdToken token = verifier.verify(tokenString);
            if (token != null) {
                GoogleIdToken.Payload payload = token.getPayload();
                if (!GOOGLE_CLIENT_ID.equals(payload.getAudience())) {
                    return null;
                } else if (!GOOGLE_CLIENT_ID.equals(payload.getAuthorizedParty())) {
                    return null;
                }
                return payload;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
