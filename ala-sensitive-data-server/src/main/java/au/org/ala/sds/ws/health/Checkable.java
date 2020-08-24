package au.org.ala.sds.ws.health;

/**
 * Provide a health check for a resource.
 */
public interface Checkable {
    /**
     * Check to see that the resource is still functioning.
     *
     * @return True if still alive, false otherwise.
     */
    public boolean check();
}
