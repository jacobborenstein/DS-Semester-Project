package edu.yu.cs.com1320.project.stage3.impl;

import edu.yu.cs.com1320.project.HashTable;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DocumentImplTest {


    @Test
    public void TestSetMetadataValue () throws URISyntaxException {
        URI uri = new URI("MiamiDolphins.com");
        DocumentImpl str = new DocumentImpl(uri,"Roster");

        //Testing: 1) if metaData is being set. 2) if it returns null if empty. 3) if it returns old value
        assertEquals(null, str.setMetadataValue("QB","MIKE WHITE"));
        assertEquals("MIKE WHITE", str.setMetadataValue("QB","TUA"));
        //testing that IllegalArgument is throw for empty and null Key
        assertThrows(IllegalArgumentException.class, () -> {str.setMetadataValue("", "test");});
        assertThrows(IllegalArgumentException.class, () -> {str.setMetadataValue(null, "test");});
    }

    @Test
    public void TestGetMetadataValue() throws URISyntaxException{
        URI uri = new URI("MiamiDolphins.com");
        DocumentImpl str = new DocumentImpl(uri,"Roster");
        str.setMetadataValue("QB","TUA");

        //Testing if null is returned to a key that doesn't exist
        assertEquals(null, str.getMetadataValue("CB"));
        //Testing if value for given key is returned
        assertEquals("TUA", str.getMetadataValue("QB"));
        //Testing for illegal argument exception
        assertThrows(IllegalArgumentException.class, () -> {str.getMetadataValue(null);});
        assertThrows(IllegalArgumentException.class, () -> {str.getMetadataValue("");});
    }

    @Test
    public void TestGetMetadata() throws URISyntaxException{
        URI uri = new URI("MiamiDolphins.com");
        DocumentImpl str = new DocumentImpl(uri,"Roster");
        str.setMetadataValue("QB","TUA");
        str.setMetadataValue("HB","Mostert");
        str.setMetadataValue("CB","X");
        HashTable<String, String> Test = str.getMetadata();

        //testing if hashmap was copied by test the size and if key matches value
        assertEquals(3, Test.size());
        assertEquals("TUA",Test.get("QB"));
        assertEquals("X",Test.get("CB"));
        assertEquals("Mostert",Test.get("HB"));
    }

    @Test
    public  void  TestGetDocumentTxt() throws URISyntaxException{
        URI uri = new URI("MiamiDolphins.com");
        DocumentImpl str = new DocumentImpl(uri,"Roster");

        //testing if Document txt is returned
        assertEquals("Roster",str.getDocumentTxt());
    }

    @Test
    public  void  TestGetDocumentBinaryData() throws URISyntaxException{
        URI uri = new URI("MiamiDolphins.com");
        byte[] byteArray = new byte[]{82, 111, 115, 116, 101, 114};
        DocumentImpl str = new DocumentImpl(uri,byteArray);

        //testing if Document byte[] is returned
       assertEquals(byteArray,str.getDocumentBinaryData());
    }

    @Test
    public  void  TestGetKey() throws URISyntaxException {
        URI uri = new URI("MiamiDolphins.com");
        DocumentImpl str = new DocumentImpl(uri, "Roster");

        //testing if uri is returned
        assertEquals(uri, str.getKey());
    }

}
