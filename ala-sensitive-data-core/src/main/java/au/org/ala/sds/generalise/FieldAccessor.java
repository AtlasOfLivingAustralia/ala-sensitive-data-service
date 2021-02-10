package au.org.ala.sds.generalise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import org.gbif.dwc.terms.Term;
import org.gbif.dwc.terms.TermFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Access for a field.
 * <p>
 * Fields are provided as maps of properties.
 * </p>
 */
@JsonSerialize(using = FieldAccessor.Serializer.class)
@JsonDeserialize(using = FieldAccessor.Deserializer.class)
public class FieldAccessor {
    /** The field name */
    @Getter
    private Term field;
    /** The list of keys to use when accessing the field */
    private List<String> keys;
    /** The default key to use */
    private String defaultKey;

    /**
     * Construct from a field term.
     *
     * @param field The field term
     */
    public FieldAccessor(@NonNull Term field) {
        this.field = field;
        this.keys = this.buildKeys(field);
        this.defaultKey = this.field.simpleName();
    }

    /**
     * Construct from a field name.
     * <p>
     * A {@link TermFactory} is used to convert this name into a {@link Term}
     * </p>
     *
     * @param field The field name, URI or prefixed name
     */
    public FieldAccessor(String field) {
        this(TermFactory.instance().findTerm(field));
    }

    /**
     * Check to see if a map of properties alreayd has a value.
     *
     * @param properties The properties
     * @param <T> The property type
     *
     * @return True if there is already a value set for this field, even if null
     */
    public <T> boolean isSet(Map<String, T> properties) {
        return this.keys.stream().anyMatch(k -> properties.containsKey(k));
    }

    /**
     * Get a value for this field.
     * <p>
     * The resulting access object contains the string key under which the object
     * was found and the actual value.
     * </p>
     *
     * @param properties The properties to search
     * @param <T> The property type
     *
     * @return An value for the fied
     */
    public <T> Value<T> get(Map<String, T> properties) {
        Optional<String> key = this.keys.stream().filter(k -> properties.containsKey(k)).findFirst();
        Optional<T> value = key.map(k -> properties.get(k)).filter(v -> v != null && !((v instanceof String) && ((String) v).isEmpty()));
        return new Value(key.orElse(this.defaultKey), value);
    }

    /**
     * Build the keys to use when accessing the field.
     * <p>
     * By default, look for the URI, the prefixed name and the simple name.
     * </p>>
     *
     * @return The list of keys to use.
     */
    protected List<String> buildKeys(Term field) {
        return Arrays.asList(
            field.qualifiedName(),
            field.prefixedName(),
            field.simpleName()
        );
    }

    /**
     * An actual value for a field, including the key used to access it.
     */
     public class Value<T> {
        /** The key that actually produced the value */
        @Getter
        private @NonNull String key;
        /** The value that is present */
        @Getter
        private @NonNull Optional<T> value;

        public Value(@NonNull String key, @NonNull Optional<T> value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Is this value non-null
         *
         * @return True if a value is present
         */
        public boolean hasValue() {
            return this.value.isPresent();
        }

        /**
         * Update the data with a new value.
         *
         * @param update The updated value
         * @param original The map of original values
         * @param updated The map of updated values
         *
         * @param <M> The type of map being updated
         */
        public <M> void set(Optional<T> update, Map<String, M> original, Map<String, M> updated) {
            original.put(this.key, (M) this.value.orElse(null));
            updated.put(this.key, (M) update.orElse(null));
        }
    }

    public static class Serializer extends StdSerializer<FieldAccessor> {
        public Serializer() {
            super(FieldAccessor.class);
        }

        /**
         * Serialize the field accessor.
         * <p>
         * The only thing that completely defines the accessort is the field term,
         * so the prefixed form (eg "dwc:decimalLatitude") is used.
         * </p>
         *
         * @param value The value to serialise
         * @param gen The hson generator
         * @param provider The provider for context
         *
         * @throws IOException if unable to serialise
         */
        @Override
        public void serialize(FieldAccessor value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.getField().prefixedName());
        }
    }

    public static class Deserializer extends StdDeserializer<FieldAccessor> {
        public Deserializer() {
            super(FieldAccessor.class);
        }

        /**
         * Get a field accessor, based on a term in prefixed, URI or name form.
         *
         * @param p    Parsed used for reading JSON content
         * @param ctxt Context that can be used to access information about
         *             this deserialization activity.
         * @return Deserialized value
         */
        @Override
        public FieldAccessor deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            String value = p.getText();

            if (value == null || value.isEmpty())
                return null;
            try {
                Term field = TermFactory.instance().findTerm(value);
                return new FieldAccessor(field);
            } catch (IllegalArgumentException ex) {
                throw new JsonParseException(p, "Unable to read term for " + value, ex);
            }
         }
    }
}
