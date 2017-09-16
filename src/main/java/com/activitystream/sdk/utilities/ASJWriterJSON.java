package com.activitystream.sdk.utilities;

import com.activitystream.model.ASEntity;
import com.activitystream.model.interfaces.BaseStreamItem;
import com.activitystream.model.ASEvent;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ASJWriterJSON implements Closeable {

    Map<File, BufferedWriter> asWriters = new LinkedHashMap();
    String ouputPath;
    String nameTempalate;

    public ASJWriterJSON(String ouputPath, String nameTempalate) {
        this.ouputPath = ouputPath;
        this.nameTempalate = nameTempalate;
    }

    public <T extends BaseStreamItem> void writeStreamItems(T[] streamItem) throws IOException {
        for (T item : streamItem) {
            writeStreamItem(item);
        }
    }

    public void writeStreamItem(BaseStreamItem streamItem) throws IOException {
        String fileSegment;
        String jsonString;
        if (streamItem instanceof ASEntity) {
            jsonString = ((ASEntity) streamItem).toJSON();
            fileSegment = ((ASEntity) streamItem).getEntityReference().getEntityTypeReference().getLeafEntityType();
        } else if (streamItem instanceof ASEvent) {
            jsonString = ((ASEvent) streamItem).toJSON();
            fileSegment = ((ASEvent) streamItem).getOrigin();
        } else {
            throw new RuntimeException("AS Writer is only intended for ASEntity and ASEvent items");
        }

        File outputDirectory = new File(this.ouputPath);
        outputDirectory.mkdirs();
        File outputFile = new File(this.ouputPath + File.separator + this.nameTempalate.replaceAll("\\{type\\}",fileSegment));
        if (!asWriters.containsKey(outputFile)) {
            outputFile.createNewFile();
            asWriters.put(outputFile,new BufferedWriter(new FileWriter(outputFile.getAbsoluteFile())));
        }
        BufferedWriter entitiesWriter = asWriters.get(outputFile);
        entitiesWriter.write(jsonString + "\n");

    }

    public void close() throws RuntimeException {
        asWriters.forEach((s, bufferedWriter) -> {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error closing AS writer");
            }
        });
    }

}
