//                                                                                          בס"ד
package edu.yu.cs.com1320.project.stage6.impl;

import edu.yu.cs.com1320.project.MinHeap;
//import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.BTreeImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage6.DocumentStore;
import edu.yu.cs.com1320.project.undo.CommandSet;
import edu.yu.cs.com1320.project.undo.GenericCommand;
import edu.yu.cs.com1320.project.undo.Undoable;
import edu.yu.cs.com1320.project.stage6.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.Consumer;

public class DocumentStoreImpl implements DocumentStore {
    private BTreeImpl<URI,Document> tree = new BTreeImpl<>();

    private StackImpl<Undoable> stack = new StackImpl();
    private TrieImpl<DocURI> Trie = new TrieImpl<>();
    private MinHeap<DocURI> docHeap = new MinHeapImpl<>();

    private HashMap<HashMap<String,String>,HashSet<URI>> MetaData = new HashMap<>();

    private HashMap<URI,DocURI> docURIset = new HashMap();
    private int MaxDocumentCount = 0;
    private int DocCount = 0;
    private int MaxDocumentBytes = 0;
    private int DocBytes = 0;


    public DocumentStoreImpl(){
      this.tree.setPersistenceManager(new DocumentPersistenceManager());
    }
    public DocumentStoreImpl(File baseDir){
        this.tree.setPersistenceManager(new DocumentPersistenceManager(baseDir));
    }


    //this class is meant for the MinHeap, Holds the Documents LastTime used and the Document URI
     private class DocURI implements Comparable<DocURI> {
        private long nanoSeconds;

        private URI uri;
        private HashMap<String,String> metaData;

        private DocURI(URI uri){
            this.uri = uri;
            this.nanoSeconds = 0;
        }

        @Override
        public int hashCode() {
            int result = this.uri.hashCode();
            result = 31 * result + Objects.hash(nanoSeconds);
            return Math.abs(result);
        }


        @Override
         public int compareTo(DocURI o) {
            if (o == null || this.nanoSeconds > o.nanoSeconds){
                return 1;
            }
            //if the document being compared to this document was last used longer.
            if (this.nanoSeconds < o.nanoSeconds){
                return -1;
            }
            //last used is the same
            return 0;
        }
        //two DocURI are considered == if URI is the same (Helpful for Reheapify)
        @Override
        public boolean equals(Object o){
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DocURI document = (DocURI) o;
            return Objects.equals(uri, document.uri);
        }

    }



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
    public String setMetadata(URI uri, String key, String value) throws IOException{
        if (uri == null || uri.toString().isEmpty() || this.tree.get(uri) == null || key == null || key.isEmpty()){
            throw new IllegalArgumentException();
        }

        //reHeapify the doc
        //IF doc was already in storage, it will just be a normal reHeap
        //IF doc was on disk it will do proper requirments inorder to bring it back
        reHeapifyDS(uri);
        Set<URI> uris = reHeapifyDS(uri);

        //get the doc
        Document doc = this.tree.get(uri);




        //doc's old meta data hashmap
        HashMap<String,String> oldHM = (doc.getMetadata());

        //get the previous value of the metaData, null if there was none
        String old = doc.getMetadataValue(key);

        //create consumer
        consumerUndoMetaData(uri,key,old,uris,oldHM);


        //delete old meta data where it was b4
        if (oldHM != null && this.MetaData.get(oldHM) != null) {
            this.MetaData.get(oldHM).remove(uri);
            //if its old meta data is empty of any uris, deleted it
            if (this.MetaData.get(oldHM).isEmpty()){
                this.MetaData.remove(oldHM);
            }
        }


        //set the new MetaData
        doc.setMetadataValue(key, value);

        //update meta date hashmap
        updateMetaDataMap(uri,doc.getMetadata());

        //return old MetaData value for given key, null if none
        return old;
    }
    private void consumerUndoMetaData(URI uri, String key, String oldValue, Set<URI> deletedURIs,HashMap<String,String> oldHM) {

        //create the consumer
        Consumer<URI> UndoMetaData = (i) -> {

            //delete the metadata u want to undo, put uri back in set with its old meta data
            updateMetaDataMap(uri,oldHM);


            //set the meta data
            //If there WAS NOT a previous value for the given key then @param oldValue will == null
            //IF there WAS a previous value for the given key then @param oldValue will == that old value
            this.tree.get(i).setMetadataValue(key,oldValue);

            //re Heapify the uri
            reHeapifyDS(i);
            for (URI u: deletedURIs){
                addDocToHeap(u);
            }
        };

        GenericCommand<URI> MetaDataCommand = new GenericCommand<>(uri,UndoMetaData);
        this.stack.push(MetaDataCommand);
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
    public String getMetadata(URI uri, String key) throws IOException {
        if (uri == null || uri.toString().isEmpty() || this.tree.get(uri) == null || key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
        //IF doc was already in storage, will do a normal reHeap
        //IF doc was on disk, will call that doc back from disk and push any docs to disk (if needed), Doc will then also be added to the heap`
        reHeapifyDS(uri);
        return this.tree.get(uri).getMetadataValue(key);
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
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException  {
        if (uri == null || uri.toString().isEmpty() || format == null) {
            throw new IllegalArgumentException();
        }
        if (input == null) { //CALL TO DELETE*****
            //doc exists, delete it and return the hashcode
            if (this.tree.get(uri) != null)  {
                int old = this.tree.get(uri).hashCode();
                delete(uri);
                return old;
            }
            //doc didn't exist
            return 0;
        }

        // input IS NOT NULL so we have to add the Doc
        byte[] inputS;

        try {
            input.reset();
            inputS = input.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //TXT type doc
            if (format == DocumentFormat.TXT) { // if formant == TXT
                return TXTput(uri, new String(inputS));
            }
         //BINARY type doc
        //if format == BINARY
        return BINARYput(uri, inputS);
    }

    private int BINARYput(URI uri, byte[] bytes) {
        //create new doc that is BINARY type
        Document doc = new DocumentImpl(uri, bytes);
        if (this.MaxDocumentBytes > 1 && getDocumentBytes(doc) > this.MaxDocumentBytes){
            throw new IllegalArgumentException();
        }

        int zeroOrHashcode = 0;

        //if doc with same uri is already in bTree storage
        //get its hashcode so it can be returned
        Document oldDoc = this.tree.get(uri);
        if  (oldDoc != null){
            zeroOrHashcode = oldDoc.hashCode();
            //if prevouis doc was txt, delete the words
            deleteWords(oldDoc);
        }

        //put will either update the doc if it's uri was already in storage, OR will add it brand new
        this.tree.put(uri,doc);
        //will either reheapify the existing doc or realize its not in storage so its a new doc, and then call addDocToHeap
        reHeapifyDS(uri);


        //add the MetaData to hashmap list last param null b/c
        updateMetaDataMap(uri,doc.getMetadata());


        consumerUndoPut(doc,oldDoc,uri);

        //return zero if the doc is new, OR the old docs hashcode
        return zeroOrHashcode;
    }


    private int TXTput(URI uri, String words) {

        //create new document that is TXT type
        Document doc = new DocumentImpl(uri,words,null);

        //IF there is a Max amount of bytes AND this SINGLE DOC ITSELF IS OVER THE LIMIT throw an exception
        if (this.MaxDocumentBytes > 1 && getDocumentBytes(doc) > this.MaxDocumentBytes){
            throw new IllegalArgumentException();
        }

        int zeroOrHashcode = 0;

        //if doc with same uri is already in bTree storage
        //get its hashcode so it can be returned
        Document oldDoc = this.tree.get(uri);
        if  (oldDoc != null){
            zeroOrHashcode = oldDoc.hashCode();
            //if prevouis doc was txt delte its words
            deleteWords(oldDoc);
        }

        //put will either update the doc if it's uri was already in storage, OR will add it brand new
        this.tree.put(uri,doc);

        //add the words of the txt doc to the trie with a DocURI that matches
        putWordsInTrie(doc);

        //will either reheapify the existing doc or realize its not in storage so its a new doc, and then call addDocToHeap
        reHeapifyDS(uri);

        //add the MetaData to hashmap list last param null b/c
        updateMetaDataMap(uri,doc.getMetadata());

        consumerUndoPut(doc,oldDoc,uri);


        return zeroOrHashcode;
    }



    //@param if doc is null - means this was a fresh put, document's hashmap will be empty,
    //if it isn't null then param @HashMap will be docs old HashMap, and we get new hashmap by calling it from doc param
    //we need the old hashmap because we need to remove the uri from prev hashmap
    private void updateMetaDataMap(URI uri, HashMap<String,String> Hashmap){


//        //doc's hashmap was set so need to move it in proper spot
//        if (oldHM != null) {
//            this.MetaData.get(oldHM).remove(uri);
//        }



        //if the hashmap doesn't exists yet
        if (!this.MetaData.containsKey(Hashmap)){
            HashMap<String,String> HMP = new HashMap<String,String>();
            for (Map.Entry<String,String> entry : Hashmap.entrySet()){
                HMP.put(entry.getKey(),entry.getValue());
            }

            HashSet<URI> hURI = new HashSet<>();
            hURI.add(uri);
            this.MetaData.put(HMP,hURI);

        } else {
            //the hashmap is in there, add uri to its list
            this.MetaData.get(Hashmap).add(uri);
        }

    }




    private void putWordsInTrie(Document doc){
        if (doc != null && doc.getDocumentTxt() != null ) {
            Set<String> wordsInDoc = doc.getWords();
            //add all words from the document into the trie
            for (String w : wordsInDoc) {
                if (!this.Trie.get(w.replaceAll("[^a-zA-Z0-9]", "")).contains(new DocURI(doc.getKey()))) {
                    this.Trie.put(w.replaceAll("[^a-zA-Z0-9]", ""), new DocURI(doc.getKey()));
                }
            }
        }
    }

    private void consumerUndoDelete(Document doc,URI uri){
        Consumer<URI> undoDelete = (i) -> {
            if (this.MaxDocumentBytes > 1 && getDocumentBytes(doc) > this.MaxDocumentBytes){
                throw new IllegalArgumentException();
            }

            //put the Document back into storage
            this.tree.put(i,doc);
            //add back to heap
            addDocToHeap(i);
            //put the words of the document back into the trie
            putWordsInTrie(doc);

            updateMetaDataMap(doc.getKey(),doc.getMetadata());

        };
        this.stack.push(new GenericCommand<>(uri,undoDelete));
    }


   //@param doc == the doc that is being undone
    private void consumerUndoPut (Document doc, Document oldDoc, URI uri){

        Consumer<URI> undoPut = (i) -> {

            //delete the Doc off the heap
            //this method also nulls out the document, however we already got the old document (if there is one) in the oldDoc Variable...
            //and we call this.tree.put(uri,oldDoc) just incase the oldDoc is not null and is in fact the old Document
            deleteSpecificDoc(uri);

            //delete the words of the doc that is being undone doc
            deleteWords(doc);

            //IF URI DID exist before, it will replace it with that document, oldDoc will == that doc
            //IF URI DIDN"T exist before, it will replace with null, oldDoc will == null
            this.tree.put(i,oldDoc);

            putWordsInTrie(oldDoc);

            //if there was a previous doc then --- will either reheapify the existing doc or realize its not in storage so its a new doc, and then call addDocToHeap
            if(oldDoc != null){
                reHeapifyDS(uri);
            }





//            //the reHeapify method will add the doc to the heap if needed but we want it to be added with the same nano
//            this.docHeap.reHeapify();
        };
        this.stack.push(new GenericCommand<>(uri, undoPut));
    }


    /**
     * @param url the unique identifier of the document to get
     * @return the given document
     */
    @Override
    public Document get(URI url) throws IOException {
        if (this.tree.get(url) == null){
            return null;
        }
        reHeapifyDS(url);
        return this.tree.get(url);
    }
    /**
     * @param url the unique identifier of the document to delete
     * @return true if the document is deleted, false if no document exists with that URI
     */
    @Override
    public boolean delete(URI url)  {
        Document doc = this.tree.get(url);

        //if there is no such doc
        if (doc == null){
            return false;
        }

        //create consumer by calling get method to return the Document that is represented by that uri
        consumerUndoDelete(doc,url);

        //remove Doc from heap and storage
        deleteSpecificDoc(url);

        //return true (succesful delete)
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
            GenericCommand<URI> peek = (GenericCommand) this.stack.peek();
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
                GenericCommand<URI> genCom = (GenericCommand<URI>) command;
                //if the uri is found
                if (genCom.getTarget().equals(url)) {
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
                    CommandSet<URI> commandSet = (CommandSet<URI>) command;
                    // uri was found in CommandSet
                    if (URIundoCommandSet(url, commandSet, tempStack)){
                        return;
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    private boolean URIundoCommandSet(URI url, CommandSet<URI> commandSet, StackImpl<Undoable> tempStack) {
        if (commandSet.containsTarget(url)){
            commandSet.undo(url);
            if(commandSet.size() == 0){
                this.stack.pop();
            }
            while (tempStack.size() != 0){
                //push tempStack back to main STack
            this.stack.push(tempStack.pop());
            }
            return true;
        //uri was not found. Push to tempStack then for back to top of while loop
        } else{
            tempStack.push(this.stack.pop());
            }
        return false;
    }

    private static class DocSort implements Comparator<Document> {
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
    public List<Document> search(String keyword) throws IOException{

        //.replaceAll("[^a-zA-Z0-9]", "")
        Set<DocURI> set = this.Trie.get(keyword);
        List<Document> docsToReturn = new ArrayList<>();

        for (DocURI doc: set){

            //since the doc contains the word, we reHeapify and add back to storage
            reHeapifyDS(doc.uri);

            //add the doc to the list of Docs to be retunred
            docsToReturn.add(this.tree.get(doc.uri));
        }

        //sort the docsToReturn list from greatest to least (greatest being the word shows up the most)
        DocSort dSort = new DocSort();
        dSort.word = keyword;
        docsToReturn.sort(dSort);

        return docsToReturn;
    }
    /**
     * Retrieve all documents that contain text which starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    @Override
    public List<Document> searchByPrefix(String keywordPrefix) throws IOException {

        List<DocURI> list = this.Trie.getAllWithPrefixSorted(keywordPrefix,null);
        List<Document> docsToReturn = new ArrayList<>();

        for (DocURI doc: list){

            //since the doc contains the word, we reHeapify and add back to storage
            reHeapifyDS(doc.uri);

            //add the doc to the list of Docs to be retunred
            docsToReturn.add(this.tree.get(doc.uri));
        }
       sortPreFix dSort = new sortPreFix();
        dSort.word = keywordPrefix;
        docsToReturn.sort(dSort);

        return  docsToReturn;
    }
    private static class sortPreFix implements Comparator<Document> {
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
        Set<DocURI> urisToDelete = this.Trie.deleteAll(keyword);

        //docs to put into undo
        Set<Document> deletedDocs = new HashSet<>();

        // go through the set of delted documents and add their key (a.k.a their URI) to urisToDelete and then return the uriSet
        for (DocURI docURI: urisToDelete){

            uriSet.add(docURI.uri);

            //add doc to set so we can udno
            deletedDocs.add(this.tree.get(docURI.uri));

            //delete the words from the trie
            deleteWords(this.tree.get(docURI.uri));

            //remove Doc from memory
            deleteSpecificDoc(docURI.uri);
        }
        UndoCommandSetDelete(deletedDocs);
        return uriSet;
    }
    private void UndoCommandSetDelete(Set<Document> documents){
        CommandSet<URI> commandSet = new CommandSet<>();
        for (Document doc : documents ){
           //create consumer to put doc back
            Consumer<URI> undoDelete = (i) -> {
                //if this doc is too big
                if (this.MaxDocumentBytes > 1 && getDocumentBytes(doc) > this.MaxDocumentBytes){
                    throw new IllegalArgumentException();
                }



                //add back to storage
                this.tree.put(doc.getKey(),doc);

                //add back to heap and tree
                addDocToHeap(doc.getKey());

                //when consumer is executed it will add words into the Trie
               putWordsInTrie(doc);

                updateMetaDataMap(doc.getKey(),doc.getMetadata());

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

        //URIs to be returned
        Set<URI> uriSet = new HashSet<>();

        //get all the DocURI that have this prefix
        Set<DocURI> urisToDelete = this.Trie.deleteAllWithPrefix(keywordPrefix);

        //docs to put into undo
        Set<Document> deletedDocs = new HashSet<>();

        // go through the set of delted documents and add their key (a.k.a their URI) to urisToDelete and then return the uriSet
        for (DocURI docURI: urisToDelete){

            uriSet.add(docURI.uri);

            //add doc to set so we can udno
            deletedDocs.add(this.tree.get(docURI.uri));

            //delte the words from trie
            deleteWords(this.tree.get(docURI.uri));

            //remove Doc from memory
            deleteSpecificDoc(docURI.uri);
        }

        //create undo command
        UndoCommandSetDelete(deletedDocs);
        return uriSet;
    }

    private void deleteWords(Document doc){
        //if doc is txt type, remove this DocURI from the words in the trie that it;s Document contains
        if (doc != null && doc.getDocumentTxt()!= null) {
            for (String s : doc.getWords()) {
                this.Trie.delete(s.replaceAll("[^a-zA-Z0-9]",""), this.docURIset.get(doc.getKey()));
            }
        }
    }
    /**
     * @param keysValues metadata key-value pairs to search for
     * @return a List of all documents whose metadata contains ALL OF the given values for the given keys. If no documents contain all the given key-value pairs, return an empty list.
     */
    @Override
    public List<Document> searchByMetadata(Map<String, String> keysValues) throws IOException{

        //list of docs to return
        List<Document> MetaDataList = new ArrayList<>();

        //if there are no Docs with that metadata return the list empty
        if (!this.MetaData.containsKey(keysValues)){
            return MetaDataList;
        }

        //get the set of URIs matching the Hashmap from this.MetaData
        //go through the set of URI's and get it's uris and add the URI's doc to MetaDataList
        for (URI uri: this.MetaData.get(keysValues)){
            MetaDataList.add(this.tree.get(uri));

            //since the doc contains the word, we reHeapify and add back to storage
            reHeapifyDS(uri);
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
    public List<Document> searchByKeywordAndMetadata(String keyword, Map<String, String> keysValues) throws IOException{

        //List of Docs to return
        List<Document> DocsWithKeywordAndMetadata = new ArrayList<>();

        //a set of DocURIs contains the keyword
        Set<DocURI> DocURIsWithKeyword  = this.Trie.get(keyword);

        //go throught all the URIs that contain the key word and see if that URI is also in the metaData hashmap for the @param metaData
        if ((this.MetaData.containsKey(keysValues))) {

            for (DocURI DocURI : DocURIsWithKeyword) {

                //if its in the MetaData hashMap
                if (this.MetaData.get(keysValues).contains(DocURI.uri)) {

                    //add it toe the list of docs to return
                    DocsWithKeywordAndMetadata.add(this.tree.get(DocURI.uri));

                    //since the doc contains the word and HashMap, we reHeapify and add back to storage
                    reHeapifyDS(DocURI.uri);
                }
            }
        }
        DocSort dSort = new DocSort();
        dSort.word = keyword;
        DocsWithKeywordAndMetadata.sort(dSort);
        return DocsWithKeywordAndMetadata;
    }

    /**
     * Retrieve all documents that contain text which starts with the given prefix AND which has the given key-value pairs in its metadata
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    @Override
    public List<Document> searchByPrefixAndMetadata(String keywordPrefix, Map<String, String> keysValues) throws IOException{

        //List of Docs to return
        List<Document> DocsWithKeywordAndMetadata = new ArrayList<>();

        //a set of DocURIs contains the keyword Prefix
        List<DocURI> DocURIsWithKeyword  = this.Trie.getAllWithPrefixSorted(keywordPrefix,null);

        //go throught all the URIs that contain the key word and see if that URI is also in the metaData hashmap for the @param metaData
        if ((this.MetaData.containsKey(keysValues))) {

            for (DocURI DocURI : DocURIsWithKeyword) {

                //if its in the MetaData hashMap
                if (this.MetaData.get(keysValues).contains(DocURI.uri)) {

                    //add it toe the list of docs to return
                    DocsWithKeywordAndMetadata.add(this.tree.get(DocURI.uri));

                    //since the doc contains the word and HashMap, we reHeapify and add back to storage
                    reHeapifyDS(DocURI.uri);
                }
            }
        }

        //sort the list
        sortPreFix dSort = new sortPreFix();
        dSort.word = keywordPrefix;
        DocsWithKeywordAndMetadata.sort(dSort);

        return DocsWithKeywordAndMetadata;
    }
    /**
     * Completely remove any trace of any document which has the given key-value pairs in its metadata
     * Search is CASE SENSITIVE.
     * @return a Set of URIs of the documents that were deleted.
     */
    @Override
    public Set<URI> deleteAllWithMetadata(Map<String, String> keysValues) throws IOException {

        //URIs to delete and return
        Set<URI> URIsWithMD = this.MetaData.get(keysValues);
        //if there is no such meta data, return an empty set
        if (URIsWithMD == null){
            return new HashSet<URI>();
        }

        Set<URI> URIsToReturn = new HashSet<>();

        //copy the uris to delete
        for (URI uri : URIsWithMD){
            String a = uri.toString();
            try {
                URIsToReturn.add(new URI(a));
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        Set<Document> deletedDocs = new HashSet<>();

        //go through each URI and delete it
        for (URI uri : URIsToReturn){


            //add doc to set so we can udno
            deletedDocs.add(this.tree.get(uri));

            //delte the words from trie
            deleteWords(this.tree.get(uri));

            //remove Doc from memory
            deleteSpecificDoc(uri);
        }

        //create undo command
        UndoCommandSetDelete(deletedDocs);

        return URIsToReturn;
    }
    /**
     * Completely remove any trace of any document which contains the given keyword AND which has the given key-value pairs in its metadata
     * Search is CASE SENSITIVE.
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */
    @Override
    public Set<URI> deleteAllWithKeywordAndMetadata(String keyword, Map<String, String> keysValues) throws IOException{

        //Set of URIs to return
        Set<URI> deletedURIs = new HashSet<>();

        //
        Set<DocURI> URIsWitKeyword = this.Trie.get(keyword);

        Set<Document> deletedDocs = new HashSet<>();

        //if there are URI mapped to this MetaData map (i.e. this MetaData exists)
        if (this.MetaData.containsKey(keysValues)){

            //go through all URIs in URIsWithKeyword and see if it matches the metadata
            for (DocURI DocURI : URIsWitKeyword) {

                //if the URI matches the metaData...
                if (this.MetaData.get(keysValues).contains(DocURI.uri)){

                    //add the uri
                    deletedURIs.add(DocURI.uri);

                    //add to Document set for undo
                    deletedDocs.add(this.tree.get(DocURI.uri));

                    //delete words of doc from trie
                    deleteWords(this.tree.get(DocURI.uri));

                    //delete from memory
                    deleteSpecificDoc(DocURI.uri);
                }
            }
        }
        //create undo command
        UndoCommandSetDelete(deletedDocs);

        return deletedURIs;
    }
    /**
     * Completely remove any trace of any document which contains a word that has the given prefix AND which has the given key-value pairs in its metadata
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    @Override
    public Set<URI> deleteAllWithPrefixAndMetadata(String keywordPrefix, Map<String, String> keysValues) throws IOException {

        //Set of URIs to return
        Set<URI> deletedURIs = new HashSet<>();

        //DocURIs with keyword Prefix
        List<DocURI> DocURIsWithPrefix = this.Trie.getAllWithPrefixSorted(keywordPrefix,null);

        //set of delted document for undo
        Set<Document> deletedDocument = new HashSet<>();

        //if there are URIs with this MetaData map
        if (this.MetaData.containsKey(keysValues)){

            //go through all the DocURIs that have the prefix and see if it also has the MetaDta
            for (DocURI DocURI : DocURIsWithPrefix){

                //if it also has the metaData, add it to the deletedURIs set
                if (this.MetaData.get(keysValues).contains(DocURI.uri)){
                    //add the uri
                    deletedURIs.add(DocURI.uri);

                    //add to document set for undo
                    deletedDocument.add(this.tree.get(DocURI.uri));

                    //delete the words of the doc
                    deleteWords(this.tree.get(DocURI.uri));

                    //delete from memory
                    deleteSpecificDoc(DocURI.uri);
                }
            }
        }
        //create undo command
        UndoCommandSetDelete(deletedDocument);

        //return the deleted URIs
        return deletedURIs;
    }
    /**
     * set maximum number of documents that may be stored
     * @param limit
     * @throws IllegalArgumentException if limit < 1
     */
    @Override
    public void setMaxDocumentCount(int limit) {
        if (limit < 1){
            throw new IllegalArgumentException();
        }

        //if this.MaxDocumentCount > this.DocCount it means limit for Docs is reached, we have to remove docs until its equal
        while (this.DocCount > limit){
            limitRemove();
        }

        //if there was no limit b4 or docs are removed (if needed)...
        //set the doc limit to @param limit
        this.MaxDocumentCount = limit;
    }
    /**
     * set maximum number of bytes of memory that may be used by all the documents in memory combined
     * @param limit
     * @throws IllegalArgumentException if limit < 1
     */
    @Override
    public void setMaxDocumentBytes(int limit) {
        if (limit < 1){
            throw new IllegalArgumentException();
        }

        //if the amount of bytes worth of docs being held is greater than the limit, remove docs until tis les than the limit
        if (this.DocBytes > limit) {

            //while over the limit remove
            while (this.DocBytes  > limit) {
                limitRemove();
            }
        }

        //if there was no limit b4 or after docs are removed (if needed)
        //set the Bytes limit to equal @param limit
        this.MaxDocumentBytes = limit;
    }

    //this method returns the length of byte array
    private int getDocumentBytes(Document doc){
        //document is TXT type
        if (doc.getDocumentTxt() != null){
            return doc.getDocumentTxt().getBytes().length;
        }
        //document is Binary
        return doc.getDocumentBinaryData().length;
    }

    private Set<URI> addDocToHeap(URI uri)  {

        //set of URI that where delted as a result of adding doc
        //***USED for undos ***
        Set<URI> deletedURIs= new HashSet<>();
        //get the doc
        Document doc = this.tree.get(uri);
        //get the byte size of doc
        int currentDocBytes = getDocumentBytes(doc);

        //if there is a Max Count set && count is reached
        if (this.MaxDocumentCount > 0 && this.DocCount >= this.MaxDocumentCount) {

            //remove the doc a top of heap
            //remove the bytes
            //lower doc count
            //add it to the list of URIs that got removed
            deletedURIs.add(limitRemove());
        }
        //remove its bytes from max bytes ONLY IF there is a limit and it has been reached
        if (this.MaxDocumentBytes > 1  && this.DocBytes + currentDocBytes > this.MaxDocumentBytes) {

            //if there is a limit
            // keep on removing docs till there is room
            while (this.DocBytes + currentDocBytes > this.MaxDocumentBytes) {

                //remove the doc a top of heap
                //remove the bytes
                //lower doc count
                deletedURIs.add(limitRemove());
            }
        }

        //when DocBytes and DocCount are all good OR there were no restrictions, insert the doc
        addToHeap(uri,doc, currentDocBytes);
        return deletedURIs;
    }

        //This method brings a doc back to heap and off the disk
    private void addToHeap(URI uri, Document doc, int docBytes) {

        //bring the doc back from the disk

        //set nano time
        doc.setLastUseTime(System.nanoTime());

        //create DocURI instance with nanotime
        DocURI docURI = new DocURI(uri);
        docURI.nanoSeconds = System.nanoTime();

        //add it to the heap
        this.docHeap.insert(docURI);

        //add to set that holds docURI
        this.docURIset.put(uri,docURI);

        //add to Doc count + Doc bytes
        this.DocBytes += docBytes;
        this.DocCount ++;
    }

    //this method updates the Doc's Last use time and then updates the Heap
    private Set<URI> reHeapifyDS(URI uri) {

//        try {
//
//            DocURI doc = this.docURIset.get(uri);
//            if (doc!= null) {
//                doc.nanoSeconds = System.nanoTime();
//            }
//            this.docHeap.reHeapify(doc);
//            this.tree.get(uri).setLastUseTime(System.nanoTime());
//        }
//        //elemnt was not in heap, comeing from disk
//        catch (NoSuchElementException e){
//
//            //bring back from disk and add to heap
//                return addDocToHeap(uri);
//        }
//     return new HashSet<URI>();

   //if doc is in the heap
   if (inHeap(uri)){
       DocURI doc = this.docURIset.get(uri);
        if (doc!= null) {
            doc.nanoSeconds = System.nanoTime();
        }
        this.docHeap.reHeapify(doc);
        this.tree.get(uri).setLastUseTime(System.nanoTime());
   }

   //elemnt was not in heap, comeing from disk
   else {
       return addDocToHeap(uri);
   }

   return new HashSet<URI>();
    }

    private boolean inHeap(URI uri){
        try {
            this.docHeap.reHeapify(new DocURI(uri));
            //in heap
            return true;
        }
        catch (NoSuchElementException e){
            //not in heap
            return false;
        }
    }


    //method should be called when a document has to be removed b/c of limit constraints
    private URI limitRemove() {
        //remove top doc from heap and get its URI
        URI removedDocURI = this.docHeap.remove().uri;



        //get the doc's bytes and lower bytes count storage
        this.DocBytes -= getDocumentBytes(this.tree.get(removedDocURI));

        //lower doc count
        this.DocCount --;

        //send the URI's doc in the bTree to the disk
        try {
            this.tree.moveToDisk(removedDocURI);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //return the uri that was removed
        return removedDocURI;
    }


    //deletes a specific document from memory
    private void deleteSpecificDoc(URI uri)  {
        Document doc = this.tree.get(uri);

        //doc is not in heap, either doesnt exists or is on disk
        //doc true, doc is in heap, have to remove it
        if (inHeap(uri)) {

            //remove bytes
            this.DocBytes -= getDocumentBytes(doc);
            //lower doc count
            this.DocCount--;

            //set the last time used of the doc to the highest possible value
            DocURI docuri = this.docURIset.get(uri);
            docuri.nanoSeconds = Long.MIN_VALUE;
            //reHeapify so this doc is pushed to the top of the heap
            this.docHeap.reHeapify(docuri);
            //the doc should now be on top, so remove it
            this.docHeap.remove();


        }

        //delete the doc's uri from hashmap map
        if (this.MetaData.get(doc.getMetadata()) != null) {
            this.MetaData.get(doc.getMetadata()).remove(uri);
        }

        //delete the doc from the bTree storage
       this.tree.put(uri,null);
    }

//the end
}

