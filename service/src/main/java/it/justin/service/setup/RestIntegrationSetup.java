package it.justin.service.setup;

import it.justin.service.impl.AccountServiceImpl;
import it.justin.service.impl.HealthCheck;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@ApplicationPath("/api")
public class RestIntegrationSetup extends ResourceConfig {
    private static final Logger LOG = getLogger(RestIntegrationSetup.class);

    public RestIntegrationSetup() {
        register(AccountServiceImpl.class);
        register(HealthCheck.class);
        LOG.info("starting RestInitializer");
    }
}
