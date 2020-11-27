package au.org.ala.sds.generalise;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.*;

/**
 * Type resolver for generalisations.
 * <p>
 * Generates a simple string for the generalisation type so that
 * what's going on is not explcitly tied to Java classes.
 * </p>
 * <p>
 * Class-name mappings are intialised from the <code>/generalisation-mapping.properties</code> resource.
 * </p>
 */
public class GeneralisationTypeIdResolver extends TypeIdResolverBase {
    private static final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();
    private static Map<Class<Generalisation>, String> CLASS_TO_ID = null;
    private static Map<String, JavaType> ID_TO_CLASS = null;

    public GeneralisationTypeIdResolver() {
        super(TypeFactory.defaultInstance().constructSimpleType(Generalisation.class, null), TypeFactory.defaultInstance());
    }

    private static Map<Class<Generalisation>, String> getClassToId() {
        if (CLASS_TO_ID == null)
            buildMaps();
        return CLASS_TO_ID;
    }

    private static Map<String, JavaType> getIdToClass() {
        if (ID_TO_CLASS == null)
            buildMaps();
        return ID_TO_CLASS;
    }

    synchronized private static void buildMaps() {
        if (CLASS_TO_ID != null)
            return;
        Map<Class<Generalisation>, String> cToId = new HashMap<>();
        Map<String, JavaType> idToC = new HashMap<>();
        try {
            Properties mapping = new Properties();
            mapping.load(GeneralisationTypeIdResolver.class.getResourceAsStream("/generalisation-mapping.properties"));
            mapping.forEach((className, label) -> {
                Class<Generalisation> cls = null;
                try {
                    cls = (Class<Generalisation>) Class.forName(className.toString());
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Unable to find " + className);
                }
                String name = label.toString();
                if (cToId.containsKey(cls))
                    throw new IllegalStateException("Duplcate class " + cls);
                if (idToC.containsKey(name))
                    throw new IllegalStateException("Duplicate name " + name);
                JavaType type = TYPE_FACTORY.constructSimpleType(cls, null);
                cToId.put(cls, name);
                idToC.put(name, type);
            });
        } catch (IOException ex) {
            throw new IllegalStateException("Unable to construct class/id map for Generalisation", ex);
        }
        CLASS_TO_ID = cToId;
        ID_TO_CLASS = idToC;
    }

    /**
     * Get the list of names that are mapped.
     *
     * @return The name list
     */
    public static List<String> getNames() {
        List<String> names = new ArrayList<>(getIdToClass().keySet());
        Collections.sort(names);
        return names;
    }

    @Override
    public String idFromValue(Object value) {
        return this.idFromValueAndType(value, Generalisation.class);
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return getClassToId().getOrDefault(value == null ? suggestedType : value.getClass(), "generalisation");
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        return getIdToClass().computeIfAbsent(id, k -> {
            throw new IllegalArgumentException("No class for action " + k);
        });
    }

    /**
     * Accessor for mechanism that this resolver uses for determining
     * type id from type. Mostly informational; not required to be called
     * or used.
     */
    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }
}
