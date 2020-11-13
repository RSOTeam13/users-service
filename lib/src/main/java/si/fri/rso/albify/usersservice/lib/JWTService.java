package si.fri.rso.albify.usersservice.lib;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

public class JWTService {

    private static Algorithm algorithm = Algorithm.HMAC256("secret");

    public static String generateJWT(String userId) {
        try {
            return JWT
                    .create()
                    .withSubject("AUTHENTICATION")
                    // .withExpiresAt(new Date(new Date().getTime() + (1000 * 60 * 60 * 24))) // Should be added, but not for testing.
                    .withClaim("userId", userId)
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            exception.printStackTrace();
            return null;
        }
    }
}
