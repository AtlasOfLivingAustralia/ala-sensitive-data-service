package au.org.ala.sds.ws.resources;

import io.swagger.converter.ModelConverter;
import io.swagger.converter.ModelConverterContext;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;

public class ExampleModelConverter implements ModelConverter {

    @Override
    public Model resolve(Type type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        Model model = chain.hasNext() ? chain.next().resolve(type, context, chain) : null;
        if (model != null && model.getExample() == null) {
            // build example from properties
            if (model.getProperties() != null) {
                StringBuilder sb = new StringBuilder("{");
                model.getProperties().forEach((name, property) -> {
                    if (property.getExample() != null) {
                        sb.append("\"").append(name).append("\": \"")
                          .append(property.getExample()).append("\", ");
                    }
                });
                if (sb.length() > 1) {
                    sb.setLength(sb.length() - 2); // trim trailing comma
                    sb.append("}");
                    model.setExample(sb.toString());
                }
            }
        }
        return model;
    }

    @Override
    public Property resolveProperty(Type type, ModelConverterContext context,
                                    Annotation[] annotations, Iterator<ModelConverter> chain) {
        return chain.hasNext() ? chain.next().resolveProperty(type, context, annotations, chain) : null;
    }
}