package com.mime;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.mail.Message;
import javax.mail.internet.MimeUtility;

import com.encoding.TextEncoding;
import com.google.common.base.Preconditions;
import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;

public class Mime {

    private String from, MESSAGE_ID, mimeContentType, mime;
    private String[] to;
    private final float MIME_VERSION = 1.0F;
    private List<MimeBody> bodies = new ArrayList<>();
    private String textContent;
    private String encodedType;
    private boolean isTextAvailable, isHTMLAvailable, isBodyEmpty;
    private String htmlContent;
        
    public void setHtmlContent(String htmlContent) {
        Escaper escaper = HtmlEscapers.htmlEscaper();
        this.htmlContent = TextEncoding.wrapEncodedString(escaper.escape(htmlContent));

        isHTMLAvailable = true;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public void setTo(String[] to) {
        this.to = to;
    }
    public String getHtmlContent() {
        return htmlContent;
    }

    public String getEncodedType() {
        return encodedType;
    }
    public String getTextContent() {
        return textContent;
    }
    public void addFile(File file) throws Exception {
        MimeBody mimeBody = new MimeBody();
        mimeBody.addFile(file);
        bodies.add(mimeBody);
    }

    public void setTextContent(String textContent) throws IllegalArgumentException {
        Preconditions.checkNotNull(textContent, "Null is not accepted");
        TextEncoding textEncoding = new TextEncoding();
        this.textContent = textEncoding.encodeQuotedPrintable(textContent);
        this.encodedType = textEncoding.getEncodedType();

        isTextAvailable = true;
    }
    public void addMimeBody(MimeBody mimeBody) {
        bodies.add(mimeBody); 
    }
    private void generateMimeID() {
        UUID uuid = UUID.randomUUID();
        StringBuffer buffer = new StringBuffer();
        int lastIndex = from.lastIndexOf("@");
        buffer.append("<")
                .append(uuid.toString())
                .append(">")
                .append("@<")
                .append(from.substring(lastIndex + 1))
                .append(">");
        MESSAGE_ID = buffer.toString();    
    }

    private void setMultiFileContentType() {
        String lastElement = mimeContentType.isEmpty() ? "" : mimeContentType;
       
        for(MimeBody mimeBody : bodies) {
            String currentElementContent = mimeBody.getContentType();
            String[] splitted = currentElementContent.split("/");
            String[] splittedLast = lastElement.split("/");
            if(lastElement.equals(currentElementContent)){
                continue;
            }
            else if(splittedLast[0].equals(splitted[0])) {
                mimeContentType = "multipart/alternative";
            }
            else {
                mimeContentType = "multipart/mixed";
                break;
            }
        }
    }

    private void setMimeContentType() {
        if((isTextAvailable || isHTMLAvailable) && bodies.size() > 0) {
            mimeContentType = "multipart/mixed";
        }
        else if(isTextAvailable && isHTMLAvailable) {
            mimeContentType = "multipart/alternative";
        }
        else if(isTextAvailable) {
            mimeContentType = "text/plain";
        }
        else if(isHTMLAvailable) {
            mimeContentType = "text/html";
        }
        else if(bodies.size() == 1) {
            mimeContentType = bodies.get(0).getContentType();
        }
        else if(bodies.size() > 0) {
            setMultiFileContentType();
        }
        else {
            isBodyEmpty = true;
        }
    }
    public void encodeBasics() {
        int fromLastIndex = from.lastIndexOf("@");
        try {
            String temp = MimeUtility.encodeText(from.substring(0, fromLastIndex));
            from = temp + from.substring(fromLastIndex);
            
            for(int i = 0;i < to.length;i ++) {
                int toLastIndex = to[i].lastIndexOf("@");
                String lastTemp = MimeUtility.encodeText(to[i].substring(toLastIndex));
                to[i] = lastTemp + to[i].substring(toLastIndex);
            }
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public void constructMime() throws Exception {
        if(from.isEmpty() || to.length == 0) {
            throw new Exception("Constructing a MIME without basic details");
        }
        encodeBasics();
        generateMimeID();
        setMimeContentType();

        StringBuffer buffer = new StringBuffer();
        buffer
            .append("From: " + from)
            .append("To: " + to);

    }
    

    public static void main(String[] args) {

    }
}