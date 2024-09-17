//package edu.yu.cs.com1320.project.stage4.impl;
//
//import edu.yu.cs.com1320.project.impl.HashTableImpl;
//import edu.yu.cs.com1320.project.stage5.DocumentStore;
//import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;
//import org.junit.jupiter.api.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class DocumentStoreUndoTest {
//    DocumentStoreImpl store = new DocumentStoreImpl();
//    InputStream stream = new ByteArrayInputStream("Thi?s is the OG$ input stream OG words".getBytes());
//    InputStream stream2 = new ByteArrayInputStream("2nd input stream #number two more random".getBytes());
//    InputStream stream3 = new ByteArrayInputStream("3rd input I don't know what to say".getBytes());
//    InputStream stream4 = new ByteArrayInputStream("4th stream now i'm really running out of words".getBytes());
//    InputStream stream5 = new ByteArrayInputStream("5th which because wh so ya".getBytes());
//    InputStream stream6 = new ByteArrayInputStream("number 6 input stream".getBytes());
//    InputStream stream7 = new ByteArrayInputStream("nuuuuuuu wor str*&^eam 7".getBytes());
//    InputStream stream8 = new ByteArrayInputStream("yu yu yu 8765 !@no!".getBytes());
//    InputStream stream9 = new ByteArrayInputStream("seven seventy jjjjjjjjjjjjjjjjjjjjjjjjjjjkkkkkkkkkkkkkk home of chabbad what up, wordddddddd ".getBytes());
//    InputStream stream10 = new ByteArrayInputStream("10 minyan man wor!".getBytes());
//    InputStream stream11 = new ByteArrayInputStream("number wo eleven mini".getBytes());
//    InputStream stream16 = new ByteArrayInputStream("Roseter".getBytes());
//    InputStream streamBIN = new ByteArrayInputStream("FOR BINARY".getBytes());
//
//
//
//
////   public DocumentStoreImpl createStackAndDocStore() throws URISyntaxException, IOException {
////     System.out.println(stream11.readAllBytes().length);
////       System.out.println(stream.readAllBytes().length);
////       System.out.println(stream9.readAllBytes().length);
////       DocumentStoreImpl docStore = new DocumentStoreImpl();
////       URI one = new URI("#1");
////       URI two = new URI("#2");
////       URI three = new URI("#3");
////       URI four = new URI("#4");
////       URI five = new URI("#5");
////       URI six = new URI("#6");
////       URI seven = new URI("#7");
////       URI eight = new URI("#8");
////       URI nine = new URI("#9");
////       URI ten = new URI("#10");
////       URI eleven = new URI("#11");
////       URI twelve = new URI("#12");
////       URI thirteen = new URI("#13");
////       URI fourteen = new URI("#14");
////       URI fifteen = new URI("#15");
////       URI sixteen = new URI("#16");
////       URI seventeen = new URI("#17");
////       docStore.put(stream,one, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream2,two, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream3,three, DocumentStore.DocumentFormat.TXT);
////       docStore.put(null,two, DocumentStore.DocumentFormat.TXT); //call to delete
////       docStore.put(stream4,four, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream5,five, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream6,six, DocumentStore.DocumentFormat.TXT);
//////       docStore.delete(four);
////       docStore.put(stream7,seven, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream8,eight, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream9,nine, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream10,ten, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream11,eleven, DocumentStore.DocumentFormat.BINARY);
////       docStore.put(stream16,sixteen, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream,twelve, DocumentStore.DocumentFormat.BINARY);
////       docStore.put(stream,thirteen, DocumentStore.DocumentFormat.BINARY);
////       docStore.delete(eleven);
////       docStore.put(streamBIN,fourteen, DocumentStore.DocumentFormat.TXT);
////       docStore.put(streamBIN,fifteen, DocumentStore.DocumentFormat.BINARY);
////     docStore.deleteAllWithPrefix("wor"); //1,4,9,10
////       docStore.put(stream9,seventeen, DocumentStore.DocumentFormat.BINARY);
////       docStore.delete(fifteen);
////
////
////
////
////    return docStore;
////   }
////
////   @Test
////   public void GoodTest() throws URISyntaxException, IOException {
////       System.out.println("11 byte Array: " + stream11.readAllBytes().length);
////       System.out.println("idk byte Array: " +stream.readAllBytes().length);
////       System.out.println("9 byte Array: " +stream9.readAllBytes().length);
////       //       URI one = new URI("#1");
//////       URI two = new URI("#2");
//////       URI three = new URI("#3");
//////       URI four = new URI("#4");
//////       URI five = new URI("#5");
//////       URI six = new URI("#6");
//////       URI seven = new URI("#7");
//////       URI eight = new URI("#8");
//////       URI nine = new URI("#9");
//////       URI ten = new URI("#10");
//////       URI eleven = new URI("#11");
//////       URI twelve = new URI("#12");
//////       URI thirteen = new URI("#13");
//////       URI fourteen = new URI("#14");
//////       URI fifteen = new URI("#15");
//////       URI sixteen = new URI("#16");
//////       URI seventeen = new URI("#17");
//////
////       URI HC = new URI("HC");
////       URI OC = new URI("OC");
////       URI DC = new URI("DC");
////       URI DC1 = new URI("DC1");
////
//////       DocumentStoreImpl store = createStackAndDocStore();
////       DocumentStoreImpl docStore = new DocumentStoreImpl();
////       URI one = new URI("#1");
////       URI two = new URI("#2");
////       URI three = new URI("#3");
////       URI four = new URI("#4");
////       URI five = new URI("#5");
////       URI six = new URI("#6");
////       URI seven = new URI("#7");
////       URI eight = new URI("#8");
////       URI nine = new URI("#9");
////       URI ten = new URI("#10");
////       URI eleven = new URI("#11");
////       URI twelve = new URI("#12");
////       URI thirteen = new URI("#13");
////       URI fourteen = new URI("#14");
////       URI fifteen = new URI("#15");
////       URI sixteen = new URI("#16");
////       URI seventeen = new URI("#17");
////
////
////
////       docStore.put(stream,one, DocumentStore.DocumentFormat.BINARY);
////       docStore.put(stream2,two, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream3,three, DocumentStore.DocumentFormat.TXT);
////       docStore.put(null,two, DocumentStore.DocumentFormat.TXT); //call to delete
////       docStore.put(stream4,four, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream5,five, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream6,six, DocumentStore.DocumentFormat.TXT);
//////       docStore.delete(four);
////       docStore.put(stream7,seven, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream8,eight, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream9,nine, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream10,ten, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream11,eleven, DocumentStore.DocumentFormat.BINARY);
////       docStore.put(stream16,sixteen, DocumentStore.DocumentFormat.TXT);
////       docStore.put(stream,twelve, DocumentStore.DocumentFormat.BINARY);
////       docStore.put(streamBIN,thirteen, DocumentStore.DocumentFormat.BINARY);
////       docStore.delete(eleven);
////       docStore.put(streamBIN,fourteen, DocumentStore.DocumentFormat.TXT);
////       docStore.put(streamBIN,fifteen, DocumentStore.DocumentFormat.BINARY);
////       docStore.deleteAllWithPrefix("wor"); //1,4,9,10
////       docStore.put(stream9,seventeen, DocumentStore.DocumentFormat.BINARY);
////       docStore.delete(fifteen);
////       System.out.println("BYTE"+docStore.get(seventeen).getDocumentBinaryData().length);
////
//////       System.out.println(store.stack.size());
//////       assertNull(store.get(four)); //make sure its deleted
//////       store.undo(four); //undo the delete()
//////       System.out.println("this is 4: " + store.get(four));
//////       assertNotNull(store.get(four));
////
//////*****************************************************************************************************************************************************************************
////       //delete(fifteen) is on top of stack. should not exists
////       assertEquals(null,store.get(fifteen));
////       assertNull(docStore.get(fifteen));
////       //undo the delete
////       docStore.undo();
////       //mkae sure fifteen isn't null anymore
////       assertNotNull(docStore.get(fifteen));
////       System.out.println(docStore.stack.size());
//////*****************************************************************************************************************************************************************************
////       //undo the last thing which is now a put on seventeen, 1st check if its there rn
////       assertNotNull(docStore.get(seventeen));
////       docStore.undo();
////       //make sure it was deleted
////       assertNull(docStore.get(seventeen));
////       System.out.println(docStore.stack.size()); //18
//////*****************************************************************************************************************************************************************************
////       //undo put by uri
////       assertNotNull(sixteen);
////       docStore.undo(sixteen);
////       assertNull(docStore.get(sixteen));
////       System.out.println(docStore.stack.size()); //17
////
////       //undo delete that was deleted with null pput by uri
////       assertNull(docStore.get(two)); //make sure it was in fact deleted
////       docStore.undo(two);//undo it
////       assertNotNull(docStore.get(two)); //check if its now there
////       System.out.println(docStore.stack.size());
////
////       //undo delete that was deleted with the delete() method
////       assertNull(docStore.get(four)); //make sure its deleted
////       docStore.undo(four); //undo the delete()
////       System.out.println("this is 4: " + docStore.get(four));
////       assertNotNull(docStore.get(four));
////
////
//////*****************************************************************************************************************************************************************************
////        // testing a sequence of undos after 4 puts
////       docStore.put(stream9,HC, DocumentStore.DocumentFormat.TXT);
////       docStore.put(streamBIN,DC, DocumentStore.DocumentFormat.TXT);
////      docStore.put(stream,DC1, DocumentStore.DocumentFormat.BINARY);
////       docStore.put(stream3,OC, DocumentStore.DocumentFormat.TXT);
////       //check that they r there
////       assertNotNull(docStore.get(HC));
////       assertNotNull(docStore.get(DC));
////       assertNotNull(docStore.get(DC1));
////       assertNotNull(docStore.get(OC));
////       System.out.println("shud be +4: " + docStore.stack.size());
////
////       //undo all of them
////       docStore.undo();
////       docStore.undo();
////       docStore.undo();
////       docStore.undo();
////
////       System.out.println("shud be -4: " + docStore.stack.size());
////       assertNull(docStore.get(HC));
////       assertNull(docStore.get(DC));
////       assertNull(docStore.get(DC1));
////       assertNull(docStore.get(OC));
////
////
////
////
////
////
////
////
////
////   }
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
////
//    @Test
//    public void undoURITest() throws URISyntaxException, IOException {
//
//       URI QB = new URI("QB");
//        URI HB = new URI("HB");
//        URI WR = new URI("WR");
//        URI OL = new URI("OL");
//        URI CB = new URI("CB");
//        URI DL = new URI("DL");
//        URI LB = new URI("LB");
//        URI SS = new URI("SS");
//        URI HC = new URI("HC");
//        URI OC = new URI("OC");
//        URI DC = new URI("DC");
//        URI DC1 = new URI("DC1");
//        URI DC2 = new URI("DC2");
//        URI DC3 = new URI("DC3");
//        URI DC4 = new URI("DC4");
//        URI DC5 = new URI("DC5");
//        URI DC6 = new URI("DC6");
//        URI DC7 = new URI("DC7");
//        URI DC8 = new URI("DC8");
//        URI N = new URI("DCcccc");
//
//        HashTableImpl<String,String> aaa = new HashTableImpl<>();
//        aaa.put("a","a");
//        aaa.put("b","b");
//        aaa.put("c","c");
//        System.out.println(aaa.get("a"));
//        aaa.put("a",null);
//        System.out.println("shud be null: " + aaa.get("a"));
//        System.out.println("_____________________________________");
//        System.out.println();
//
//
//
//
//
//        // undo empty stack
//        assertThrows(IllegalStateException.class, () -> {store.undo();});
//        // undo empty stack
//        assertThrows(IllegalStateException.class, () -> {store.undo(QB);});
//
//  store.put(stream3,QB, DocumentStore.DocumentFormat.TXT);
//        store.put(stream11,HB, DocumentStore.DocumentFormat.TXT);
//        store.put(stream2,DC1, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,DC2, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,DC3, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,DC4, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,DC5, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,DC6, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,DC7, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,DC8, DocumentStore.DocumentFormat.TXT);
//
//        store.put(stream,WR, DocumentStore.DocumentFormat.BINARY);
//        store.put(stream,OL, DocumentStore.DocumentFormat.BINARY);
//        store.put(stream,OL, DocumentStore.DocumentFormat.BINARY);
//        store.put(stream,CB, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,DL, DocumentStore.DocumentFormat.BINARY);
//        store.put(stream,LB, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,SS, DocumentStore.DocumentFormat.BINARY);
//        store.put(stream,N, DocumentStore.DocumentFormat.TXT);
//
//
//
//
//
//        System.out.println();
//        System.out.println("_____________________________________");
//        System.out.println();
//
//        System.out.println("THIS IS N " + store.get(N));
//
////        store.undo();
//       System.out.println(store.get(N));
////        System.out.println(store.storage.containsKey(N));
//        System.out.println(store.get(N));
//        //stack is size 8
//
//        //undo put(SS)
//        store.undo(SS);
//        assertEquals(null, store.get(SS));
//        store.undo();
//        assertEquals(null, store.get(SS));
//        //test undi(uri)
//        store.undo(HB);
//        assertEquals(null,store.get(HB));
//
//        //stack is no 6, no SS or HB
//
//        //testing meta data
//        store.setMetadata(DL,"C","W");
//        assertEquals("W",store.getMetadata(DL,"C"));
//        //checking if undoing the meta data works
//        store.undo();
//        assertEquals(null, store.getMetadata(DL,"C"));
//
////        System.out.println(store.stack.size());
//
//        //testing if meta data goes back to old value
////        System.out.println(store.storage.containsKey(OL));
//        store.setMetadata(OL,"LG","A");
//        store.setMetadata(OL,"LG","B");
//        assertEquals("B",store.getMetadata(OL,"LG"));
//        store.undo();
//        assertEquals("A",store.getMetadata(OL,"LG"));
//
//        //checking if undo(URI) works when set meta data is in middle of stack
//        //adding more to the stack
//        store.setMetadata(DL,"DT","A");
//        assertEquals("A",store.getMetadata(DL,"DT"));
//        store.put(stream,DC, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,OC, DocumentStore.DocumentFormat.BINARY);
//        store.put(stream,HC, DocumentStore.DocumentFormat.BINARY);
//        //undong the DL meta data
//        store.undo(DL);
//        store.delete(DL);
//        assertEquals(null,store.get(DL));
//        store.undo();
//        System.out.println("DLLLLLL: " + store.get(DL));
//
//
//
//
//
//        assertEquals(null,store.getMetadata(DL,"DT"));
//
//
//      //  testing a undo on delete
//        store.setMetadata(DC,"M","K");
//        store.setMetadata(DC,"M","D");
//       System.out.println("DELETE DC " + store.delete(DC));
//
////        store.delete(DC);
//        System.out.println("CONTAIN shud be null??? " + store.get(DC));
//        assertThrows(IllegalArgumentException.class, () -> {store.setMetadata(DC,"M","D");;});
////        assertEquals(null, store.storage.get(DC));
//        store.undo();
//        store.setMetadata(DC,"M","K");
//        store.setMetadata(DC,"M","D");
//        store.undo(DC);
//        assertEquals("K",store.setMetadata(DC,"M","D"));
//
//        assertEquals("D",store.setMetadata(DC,"M","D"));
//
//
//       // delete null method
//
////        store.put(null,QB, DocumentStore.DocumentFormat.TXT);
////        assertEquals(null,store.get(QB));
////        store.undo();
////        System.out.println("GET QB: " + store.get(QB));
////
////        store.setMetadata(QB,"q","b");
//
//
//
//
//
//    }
//
//
//
//
//
//
//
//
//
//
////    @Test
////    public void UndoTest() throws URISyntaxException, IOException {
////     //   InputStream stream = new ByteArrayInputStream("Roseter".getBytes());
////        URI QB = new URI("QB");
////        URI HB = new URI("HB");
////        URI WR = new URI("WR");
////        URI OL = new URI("OL");
////        URI CB = new URI("CB");
////        URI DL = new URI("DL");
////        URI LB = new URI("LB");
////        URI SS = new URI("SS");
////
////        // undo empty stack
////        assertThrows(IllegalStateException.class, () -> {store.undo();});
////        // undo empty stack
////        assertThrows(IllegalStateException.class, () -> {store.undo(QB);});
////
////        store.put(stream,QB, DocumentStore.DocumentFormat.TXT);
////        store.put(stream,HB, DocumentStore.DocumentFormat.BINARY);
////        store.put(stream, WR, DocumentStore.DocumentFormat.TXT);
////        store.put(stream, OL, DocumentStore.DocumentFormat.TXT);
////        store.put(stream,CB, DocumentStore.DocumentFormat.TXT);
//////        store.delete(WR);
////        store.setMetadata(QB,"QB2", "MIKE WHITE");
////        store.setMetadata(WR,"A","A");
//////        store.put(stream,DL, DocumentStore.DocumentFormat.TXT);
//////        store.put(stream,LB, DocumentStore.DocumentFormat.TXT);
//////        store.put(stream,SS, DocumentStore.DocumentFormat.TXT);
////
//////        store.put(null,WR, DocumentStore.DocumentFormat.TXT);
////        store.delete(WR);
////
////
////        store.setMetadata(QB, "QB1","TUA");
////
////        System.out.println(store.stack.size());
////
////        //testing undo if a MetaData is on top of command stack
//////        assertEquals("TUA",store.getMetadata(QB,"QB1")); //checking it there b4
//////        store.undo();
//////        System.out.println(store.stack.size()); //9
//////        assertEquals(null,store.getMetadata(QB,"QB1"));
//////        store.undo(QB);//8
//////        System.out.println(store.stack.size());
//////        assertEquals(null,store.getMetadata(QB,"QB2"));
//////        store.undo();//7
//////
////        System.out.println("");
////
////        store.undo();
////        System.out.println(store.stack.size()); //9
////        System.out.println(store.stack.peek().getUri().toString()); //ss
////        System.out.println("");
////
////        store.undo();
////        System.out.println(store.stack.size());//9
////        System.out.println(store.stack.peek().getUri().toString());//ss
////        System.out.println("");
////
////        store.undo();
////        System.out.println(store.stack.peek().getUri().toString());//lb
////        System.out.println(store.stack.size());//8
////        System.out.println("");
////
////        store.undo();
////        System.out.println(store.stack.peek().getUri().toString());//lb
////        System.out.println(store.stack.size());//8
////        System.out.println("");
////
//////        store.undo();
//////        System.out.println(store.stack.peek().getUri().toString());//DL
//////        System.out.println(store.stack.size());//7
//////        System.out.println("");
//////
//////        store.undo();
//////        System.out.println(store.stack.peek().getUri().toString());//DL
//////        System.out.println(store.stack.size());//7
//////        System.out.println("");
//////
//////        store.undo();
//////        System.out.println(store.stack.peek().getUri().toString());// QB
//////        System.out.println(store.stack.size());//6
//////        System.out.println("");
////////
//////        store.undo(); //undo null call for wr
//////        System.out.println(store.stack.peek().getUri().toString());// QB //CB
//////        System.out.println(store.stack.size());//6
//////        System.out.println("");
////
////        store.undo();
////        System.out.println(store.stack.peek().getUri().toString());// QB //CB
////        System.out.println(store.stack.size());//6
////        System.out.println("");
////        URI w = new URI("kkkkkk");
//////     System.out.println(store.get(WR).setMetadataValue("K","k"));
////        System.out.println(store.get(CB)); ///check if delted
////        store.setMetadata(WR,"a","A");
////
////        store.undo(HB);
////        store.setMetadata(HB,"A","A");
////
////
////
////
////
////
////
////
////
////
////
////
////
////    }
////
////    @Test
////    public void UndoURITest(){
////
////    }
////
////
////
////
////
////
//
//
//}
