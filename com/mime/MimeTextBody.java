package com.mime;

import com.encoding.TextEncoding;
import com.google.common.base.Preconditions;

public class MimeTextBody {
    
    private String textContent;
    private String encodedType;

    public String getEncodedType() {
        return encodedType;
    }
    public String getTextContent() {
        return textContent;
    }
    public String getContentType() {
        return "text/plain";
    }

    public void setTextContent(String textContent) throws IllegalArgumentException {
        Preconditions.checkNotNull(textContent, "Null is not accepted");
        TextEncoding textEncoding = new TextEncoding();
        this.textContent = textEncoding.encodeQuotedPrintable(textContent);
        this.encodedType = textEncoding.getEncodedType();
    }

}
