package net.lucenews.opensearch;

public abstract class OpenSearch {
    
    public static final Mode PASSIVE  = Mode.PASSIVE;
    public static final Mode ADAPTIVE = Mode.ADAPTIVE;
    public static final Mode STRICT   = Mode.STRICT;
    private static Mode DEFAULT_MODE;
    
    public static final Format ATOM = Format.ATOM;
    public static final Format RSS  = Format.RSS;
    private static Format DEFAULT_FORMAT;
    
    public static Format getFormat (String name) throws OpenSearchException {
        String _name = name.toLowerCase().trim();
        
        if (_name.equals("atom")) {
            return ATOM;
        }
        
        if (_name.equals("rss")) {
            return RSS;
        }
        
        throw new OpenSearchException("Unknown format: " + name);
    }
    
    public static Format getDefaultFormat () {
        if (DEFAULT_FORMAT == null) {
            DEFAULT_FORMAT = ATOM;
        }
        return DEFAULT_FORMAT;
    }
    
    public static void setDefaultFormat (OpenSearch.Format default_format) {
        DEFAULT_FORMAT = default_format;
    }
    
    public static String getContentType (OpenSearch.Format format) {
        if (format == ATOM) {
            return "application/atom+xml";
        }
        
        if (format == RSS) {
            return "text/xml";
        }
        
        return null;
    }
    
    static public final class Mode {
        
        private Mode () {
        }
        
        static public final Mode PASSIVE  = new Mode();
        static public final Mode ADAPTIVE = new Mode();
        static public final Mode STRICT   = new Mode();
    }
    
    public static Mode getDefaultMode () {
        if (DEFAULT_MODE == null) {
            DEFAULT_MODE = STRICT;
        }
        return DEFAULT_MODE;
    }
    
    public static void setDefaultMode (OpenSearch.Mode default_mode) {
        DEFAULT_MODE = default_mode;
    }
    
    static public final class Format {
        
        private Format () {
        }
        
        static public final Format ATOM = new Format();
        static public final Format RSS  = new Format();
    }
    
}
