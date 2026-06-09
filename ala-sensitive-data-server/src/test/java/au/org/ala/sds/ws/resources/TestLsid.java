package au.org.ala.sds.ws.resources;
import au.org.ala.names.search.ALANameSearcher;
import au.org.ala.names.model.NameSearchResult;
import org.junit.Test;
public class TestLsid {
    @Test
    public void test() throws Exception {
        ALANameSearcher searcher = new ALANameSearcher("/data/lucene/namematching-20210811-3");
        NameSearchResult nsr = searcher.searchForRecord("Caesia sp. Koolanooka Hills (R. Meissner & Y. Caruso 78)", null);
        System.out.println("LSID_FOR_CAESIA: " + (nsr == null ? "null" : nsr.getLsid()));
        
        nsr = searcher.searchForRecord("Thryptomene stenophylla", null);
        System.out.println("LSID_FOR_THRYPTOMENE: " + (nsr == null ? "null" : nsr.getLsid()));
    }
}
