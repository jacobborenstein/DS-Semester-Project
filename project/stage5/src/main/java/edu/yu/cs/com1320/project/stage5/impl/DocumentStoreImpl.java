//                                                                                          בס"ד
package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.MinHeap;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.undo.CommandSet;
import edu.yu.cs.com1320.project.undo.GenericCommand;
import edu.yu.cs.com1320.project.undo.Undoable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.function.Consumer;

public class DocumentStoreImpl implements DocumentStore {
    public HashTableImpl<URI, Document> storage = new HashTableImpl<>();
    public StackImpl<Undoable> stack = new StackImpl();
    private TrieImpl<Document> Trie = new TrieImpl<>();
    public MinHeap<Document> docHeap = new MinHeapImpl<>();
    public int MaxDocumentCount = 0;
    public int DocCount = 0;
    private int MaxDocumentBytes = 0;
    private int DocBytes = 0;







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
            throw new IllegalArgumentException();
        }
        Document doc = this.storage.get(uri);
        String old = doc.getMetadataValue(key);
        consumerUndoMetaData(uri,key,value);
        doc.setMetadataValue(key, value);
        reHeapifyDS(doc);
        return old;
    }
    private void consumerUndoMetaData(URI uri, String key, String value){
        //if there was a metadata previously with the same key, return it to that value
        if (this.storage.get(uri).getMetadataValue(key) != null) {
            String undoValue =  this.storage.get(uri).getMetadataValue(key);
          Consumer<URI> UndoMetaData = (i) -> {
              this.storage.get(i).setMetadataValue(key, undoValue);
              reHeapifyDS(this.storage.get(i));
          };
          GenericCommand<URI> MetaDataCommand = new GenericCommand(uri,UndoMetaData);
          this.stack.push(MetaDataCommand);
        } else { //no previous meta data with the given key
            //***CUD BE WRONG B/C CANT MAKE KEY NULL (THROWS AN E) BUT
            // SUPPOSE TO DELETE THE METADATA SO FOR NOW JUST MAKE VALUE NUL
            Consumer<URI> UndoMetaData = (i) -> {
                this.storage.get(i).setMetadataValue(key, null);
                reHeapifyDS(this.storage.get(i));
            };
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
        reHeapifyDS(this.storage.get(uri));
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
        }
        if (input == null) { //CALL TO DELETE*****
            //doc exists, delete it and return the hashcode
            if (this.storage.containsKey(uri) || this.storage.get(uri) != null)  {
                int old = this.storage.get(uri).hashCode();
                delete(uri);
                return old;
            }
            //doc didn't exist
            return 0;
        }

        // input IS NOT NULL so we have to add the Doc
        input.reset();
        byte[] inputS = input.readAllBytes();
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
//            return 0;

        }
        //if Doc is not in hashTable, add it and return 0
        if (!this.storage.containsKey(uri)) {
            //add new doc to Document storage
            this.storage.put(uri, doc);
            //add to heap
            addDocToHeap(doc);
            // creates instance of GenericCommand that will be added to the stack (for undo)
            consumerUndoPut(doc,uri,0);

            return 0;
        }
        //IF DOC ALREADY EXISTS...
        //creates instance of command and adds it to stack (for undo)
        consumerUndoPut(this.storage.get(uri),uri,1);
        //updates existing doc and returns old docs hashcode
        return putHelp_ReturnHashcode(uri, doc);
    }

    private int TXTput(URI uri, String words) {
        //create new document that is TXT type
        Document doc = new DocumentImpl(uri,words);
        //IF there is a Max amount of bytes AND this SINGLE DOC ITSELF IS OVER THE LIMIT throw an exception
        if (this.MaxDocumentBytes > 1 && getDocumentBytes(doc) > this.MaxDocumentBytes){
            throw new IllegalArgumentException();
//            return 0;
        }

        //if Doc is NOT in hashTable, add it and return 0
        if (!this.storage.containsKey(uri)) {
            //add new doc to Document storage
            this.storage.put(uri,doc);
            //add words of the doc to the trie
            putWordsInTrie(doc);
            //add to heap
            addDocToHeap(doc);
            // creates instance of GenericCommand that will be added to the stack (for undo) 0 == didn't previously exist
            consumerUndoPut(doc,uri,0);

            //return 0 b/c method requires to return 0 if document did not exist
            return 0;
        }
        //IF DOC ALREADY EXISTS...
        //creates instance of command and adds it to stack (for undo) 1 == previously existed
        consumerUndoPut(this.storage.get(uri),uri,1);
        //updates existing doc and returns old docs hashcode
        return putHelp_ReturnHashcode(uri, doc);
    }


    private int putHelp_ReturnHashcode(URI uri, Document doc){
    //THIS METHOD save lines in putHelper method. returns hashcode of old document that is being replace
        Document old = this.storage.get(uri);
        //get hashcode of the doc
        int hashcode = old.hashCode();
        //delete old doc from heap, add new one
        removeDocFromHeap(old);
        addDocToHeap(doc);
        //updates the doc
        this.storage.put(uri, doc);
        //return hashcode
        return hashcode;
    }

    private void putWordsInTrie(Document doc){
       if (doc.getDocumentBinaryData()!= null){
           return;
       }
        Set<String> wordsInDoc = doc.getWords();
       //add all words from the document into the trie
        for (String w : wordsInDoc){
            if (!this.Trie.get(w.replaceAll("[^a-zA-Z0-9]","")).contains(doc)) {
                this.Trie.put(w.replaceAll("[^a-zA-Z0-9]", ""), doc);
            }
       }
    }

    private void consumerUndoDelete(Document doc,URI uri){
        Consumer<URI> undoDelete = (i) -> {
            if (this.MaxDocumentBytes > 1 && getDocumentBytes(doc) > this.MaxDocumentBytes){
                throw new IllegalArgumentException();
            }
            //add back to heap
            addDocToHeap(doc);
            //put the Document back into storage
            this.storage.put(i,doc);
            //put the words of the document back into the trie
            putWordsInTrie(doc);

        };
        this.stack.push(new GenericCommand<>(uri,undoDelete));
    }

    private void consumerUndoPut (Document doc, URI uri, Integer exists){
        //doc NEVER EXISTED, when undoing it delete it completely
        if (exists == 0) {
            Consumer<URI> undoPut = (i) -> {
                //remove from heap
                removeDocFromHeap(this.storage.get(i));
                //put in hashTable that holds document the URI with document param null will delete the doc
                this.storage.put(i, null);
                //delete all traces of the doc (any word it is connected to)
                deleteAllTraces(doc);

            };
            this.stack.push(new GenericCommand<>(uri, undoPut));
        }
        //doc EXISTED BEFORE, so when undoing it we have to make it the old doc
        if (exists == 1) {
            Document old = this.storage.get(uri);
            Consumer<URI> undoPut = (i) -> {
                //delete all traces of the doc that took this docs spot (any word it is connected to)
                deleteAllTraces(old);
                //remove from heap
                removeDocFromHeap(old);
                //put old doc back
                addDocToHeap(doc);
                // then put in hashTable old document
                this.storage.put(i, doc);
                //add words from old document back in
                putWordsInTrie(doc);
            };
            this.stack.push(new GenericCommand<>(uri, undoPut));
        }
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
        reHeapifyDS(this.storage.get(url));
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
        Document doc = this.storage.get(url);
        //create consumer by calling get method to return the Document that is represented by that uri
        consumerUndoDelete(doc,url);
        //remove Doc from heap
        removeDocFromHeap(doc);
        //delte the document by calling put with a null value
        this.storage.put(url,null);
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
            GenericCommand peek = (GenericCommand) this.stack.peek();
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
        List<Document> list = this.Trie.getSorted(keyword,cs);
        for (Document doc: list){
            reHeapifyDS(doc);
        }
        return list;
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
        List<Document> list = this.Trie.getAllWithPrefixSorted(keywordPrefix,csPreFix);
        for (Document doc: list){
            reHeapifyDS(doc);
        }
        return list;
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
            //remove Doc from heap
            removeDocFromHeap(doc);
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
                //if this doc is too big
                if (this.MaxDocumentBytes > 1 && getDocumentBytes(doc) > this.MaxDocumentBytes){
                    throw new IllegalArgumentException();
                }
                //when consumer is executed it will add words into the Trie
               putWordsInTrie(doc);
               //add back to heap
                addDocToHeap(doc);
                //when consumer is executed it will add doc back into hashTable
                this.storage.put(doc.getKey(),doc);
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
            //remove Doc from heap
           removeDocFromHeap(doc);
            //deletes all traces of the document, if the document has any other words, the document gets removed from those nodes in the trie
            deleteAllTraces(doc);
        }
        UndoCommandSetDelete(DocSet);
        return uriSet;
    }
    // this method deletes all Traces of the @param Document. it deletes the given document from any words that it is under in the Trie
    private void deleteAllTraces(Document doc){
        //if doc is txt type
        if (doc.getDocumentTxt()!= null) {
            for (String s : doc.getWords()) {
                this.Trie.delete(s.replaceAll("[^a-zA-Z0-9]",""), doc);
            }
            //delte the document from the hashtable ****prob have to create new commands/undos that where added to stage 4
            this.storage.put(doc.getKey(), null);

        } else {
            this.storage.put(doc.getKey(), null);
        }
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
            if (this.storage.get(uri) != null){
                Document doc = this.storage.get(uri);
                //create a set of the URI's metadata value
                Set uriValueSet = new HashSet<>(doc.getMetadata().values());
                //compare the URI's keys and values. if they are both the same, add it to the list of documents
                if (doc.getMetadata().keySet().equals(keysValues.keySet()) && valueSet.equals(uriValueSet)) {
                    reHeapifyDS(doc);
                    MetaDataList.add(doc);
                }
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
        // create a set of all values in the @param keysValues map b/c .values() return a collection and we need a set b/c sets ignore order so its easier to compare
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
        //set use time to each doc
        for (Document doc: DocsWithKeyword){
            reHeapifyDS(doc);
        }
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
        // create a set where all non matching values will be stored
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
        //set use time to each doc
        for (Document doc: DocsWithKeyword){
            reHeapifyDS(doc);
        }
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
            //remove Doc from heap
            removeDocFromHeap(doc);

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
                //remove Doc from heap
                removeDocFromHeap(doc);
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
              //remove Doc from heap
              removeDocFromHeap(doc);
          }
        }
        UndoCommandSetDelete(DocSet);
        return allURIsDelted;
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
            //peek top one, remove its bytes from bytes
            getRidOfDoc(this.docHeap.peek());
            //remove its bytes from max bytes
            this.DocBytes -= getDocumentBytes(this.docHeap.remove());
            //subtract count by 1
            this.DocCount --;
        }
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
       //if the amout of bytes worth of docs being held is greater than the limit, remove docs until tis les than the limit
        if (this.DocBytes > limit) {
            //while over the limit remove
            while (this.DocBytes  > limit) {
                //get rid of doc
                getRidOfDoc(this.docHeap.peek());
                //remove bytes from storage and delete off heap
                this.DocBytes -= getDocumentBytes(this.docHeap.remove());
                //subtract one ONLY IF there is a limit on doc count
//                if (this.MaxDocumentCount > 1){
                    this.DocCount --;}
//            }
        }
        //after it removes docs or there are not set limits yet
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

    //THIS method gets rid of all traces of the doc EXCLUDING THE HEAP, shud be used when a limit is reached and we need ot remove docs from heap
    private void getRidOfDoc(Document doc) {
        //gets words off of the trie and removes from hashtable
//        if (this.storage.get(doc.getKey()) != null){
//            deleteAllTraces(doc);
//        }
        deleteAllTraces(doc);
        //remove from undo Stack
        StackImpl<Undoable> tempStack = new StackImpl<>();
        while (this.stack.size() != 0) {
            Undoable command = this.stack.peek();
            //if GenericCommand
            if (command instanceof GenericCommand) {
                GenericCommand<URI> genericCommand = (GenericCommand<URI>) command;
                //if it contains this doc, get rid of it
                if (genericCommand.getTarget().equals(doc.getKey())) {
                    stack.pop();
                } else {
                    //if its not it, push it to temp stack
                    tempStack.push(stack.pop());
                }
            } else if (command instanceof CommandSet) {
                CommandSet<URI> commandSet = (CommandSet<URI>) command;
                // If the command set contains the URI of the document, undo it and pop it
                if (commandSet.containsTarget(doc.getKey())) {
                    commandSet.undo(doc.getKey());
                    if (commandSet.undo()) {
                        stack.pop();
                    }
                } else {
                    //if its not it, push it to temp stack
                    tempStack.push(stack.pop());
                }
            }
        }
        // Push back the remaining commands to the main stack
        while (tempStack.size() != 0) {
            stack.push(tempStack.pop());
        }
    }
    private void addDocToHeap(Document doc){
        int currentDocBytes = getDocumentBytes(doc);
        //if count is reached && there is a max Count set
        if (this.MaxDocumentCount > 1 && this.DocCount == this.MaxDocumentCount){
           System.out.println("MAXXXX");
            //peek top one, remove its bytes from bytes
            getRidOfDoc(this.docHeap.peek());
            this.DocBytes -= getDocumentBytes(this.docHeap.remove());
            this.DocCount --;
            //remove its bytes from max bytes ONLY IF there is a limit
            if (this.MaxDocumentBytes > 1) {
                System.out.println("MAXXXX 22222");
                while (this.DocBytes + currentDocBytes > this.MaxDocumentBytes) {
                    getRidOfDoc(this.docHeap.peek());
                    this.DocBytes -= getDocumentBytes(this.docHeap.remove());
                    //subtract one ONLY IF there is a limit on doc count
//                    if (this.MaxDocumentCount > 1) {
                        this.DocCount--;
//                    }
                }
//                this.docHeap.remove();
//                addToHeap(doc, currentDocBytes);
//                return;
            }

//            //if it hasnt hit max Bytes
//            //remove the doc
//            this.docHeap.remove();
//            //subtract count by 1
//            this.DocCount --;
        }
        //if max bytes are reached && there is a set Max Bytes
        if (this.MaxDocumentBytes > 1 && this.DocBytes + currentDocBytes > this.MaxDocumentBytes) {
            while (this.DocBytes + currentDocBytes > this.MaxDocumentBytes) {
                getRidOfDoc(this.docHeap.peek());
                this.DocBytes -= getDocumentBytes(this.docHeap.remove());
                //subtract one ONLY IF there is a limit on doc count
                if (this.MaxDocumentCount > 1){
                    this.DocCount --;}

            }
        }
        //when DocBytes and DocCount are all good OR there was no restrictions, insert the doc
        addToHeap(doc, currentDocBytes);
        }
    private void addToHeap(Document doc, int currentDocBytes) {
        //set nano time
        doc.setLastUseTime(System.nanoTime());
        //then insert (shud be a top of heap)
        this.docHeap.insert(doc);
        //add to Doc count + Doc bytes
        this.DocBytes += currentDocBytes;
        this.DocCount ++;
    }

    //this method updates the Doc's Last use time and then updates the Heap
    private void reHeapifyDS(Document doc){
         doc.setLastUseTime(System.nanoTime());
         this.docHeap.reHeapify(doc);
    }

    //this method deletes the given doc ONLY from the heap not matter where the doc is
    private void removeDocFromHeap(Document doc){
        //remove bytes
        this.DocBytes -= getDocumentBytes(doc);
        //lower doc count
        this.DocCount --;
        //set the last time used of the doc to the highest possible value
        doc.setLastUseTime(Long.MIN_VALUE);
        //reHeapify so this doc is pushed to the top of the heap
        this.docHeap.reHeapify(doc);
        //the doc should now be on top, so remove it
       this.docHeap.remove();
    }


}

