package si.fri.rso.albify.usersservice.services.interceptors;

import org.glassfish.jersey.server.ContainerRequest;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;

@Interceptor
@Authenticate
public class AuthenticationInterceptor {

    @Inject
    ContainerRequest request;

    @AroundInvoke
    public Object intercept(InvocationContext context) throws Exception {
        if (request.getProperty("userId") == null || request.getProperty("userId").toString() == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return context.proceed();
    }
}
