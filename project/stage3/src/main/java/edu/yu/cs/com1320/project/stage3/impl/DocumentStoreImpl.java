//                                                                                          בס"ד
package edu.yu.cs.com1320.project.stage3.impl;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import edu.yu.cs.com1320.project.undo.Command;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.security.Key;
import java.util.Set;
import java.util.function.Consumer;

public class DocumentStoreImpl implements DocumentStore {
    public HashTableImpl<URI, Document> storage = new HashTableImpl<>();
    private StackImpl<Command> stack = new StackImpl();



    /**
     * set the given key-value metadata pair for the document at the given uri
     *
     * @param uri
     * @param key
     * @param value
     * @return the old value, or null if there was no previous value
     * @throws IllegalArgumentException if the uri is null or blank(1st and 2nd), if there is no document stored at that uri(3rd),
     * or if the key is null or blank
     */
    @Override
    public String setMetadata(URI uri, String key, String value) {
        if (uri == null || uri.toString().isEmpty() || !this.storage.containsKey(uri) || this.storage.get(uri) == null || key == null || key.isEmpty()){
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
          Command MetaDataCommand = new Command(uri,UndoMetaData);
          this.stack.push(MetaDataCommand);
        } else { //no previous meta data with the given key
            //***CUD BE WRONG B/C CANT MAKE KEY NULL (THROWS AN E) BUT
            // SUPPOSE TO DELETE THE METADATA SO FOR NOW JUST MAKE VALUE NUL
            Consumer<URI> UndoMetaData = (i) -> this.storage.get(i).setMetadataValue(key, null);
            Command MetaDataCommand = new Command(uri,UndoMetaData);
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
                    consumerUndoPut(uri,format); // creates instance of command and adds it to stack (for undo)
                    return 0;
                } else {
                    consumerUndoPut(uri,format); //creates instance of command and adds it to stack (for undo)
                    return putHelp(uri,doc); //if Doc is in hashtable this method updates the uri and returns old uri hashcodes
                } } if (format == DocumentFormat.BINARY) { //if format == BINARY
                Document doc = new DocumentImpl(uri, by);
                if (!this.storage.containsKey(uri)) { //if Doc is not in hashTable this adds it and return 0
                    this.storage.put(uri, doc);
                    consumerUndoPut(uri,format); //creates instance of command and adds it to stack (for undo)
                    return 0;
                } else {
                    consumerUndoPut(uri,format);//creates instance of command and adds it to stack (for undo)
                    return putHelp(uri,doc); //if Doc is in hashtable this method updates the uri and returns old uri hashcodes
                } } } consumerUndoPut(uri,format); return 0; } //***MIGHT have to delete consumerUndoPut()

    private int putHelp(URI uri, Document doc){ //save lines in put method. returns hashcode of old document that is being replace
        int hashcode = this.storage.get(uri).hashCode();
        this.storage.put(uri, doc);
        return hashcode;
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
        this.stack.push(new Command(uri,undoDelete)); //pushing the command with this consumer to the stack. code can
  }                                                 //execute when the command gets popped and .accept is called through the undo method
    private void consumerUndoPut(URI uri, DocumentFormat format){
        Consumer<URI> undoPut = (i) -> {
            try {
                this.UndoPutHelp(null,i,format);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        this.stack.push(new Command(uri,undoPut));
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
        this.stack.pop().undo();
//        Command undo = this.stack.peek();
//        undo.undo();
//        this.stack.pop();


    }
    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param url
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    @Override
    public void undo(URI url) throws IllegalStateException {
        if (this.stack.size() == 0 ||this.stack.peek() ==null){
            throw new IllegalStateException();
        }
        StackImpl<Command> tempStack = new StackImpl(); //create temp stack to store
        Command current = this.stack.peek();
        while (current.getUri() != url) { //while top of stack IS NOT the uri you are looking for.....
            tempStack.push(current);    //put current in temp stack
            this.stack.pop();           // pop current so you can check next one
            if (this.stack.size() == 0) { //if we checked all command and dont find one with the URI size will equal 0 and we......
                for (int i = 0; i < tempStack.size(); i++){
                    this.stack.push(tempStack.pop()); // putting temp stack command back into main stack before we throw the exception
                }
                throw new IllegalStateException();
            } else {
                current= this.stack.peek();
            }
        }
        // if we find the URI undo it/Pop it and put everything back in
        this.stack.pop().undo();
        while (tempStack.peek()!= null){
            this.stack.push(tempStack.pop()); //putting temp back into main
        }
    }

    private void UndoPutHelp(InputStream input, URI uri, DocumentFormat format) throws IOException{
        if (uri == null || uri.toString().isEmpty() || format == null) {
            throw new IllegalArgumentException();
        }
//        if (input == null) { //CALL TO DELETE*****
//            if ( !this.storage.containsKey(uri) || this.storage.get(uri) == null)  {
//
//            }
//            else {
                int old = this.storage.get(uri).hashCode();
                this.storage.put(uri,null);//////calling put in HashTableImpl with null as value deletes the uri
//                consumerUndoDelete(input,uri,format); // creates instance of command and adds it to stack (for undo)

//            }
//        }
    }

    private void UndoDeleteHelp(InputStream input, URI uri, DocumentFormat format) throws IOException {
        byte[] by = input.readAllBytes();
        String str = new String(by);
        if (format == DocumentFormat.TXT) { // if formant == TXT
            Document doc = new DocumentImpl(uri, str);
            if (!this.storage.containsKey(uri)) { //if Doc is not in hashTable this adds it and return 0
                this.storage.put(uri, doc);
//                consumerUndoPut(uri,format); // creates instance of command and adds it to stack (for undo)
                return;
            } else {
//                consumerUndoPut(uri,format); //creates instance of command and adds it to stack (for undo)
                putHelp(uri, doc); //if Doc is in hashtable this method updates the uri and returns old uri hashcodes
                return;
            }
        }
        if (format == DocumentFormat.BINARY) { //if format == BINARY
            Document doc = new DocumentImpl(uri, by);
            if (!this.storage.containsKey(uri)) { //if Doc is not in hashTable this adds it and return 0
                this.storage.put(uri, doc);
//                consumerUndoPut(uri,format); //creates instance of command and adds it to stack (for undo)
                return;
            } else {
                //creates instance of command and adds it to stack (for undo)
                putHelp(uri, doc);
                return;//if Doc is in hashtable this method updates the uri and returns old uri hashcodes
//            } } } consumerUndoPut(uri,format); return 0; } //***MIGHT have to delete consumerUndoPut()

            }
        }
    }

//    private void h(URI uri){
//        new Set<Key>() = this.get(uri).getMetadata().keySet();
//        for (String key; this.get(uri).getMetadata().key);
//    }
}