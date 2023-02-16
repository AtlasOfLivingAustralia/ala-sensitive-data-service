package au.org.ala.sds.ws;

import au.org.ala.sds.util.Configuration;
import au.org.ala.sds.ws.core.SDSConfiguration;
import au.org.ala.sds.ws.health.ResourceCheck;
import au.org.ala.sds.ws.resources.ConservationResource;
import au.org.ala.sds.ws.resources.ModelResource;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.Application;
import io.dropwizard.bundles.redirect.PathRedirect;
import io.dropwizard.bundles.redirect.RedirectBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ALASensitiveDataServiceApplication extends Application<ALASensitiveDataServiceConfiguration> {
    /** Timeout for shutdown request */
    private static final long SHUTDOWN_SECONDS = 30;
    /** The application environment */
    private Environment environment;

    public static void main(final String[] args) throws Exception {
        new ALASensitiveDataServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "ALASDSService";
    }

    @Override
    public void initialize(final Bootstrap<ALASensitiveDataServiceConfiguration> bootstrap) {
        bootstrap.addBundle(new RedirectBundle(
                new PathRedirect(ImmutableMap.<String, String>builder()
                        .put("/", "/swagger")
                        .put("/index.htm", "/swagger")
                        .put("/index.html", "/swagger")
                        .build())
        ));
        bootstrap.addBundle(new SwaggerBundle<ALASensitiveDataServiceConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ALASensitiveDataServiceConfiguration configuration) {
                 return configuration.getSwagger();
            }
        });
    }

    @Override
    public void run(final ALASensitiveDataServiceConfiguration configuration,
                    final Environment environment) {
        this.environment = environment;
        configuration.getConservation().configureSds();
        final ConservationResource conservation = new ConservationResource(configuration.getConservation());
        this.environment.jersey().register(conservation);
        this.environment.healthChecks().register("conservation", new ResourceCheck(conservation));
        final ModelResource model = new ModelResource(configuration.getConservation());
        this.environment.jersey().register(model);
        this.environment.healthChecks().register("model", new ResourceCheck(model));
    }

    /**
     * Shutdown with timeout
     *
     * @throws Exception if unable to complete the shutdown
     */
    public void shutdown() throws Exception {
        Future<Void> future = this.environment.getApplicationContext().shutdown();
        future.get(30, TimeUnit.SECONDS);
    }
}
