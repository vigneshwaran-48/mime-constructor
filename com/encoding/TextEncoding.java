package com.encoding;

import java.io.UnsupportedEncodingException;

public class TextEncoding {

    private String encodedType = "7bit";

    public String getEncodedType() {
        return encodedType;
    }
    
    public String encodeQuotedPrintable(String inputString) {
        
        byte[] bytes = null;
        try {
            bytes = inputString.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder encodedString = new StringBuilder();

        for (byte b : bytes) {
            if (b >= 32 && b <= 126) {
                encodedString.append((char) b);
            } else {
                encodedType = "quoted-printable";
                encodedString.append(String.format("=%02X", b));
            }
        }
        return encodedString.toString();
    }

    public static String wrapEncodedString(String encodedString) {
        StringBuffer wrappedString = new StringBuffer();
        for(int i = 0;i < encodedString.length();i++) {
            if(i % 75 == 0) {
                wrappedString.append(encodedString.charAt(i) + "\r\n");
            }
            else {
                wrappedString.append(encodedString.charAt(i));
            }
        }
        return wrappedString.toString();
    }
    public static void main(String[] args) {
        TextEncoding encoding = new TextEncoding();
        System.out.println(encoding.encodeQuotedPrintable("Mõhämêd SathīkMõhämêd") + " => " + encoding.getEncodedType());
    }
}
