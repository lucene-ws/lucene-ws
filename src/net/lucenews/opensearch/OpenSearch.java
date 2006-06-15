package net.lucenews.opensearch;

public abstract class OpenSearch {
    
    public static final Mode PASSIVE  = Mode.PASSIVE;
    public static final Mode ADAPTIVE = Mode.ADAPTIVE;
    public static final Mode STRICT   = Mode.STRICT;
    
    public static final Format ATOM = Format.ATOM;
    public static final Format RSS  = Format.RSS;
    
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
    
    static public final class Mode {
        
        private Mode () {
        }
        
        static public final Mode PASSIVE  = new Mode();
        static public final Mode ADAPTIVE = new Mode();
        static public final Mode STRICT   = new Mode();
    }
    
    static public final class Format {
        
        private Format () {
        }
        
        static public final Format ATOM = new Format();
        static public final Format RSS  = new Format();
    }
    
}
