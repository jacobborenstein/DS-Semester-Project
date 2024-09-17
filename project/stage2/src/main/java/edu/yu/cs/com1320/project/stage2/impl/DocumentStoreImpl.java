package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;

public class DocumentStoreImpl implements DocumentStore {
    HashTableImpl<URI, Document> storage = new HashTableImpl<>();




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
        this.storage.get(uri).setMetadataValue(key, value);
        return old;
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
        }if (input == null) {
            if ( !this.storage.containsKey(uri) || this.storage.get(uri) == null)  { return 0;
            } else { int old = this.storage.get(uri).hashCode();
                this.storage.put(uri,null);//////changed
                return old;
            } } else {
            byte[] by = input.readAllBytes();
            String str = new String(by);
            if (format == DocumentFormat.TXT) {
                Document doc = new DocumentImpl(uri, str);
                if (!this.storage.containsKey(uri)) {
                    this.storage.put(uri, doc);
                    return 0;
                } else {
                    int hashcode = this.storage.get(uri).hashCode();
                    this.storage.put(uri, doc);
                    return hashcode;
                } } if (format == DocumentFormat.BINARY) {
                Document doc = new DocumentImpl(uri, by);
                if (!this.storage.containsKey(uri)) {
                    this.storage.put(uri, doc);
                    return 0;
                } else {
                    int hashcode = this.storage.get(uri).hashCode();
                    this.storage.put(uri, doc);
                    return hashcode;
                } } } return 0; }
    /**
     * @param url the unique identifier of the document to get
     * @return the given document
     */
    @Override
    public Document get(URI url) {
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
        } else {
            this.storage.put(url,null); //// changed
            return true;
        }
    }
}
