package com.mime;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import com.encoding.TextEncoding;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

public class MimeBody {
    
    private String contentType;
    private String fileContents;
    private String fileName;
    private String encodedType = "base64";

    public String getContentType() {
        return contentType;
    }
    public String getFileContents() {
        return fileContents;
    }
    public String getFileName() {
        return fileName;
    }
    public String getEncodedType() {
        return encodedType;
    }

    public void addFile(File file) throws Exception {
        Preconditions.checkNotNull(file, "Null is not accepted");
        Preconditions.checkArgument(file.isFile(), "%s is not a File" + file.getAbsolutePath());

        DataSource dataSource = new FileDataSource(file);
        fileName = dataSource.getName();
        contentType = dataSource.getContentType();

        try {
            byte[] fileBytes = Files.toByteArray(file);
            String fileContents = Base64.getEncoder().encodeToString(fileBytes);
            fileContents = TextEncoding.wrapEncodedString(fileContents);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        MimeBody mimeFileBody = new MimeBody();
    }
}