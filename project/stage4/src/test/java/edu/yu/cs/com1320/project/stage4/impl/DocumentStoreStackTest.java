//package edu.yu.cs.com1320.project.stage4.impl;
//
//import edu.yu.cs.com1320.project.impl.HashTableImpl;
//import edu.yu.cs.com1320.project.stage4.DocumentStore;
//import org.junit.jupiter.api.Test;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.net.URISyntaxException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class DocumentStoreStackTest {
//    DocumentStoreImpl store = new DocumentStoreImpl();
//    InputStream stream = new ByteArrayInputStream("Roseter".getBytes());
//
//    @Test
//    public void undoURITest() throws URISyntaxException, IOException {
//        URI QB = new URI("QB");
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
//        store.put(stream,QB, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,HB, DocumentStore.DocumentFormat.TXT);
//        store.put(stream,DC1, DocumentStore.DocumentFormat.TXT);
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
////        store.delete(LB);
//        store.put(stream,SS, DocumentStore.DocumentFormat.BINARY);
////        System.out.println(store.get(SS));
////        store.delete(SS);
//        store.put(stream,N, DocumentStore.DocumentFormat.TXT);
//
//
////        System.out.println(store.get(SS));
////        store.undo(SS);
////        System.out.println(store.get(SS));
////        System.out.println("_________________");
//
//
////        System.out.println(store.get(N));
////        store.undo();
////        System.out.println("Shud be null: " + store.get(N));
////        System.out.println();
////
////        System.out.println(store.get(SS));
////        store.undo();
////        System.out.println("Shud be null: " + store.get(SS));
////        System.out.println();
////
////        System.out.println(store.get(LB));
////        store.undo();
////        System.out.println("Shud be null: " + store.get(LB));
////        System.out.println();
////
////        System.out.println(store.get(DL));
////        store.undo();
////        System.out.println("Shud be null: " + store.get(DL));
////        System.out.println();
////
////
////        System.out.println();
////        System.out.println("_____________________________________");
////        //TEST LINE 227
////        System.out.println(store.get(DC3));
////        System.out.println(store.delete(DC3));
////        System.out.println("Shud be null: " + store.get(DC3));
////        store.undo(DC3);
////        System.out.println(store.get(DC3));
////        System.out.println(store.get(DC3).getKey().toString());
////        System.out.println();
////        System.out.println("_____________________________________");
////        System.out.println();
////
////
////        System.out.println(store.get(DC2));
////        store.undo(DC2);
////        System.out.println("Shud be null: " + store.get(DC2));
////
////
////
////
//
//
////
////
////        //TEST
////
////
////
////
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
//        store.undo();
//       System.out.println(store.get(N));
////        System.out.println(store.storage.containsKey(N));
//        System.out.println(store.get(N));
//        //stack is size 8
//
//        //undo put(SS)
//        store.undo();
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
//        store.put(null,QB, DocumentStore.DocumentFormat.TXT);
//        assertEquals(null,store.get(QB));
//        store.undo();
//        System.out.println("GET QB: " + store.get(QB));
//
//        store.setMetadata(QB,"q","b");
//
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
