//                                                                                                                                  ב״סד
package edu.yu.cs.com1320.project.stage4.impl;
import edu.yu.cs.com1320.project.stage6.Document;
import edu.yu.cs.com1320.project.stage6.DocumentStore;
import edu.yu.cs.com1320.project.stage6.impl.DocumentStoreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
//

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Stage6Test {


    DocumentStoreImpl store;


    //Before each Test create a doc store
    @BeforeEach
    public void setUp() {
        store = new DocumentStoreImpl();
    }


    @Test
    public void testBasicAPI() throws URISyntaxException, IOException {
        URI uri1 = new URI("http://www.yu.edu/documents/doc1");
        URI uri2 = new URI("http://www.yu.edu/documents/doc2");
        URI uri3 = new URI("http://www.yu.edu/documents/doc3");

        URI uri4 = new URI("http://www.test.com/documents/Tdoc4");
        URI uri5 = new URI("http://www.test.com/documents/Tdoc5");
        URI uri6 = new URI("http://www.test.com/documents/Tdoc6");

        URI uri7 = new URI("http://www.java.com/j/1stDoc/Jdoc7");
        URI uri8 = new URI("http://www.java.com/j/work/Jdoc8");
        URI uri9 = new URI("http://www.java.com/j/work/Jdoc9");

        URI uri10 = new URI("http://plsWork.com/pls/work/PWdoc10");
        URI uri11 = new URI("http://plsWork.com/pls/work/PWdoc11");
        URI uri12 = new URI("http://plsWork.com/pls/work/PWdoc12");

        InputStream stream1 = new ByteArrayInputStream("Thi?s is the Document Stream of www.yu.edu Doc1".getBytes());
        InputStream stream2 = new ByteArrayInputStream("Doc2 input stream, lets hope this wokrs well!".getBytes());
        InputStream stream3 = new ByteArrayInputStream("number three ftb, come on! we are almost there".getBytes());
        InputStream stream4 = new ByteArrayInputStream("LETS MAKE THIS BINARY DOC".getBytes());
        InputStream stream5 = new ByteArrayInputStream("let's not make this doc a binary one".getBytes());
        InputStream stream6 = new ByteArrayInputStream("#THIS IS BINARYYYY".getBytes());
        InputStream stream7 = new ByteArrayInputStream("num 7 Jdoc1 is not a binary doc, emphasis on doc ".getBytes());
        InputStream stream8 = new ByteArrayInputStream("number 8 on the stream let us hope that this all works out Bezh!".getBytes());
        InputStream stream9 = new ByteArrayInputStream("binary doc confirmed".getBytes());
        InputStream stream10 = new ByteArrayInputStream("what is going on with this, just finish it already!".getBytes());
        InputStream stream11 = new ByteArrayInputStream("BINARYYYYYYYYYYYYYYY".getBytes());
        InputStream stream12 = new ByteArrayInputStream("With the help of heaven we will get through this".getBytes());

        //put them all in
        store.put(stream1, uri1, DocumentStore.DocumentFormat.TXT);
        store.put(stream2, uri2, DocumentStore.DocumentFormat.TXT);
        store.put(stream3, uri3, DocumentStore.DocumentFormat.TXT);
        store.put(stream4, uri4, DocumentStore.DocumentFormat.BINARY);
        store.put(stream5, uri5, DocumentStore.DocumentFormat.TXT);
        store.put(stream6, uri6, DocumentStore.DocumentFormat.BINARY);
        store.put(stream7, uri7, DocumentStore.DocumentFormat.TXT);
        store.put(stream8, uri8, DocumentStore.DocumentFormat.TXT);
        store.put(stream9, uri9, DocumentStore.DocumentFormat.BINARY);
        store.put(stream10, uri10, DocumentStore.DocumentFormat.TXT);
        store.put(stream11, uri11, DocumentStore.DocumentFormat.BINARY);
        store.put(stream12, uri12, DocumentStore.DocumentFormat.TXT);

        //tests
        assertEquals("Thi?s is the Document Stream of www.yu.edu Doc1",store.get(uri1).getDocumentTxt());


//        store.docHeap.print();

        //test setMetaData moves doc to bottom of heap
//        System.out.println(store.MetaData.get);

//*************************************************************************************************************************************************************************

        //1 PUT undo

        //undo the latest put (URI 12)
        assertNotNull(store.get(uri12));
        //shud undo put of uri 12
        store.undo();
        assertNull(store.get(uri12));

        //undo a specific put on a doc
        assertNotNull(store.get(uri4));
        //shud undo put of uri 12
        store.undo(uri4);
        assertNull(store.get(uri4));

//*************************************************************************************************************************************************************************

        //2 METADATA
        store.setMetadata(uri1,"Key1","value1");
//        System.out.println(store.get(uri1).getMetadataValue("Key1"));
        store.undo(uri1);
//        System.out.println(store.get(uri1).getMetadataValue("Key1"));
        //uri 1 shud not be on top
//        store.docHeap.print();


//*************************************************************************************************************************************************************************
    //3 DELETE
    store.delete(uri2);
    assertNull(store.get(uri2));

    store.put(null,uri6, DocumentStore.DocumentFormat.TXT);
    assertNull(store.get(uri6));

    store.undo(uri2);
    assertNotNull(store.get(uri2));

    store.undo();
    assertNotNull(store.get(uri6));
//*************************************************************************************************************************************************************************
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        store.docHeap.print();

    }



@Test
public void testSearches() throws URISyntaxException, IOException {
    URI uri1 = new URI("http://www.yu.edu/documents/doc1");
    URI uri2 = new URI("http://www.yu.edu/documents/doc2");
    URI uri3 = new URI("http://www.yu.edu/documents/doc3");

    URI uri4 = new URI("http://www.test.com/documents/Tdoc4");
    URI uri5 = new URI("http://www.test.com/documents/Tdoc5");
    URI uri6 = new URI("http://www.test.com/documents/Tdoc6");

    URI uri7 = new URI("http://www.java.com/j/1stDoc/Jdoc7");
    URI uri8 = new URI("http://www.java.com/j/work/Jdoc8");
    URI uri9 = new URI("http://www.java.com/j/work/Jdoc9");

    URI uri10 = new URI("http://plsWork.com/pls/work/PWdoc10");
    URI uri11 = new URI("http://plsWork.com/pls/work/PWdoc11");
    URI uri12 = new URI("http://plsWork.com/pls/work/PWdoc12");

    InputStream stream1 = new ByteArrayInputStream("Thi?s is the Document Stream of www.yu.edu Doc1".getBytes());
    InputStream stream2 = new ByteArrayInputStream("Doc2 input stream, lets hope this wokrs well!".getBytes());
    InputStream stream3 = new ByteArrayInputStream("number three ftb, come on! we are almost there yuh!".getBytes());
    InputStream stream4 = new ByteArrayInputStream("LETS MAKE THIS BINARY DOC".getBytes());
    InputStream stream5 = new ByteArrayInputStream("let's not make this doc a binary, lets make it Str for ".getBytes());
    InputStream stream6 = new ByteArrayInputStream("#THIS IS BINARYYYY #hope".getBytes());
    InputStream stream7 = new ByteArrayInputStream("num 7 Jdoc1 Stringy is not a binary doc, emphasis on doc ".getBytes());
    InputStream stream8 = new ByteArrayInputStream("number 8 on the stream let us hope that this all works out Bezh!".getBytes());
    InputStream stream9 = new ByteArrayInputStream("binary doc confirmed".getBytes());
    InputStream stream10 = new ByteArrayInputStream("what is going on with this HOPE, just finish it already!".getBytes());
    InputStream stream11 = new ByteArrayInputStream("BINARYYYYYYYYYYYYYYY yuh".getBytes());
    InputStream stream12 = new ByteArrayInputStream("With the help of heaven we will get through this, HaVE #hope".getBytes());

    //put them all in
    store.put(stream1, uri1, DocumentStore.DocumentFormat.TXT);
    store.put(stream2, uri2, DocumentStore.DocumentFormat.TXT);
    store.put(stream3, uri3, DocumentStore.DocumentFormat.TXT);
    store.put(stream4, uri4, DocumentStore.DocumentFormat.BINARY);
    store.put(stream5, uri5, DocumentStore.DocumentFormat.TXT);
    store.put(stream6, uri6, DocumentStore.DocumentFormat.BINARY);
    store.put(stream7, uri7, DocumentStore.DocumentFormat.TXT);
    store.put(stream8, uri8, DocumentStore.DocumentFormat.TXT);
    store.put(stream9, uri9, DocumentStore.DocumentFormat.BINARY);
    store.put(stream10, uri10, DocumentStore.DocumentFormat.TXT);
    store.put(stream11, uri11, DocumentStore.DocumentFormat.BINARY);
    store.put(stream12, uri12, DocumentStore.DocumentFormat.TXT);

//*************************************************************************************************************************************************************************

    //4 SEARCH

    //search for the word hope, shud be URI 2,12,8
    Set<URI> expectedURIs = new HashSet<>();
    expectedURIs.add(uri2);
    expectedURIs.add(uri8);
    expectedURIs.add(uri12);

    List<Document> search = store.search("hope");
    Set<URI> URIsInSearch = new HashSet<>();
    for (Document doc : search){
        URIsInSearch.add(doc.getKey());
    }
    assertEquals(expectedURIs, URIsInSearch);

//*************************************************************************************************************************************************************************

    //search by prefix

    Set<URI> expectedURIsPF = new HashSet<>();
    expectedURIsPF.add(uri1);
    expectedURIsPF.add(uri8);
    expectedURIsPF.add(uri2);
    expectedURIsPF.add(uri12);
    expectedURIsPF.add(uri3);
    expectedURIsPF.add(uri5);
    expectedURIsPF.add(uri10);

    List<Document> searchPF = store.searchByPrefix("th");
    Set<URI> URIsInSearchPF = new HashSet<>();
    for (Document doc : searchPF){
        URIsInSearchPF.add(doc.getKey());
    }
    assertEquals(expectedURIsPF, URIsInSearchPF);

//*************************************************************************************************************************************************************************

    //search by metaData
    Map<String,String> map = new HashMap<>();
        map.put("QB","Tua");
        map.put("HB", "Mostert");
        map.put("WR", "Hill");
        map.put("LB", "Chubb");



        //set meta data the same for dif doc
        store.setMetadata(uri1, "QB", "Tua");
        store.setMetadata(uri1, "HB", "Mostert");
        store.setMetadata(uri1, "WR", "Hill");
        store.setMetadata(uri1, "LB", "Chubb");



//    System.out.println(store.MetaData.get(new HashMap<String,String>()));
//    System.out.println(store.MetaData.values());
//    System.out.println(store.MetaData.keySet());

    //set on docs metadata
        store.setMetadata(uri7, "QB", "Tua");
        store.setMetadata(uri7, "HB", "Mostert");
        store.setMetadata(uri7, "WR", "Hill");
        store.setMetadata(uri7, "LB", "Chubb");



        //set meta data the same for dif doc
        store.setMetadata(uri5, "QB", "Tua");
        store.setMetadata(uri5, "HB", "Mostert");
        store.setMetadata(uri5, "WR", "Hill");
        store.setMetadata(uri5, "LB", "Chubb");


        store.setMetadata(uri3 ,"A","aaa");
        store.setMetadata(uri3,"BB","bb");
        store.setMetadata(uri3,"CCC","c");

        store.setMetadata(uri11 ,"A","aaa");
        store.setMetadata(uri11,"BB","bb");
        store.setMetadata(uri11,"CCC","c");


    Set<URI> expectedURIsMD = new HashSet<>();
    expectedURIsMD.add(uri1);
    expectedURIsMD.add(uri7);
    expectedURIsMD.add(uri5);


    List<Document> searchMD = store.searchByMetadata(map);
    Set<URI> URIsInSearchMD = new HashSet<>();
    for (Document doc : searchMD){
        URIsInSearchMD.add(doc.getKey());
    }
    assertEquals(expectedURIsMD, URIsInSearchMD);

    //2nd test
    HashMap<String,String> map2 = new HashMap<>();
    map2.put("A","aaa");
    map2.put("BB","bb");
    map2.put("CCC","c");

    Set<URI> expectedURIsMD2 = new HashSet<>();
    expectedURIsMD2.add(uri3);
    expectedURIsMD2.add(uri11);

    List<Document> searchMD2 = store.searchByMetadata(map2);
    Set<URI> URIsInSearchMD2 = new HashSet<>();
    for (Document doc : searchMD2){
        URIsInSearchMD2.add(doc.getKey());
    }
    assertEquals(expectedURIsMD2, URIsInSearchMD2);


//*************************************************************************************************************************************************************************


    //search by keyword and metadata

    Set<URI> expectedURIsKM = new HashSet<>();
    expectedURIsKM.add(uri3);

    List<Document> searchKM = store.searchByKeywordAndMetadata("yuh",map2);
    Set<URI> URIsInSearchKM = new HashSet<>();
    for (Document doc : searchKM){
        URIsInSearchKM.add(doc.getKey());
    }
    assertEquals(expectedURIsKM, URIsInSearchKM);



//*************************************************************************************************************************************************************************

    //search by Prefix and MetaData

    Set<URI> expectedURIsPM = new HashSet<>();
    expectedURIsPM.add(uri1);
    expectedURIsPM.add(uri7);
    expectedURIsPM.add(uri5);

    List<Document> searchPM = store.searchByPrefixAndMetadata("Str",map);
    Set<URI> URIsInSearchPM = new HashSet<>();
    for (Document doc : searchPM){
        URIsInSearchPM.add(doc.getKey());
    }
    assertEquals(expectedURIsPM, URIsInSearchPM);
}


    @Test
    public void testDeletes() throws URISyntaxException, IOException {
        URI uri1 = new URI("http://www.yu.edu/documents/doc1");
        URI uri2 = new URI("http://www.yu.edu/documents/doc2");
        URI uri3 = new URI("http://www.yu.edu/documents/doc3");

        URI uri4 = new URI("http://www.test.com/documents/Tdoc4");
        URI uri5 = new URI("http://www.test.com/documents/Tdoc5");
        URI uri6 = new URI("http://www.test.com/documents/Tdoc6");

        URI uri7 = new URI("http://www.java.com/j/1stDoc/Jdoc7");
        URI uri8 = new URI("http://www.java.com/j/work/Jdoc8");
        URI uri9 = new URI("http://www.java.com/j/work/Jdoc9");

        URI uri10 = new URI("http://plsWork.com/pls/work/PWdoc10");
        URI uri11 = new URI("http://plsWork.com/pls/work/PWdoc11");
        URI uri12 = new URI("http://plsWork.com/pls/work/PWdoc12");

        InputStream stream1 = new ByteArrayInputStream("Thi?s is the Document Stream of www.yu.edu @Jdizzzls Doc1".getBytes());
        InputStream stream2 = new ByteArrayInputStream("Doc2 input stream, lets hope this wokrs well!".getBytes());
        InputStream stream3 = new ByteArrayInputStream("number three ftb, come on! we are almost there yuh!".getBytes());
        InputStream stream4 = new ByteArrayInputStream("LETS MAKE THIS BINARY DOC".getBytes());
        InputStream stream5 = new ByteArrayInputStream("let's not make this doc a binary, lets make it Str for ".getBytes());
        InputStream stream6 = new ByteArrayInputStream("#THIS IS BINARYYYY #hope".getBytes());
        InputStream stream7 = new ByteArrayInputStream("num 7 Jdoc1 Stringy is not a binary doc, emphasis on doc ".getBytes());
        InputStream stream8 = new ByteArrayInputStream("number 8 on the stream let us hope that this all works out Bezh!".getBytes());
        InputStream stream9 = new ByteArrayInputStream("binary doc confirmed".getBytes());
        InputStream stream10 = new ByteArrayInputStream("what is going on with this HOPE, just finish it already!".getBytes());
        InputStream stream11 = new ByteArrayInputStream("BINARYYYYYYYYYYYYYYY yuh".getBytes());
        InputStream stream12 = new ByteArrayInputStream("With the help of heaven we will get through this, HaVE #hope. Jdaabdi".getBytes());

        //put them all in
        store.put(stream1, uri1, DocumentStore.DocumentFormat.TXT);
        store.put(stream2, uri2, DocumentStore.DocumentFormat.TXT);
        store.put(stream3, uri3, DocumentStore.DocumentFormat.TXT);
        store.put(stream4, uri4, DocumentStore.DocumentFormat.BINARY);
        store.put(stream5, uri5, DocumentStore.DocumentFormat.TXT);
        store.put(stream6, uri6, DocumentStore.DocumentFormat.BINARY);
        store.put(stream7, uri7, DocumentStore.DocumentFormat.TXT);
        store.put(stream8, uri8, DocumentStore.DocumentFormat.TXT);
        store.put(stream9, uri9, DocumentStore.DocumentFormat.BINARY);
        store.put(stream10, uri10, DocumentStore.DocumentFormat.TXT);
        store.put(stream11, uri11, DocumentStore.DocumentFormat.BINARY);
        store.put(stream12, uri12, DocumentStore.DocumentFormat.TXT);


        //Delete All

        //delete all with the word "it"   uris 5,10

        Set<URI> exepctedURIs = new HashSet<>();
            exepctedURIs.add(uri5);
        exepctedURIs.add(uri10);
        assertEquals(exepctedURIs,store.deleteAll("it"));

        //make sure the get call to them is null
        assertNull(store.get(uri5));
        assertNull(store.get(uri10));

        //undo them then call get to make sure they arnt null anymore
        store.undo(uri5);
        store.undo();
        assertNotNull(store.get(uri5));
        assertNotNull(store.get(uri10));


    //*************************************************************************************************************************************************************************

        //delete all with Prefix

        //delete all with the prefix "jd" URIs 1,7,12
        Set<URI> exepctedURIsP = new HashSet<>();
        exepctedURIsP.add(uri1);
        exepctedURIsP.add(uri12);
        exepctedURIsP.add(uri7);
        assertEquals(exepctedURIsP,store.deleteAllWithPrefix("Jd"));

        //make sure the get call to them is null
        assertNull(store.get(uri7));
        assertNull(store.get(uri12));
        assertNull(store.get(uri1));

        //undo them then call get to make sure they arnt null anymore
        store.undo();
        assertNotNull(store.get(uri7));
        assertNotNull(store.get(uri12));
        assertNotNull(store.get(uri1));


    //*************************************************************************************************************************************************************************


        //delete all with metadata

        Map<String,String> map = new HashMap<>();
        map.put("QB","Tua");
        map.put("HB", "Mostert");
        map.put("WR", "Hill");
        map.put("LB", "Chubb");

        //2nd test
        HashMap<String,String> map2 = new HashMap<>();
        map2.put("A","aaa");
        map2.put("BB","bb");
        map2.put("CCC","c");



        //set meta data the same for dif doc
        store.setMetadata(uri1, "QB", "Tua");
        store.setMetadata(uri1, "HB", "Mostert");
        store.setMetadata(uri1, "WR", "Hill");
        store.setMetadata(uri1, "LB", "Chubb");

        //set on docs metadata
        store.setMetadata(uri7, "QB", "Tua");
        store.setMetadata(uri7, "HB", "Mostert");
        store.setMetadata(uri7, "WR", "Hill");
        store.setMetadata(uri7, "LB", "Chubb");

        //set meta data the same for dif doc
        store.setMetadata(uri5, "QB", "Tua");
        store.setMetadata(uri5, "HB", "Mostert");
        store.setMetadata(uri5, "WR", "Hill");
        store.setMetadata(uri5, "LB", "Chubb");


        store.setMetadata(uri3 ,"A","aaa");
        store.setMetadata(uri3,"BB","bb");
        store.setMetadata(uri3,"CCC","c");

        store.setMetadata(uri11 ,"A","aaa");
        store.setMetadata(uri11,"BB","bb");
        store.setMetadata(uri11,"CCC","c");

        //1,7,5
        Set<URI> expectedURIsDM = new HashSet<>();
        expectedURIsDM.add(uri5);
        expectedURIsDM.add(uri7);
        expectedURIsDM.add(uri1);


        assertNotNull(store.get((uri5)));
        assertNotNull(store.get((uri7)));
        assertNotNull(store.get((uri1)));

        assertEquals(expectedURIsDM,store.deleteAllWithMetadata(map));

        assertNull(store.get((uri5)));
        assertNull(store.get((uri7)));
        assertNull(store.get((uri1)));

        //undo them
        store.undo(uri7);
        store.undo(uri1);
        store.undo(uri5);

        assertNotNull(store.get((uri5)));
        assertNotNull(store.get((uri7)));
        assertNotNull(store.get((uri1)));

//*************************************************************************************************************************************************************************

        //delete all with keyword and metadata

        Set<URI> expectedURIsKM = new HashSet<>();
        expectedURIsKM.add(uri3);

        assertEquals(expectedURIsKM,store.deleteAllWithKeywordAndMetadata("yuh",map2));
        assertNull(store.get(uri3));
        store.undo();
        assertNotNull(store.get(uri3));


//*************************************************************************************************************************************************************************

        //delete all with prefix and metadata
        Set<URI> expectedURIsPM = new HashSet<>();
        expectedURIsPM.add(uri1);
        expectedURIsPM.add(uri7);
        expectedURIsPM.add(uri5);

        assertNotNull(store.get(uri1));
        assertNotNull(store.get(uri7));
        assertNotNull(store.get(uri5));

        assertEquals(expectedURIsPM,store.deleteAllWithPrefixAndMetadata("Str",map));

        assertNull(store.get(uri1));
        assertNull(store.get(uri7));
        assertNull(store.get(uri5));

        store.undo();

        assertNotNull(store.get(uri1));
        assertNotNull(store.get(uri7));
        assertNotNull(store.get(uri5));


    }



@Test
public void testLimits() throws URISyntaxException, IOException {

    URI uri1 = new URI("http://www.yu.edu/documents/doc1");
    URI uri2 = new URI("http://www.yu.edu/documents/doc2");
    URI uri3 = new URI("http://www.yu.edu/documents/doc3");

    URI uri4 = new URI("http://www.test.com/documents/Tdoc4");
    URI uri5 = new URI("http://www.test.com/documents/Tdoc5");
    URI uri6 = new URI("http://www.test.com/documents/Tdoc6");

    URI uri7 = new URI("http://www.java.com/j/1stDoc/Jdoc7");
    URI uri8 = new URI("http://www.java.com/j/work/Jdoc8");
    URI uri9 = new URI("http://www.java.com/j/work/Jdoc9");

    URI uri10 = new URI("http://plsWork.com/pls/work/PWdoc10");
    URI uri11 = new URI("http://plsWork.com/pls/work/PWdoc11");
    URI uri12 = new URI("http://plsWork.com/pls/work/PWdoc12");

    InputStream stream1 = new ByteArrayInputStream("Thi?s is the Document Stream of www.yu.edu Doc1".getBytes());
    InputStream stream2 = new ByteArrayInputStream("Doc2 input stream, lets hope this wokrs well!".getBytes());
    InputStream stream3 = new ByteArrayInputStream("number three ftb, come on! we are almost there yuh!".getBytes());
    InputStream stream4 = new ByteArrayInputStream("LETS MAKE THIS BINARY DOC".getBytes());
    InputStream stream5 = new ByteArrayInputStream("let's not make this doc a binary, lets make it Str for ".getBytes());
    InputStream stream6 = new ByteArrayInputStream("#THIS IS BINARYYYY #hope".getBytes());
    InputStream stream7 = new ByteArrayInputStream("num 7 Jdoc1 Stringy is not a binary doc, emphasis on doc ".getBytes());
    InputStream stream8 = new ByteArrayInputStream("number 8 on the stream let us hope that this all works out Bezh!".getBytes());
    InputStream stream9 = new ByteArrayInputStream("binary doc confirmed".getBytes());
    InputStream stream10 = new ByteArrayInputStream("what is going on with this HOPE, just finish it already!".getBytes());
    InputStream stream11 = new ByteArrayInputStream("BINARYYYYYYYYYYYYYYY yuh".getBytes());
    InputStream stream12 = new ByteArrayInputStream("With the help of heaven we will get through this, HaVE #hope".getBytes());

//    System.out.println("1  " + "Thi?s is the Document Stream of www.yu.edu Doc1".getBytes().length);
//    System.out.println("2  " + "Doc2 input stream, lets hope this wokrs well!".getBytes().length);
//    System.out.println("3  " + "number three ftb, come on! we are almost there yuh!".getBytes().length);
//    System.out.println("4  " + "LETS MAKE THIS BINARY DOC".getBytes().length);
//    System.out.println("5  " + "let's not make this doc a binary, lets make it Str for ".getBytes().length);
//    System.out.println("6  " + "#THIS IS BINARYYYY #hope".getBytes().length);
//    System.out.println("7   " + "num 7 Jdoc1 Stringy is not a binary doc, emphasis on doc ".getBytes().length);


    //put them all in
    store.put(stream1, uri1, DocumentStore.DocumentFormat.TXT);
    store.put(stream2, uri2, DocumentStore.DocumentFormat.TXT);
    store.put(stream3, uri3, DocumentStore.DocumentFormat.TXT);
    store.put(stream4, uri4, DocumentStore.DocumentFormat.BINARY);
    store.put(stream5, uri5, DocumentStore.DocumentFormat.TXT);
    store.put(stream6, uri6, DocumentStore.DocumentFormat.BINARY);
    store.put(stream7, uri7, DocumentStore.DocumentFormat.TXT);
    store.put(stream8, uri8, DocumentStore.DocumentFormat.TXT);
    store.put(stream9, uri9, DocumentStore.DocumentFormat.BINARY);
    store.put(stream10, uri10, DocumentStore.DocumentFormat.TXT);
    store.put(stream11, uri11, DocumentStore.DocumentFormat.BINARY);
    store.put(stream12, uri12, DocumentStore.DocumentFormat.TXT);

//    store.docHeap.print();
//    store.setMaxDocumentCount(10);
//    store.get(uri1);
//    store.get(uri2);
//    store.setMaxDocumentCount(12);
//    store.get(uri4);

    store.setMaxDocumentBytes(100);
    store.setMaxDocumentCount(12);

//    System.out.println("HERE");
//    store.docHeap.print();

//    System.out.println("-----------------------------------------------");
//    store.docHeap.print();
//    System.out.println("yes");

//    System.out.println(store.searchByPrefix("Str"));


    Map<String,String> map = new HashMap<>();
    map.put("QB","Tua");
    map.put("HB", "Mostert");
    map.put("WR", "Hill");
    map.put("LB", "Chubb");

    //2nd test
    HashMap<String,String> map2 = new HashMap<>();
    map2.put("A","aaa");
    map2.put("BB","bb");
    map2.put("CCC","c");



//    System.out.println(store.get(uri1).getMetadata().entrySet());

    //set meta data the same for dif doc
    store.setMetadata(uri1, "QB", "Tua");
    store.setMetadata(uri1, "HB", "Mostert");
    store.setMetadata(uri1, "WR", "Hill");
    store.setMetadata(uri1, "LB", "Chubb");

    //set on docs metadata
    store.setMetadata(uri7, "QB", "Tua");
    store.setMetadata(uri7, "HB", "Mostert");
    store.setMetadata(uri7, "WR", "Hill");
    store.setMetadata(uri7, "LB", "Chubb");

    //set meta data the same for dif doc
    store.setMetadata(uri5, "QB", "Tua");
    store.setMetadata(uri5, "HB", "Mostert");
    store.setMetadata(uri5, "WR", "Hill");
    store.setMetadata(uri5, "LB", "Chubb");


    store.setMetadata(uri3 ,"A","aaa");
    store.setMetadata(uri3,"BB","bb");
    store.setMetadata(uri3,"CCC","c");

    store.setMetadata(uri11 ,"A","aaa");
    store.setMetadata(uri11,"BB","bb");
    store.setMetadata(uri11,"CCC","c");

//    store.setMaxDocumentCount(1);
//
//    System.out.println("-----------------------------------------------");
//    store.docHeap.print();
//    System.out.println();
//    store.get(uri10);
//    store.get(uri8);
//    store.get(uri9);
//    store.get(uri6);
//    store.get(uri2);
//    store.setMaxDocumentCount(2);
//    System.out.println("-----------------------------------------------");
//    store.docHeap.print();
//    System.out.println();
//    store.deleteAllWithPrefix("Str");
//    System.out.println("-----------------------------------------------");
//    store.docHeap.print();
//    System.out.println();
//    store.undo();
//    System.out.println(store.searchByPrefixAndMetadata("Str",map));
//    System.out.println("-----------------------------------------------");
//    store.docHeap.print();

//
//
//
//    System.out.println(store.searchByMetadata(map));
//    System.out.println("-----------------------------------------------");
//    store.docHeap.print();
//    System.out.println();








    }





}
