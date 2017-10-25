package it.justin.service.setup;

import it.justin.service.impl.AccountServiceImpl;
import it.justin.service.impl.HealthCheck;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@ApplicationPath("/api")
public class RestIntegrationSetup extends ResourceConfig {
    private static final Logger LOG = getLogger(RestIntegrationSetup.class);

    public RestIntegrationSetup() {
        this.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, false);
        this.property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);
        register(GenericExceptionMapper.class);
        register(AccountServiceImpl.class);
        register(HealthCheck.class);
        LOG.info("starting RestInitializer");
    }
}
