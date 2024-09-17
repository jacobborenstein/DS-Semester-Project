package edu.yu.cs.com1320.project.stage6.impl;

import com.google.gson.*;
import edu.yu.cs.com1320.project.stage6.Document;
import edu.yu.cs.com1320.project.stage6.impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.stage6.PersistenceManager;
import jakarta.xml.bind.DatatypeConverter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {

    private final String file;
    private final Gson myGson = new GsonBuilder().registerTypeAdapter(DocumentImpl.class, new docSer()).create();

    public DocumentPersistenceManager(){
        this.file = System.getProperty("user.dir");
    }
    public DocumentPersistenceManager(File baseDir){
        this.file = baseDir.getPath();
    }


    private static class docSer implements JsonDeserializer<Document>, JsonSerializer<Document>{

        @Override
        public Document deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject o = jsonElement.getAsJsonObject();

            if (!o.has("wordmap") && !o.has("txt") && !o.has("binary") && !o.has("metadata")) {
                throw new JsonParseException("Missing required properties in JSON object");
            }

            URI uri = null;
            try {
                uri = new URI(o.get("uri").getAsString());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
//            uri = jsonDeserializationContext.deserialize(o.get("wordmap"), URI.class);

            Document doc;

            //doc is txt type
            if (o.get("txt")!= null) {
                //create new txt with uri, txtContent,and get wordmap
                doc = new DocumentImpl(uri,o.get("txt").getAsString(),jsonDeserializationContext.deserialize(o.get("wordmap"), HashMap.class));
            } else {
                //doc is binary type

//                byte[] byt = DatatypeConverter.parseBase64Binary(o.get("binary"));
                doc = new DocumentImpl(uri,DatatypeConverter.parseBase64Binary(o.get("binary").getAsString()));
            }

            //if there is meta data, add it to doc
            if (o.get("MetaData") != null){
                doc.setMetadata(jsonDeserializationContext.deserialize(o.get("MetaData"), HashMap.class));
            }

            return doc;
        }

        @Override
        public JsonElement serialize(Document document, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject o = new JsonObject();
            //uri of doc
            o.addProperty("uri",document.getKey().toString() );

            //meta Data
            o.add("MetaData",jsonSerializationContext.serialize(document.getMetadata()));

            //contents
            //if txt type
            if (document.getDocumentTxt() != null){
                o.addProperty("txt", document.getDocumentTxt());
                // word count map
                o.add("WordMap",jsonSerializationContext.serialize(document.getWordMap()));

            }
            //if binary type
            else {
                String bin = DatatypeConverter.printBase64Binary(document.getDocumentBinaryData());
                o.addProperty("binary", bin);
            }

            return o;
        }
    }


    @Override
    public void serialize(URI key, Document val) throws IOException {
        //Take "hhtp://" out and replace it with the Directory path
        //add ".json" to the end
        String l = key.toString();
        String directoryPath = l.replaceFirst("^http://", this.file + File.separator);
        directoryPath = directoryPath.substring(0, directoryPath.lastIndexOf("/"));

        //create the directory if it doesnt exist
        File directory = new File(directoryPath);
        directory.mkdirs();

        String fileName = l.substring(l.lastIndexOf("/") + 1) + ".json";
        String filePath = directoryPath + File.separator + fileName;
        //Serialize the Document
        String jsonDoc = this.myGson.toJson(val);

        try (FileWriter writeFile = new FileWriter(filePath)) {
            //put json doc in the correct directory
            writeFile.write(jsonDoc);
        }
    }

    @Override
    public Document deserialize(URI key) throws IOException {
        String l = key.toString();
        String directoryPath = l.replaceFirst("^http://", this.file + File.separator);
        directoryPath = directoryPath.substring(0, directoryPath.lastIndexOf("/"));
        String fileName = l.substring(l.lastIndexOf("/") + 1) + ".json";
        String filePath = directoryPath + File.separator + fileName;


        //check if file exist
        if (!new File(filePath).exists() ){
            return null;
        }

        //find the file
        try (FileReader readFile = new FileReader(filePath)) {

            //get the doc
            DocumentImpl docToReturn =  this.myGson.fromJson(readFile, DocumentImpl.class);

            //delete the doc
            this.delete(key);

            //return the doc
            return docToReturn;
        }
    }

    @Override
    public boolean delete(URI key) throws IOException {
        String l = key.toString();
        String directoryPath = l.replaceFirst("^http://", this.file + File.separator);
        directoryPath = directoryPath.substring(0, directoryPath.lastIndexOf("/"));
        String fileName = l.substring(l.lastIndexOf("/") + 1) + ".json";
        String filePath = directoryPath + File.separator + fileName;

        //check if file exist
        if (!new File(filePath).exists() ){
            return false;
        }

        //find the file and delete if its there
        File del = new File(filePath);
        boolean deleted = del.delete();
        deleteEmptyFolders(directoryPath);
        return deleted;
    }


    private void deleteEmptyFolders(String file) {
        File folder = new File(file);
        File[] files = folder.listFiles();

        if (files.length == 0){
            folder.delete();
            //create new string of path without latest folder that got delted
            int lastSlash = file.lastIndexOf("/");
            deleteEmptyFolders(file.substring(0,lastSlash));
        }
    }




}