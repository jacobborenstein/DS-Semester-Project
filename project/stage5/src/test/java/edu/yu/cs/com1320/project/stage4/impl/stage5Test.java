package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class stage5Test {

    @Test
    public void minHeapTest(){
        MinHeapImpl<Integer> heap = new MinHeapImpl();
        heap.insert(3);
        heap.insert(7);
        heap.insert(88);
        heap.insert(2);
        heap.insert(1);
        heap.insert(32);
        heap.reHeapify(32);
        heap.insert(4);
        heap.reHeapify(4);




        //Testing peek/remove
        assertEquals(1,heap.peek());
        assertEquals(1, heap.remove());
        assertEquals(2, heap.peek());
        assertEquals(2,heap.remove());
        assertEquals(3, heap.remove());
        assertEquals(4, heap.remove());
        assertEquals(7, heap.remove());
        assertEquals(32, heap.remove());
        assertEquals(88, heap.remove());
        assertThrows(NoSuchElementException.class, () -> {heap.remove();});
    }


    @Test
    public void testErrorScenario() {
        MinHeapImpl<String> heap = new MinHeapImpl<>();
        heap.insert("HEY");
        heap.insert("YOU");
//        heap.insert(3);
//        heap.insert(7);

        // Now, let's create another MinHeapImpl instance without calling the constructor
        // This will leave the 'elements' array uninitialized
//        MinHeapImpl<Integer> anotherHeap = new MinHeapImpl<>();
//
//        // Attempting to insert an element into 'anotherHeap' will cause NullPointerException
//        // because 'elements' array is not initialized
//        anotherHeap.insert(10);
    }


    @Test
    public void deleteMethodHeapTest() throws URISyntaxException, IOException, IOException, URISyntaxException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        store.setMaxDocumentBytes(450);
        store.setMaxDocumentCount(3);
System.out.println("rock aweut pysihvdshiu DELe@ted op  tooo  HERRO app These to  T Thes$e Thes$e are the words, awesome please ign#ore th*e weird ones 123$ Thes$e! Thes$e!".getBytes().length);
        InputStream stream1 = new ByteArrayInputStream(" rock aweut pysihvdshiu DELe@ted op  tooo  HERRO app These to  T Thes$e Thes$e are the words, awesome please ign#ore th*e weird ones 123$ Thes$e! Thes$e!".getBytes());
        InputStream stream2 = new ByteArrayInputStream("awe Th pyyyyy pkkkkkk apple".getBytes());
        InputStream stream3 = new ByteArrayInputStream("Theseeee  Topppp awesom These awqo These These These These$e".getBytes());
        InputStream stream4 = new ByteArrayInputStream("Theseee DE!Leted pdysabcivbcizuw  aweqo op222 too appoqweh app To aeeppe aee aweoi These app appo appt appw appq These These appllll These$e".getBytes());
        InputStream stream5 = new ByteArrayInputStream("rock Topiuy78990 py  DELe@ted$  awe#244 pdyHERRO awe#244 These tooo These to to appppp Thes appp helloabduwu273d Thesee$ Thesee! d".getBytes());
        InputStream stream6 = new ByteArrayInputStream("app appl awleqpy !@#H*&^%#O)@*#*P These HERRO app awqo appewrf appe app6".getBytes());

        System.out.println(stream1.readAllBytes().length);
        System.out.println(stream2.readAllBytes().length);
        System.out.println(stream3.readAllBytes().length);
        System.out.println(stream4.readAllBytes().length);

        URI uri1 = new URI("Uri1");
        URI uri2 = new URI("uri2");
        URI uri3 = new URI("uri3");
        URI uri4 = new URI("uri4");
        URI uri5 = new URI("uri5");
        URI uri6 = new URI("uri6");

        store.put(stream1,uri1, DocumentStore.DocumentFormat.TXT);
        store.put(stream2,uri2, DocumentStore.DocumentFormat.TXT);
        store.delete(uri1);
        store.undo(uri1);
        System.out.println(store.get(uri1));

        store.put(stream3,uri3, DocumentStore.DocumentFormat.BINARY);
//        store.search("rock");

        store.put(stream4,uri4, DocumentStore.DocumentFormat.BINARY);

//        store.put(stream5,uri5, DocumentStore.DocumentFormat.BINARY);
//        store.put(stream6,uri6, DocumentStore.DocumentFormat.BINARY);
        store.docHeap.print();
        System.out.println(store.DocCount);
        System.out.println(store.MaxDocumentCount);
        System.out.println(store.get(uri1));
////       store.undo();
//        assertNull(store.get(uri1));
//        assertNotNull(store.get(uri2));
//        assertNotNull(store.get(uri3));
////        assertNotNull(store.get(uri4));
////        System.out.println(store.get(uri4));
////      store.undo();
//        store.docHeap.print();
//        store.docHeap.print();
//        store.delete(uri1);
//        store.delete(uri2);
//        store.delete(uri3);
//        store.delete(uri4);
//        System.out.println(store.get(uri1));
//        System.out.println(store.get(uri2));
//        System.out.println(store.get(uri3));
//        System.out.println(store.get(uri4));
//        store.undo();store.undo();store.undo();store.undo();
//       System.out.println(store.get(uri1));
//        System.out.println(store.get(uri2));
//        System.out.println(store.get(uri3));
//        System.out.println(store.get(uri4));
//
//
//
//
////        store.put(stream2,uri2, DocumentStore.DocumentFormat.TXT);
////        store.put(stream3,uri3, DocumentStore.DocumentFormat.TXT);
////        store.put(stream4,uri4, DocumentStore.DocumentFormat.TXT);
////        store.put(stream5,uri5, DocumentStore.DocumentFormat.TXT);
////        store.put(stream6,uri6, DocumentStore.DocumentFormat.TXT);
//        store.docHeap.print();
////        System.out.println("1: " + store.get(uri1));
////        System.out.println("2: " + store.get(uri2));
////        System.out.println("3: " + store.get(uri3));
////        System.out.println("4: " + store.get(uri4));
////        System.out.println("5: " + store.get(uri5));
////        System.out.println("6: " + store.get(uri6));
////        System.out.println(store.get(uri1).getDocumentBinaryData());
//
//
//
//        Map<String,String> map = new HashMap<>();
//        map.put("QB","Tua");
//        map.put("HB", "Mostert");
//        map.put("WR", "Hill");
//        map.put("LB", "Chubb");
////
////        //set meta data the same for dif doc
////        store.setMetadata(uri1, "QB", "Tua");
////        store.setMetadata(uri1, "HB", "Mostert");
////        store.setMetadata(uri1, "WR", "Hill");
////        store.setMetadata(uri1, "LB", "Chubb");
////
////        //set on docs metadata
////        store.setMetadata(uri2, "QB", "Tua");
////        store.setMetadata(uri2, "HB", "Mostert");
////        store.setMetadata(uri2, "WR", "Hill");
////        store.setMetadata(uri2, "LB", "Chubb");
////
////
////
////        //set meta data the same for dif doc
////        store.setMetadata(uri5, "QB", "Tua");
////        store.setMetadata(uri5, "HB", "Mostert");
////        store.setMetadata(uri5, "WR", "Hill");
////        store.setMetadata(uri5, "LB", "Chubb");
////
////        store.setMetadata(uri3 ,"A","aaa");
////        store.setMetadata(uri3,"BB","bb");
////        store.setMetadata(uri3,"CCC","c");
////        System.out.println("1: " + store.get(uri1).getLastUseTime());
////        System.out.println("2: " + store.get(uri2).getLastUseTime());
////        System.out.println("3: " + store.get(uri3).getLastUseTime());
////        System.out.println("4: " + store.get(uri4).getLastUseTime());
////        System.out.println("5: " + store.get(uri5).getLastUseTime());
////        System.out.println("6: " + store.get(uri6).getLastUseTime());
//
////        System.out.println("1: " + store.get(uri1).getDocumentTxt().length());
////        System.out.println("2; " + store.get(uri2).getDocumentTxt().length());
////        System.out.println("3: "+store.get(uri3).getDocumentTxt().length());
////        System.out.println("4: "+ store.get(uri4).getDocumentTxt().length());
////        System.out.println("5: " + store.get(uri5).getDocumentTxt().length());
////        System.out.println("6: " + store.get(uri6).getDocumentTxt().length());
////System.out.println("NEW HEEP");
////       store.search("pdy"); // 6
////        store.searchByMetadata(map); //5,2,1
////        store.docHeap.print();
//////        store.setMaxDocumentBytes(400);
//////        store.setMaxDocumentCount(3);
////
////
////        store.docHeap.print();
////
////
//        //deleteAll uri 1 and 4 and 5
//        System.out.println("deleteALL");
//        store.deleteAll("DELeted");
////        assertEquals(Collections.emptyList(),store.search("DELeted"));
//        store.docHeap.print();
//        store.undo();
//        store.docHeap.print();
//        System.out.println();
//
//        //delete all with prefix 5,3
//
//        System.out.println("deleteALL");
//        store.deleteAllWithPrefix("Top");
//        store.docHeap.print();
//        store.undo();
//        store.docHeap.print();
//        System.out.println();
//
//
//        //all with metadata 1,2,5
//        System.out.println("deleteALLwith metadata");
//        System.out.println("URI 1     " + store.storage.get(uri1));
//        store.deleteAllWithMetadata(map);
//        store.docHeap.print();
//        store.undo();
//        store.docHeap.print();
//
//        //keyword and meta data 1,5
//        System.out.println("deleteALL with keyword and metadata");
//        store.deleteAllWithKeywordAndMetadata("HERRO",map);
//        store.docHeap.print();
//        store.undo();
//        store.docHeap.print();
//
//        //prefix meta data 1,2,5
//        store.deleteAllWithPrefixAndMetadata("py",map);
//        store.docHeap.print();
//        store.undo();
//        store.docHeap.print();
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
//
//
//

    }
    @Test
    public void IDK() throws URISyntaxException, IOException {
        DocumentStoreImpl store = new DocumentStoreImpl();
        InputStream stream1 = new ByteArrayInputStream("rock aweut op  tooo Thesee app These to  Theseeee Thes$e are the words, awesome please ign#ore th*e weird ones 123$".getBytes());
        InputStream stream2 = new ByteArrayInputStream("awe Th opkkkkkk apple".getBytes());
        InputStream stream3 = new ByteArrayInputStream("Theseeee  opppp awesom These awqo These These These These$e".getBytes());
        InputStream stream4 = new ByteArrayInputStream("Theseee aweqo op222 too appoqweh app To aeeppe aee aweoi These app appo appt appw appq These These appllll These$e".getBytes());
        InputStream stream5 = new ByteArrayInputStream("rock Theseeee opiuy78990 awe#244 These tooo These to to appppp appp  These$e".getBytes());
        InputStream stream6 = new ByteArrayInputStream("app appl awleq !@#H*&^dsauhvfausvuyvdfausvf !@#H*&^dsauhvfausvuyvdfaus !@#H*&^dsauhvfausvuyvdfaus !@#H*&^dsauhvfausvuyvdfaussd appewrf appe app6".getBytes());
        InputStream stream7 = new ByteArrayInputStream("app appl awleq !@#H*&^dsauhvfausvuyvdfausvfsd appewrf appe app6".getBytes());
        InputStream stream8 = new ByteArrayInputStream("app appl aapp awqo appewrf appe app6".getBytes());
        InputStream stream9 = new ByteArrayInputStream("app appl awleqqafgw rwseabfiags ydfcvuyas ewrf appe app6".getBytes());
        InputStream stream10 = new ByteArrayInputStream("app app 198yt2318gdsah acibj hae euigd dd6".getBytes());
        InputStream stream11 = new ByteArrayInputStream("app appl awleq !@#H*& asoiujdb wo198yjkalclmbvlpds[oj ".getBytes());
        InputStream stream12 = new ByteArrayInputStream("app appl awleq !@#H*&^%#Oedqasdj as was".getBytes());


        InputStream stream13 = new ByteArrayInputStream("Thi?s is the OG$ input stream OG words".getBytes());
        InputStream stream14 = new ByteArrayInputStream("4th stream now i'm really running out of words".getBytes());
        InputStream stream15 = new ByteArrayInputStream("5th which because wh so ya".getBytes());
        InputStream stream16 = new ByteArrayInputStream("number 6 input stream".getBytes());
        InputStream stream17 = new ByteArrayInputStream("nuuuuuuu wor str*&^eam 7".getBytes());
        InputStream stream18 = new ByteArrayInputStream("yu yu yu 8765 !@no!".getBytes());
        InputStream stream19 = new ByteArrayInputStream("seven seventy jjjjjjjjjjjjjjjjjjjjjjjjjjjkkkkkkkkkkkkkk home of chabbad what up, wordddddddd ".getBytes());
        InputStream stream20 = new ByteArrayInputStream("10 minyan man wor!".getBytes());
        InputStream stream21 = new ByteArrayInputStream("number wo eleven mini".getBytes());
        InputStream stream22 = new ByteArrayInputStream("Roseter".getBytes());
        InputStream streamBIN = new ByteArrayInputStream("FOR BINARY".getBytes());


        URI uri1 = new URI("Uri1");
        URI uri2 = new URI("uri2");
        URI uri3 = new URI("uri3");
        URI uri4 = new URI("uri4");
        URI uri5 = new URI("uri5");
        URI uri6 = new URI("uri6");
        URI uri7 = new URI("uri7");
        URI uri8 = new URI("uri8");
        URI uri9 = new URI("uri9");
        URI uri10 = new URI("uri10");
        URI uri11 = new URI("uri11");
        URI uri12 = new URI("uri12");
        URI QB = new URI("QB");
        URI HB = new URI("HB");
        URI WR = new URI("WR");
        URI OL = new URI("OL");
        URI CB = new URI("CB");
        URI DL = new URI("DL");
        URI LB = new URI("LB");
        URI SS = new URI("SS");
        URI HC = new URI("HC");
        URI OC = new URI("OC");
        URI DC = new URI("DC");
        URI DC1 = new URI("DC1");
        URI DC2 = new URI("DC2");
        URI DC3 = new URI("DC3");
        URI DC4 = new URI("DC4");
        URI DC5 = new URI("DC5");
        URI DC6 = new URI("DC6");
        URI DC7 = new URI("DC7");
        URI DC8 = new URI("DC8");
        URI N = new URI("DCcccc");


        store.put(stream1, uri1, DocumentStore.DocumentFormat.BINARY);
        store.put(stream2, uri2, DocumentStore.DocumentFormat.TXT);
        store.put(stream3, uri3, DocumentStore.DocumentFormat.BINARY);
        store.put(stream4, uri4, DocumentStore.DocumentFormat.TXT);
        store.put(stream5, uri5, DocumentStore.DocumentFormat.BINARY);
        store.put(stream6, uri6, DocumentStore.DocumentFormat.TXT);
        store.put(stream7,uri7, DocumentStore.DocumentFormat.BINARY);
        store.put(stream8, uri8, DocumentStore.DocumentFormat.BINARY);
        store.put(stream9,uri9, DocumentStore.DocumentFormat.TXT);
        store.put(stream10, uri10, DocumentStore.DocumentFormat.BINARY);
        store.put(stream11,uri11, DocumentStore.DocumentFormat.TXT);
        store.put(stream12,uri12, DocumentStore.DocumentFormat.TXT);
        store.put(stream11,HB, DocumentStore.DocumentFormat.TXT);
        store.put(stream18,DC5, DocumentStore.DocumentFormat.TXT);
        store.put(stream2,DC1, DocumentStore.DocumentFormat.TXT);
        store.put(stream10,DC2, DocumentStore.DocumentFormat.TXT);
        store.put(stream15,DC3, DocumentStore.DocumentFormat.TXT);
        store.put(stream17,DC4, DocumentStore.DocumentFormat.TXT);
        store.put(stream22,DC6, DocumentStore.DocumentFormat.TXT);
        store.put(stream2,DC7, DocumentStore.DocumentFormat.TXT);
        store.put(stream21,DC8, DocumentStore.DocumentFormat.TXT);
        store.put(stream18,WR, DocumentStore.DocumentFormat.BINARY);
        store.put(stream19,OL, DocumentStore.DocumentFormat.BINARY);
        store.put(stream20,OL, DocumentStore.DocumentFormat.BINARY);
        store.put(stream3,CB, DocumentStore.DocumentFormat.TXT);
        store.put(stream16,DL, DocumentStore.DocumentFormat.BINARY);
        store.put(stream17,LB, DocumentStore.DocumentFormat.TXT);
        store.put(stream2,SS, DocumentStore.DocumentFormat.BINARY);
        store.put(stream11,N, DocumentStore.DocumentFormat.TXT);
        System.out.println(store.stack.size());
        System.out.println(store.stack.size());
        store.undo(uri8);
        store.undo(uri1);
        store.undo(uri2);
//        store.undo(uri10);
//        store.undo(WR);
//        store.undo(DC5);
        System.out.println(store.stack.size());


        System.out.println("STORE 1 " + store.get(uri1));
        System.out.println("STORE 2 " + store.get(uri2));
        System.out.println("STORE 3 " + store.get(uri3));
        System.out.println("STORE 4 " + store.get(uri4));
        System.out.println(store.stack.size());
        System.out.println(store.docHeap.peek().getKey().toString());
        store.docHeap.print();
        store.delete(uri4);
//        store.undo();
        System.out.println("AFTER DELETE OF URI 4");
        System.out.println("STORE 4 " + store.get(uri4));
        store.docHeap.print();
    }
}
