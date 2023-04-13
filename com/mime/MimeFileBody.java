package com.mime;

import java.io.File;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

import com.google.common.base.Preconditions;

public class MimeFileBody {
    
    private String contentType;
    private byte[] fileContents;
    private String fileName;
    private String encodedType = "base64";

    public String getContentType() {
        return contentType;
    }
    public byte[] getFileContents() {
        return fileContents;
    }
    public String getFileName() {
        return fileName;
    }
    public String getEncodedType() {
        return encodedType;
    }

    public void addFile(File file) throws IllegalArgumentException {
        Preconditions.checkNotNull(file, "Null is not accepted");
        Preconditions.checkArgument(file.isFile(), "%s is not a File" + file.getAbsolutePath());

        DataSource dataSource = new FileDataSource(file);

    }
}