package com.mime;

import java.util.*;

public class Mime {

    private String from, MESSAGE_ID;
    private String[] to;
    private final float MIME_VERSION = 1.0F;
    private Set<String> mainTypes = new HashSet<>();
    private List<MimeBody> bodies = new ArrayList<>();

    public static void main(String[] args) {

    }
    

    private class MimeTextBody {
        private String mainType;
        private String subType;
        private String text;
    }
    private class MimeBody {
        private String mainType;
        private String subType;
        private byte[] fileData;

    }
}