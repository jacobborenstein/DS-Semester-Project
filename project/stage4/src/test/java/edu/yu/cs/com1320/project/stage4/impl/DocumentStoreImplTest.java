package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.stage4.DocumentStore;
import edu.yu.cs.com1320.project.stage4.impl.DocumentStoreImpl;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DocumentStoreImplTest {

    DocumentStoreImpl store = new DocumentStoreImpl();

//
    byte [] sr2 = "team".getBytes();
    InputStream stream = new ByteArrayInputStream("Roseter".getBytes());
    InputStream stream2 = new ByteArrayInputStream(sr2);


    @Test
    public void TestPutAndGet () throws URISyntaxException, IOException {
        URI uri = new URI("MiamiDolphins.com");
        URI urinull = new URI("");
        URI uriA = new URI("aa");
        DocumentImpl str = new DocumentImpl(uri,"Team");

        //testing that Illegal argument exception is thrown
        assertThrows(IllegalArgumentException.class, () -> {store.put(stream,null, DocumentStore.DocumentFormat.TXT);});
        assertThrows(IllegalArgumentException.class, () -> {store.put(stream,uri, null);});
        assertThrows(IllegalArgumentException.class, () -> {store.put(stream,urinull, DocumentStore.DocumentFormat.TXT);});

        //testing putting a document in the storage
        assertEquals(0,store.put(stream,uri, DocumentStore.DocumentFormat.TXT));
        InputStream t = new ByteArrayInputStream("Roster".getBytes());


        //testing the get method
        store.put(t,uri, DocumentStore.DocumentFormat.TXT);
        assertEquals("Roster",store.get(uri).getDocumentTxt());

        //checking that hashcode is returned if uri is in map already
        long aaa = store.get(uri).hashCode();
        assertEquals( aaa, store.put(t,uri, DocumentStore.DocumentFormat.TXT));

    }

    @Test
    public void TestSetMetadataAndGetMetadata() throws URISyntaxException, IOException {
        URI uri = new URI("MiamiDolphins.com");
        URI uriWrong = new URI("WRONG");
        URI uriEmpty = new URI("");
        InputStream stream = new ByteArrayInputStream("Roseter".getBytes());
        store.put(stream,uri, DocumentStore.DocumentFormat.TXT);

        //testing for proper illegal argument exceptions
        //setMetaData
        assertThrows(IllegalArgumentException.class, () -> {store.setMetadata(null,"A","b");}); // uri null
        assertThrows(IllegalArgumentException.class, () -> {store.setMetadata(uriWrong,"A","b");}); //uri not found
        assertThrows(IllegalArgumentException.class, () -> {store.setMetadata(uriWrong,"","b");}); // empty key
        assertThrows(IllegalArgumentException.class, () -> {store.setMetadata(uriWrong,null,"b");}); //null key
        assertThrows(IllegalArgumentException.class, () -> {store.setMetadata(uriEmpty,null,"b");}); //uri empty
        //getMetaData
        assertThrows(IllegalArgumentException.class, () -> {store.getMetadata(null,"null");}); // uri null
        assertThrows(IllegalArgumentException.class, () -> {store.getMetadata(uriWrong, "wrong");}); //uri not found
        assertThrows(IllegalArgumentException.class, () -> {store.getMetadata(uri,"");}); // empty key
        assertThrows(IllegalArgumentException.class, () -> {store.getMetadata(uri,null);}); //null key
        assertThrows(IllegalArgumentException.class, () -> {store.setMetadata(uriEmpty,null,"b");}); //uri empty


        //testing setting meta data
        store.setMetadata(uri,"QB","TUA");
        assertEquals("TUA",store.setMetadata(uri, "QB","MIKE"));

        //testing getting metta Data
        store.setMetadata(uri,"QB","TUA");
        assertEquals("TUA", store.getMetadata(uri,"QB"));

    }

    @Test
    public void TestDelete() throws URISyntaxException, IOException {
        URI uri = new URI("MiamiDolphins.com");
        DocumentImpl str = new DocumentImpl(uri,"Team");
        InputStream stream = new ByteArrayInputStream("Roseter".getBytes());


        assertEquals(false, store.delete(uri) );
        store.put(stream,uri, DocumentStore.DocumentFormat.TXT);
        assertEquals(true, store.delete(uri) );

    }
//    @Test
//    public void TestDStoreStack() throws URISyntaxException, IOException {
//        URI uri = new URI("MiamiDolphins.com");
//        URI uriWrong = new URI("WRONG");
//        URI uriaa = new URI("aa");
//        URI QB = new URI("QB");
//        URI HB = new URI("HB");
//        URI WR = new URI("WR");
//        InputStream stream = new ByteArrayInputStream("Roseter".getBytes());
//
//        store.put(stream,uri, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,uriWrong, DocumentStore.DocumentFormat.BINARY);
//        store.put(stream,uriaa, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,QB, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,HB, DocumentStore.DocumentFormat.BINARY);
//        store.put(stream,WR, DocumentStore.DocumentFormat.TXT);
//       System.out.println(store.stack.size());
//       System.out.println(store.get(WR));
//        store.undo();
//        System.out.println(store.stack.size());
//        System.out.println(store.get(WR));

//    }

}
