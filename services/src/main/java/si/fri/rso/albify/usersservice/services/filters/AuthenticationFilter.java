package si.fri.rso.albify.usersservice.services.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

@Provider
@Authenticate
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext ctx) throws IOException {
        System.out.println("FILTERING REQUEST: ");

        String authToken = ctx.getHeaderString("Authorization");
        System.out.println("AUTH TOKEN: " + authToken);

        String userId = null;

        if (authToken != null) {
            try {
                Algorithm algorithm = Algorithm.HMAC256("secret");
                JWTVerifier verifier = JWT
                        .require(algorithm)
                        .withSubject("AUTHENTICATION")
                        .build();
                DecodedJWT jwt = verifier.verify(authToken);

                Map<String, Claim> claims = jwt.getClaims();
                userId = claims.get("userId").asString();

            } catch (JWTDecodeException exception) {
                userId = null;
            }
        }

        System.out.println("USER ID: " + userId);

        if (userId == null) {
            ctx.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
        ctx.setProperty("userId", userId);
    }
}