package au.org.ala.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.ComparisonFailure;

import java.io.InputStreamReader;

public class TestUtils {

    protected ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper;
    }
    
    protected String getResource(String name) throws Exception {
        InputStreamReader reader = null;

        try {
            reader = new InputStreamReader(this.getClass().getResourceAsStream(name));
            StringBuilder builder = new StringBuilder(1024);
            char[] buffer = new char[1024];
            int n;

            while ((n = reader.read(buffer)) >= 0) {
                if (n == 0)
                    Thread.sleep(100);
                else
                    builder.append(buffer, 0, n);
            }
            return builder.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    protected void compareToResource(String name, String actual) throws Exception {
        String expected = this.getResource(name);
        compareNoSpaces(expected, actual);
    }

    /**
     * Compare two strings, ignoring whitespace during the comparison
     *
     * @param expected The expected string
     * @param actual The actual string
     */
    public static void compareNoSpaces(String expected, String actual) {
        String expected1 = expected.replace('\n', ' ');
        expected1 = expected1.replaceAll("\\s+", " ").trim();
        String actual1 = actual.replace('\n', ' ');
        actual1 = actual.replaceAll("\\s+", " ").trim();
        try {
            Assert.assertEquals(expected1, actual1);
        } catch (ComparisonFailure fail) {
            throw new ComparisonFailure(fail.getMessage(), expected, actual);
        }
    }

}
