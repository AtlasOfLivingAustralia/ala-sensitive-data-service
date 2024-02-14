package au.org.ala.sds.ws;

import au.org.ala.sds.ws.health.ResourceCheck;
import au.org.ala.sds.ws.resources.ConservationResource;
import au.org.ala.sds.ws.resources.ModelResource;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.Application;
import io.dropwizard.bundles.redirect.PathRedirect;
import io.dropwizard.bundles.redirect.RedirectBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import in.vectorpro.dropwizard.swagger.SwaggerBundle;
import in.vectorpro.dropwizard.swagger.SwaggerBundleConfiguration;

public class ALASensitiveDataServiceApplication extends Application<ALASensitiveDataServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new ALASensitiveDataServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "ALASDSService";
    }

    @Override
    public void initialize(final Bootstrap<ALASensitiveDataServiceConfiguration> bootstrap) {
        PathRedirect redirect = new PathRedirect(ImmutableMap.<String, String>builder()
                .put("/", "/swagger")
                .put("/index.htm", "/swagger")
                .put("/index.html", "/swagger")
                .build());
        bootstrap.addBundle(new RedirectBundle(redirect));
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
        final ConservationResource conservation = new ConservationResource(configuration.getConservation());
        environment.jersey().register(conservation);
        environment.healthChecks().register("conservation", new ResourceCheck(conservation));
        final ModelResource model = new ModelResource(configuration.getConservation());
        environment.jersey().register(model);
        environment.healthChecks().register("model", new ResourceCheck(model));
    }
}
