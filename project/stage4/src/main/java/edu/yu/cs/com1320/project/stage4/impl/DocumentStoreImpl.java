//                                                                                          בס"ד
package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.DocumentStore;
import edu.yu.cs.com1320.project.undo.CommandSet;
import edu.yu.cs.com1320.project.undo.GenericCommand;
import edu.yu.cs.com1320.project.undo.Undoable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;

public class DocumentStoreImpl implements DocumentStore {
    private HashTableImpl<URI, Document> storage = new HashTableImpl<>();
    private StackImpl<Undoable> stack = new StackImpl();
    private TrieImpl<Document> Trie = new TrieImpl<>();






    /**
     * set the given key-value metadata pair for the document at the given uri
     *
  //   * @param uri
     * @param key
    // * @param value
     * @return the old value, or null if there was no previous value
     * @throws IllegalArgumentException if the uri is null or blank(1st and 2nd), if there is no document stored at that uri(3rd),
     * or if the key is null or blank
     */

    @Override
    public String setMetadata(URI uri, String key, String value) {
        if (uri == null || uri.toString().isEmpty() || !this.storage.containsKey(uri) || this.storage.get(uri) == null || key == null || key.isEmpty()){
//            if (!this.storage.containsKey(uri)){
//                System.out.println("CONTAINS KEY PROB " + uri.toString());
//            }
//            if( this.storage.get(uri) == null){
//                System.out.println(".get() PROB");
//            }
            throw new IllegalArgumentException();
        }
        String old = this.storage.get(uri).getMetadataValue(key);
        consumerUndoMetaData(uri,key,value);
        this.storage.get(uri).setMetadataValue(key, value);
        return old;
    }
    private void consumerUndoMetaData(URI uri, String key, String value){
        //if there was a metadata previously with the same key, return it to that value
        if (this.storage.get(uri).getMetadataValue(key) != null) {
            String undoValue =  this.storage.get(uri).getMetadataValue(key);
          Consumer<URI> UndoMetaData = (i) -> this.storage.get(i).setMetadataValue(key,undoValue);
          GenericCommand<URI> MetaDataCommand = new GenericCommand(uri,UndoMetaData);
          this.stack.push(MetaDataCommand);
        } else { //no previous meta data with the given key
            //***CUD BE WRONG B/C CANT MAKE KEY NULL (THROWS AN E) BUT
            // SUPPOSE TO DELETE THE METADATA SO FOR NOW JUST MAKE VALUE NUL
            Consumer<URI> UndoMetaData = (i) ->  this.storage.get(i).setMetadataValue(key, null);
            GenericCommand<URI> MetaDataCommand = new GenericCommand(uri,UndoMetaData);
            this.stack.push(MetaDataCommand);
        }
    }
    /**
     * get the value corresponding to the given metadata key for the document at the given uri
     *
     * @param uri
     * @param key
     * @return the value, or null if there was no value
     * @throws IllegalArgumentException if the uri is null or blank(1st,2nd), if there is no document stored at that uri, or if the key is null or blank
     */
    @Override
    public String getMetadata(URI uri, String key) {
        if (uri == null || uri.toString().isEmpty() || !this.storage.containsKey(uri) || this.storage.get(uri) == null || key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return this.storage.get(uri).getMetadataValue(key);

    }

    /**
     * @param input  the document being put
     * @param uri    unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. If there is a previous doc, return the hashCode of the previous doc.
     * If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     * @throws IOException              if there is an issue reading input
     * @throws IllegalArgumentException if uri is null or empty, or format is null
     */
    @Override
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException {
        if (uri == null || uri.toString().isEmpty() || format == null) {
            throw new IllegalArgumentException();
        }if (input == null) { //CALL TO DELETE*****
            if ( !this.storage.containsKey(uri) || this.storage.get(uri) == null)  { return 0;
            } else { int old = this.storage.get(uri).hashCode();
                consumerUndoDelete(uri,format); // creates instance of command and adds it to stack (for undo)
                this.storage.put(uri,null);//////calling putin HashTableImpl with null as value deletes the uri
                return old;
            } } else { // input IS NOT NULL so we have to add the Doc
            byte[] by = input.readAllBytes();
            String str = new String(by);
            if (format == DocumentFormat.TXT) { // if formant == TXT
                Document doc = new DocumentImpl(uri, str);
                if (!this.storage.containsKey(uri)) { //if Doc is not in hashTable this adds it and return 0
                    this.storage.put(uri, doc);
                    putWordsInTrie(doc);   //add words of the doc to the trie
                    consumerUndoPut(uri,format); // creates instance of command and adds it to stack (for undo)
                    return 0;    } else {
                    consumerUndoPut(uri,format); //creates instance of command and adds it to stack (for undo)
                   // putWordsInTrie(doc);   //add words of the doc to the trie
                    return putHelp(uri,doc); //if Doc is in hashtable this method updates the uri and returns old uri hashcodes
                } } if (format == DocumentFormat.BINARY) { //if format == BINARY
                Document doc = new DocumentImpl(uri, by);
                if (!this.storage.containsKey(uri)) { //if Doc is not in hashTable this adds it and return 0
                    this.storage.put(uri, doc);
                    consumerUndoPut(uri,format); //creates instance of command and adds it to stack (for undo)
                    return 0;    } else {
                    consumerUndoPut(uri,format);//creates instance of command and adds it to stack (for undo)
                    return putHelp(uri,doc); //if Doc is in hashtable this method updates the uri and returns old uri hashcodes
                } } } consumerUndoPut(uri,format); return 0; } //***MIGHT have to delete consumerUndoPut()

    private int putHelp(URI uri, Document doc){ //save lines in put method. returns hashcode of old document that is being replace
        int hashcode = this.storage.get(uri).hashCode();
        this.storage.put(uri, doc);
        return hashcode;
    }

    private void putWordsInTrie(Document doc){
       Set<String> wordsInDoc = doc.getWords();
       //add all words from the document into the trie
       for (String w : wordsInDoc){
           this.Trie.put(w,doc);
       }
    }

    private void consumerUndoDelete(URI uri, DocumentFormat format) {
        InputStream input = null;
        if (format == DocumentFormat.BINARY) {
            input = getBytes(uri, DocumentFormat.BINARY);
        } else {
            if (format == DocumentFormat.TXT) {
                input = getBytes(uri, DocumentFormat.TXT);
            }
        }
        consumerMakerForUndoDelete(input,uri,format);
    }
    private void consumerMakerForUndoDelete(InputStream input, URI uri, DocumentFormat format){
        if (this.storage.get(uri).getMetadata()!= null){

        }
        Consumer<URI> undoDelete = (i) -> {
            try {
                this.UndoDeleteHelp(input, i, format);     //  creates a consumer that will execute the put method with the
            } catch (IOException e) {           //  given params. the code will be execute when .accept(i) is called.
                throw new RuntimeException(e);
            }
        };
        this.stack.push(new GenericCommand<URI>(uri,undoDelete)); //pushing the command with this consumer to the stack. code can
  }                                                 //execute when the command gets popped and .accept is called through the undo method
    private void consumerUndoPut(URI uri, DocumentFormat format){
        Consumer<URI> undoPut = (i) -> this.storage.put(i,null);
        this.stack.push(new GenericCommand<URI>(uri,undoPut));
    }
    private InputStream getBytes(URI uri, DocumentFormat format) {
        if (format == DocumentFormat.TXT) {
            InputStream stream = new ByteArrayInputStream(this.storage.get(uri).getDocumentTxt().getBytes());
            return stream;
        }
        InputStream stream = new ByteArrayInputStream(this.storage.get(uri).getDocumentBinaryData());
        return stream;
    }

    /**
     * @param url the unique identifier of the document to get
     * @return the given document
     */
    @Override
    public Document get(URI url) {
        if (this.storage.get(url) == null){
            return null;
        }
        return this.storage.get(url);
    }
    /**
     * @param url the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    @Override
    public boolean delete(URI url) {
        if (!this.storage.containsKey(url) || this.storage.get(url) == null) {
            return false;
        }
        DocumentFormat format = null;
          if (this.storage.get(url).getDocumentTxt() != null) { // uri is TXT type
              format = DocumentFormat.TXT;
          } else {
              if (this.storage.get(url).getDocumentBinaryData() != null) {
                  format = DocumentFormat.BINARY;
              }
          }
            consumerUndoDelete(url,format);
            this.storage.put(url,null); //// changed
            return true;

    }
    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */

    @Override
    public void undo() throws IllegalStateException {
        if (this.stack.size() == 0 ||this.stack.peek() ==null){
            throw new IllegalStateException();
        }
        //is a command set
        if (this.stack.peek() instanceof CommandSet<?>){
            ((CommandSet<?>) this.stack.pop()).undoAll();
        }else {
            //Is Generic Command
            this.stack.pop().undo();
        }
    }
    /**
     * undo the last put or delete that was done with the given URI as its key
    // * @param url
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */





        @Override
    public void undo(URI url) throws IllegalStateException {
            if (this.stack.size() == 0 || this.stack.peek() == null) {
            throw new IllegalStateException();
        }
        StackImpl<Undoable> tempStack = new StackImpl(); //create temp stack to store
        while (this.stack.size() != 0){
            Undoable command = this.stack.peek();
            //top command on stack is a GenericCommand
            if (command instanceof GenericCommand<?>){
                //if the uri is found
                if (((GenericCommand<?>) command).getTarget().equals(url)) {
                    this.stack.pop().undo();
                    while (tempStack.size() != 0) {
                        //push tempStack back to main STack
                        this.stack.push(tempStack.pop());
                    } return;
                    //uri was not found. Push to tempStack then for back to top of while loop
                } else {        tempStack.push(this.stack.pop());       }
            //command is a CommandSet
            } else {
                if (command instanceof CommandSet<?>){
                // uri was found in CommandSet
                    if (((CommandSet<URI>) command).contains(url)){
                        ((CommandSet<URI>) command).undo(url);
                        if(((CommandSet<URI>) command).undo() == true){
                            this.stack.pop();
                        }
                        while (tempStack.size() != 0){
                        //push tempStack back to main STack
                        this.stack.push(tempStack.pop());
                        } return;
                    //uri was not found. Push to tempStack then for back to top of while loop
                    } else{     tempStack.push(this.stack.pop());      }
                }
            }
        }
        throw new IllegalStateException();
    }

    private static class compSearch implements Comparator<Document> {
        private String word;
        @Override
        public int compare(Document o1, Document o2) {
            return o2.wordCount(word) - o1.wordCount(word);
        }
    }

    /**
     * Retrieve all documents whose text contains the given keyword.
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE SENSITIVE.
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    @Override
    public List<Document> search(String keyword) {
        compSearch cs = new compSearch();
        cs.word = keyword;
        return this.Trie.getSorted(keyword,cs);
    }
    /**
     * Retrieve all documents that contain text which starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    @Override
    public List<Document> searchByPrefix(String keywordPrefix) {
        compSearchPreFix csPreFix = new compSearchPreFix();
        csPreFix.word = keywordPrefix;
        return this.Trie.getAllWithPrefixSorted(keywordPrefix,csPreFix);
    }
    private static class compSearchPreFix implements Comparator<Document> {
        private String word;
        @Override
        public int compare(Document o1, Document o2) {
            return docPreFix(o2) - docPreFix(o1);
           }
           //method to check how many times a prefix is in a doc
        private int docPreFix(Document doc){
            int i = 0;
            for (String s : doc.getWords()){
                if (s.startsWith(this.word)){
                    i++;
                }
            }
            return i;
        }
    }
    /**
     * Completely remove any trace of any document which contains the given keyword
     * Search is CASE SENSITIVE.
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */
    @Override
    public Set<URI> deleteAll(String keyword) {
        Set<URI> uriSet = new HashSet<>();
        Set<Document> DocSet = this.Trie.deleteAll(keyword);
        // go through the set of delted documents and add their key (a.k.a their URI) to uriSet and then return the uriSet
        for (Document doc: DocSet){
            uriSet.add(doc.getKey());
            deleteAllTraces(doc);
            //delte the document from the hashtable ****prob have to create new commands/undos that where added to stage 4
           this.storage.put(doc.getKey(),null);

        }
        UndoCommandSetDelete(DocSet);
        return uriSet;
    }
    private void UndoCommandSetDelete(Set<Document> docCollection){
        CommandSet<URI> commandSet = new CommandSet<>();
        for (Document doc : docCollection ){
           //create consumer to put doc back
            Consumer<URI> undoDelete = (i) -> {
               //when consumer is executed it will add doc back into hashTable
                this.storage.put(doc.getKey(),doc);
                //when consumer is executed it will add words into the Trie
               putWordsInTrie(doc);
           };
            //add generic command to the commandSet
            commandSet.addCommand(new GenericCommand<>(doc.getKey(),undoDelete));
        }
        //after all documents are added to the command set, add command set to the stack
        this.stack.push(commandSet);
    }
    /**
     * Completely remove any trace of any document which contains a word that has the given prefix
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    @Override
    public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
        Set<URI> uriSet = new HashSet<>();
        Set<Document> DocSet = this.Trie.deleteAllWithPrefix(keywordPrefix);
        // go through the set of delted documents and add their key (a.k.a their URI) to uriSet and then return the uriSet
        for (Document doc : DocSet) {
            uriSet.add(doc.getKey());
            //deletes all traces of the document, if the document has any other words, the document gets removed from those nodes in the trie
            deleteAllTraces(doc);
            //********delte the document from the hashtable ****prob have to create new commands/undos that where added to stage 4
            //*********** this.storage.put(doc.getKey(), null);
        }
        UndoCommandSetDelete(DocSet);
        return uriSet;
    }
    // this method deletes all Traces of the @param Document. it deletes the given document from any words that it is under in the Trie
    private void deleteAllTraces(Document doc){
        for (String s : doc.getWords()){
            this.Trie.delete(s, doc);
        }
        //delte the document from the hashtable ****prob have to create new commands/undos that where added to stage 4
        this.storage.put(doc.getKey(), null);
    }
    /**
     * @param keysValues metadata key-value pairs to search for
     * @return a List of all documents whose metadata contains ALL OF the given values for the given keys. If no documents contain all the given key-value pairs, return an empty list.
     */
    @Override
    public List<Document> searchByMetadata(Map<String, String> keysValues) {
        List<Document> MetaDataList = new ArrayList<>();
        // create a set of all values in the @param keysValues map b/c .values() return a collection and we need a set b/c sets ignore order so its eaiser to comapre
        Set valueSet = new HashSet(keysValues.values());
        for (URI uri : this.storage.keySet()) {
            //create a set of the URI's metadata value
            Set uriValueSet = new HashSet<>(this.storage.get(uri).getMetadata().values());
               //compare the URI's keys and values. if they are both the same, add it to the list of documents
                if (this.storage.get(uri).getMetadata().keySet().equals(keysValues.keySet()) && valueSet.equals(uriValueSet)) {
                    MetaDataList.add(this.storage.get(uri));
                }
            }
    return MetaDataList;
    }
    /**
     * Retrieve all documents whose text contains the given keyword AND which has the given key-value pairs in its metadata
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE SENSITIVE.
     * @param keyword
     * @param keysValues
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    @Override
    public List<Document> searchByKeywordAndMetadata(String keyword, Map<String, String> keysValues) {
        List<Document> DocsWithKeyword = this.search(keyword);
        Set nonMatch = new HashSet<Document>();
        // create a set of all values in the @param keysValues map b/c .values() return a collection and we need a set b/c sets ignore order so its eaiser to comapre
        Set ValueSet = new HashSet(keysValues.values());
        for (Document docs : DocsWithKeyword) {
         //create a set of the URI's metadata value
            Set uriValueSet = new HashSet<>(docs.getMetadata().values());
         //if keys and values dont match, remove the document from the sorted list above (DocsWithKeyword)
            if (!keysValues.keySet().equals(docs.getMetadata().keySet()) && !ValueSet.equals(uriValueSet)) {
                nonMatch.add(docs);
            }
        }
        DocsWithKeyword.removeAll(nonMatch);
        return DocsWithKeyword;
    }

    /**
     * Retrieve all documents that contain text which starts with the given prefix AND which has the given key-value pairs in its metadata
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    @Override
    public List<Document> searchByPrefixAndMetadata(String keywordPrefix, Map<String, String> keysValues) {
        //get a list of all documents that contain the prefix
        List<Document> DocsWithKeyword = this.searchByPrefix(keywordPrefix);
        // create a set of all values in the @param keysValues map b/c .values() return a collection and we need a set b/c sets ignore order so its eaiser to comapre
        Set nonMatch = new HashSet<Document>();
        // create a set of all values in the @param keysValues map b/c .values() return a collection and we need a set b/c sets ignore order so its eaiser to comapre
        Set ValueSet = new HashSet(keysValues.values());
        for (Document docs : DocsWithKeyword) {
            //create a set of the URI's metadata value
            Set uriValueSet = new HashSet<>(docs.getMetadata().values());
            //if keys and values dont match, remove the document from the sorted list above (DocsWithKeyword)
            if (!keysValues.keySet().equals(docs.getMetadata().keySet()) && !ValueSet.equals(uriValueSet)) {
                nonMatch.add(docs);
            }
        }
        DocsWithKeyword.removeAll(nonMatch);
        return DocsWithKeyword;
    }
    /**
     * Completely remove any trace of any document which has the given key-value pairs in its metadata
     * Search is CASE SENSITIVE.
     * @return a Set of URIs of the documents that were deleted.
     */
    @Override
    public Set<URI> deleteAllWithMetadata(Map<String, String> keysValues) {
        Set<URI> allURIsDelted = new HashSet<>();
        Set<Document> DocSet = new HashSet<>(this.searchByMetadata(keysValues));
        for (Document doc: DocSet){
            allURIsDelted.add(doc.getKey());
            //deletes all traces of the document, if the document has any other words, the document gets removed from those nodes in the trie
            deleteAllTraces(doc);
            //*********delte the document from the hashtable ****prob have to create new commands/undos that where added to stage 4
            //********this.storage.put(doc.getKey(), null);
        }
        UndoCommandSetDelete(DocSet);
        return allURIsDelted;
    }
    /**
     * Completely remove any trace of any document which contains the given keyword AND which has the given key-value pairs in its metadata
     * Search is CASE SENSITIVE.
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */
    @Override
    public Set<URI> deleteAllWithKeywordAndMetadata(String keyword, Map<String, String> keysValues) {
        Set<URI> allURIsDelted = new HashSet<>();
        Set<Document> DocSet = new HashSet<>();
        for (Document doc: this.searchByMetadata(keysValues)){
            if (doc.getWords().contains(keyword)){
                allURIsDelted.add(doc.getKey());
                DocSet.add(doc);
                //deletes all traces of the document, if the document has any other words, the document gets removed from those nodes in the trie
                deleteAllTraces(doc);
                //**************delte the document from the hashtable ****prob have to create new commands/undos that where added to stage 4
                //**************this.storage.put(doc.getKey(), null);
            }
        }
        UndoCommandSetDelete(DocSet);
        return allURIsDelted;
    }
    /**
     * Completely remove any trace of any document which contains a word that has the given prefix AND which has the given key-value pairs in its metadata
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    @Override
    public Set<URI> deleteAllWithPrefixAndMetadata(String keywordPrefix, Map<String, String> keysValues) {
        Set<URI> allURIsDelted = new HashSet<>();
        List DocsWithPrefix = this.searchByPrefix(keywordPrefix);
        Set<Document> DocSet = new HashSet<>();
        for (Document doc: this.searchByMetadata(keysValues)){
          if (DocsWithPrefix.contains(doc)) {
              allURIsDelted.add(doc.getKey());
              DocSet.add(doc);
              //deletes all traces of the document, if the document has any other words, the document gets removed from those nodes in the trie
              deleteAllTraces(doc);
              //**************delte the document from the hashtable ****prob have to create new commands/undos that where added to stage 4
              //**************this.storage.put(doc.getKey(), null);
          }
        }
        UndoCommandSetDelete(DocSet);
        return allURIsDelted;
    }

    private void UndoDeleteHelp(InputStream input, URI uri, DocumentFormat format) throws IOException {
        byte[] by = input.readAllBytes();
        String str = new String(by);
        if (format == DocumentFormat.TXT) { // if formant == TXT
            Document doc = new DocumentImpl(uri, str);
            if (!this.storage.containsKey(uri)) { //if Doc is not in hashTable this adds it and return 0
                this.storage.put(uri, doc);
            } else {
                putHelp(uri, doc); //if Doc is in hashtable this method updates the uri and returns old uri hashcodes
                putWordsInTrie(doc);
//                for (String s : doc.getWords()){
//                    this.Trie.put(s,doc);
//                }
            }
        }
        if (format == DocumentFormat.BINARY) { //if format == BINARY
            Document doc = new DocumentImpl(uri, by);
            if (!this.storage.containsKey(uri)) { //if Doc is not in hashTable this adds it and return 0
                this.storage.put(uri, doc);
            } else {
                putHelp(uri, doc);
                putWordsInTrie(doc);
            }
        }
    }
    private Hashtable<String, String> copyMetaData(Document doc){
        Hashtable<String,String> copy = new Hashtable<>((Hashtable<String, String>) doc.getMetadata());
        return copy;
    }





}