//                                                                                          בס"ד
package edu.yu.cs.com1320.project.stage6.impl;


import java.io.IOException;
import java.util.HashMap;
import edu.yu.cs.com1320.project.stage6.Document;

import java.net.URI;
import java.util.*;

public class DocumentImpl implements Document {

    private URI identifier;
    private HashMap<String,String> MetaData;
    private String stringContent;
    private byte[] byteContent;

   private HashMap<String,Integer> wordMap;
   private long nanoSeconds = 0;


    public DocumentImpl (URI uri, String txt, Map<String,Integer> wordCountMap){
        if (uri == null || txt == null || uri.toString().isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.identifier = uri;
        this.MetaData = new HashMap<>();
        this.stringContent = txt;

        //being deserliazed
        if (wordCountMap != null){
            this.wordMap = (HashMap<String, Integer>) wordCountMap;
        } else {
        //Doc was just created
            this.wordMap = new HashMap<>();
            this.addWords();
        }
    }

    public DocumentImpl (URI uri, byte[] binaryData){
        if (uri == null || binaryData == null || uri.toString().isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.identifier = uri;
        this.MetaData = new HashMap<>();
        this.byteContent = binaryData;
    }
    private void addWords(){
        String[] splitWords = this.stringContent.split(" ");
        for (String w : splitWords){
            //if word already exist, update the value by 1
            if (this.wordMap.get(w) != null){
                int value = this.wordMap.get(w);
                this.wordMap.put(w,value + 1);
            }
            //if word doesnt exist then add it with value 1
            else {
                this.wordMap.put(w, 1);
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
    public HashMap<String, String> getMetadata() {
       HashMap<String,String> copy = new HashMap<>();
        copy = this.MetaData;
        return copy;
    }

    @Override
    public void setMetadata(HashMap<String, String> metadata) {
        this.MetaData = metadata;
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
        if (this.byteContent != null || !this.wordMap.containsKey(word)){
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
        if (this.wordMap == null){
            return Collections.emptySet();
        }
        return this.wordMap.keySet();
    }

    /**
     * return the last time this document was used, via put/get or via a search result
     * (for stage 4 of project)
     */
    @Override
    public long getLastUseTime() {
        return this.nanoSeconds;
    }

    @Override
    public void setLastUseTime(long timeInNanoseconds) {
        this.nanoSeconds = timeInNanoseconds;
    }

    @Override
    public HashMap<String, Integer> getWordMap() {
        return this.wordMap;
    }

    @Override
    public void setWordMap(HashMap<String, Integer> wordMap) {
        this.wordMap=wordMap;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentImpl document = (DocumentImpl) o;
        return  Objects.equals(identifier, document.identifier) && Objects.equals(MetaData, document.MetaData) && Objects.equals(stringContent, document.stringContent) && Arrays.equals(byteContent, document.byteContent);
    }

    @Override
    public int hashCode() {
        int result = this.identifier.hashCode();
        result = 31 * result + (this.stringContent != null ? this.stringContent.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(this.byteContent);
        return Math.abs(result);



    }

    @Override
    public int compareTo(Document o) {
        //this document was last used longer
        if (o == null || this.nanoSeconds > o.getLastUseTime()){
            return 1;
        }
        //if the document being compared to this document was last used longer.
        if (this.nanoSeconds < o.getLastUseTime()){
            return -1;
        }
        //last used is the same
        return 0;
    }
}