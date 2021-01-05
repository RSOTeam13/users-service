package si.fri.rso.albify.usersservice.api.v1;

import si.fri.rso.albify.usersservice.services.filters.AuthenticationFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/v1")
public class UsersServiceApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(AuthenticationFilter.class);
        return resources;
    }
}
