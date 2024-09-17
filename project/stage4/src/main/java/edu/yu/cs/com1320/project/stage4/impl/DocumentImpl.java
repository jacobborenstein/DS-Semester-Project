//                                                                                          בס"ד
package edu.yu.cs.com1320.project.stage4.impl;


import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage4.Document;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class DocumentImpl implements Document {

    URI identifier;
    public HashTableImpl<String,String> MetaData;
    String stringContent;
    byte[] byteContent;

   public HashMap<String,Integer> wordMap;


    public DocumentImpl (URI uri, String txt){
        if (uri == null || txt == null || uri.toString().isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.identifier = uri;
        this.MetaData = new HashTableImpl<>();
        this.stringContent = txt;
        this.wordMap = new HashMap<>();
        this.addWords();
    }

    public DocumentImpl (URI uri, byte[] binaryData){
        if (uri == null || binaryData == null || uri.toString().isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.identifier = uri;
        this.MetaData = new HashTableImpl<>();
        this.byteContent = binaryData;

    }
    private void addWords(){
        String[] splitWords = this.stringContent.split(" ");
        for (String w : splitWords){
            //if word already exist, update the value by 1
            if (this.wordMap.get(w.replaceAll("[^a-zA-Z0-9]","")) != null){
                int value = this.wordMap.get(w.replaceAll("[^a-zA-Z0-9]",""));
                this.wordMap.put(w.replaceAll("[^a-zA-Z0-9]",""),value + 1);
            }
            //if word doesnt exist then add it with value 1
            else {
                this.wordMap.put(w.replaceAll("[^a-zA-Z0-9]", ""), 1);
            }
        }
    }


    /**
     * @param key   key of document metadata to store a value for
     * @param value value to store
     * @return old value, or null if there was no old value
     * @throws IllegalArgumentException if the key is null or blank
     */
    @Override
    public String setMetadataValue(String key, String value) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
        String oldValue = this.MetaData.get(key);
        this.MetaData.put(key, value);
       return oldValue;
    }

     /**
             * @param key metadata key whose value we want to retrieve
     * @return corresponding value, or null if there is no such key
     * @throws IllegalArgumentException if the key is null or blank
     */
    @Override
    public String getMetadataValue(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return this.MetaData.get(key);
    }

    /**

     */
    @SuppressWarnings("unchecked")
    @Override
    public HashTable<String, String> getMetadata() {
       HashTableImpl<String,String> copy = new HashTableImpl<>();
//       for (int i=0; i < 5; i++){
//           copy.array[i] = this.MetaData.array[i];
//       }
        copy = this.MetaData;
        return copy;
    }

    /**
     * @return content of text document
     */
    @Override
    public String getDocumentTxt() {
        return this.stringContent;
    }

    /**
     * @return content of binary data document
     */
    @Override
    public byte[] getDocumentBinaryData() {
        if(this.byteContent == null){
            return null;
        }
        return this.byteContent;
    }

    /**
     * @return URI which uniquely identifies this document
     */
    @Override
    public URI getKey() {
        if(this.identifier == null){
            return null;
        }
        return this.identifier;
    }
    /**
     * how many times does the given word appear in the document?
     * @param word
     * @return the number of times the given words appears in the document. If it's a binary document, return 0.
     */
    @Override
    public int wordCount(String word) {
//        document is binary, return 0
        if (this.byteContent != null){
            return 0;
        }
        //is not binary
        return this.wordMap.get(word);
    }
    /**
     * @return all the words that appear in the document
     */
    @Override
    public Set<String> getWords() {
        return this.wordMap.keySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentImpl document = (DocumentImpl) o;
        return Objects.equals(identifier, document.identifier) && Objects.equals(MetaData, document.MetaData) && Objects.equals(stringContent, document.stringContent) && Arrays.equals(byteContent, document.byteContent);
    }

    @Override
    public int hashCode() {
        int result = this.identifier.hashCode();
        result = 31 * result + (this.stringContent != null ? this.stringContent.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(this.byteContent);
        return Math.abs(result);



    }
}


