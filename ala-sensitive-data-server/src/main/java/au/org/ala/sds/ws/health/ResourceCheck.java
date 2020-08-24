package au.org.ala.sds.ws.health;

import au.org.ala.sds.ws.resources.ConservationResource;
import com.codahale.metrics.health.HealthCheck;

/**
 * Health check for the name search resource, to make
 * sure that nothing has gone away by accident.
 */
public class ResourceCheck extends com.codahale.metrics.health.HealthCheck {
    private Checkable resource;

    public ResourceCheck(Checkable resource) {
        this.resource = resource;
    }

    @Override
    protected Result check() throws Exception {
        if (this.resource.check())
            return Result.healthy();
        else
            return Result.unhealthy("Name search unable to search index");
    }
}
