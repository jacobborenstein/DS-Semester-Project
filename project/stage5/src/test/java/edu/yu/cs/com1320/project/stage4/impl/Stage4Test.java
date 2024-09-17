 package edu.yu.cs.com1320.project.stage4.impl;


 import edu.yu.cs.com1320.project.stage5.Document;
 import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;

 import java.io.ByteArrayInputStream;
 import java.io.InputStream;
 import java.util.Comparator;

 public class Stage4Test {

     DocumentStoreImpl store = new DocumentStoreImpl();
     InputStream stream1 = new ByteArrayInputStream("rock aweut op  tooo  app These to  T Thes$e Thes$e are the words, awesome please ign#ore th*e weird ones 123$ Thes$e! Thes$e!".getBytes());
     InputStream stream2 = new ByteArrayInputStream("awe Th opkkkkkk apple".getBytes());
     InputStream stream3 = new ByteArrayInputStream("Theseeee  opppp awesom These awqo These These These These$e".getBytes());
     InputStream stream4 = new ByteArrayInputStream("Theseee aweqo op222 too appoqweh app To aeeppe aee aweoi These app appo appt appw appq These These appllll These$e".getBytes());
     InputStream stream5 = new ByteArrayInputStream("rock Topiuy78990 awe#244 awe#244 These tooo These to to appppp Thes appp helloabduwu273d Thesee$ Thesee! d".getBytes());
     InputStream stream6 = new ByteArrayInputStream("app appl awleq !@#H*&^%#O)@*#*P These app awqo appewrf appe app6".getBytes());

     public static class compSearch implements Comparator<Document> {
         private String word;
         @Override
         public int compare(Document o1, Document o2) {
             return o2.wordCount(word) - o1.wordCount(word);
         }
     }

     public static class comp implements Comparator<Document> {

         @Override
         public int compare(Document o1, Document o2) {
             return 0;
         }
     }

//     @Test
// public void Stage4Test() throws URISyntaxException, IOException {
//         URI uri1 = new URI("Uri1");
//         URI uri2 = new URI("uri2");
//         URI uri3 = new URI("uri3");
//         URI uri4 = new URI("uri4");
//         URI uri5 = new URI("uri5");
//         URI uri6 = new URI("uri6");
//         URI uri12 = new URI("uri62222");
//
//         this.store.put(stream1,uri1, DocumentStore.DocumentFormat.TXT);
//         this.store.put(stream2,uri2, DocumentStore.DocumentFormat.TXT);
//         this.store.put(stream3,uri3, DocumentStore.DocumentFormat.TXT);
//         this.store.put(stream4,uri4, DocumentStore.DocumentFormat.TXT);
//         this.store.put(stream5,uri5, DocumentStore.DocumentFormat.TXT);
//         this.store.put(stream6,uri6, DocumentStore.DocumentFormat.TXT);
//
//
//         DocumentImpl d = new DocumentImpl(uri2,"Theseeee These These These These$e" );
// //        assertEquals(1, store.Trie.get("These"));
// //        assertEquals(1, store.Trie.get("are"));
//         System.out.println("1: " + store.get(uri1));
//         System.out.println("2: " + store.get(uri2));
//         System.out.println("3: " + store.get(uri3));
//         System.out.println("4: " + store.get(uri4));
//         System.out.println("5: " + store.get(uri5));
//         System.out.println("6: " + store.get(uri6));
//         System.out.println("Thesee".replaceAll("[^a-zA-Z0-9]","").equals( "Thesee!".replaceAll("[^a-zA-Z0-9]","")));
//
//         System.out.println();
////         store.deleteAllTraces(store.get(uri5));
////         store.deleteAllTraces(store.get(uri1));
//         System.out.println(store.searchByPrefix("Th"));
//         System.out.println();
//
//         Map<String,String> map = new HashMap<>();
//         map.put("QB","Tua");
//         map.put("HB", "Mostert");
//         map.put("WR", "Hill");
//         map.put("LB", "Chubb");
//
//         Map<String,String> map2 = new HashMap<>();
//         map2.put("A","aaa");
//         map2.put("BB","bb");
//         map2.put("CCC","c");
//
//
// //***************************************************************
//         store.setMetadata(uri3 ,"A","aaa");
//         store.setMetadata(uri3,"BB","bb");
//         store.setMetadata(uri3,"CCC","c");
//
//         store.setMetadata(uri6 ,"A","aaa");
//         store.setMetadata(uri6,"BB","bb");
//         store.setMetadata(uri6,"CCC","c");
//
//
//
//         //test sear by meta data
//         //set on docs metadata
//         store.setMetadata(uri2, "QB", "Tua");
//         store.setMetadata(uri2, "HB", "Mostert");
//         store.setMetadata(uri2, "WR", "Hill");
//         store.setMetadata(uri2, "LB", "Chubb");
//
//         //set meta data the same for dif doc
//         store.setMetadata(uri1, "QB", "Tua");
//         store.setMetadata(uri1, "HB", "Mostert");
//         store.setMetadata(uri1, "WR", "Hill");
//         store.setMetadata(uri1, "LB", "Chubb");
//
//         //set meta data the same for dif doc
//         store.setMetadata(uri5, "QB", "Tua");
//         store.setMetadata(uri5, "HB", "Mostert");
//         store.setMetadata(uri5, "WR", "Hill");
//         store.setMetadata(uri5, "LB", "Chubb");
//
//         //set meta data the same for dif doc but add one more with DIF KEY , so shouldnt be considered a match
//
//
//         //set meta data the same for dif doc but add one more with DIF VALUE , so shouldnt be considered a match
//         store.setMetadata(uri4, "QB", "Tua");
//         store.setMetadata(uri4, "HB", "Mostert");
//         store.setMetadata(uri4, "WR", "Hill");
//         store.setMetadata(uri4, "LB", "Chubb");
//
//
//
//         URI TEST =  new URI("TEST");
//         store.put(stream2,TEST,DocumentStore.DocumentFormat.BINARY);
//         System.out.println("GET TEST: " + store.get(TEST).toString());
//
//         System.out.println("*************************");
//         for (String s: store.get(uri5).getWords()){
//             System.out.println(s.replaceAll("[^a-zA-Z0-9]",""));
//         }
//
//         System.out.println();
//
// ////////////Delte all with meta data *****************************************************************
// ;
//
//
////         System.out.println("deleting  b/c meta data 1,5,2,4: " + store.deleteAllWithMetadata(map));
////         System.out.println("deleting  b/c meta data 3 & 6: " + store.deleteAllWithMetadata(map2));
////        System.out.println("deleting  b/c meta data 3 & 6 SHUD BE EMPTY : " + store.deleteAllWithMetadata(map2));
////         System.out.println("SHOULD BE EMPTY 1,5,2,4: " + store.searchByMetadata(map));
////         System.out.println("SHOULD BE EMPTY 3,6: " + store.searchByMetadata(map));
////
////         store.undo(TEST);
////         System.out.println("GET TEST null:  " + store.get(TEST));
////
////
////         store.undo();
////         store.undo();
////         System.out.println("SHOULD BE full 1,5,2,4: " + store.searchByMetadata(map));
////         System.out.println("SHOULD BE full 3,6: " + store.searchByMetadata(map2));
//
//
//// *********************************************************************************************************************
// //Delete all with meta data and keyword
//         System.out.println("DELETE matching Meta data and keyWord 1 & 5: " + store.deleteAllWithKeywordAndMetadata("tooo",map));
//         System.out.println("DELETE matching Meta data and keyWord 3 & 6: " + store.deleteAllWithKeywordAndMetadata("awqo",map2));
//         System.out.println("shud be empy: " + store.searchByKeywordAndMetadata("tooo",map));
//         System.out.println("shud be empy: " + store.searchByKeywordAndMetadata("awqo",map2));
//
//         store.undo();
//         System.out.println("shud be full  3 & 6: " + store.searchByKeywordAndMetadata("awqo",map2));
//         System.out.println("shud still be empty 1,5: " + store.searchByKeywordAndMetadata("tooo",map));
//         store.undo();
//         System.out.println("shud be full 1,5: " + store.searchByKeywordAndMetadata("tooo",map));
//
// //*********************************************************************************************************************
//
//
//         ////delete all with meta data and prefix
//         System.out.println("HOP" + store.search("HOP"));
//         System.out.println("DELETING meta data and prefix 1,2,4,5: " + store.deleteAllWithPrefixAndMetadata("op",map));
//        System.out.println("SHUD BE EMPTY 1,2,4,5: " + store.searchByPrefixAndMetadata("op",map));
//
//
//        store.undo();
//
//        System.out.println("SHUD BE full 1,2,4,5: " + store.searchByPrefixAndMetadata("op",map));
//
// //*********************************************************************************************************************
//         System.out.println("_-----------------------------------------");
//         System.out.println();
//         System.out.println("Matching meta data and prefix 1,2,4,5: " + store.searchByPrefixAndMetadata("awe",map));
//
//         System.out.println("_-----------------------------------------");
//         System.out.println();
//         System.out.println("matching meta data 1,5,2,4: " + store.searchByMetadata(map));
//         System.out.println("matching Meta data and keyWord 1 & 5: " + store.searchByKeywordAndMetadata("tooo",map));
//         System.out.println("matching meta data 3 & 6: " + store.searchByMetadata(map2));
//         System.out.println("matching Meta data and keyWord 3 & 6: " + store.searchByKeywordAndMetadata("awqo",map2));
//         System.out.println();
//         System.out.println("_-----------------------------------------");
//         System.out.println();
//
//         //***************************************************************
//        System.out.println("ERROR");
//        System.out.println("1    " + store.get(uri1));
//         System.out.println("5    " + store.get(uri5));
//         store.docHeap.print();
//       System.out.println("Deleted URIs: 1 ,5 " + store.deleteAll("to"));
//         System.out.println(store.searchByPrefix("to"));
//       System.out.println();
//         store.undo();
//         System.out.println("SHUD HAVE 3; " + store.searchByPrefix("to"));
//         System.out.println("Deleted URIs: 1 ,5 " + store.deleteAll("to"));
//         store.docHeap.print();
//
//
//         System.out.println("1: " + store.get(uri1));
//         System.out.println("2: " + store.get(uri2));
//         System.out.println("3: " + store.get(uri3));
//         System.out.println("4: " + store.get(uri4));
//         System.out.println("5: " + store.get(uri5));
//         System.out.println("6: " + store.get(uri6));
//
//
//         System.out.println();
//
//
//
//         System.out.println(store.searchByPrefix("app"));
//         System.out.println(store.search("These"));
//
//         System.out.println();
//         System.out.println("_-----------------------------------------");
//         System.out.println();
//         System.out.println(store.searchByPrefix("Th"));
//         System.out.println();
//         store.docHeap.print();
//         System.out.println("HERE");
//         System.out.println(store.get(uri5));
//         System.out.println(store.search("helloabduwu273d"));
//
//        compSearch cs = new compSearch();
//         System.out.println(store.Trie.getAllWithPrefixSorted("Th",cs).size());
//         System.out.println(store.searchByPrefix("Th"));
//         System.out.println(store.deleteAllWithPrefix("Th"));
//
//         System.out.println("AGAIN");
//         System.out.println(store.deleteAllWithPrefix("Th"));
//
//         System.out.println();
//         System.out.println("Shud all be null:");
//         System.out.println("1: " + store.get(uri1));
//         System.out.println("2: " + store.get(uri2));
//         System.out.println("3: " + store.get(uri3));
//         System.out.println("4: " + store.get(uri4));
//         System.out.println("5: " + store.get(uri5));
//         System.out.println("6: " + store.get(uri6));
//         System.out.println();
//         store.undo();
//         store.undo();
//         System.out.println("Shud all be back:");
//         System.out.println("1: " + store.get(uri1));
//         System.out.println("2: " + store.get(uri2));
//         System.out.println("3: " + store.get(uri3));
//         System.out.println("4: " + store.get(uri4));
//         System.out.println("5: " + store.get(uri5));
//         System.out.println("6: " + store.get(uri6));
//
//         System.out.println(store.searchByPrefix("Th"));
//
//         System.out.println();
//         System.out.println("_-----------------------------------------");
//         System.out.println();
//
//
//
//
//
//
// }
//
//




 }
