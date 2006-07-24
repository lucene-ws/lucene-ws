package net.lucenews;

import java.io.*;
import java.util.*;
import javax.servlet.http.*;
import org.apache.log4j.*;

public class HttpURI {
    
    private String       host;
    private Integer      port;
    private List<String> pathComponents;
    private Map<String,String[]> parameters;
    
    
    
    public HttpURI () {
        parameters     = new LinkedHashMap<String,String[]>();
        pathComponents = new LinkedList<String>();
    }
    
    public HttpURI (HttpServletRequest request) {
        this();
        
        StringBuffer url = request.getRequestURL();
        
        String queryString = request.getQueryString();
        if (queryString != null) {
            url.append("?");
            url.append(queryString);
        }
        
        setUri(url.toString());
    }
    
    public HttpURI (String uri) {
        this();
        setUri( uri );
    }
    
    protected void setUri (String uri) {
        Logger.getLogger( HttpURI.class ).debug( "STARTED SETTING URI TO " + uri );
        
        String protocol = "http://";
        
        if (uri.startsWith(protocol)) {
            uri = uri.substring(protocol.length());
        }
        
        int indexOfSlash = uri.indexOf("/");
        int indexOfColon = uri.indexOf(":");
        
        if (indexOfSlash < 0) {
            if (indexOfColon < 0) {
                setHost( uri );
            }
            else {
                setHost( uri.substring( 0, indexOfColon ) );
                setPort( Integer.valueOf( uri.substring( indexOfColon + 1 ) ) );
            }
        }
        else {
            if (indexOfColon < 0) { // port not specified
                setHost( uri.substring( 0, indexOfSlash ) );
                setPort( null );
            }
            else { // port specified
                setHost( uri.substring( 0, indexOfColon ) );
                setPort( Integer.valueOf( uri.substring( indexOfColon + 1, indexOfSlash ) ) );
            }
            
            int indexOfQuestion = uri.indexOf("?");
            if (indexOfQuestion < 0) { // parameters not specified
                setPath( uri.substring( indexOfSlash ) );
            }
            else { // parameters specified
                setPath( uri.substring( indexOfSlash, indexOfQuestion ) );
                
                String parameters = uri.substring( indexOfQuestion + 1 );
                setParameters( parameters );
            }
        }
        
        Logger.getLogger( HttpURI.class ).debug( "FINISHED SETTING URI TO " + uri + ", PATH = " + getPath() );
    }
    
    
    
    public String getHost () {
        return host;
    }
    
    public void setHost (String host) {
        this.host = host;
    }
    
    
    
    public Integer getPort () {
        return port;
    }
    
    public void setPort (Integer port) {
        this.port = port;
    }
    
    
    
    public Integer getDefaultPort () {
        return 80;
    }
    
    
    
    public String getPath () {
        StringBuffer path = new StringBuffer();
        
        Iterator<String> iterator = getPathComponents().iterator();
        while (iterator.hasNext()) {
            String pathComponent = iterator.next();
            
            path.append("/");
            if (pathComponent != null) {
                path.append( pathComponent );
            }
        }
        
        return path.toString();
    }
    
    public void setPath (String path) {
        List<String> pathComponents = new LinkedList<String>();
        
        Logger.getLogger( HttpURI.class ).debug( "SETTING PATH TO " + path );
        
        if (path != null) {
            Iterator<String> iterator = Arrays.asList( path.split( "/" ) ).iterator();
            
            boolean first = true;
            while (iterator.hasNext()) {
                String pathComponent = iterator.next();
                
                if (first) {
                    if (pathComponent != null && pathComponent.length() > 0) {
                        pathComponents.add( pathComponent );
                    }
                    first = false;
                }
                else {
                    pathComponents.add( pathComponent );
                }
            }
        }
        
        setPathComponents( pathComponents );
    }
    
    public List<String> getPathComponents () {
        return pathComponents;
    }
    
    public void setPathComponents (String... pathComponents) {
        this.pathComponents = Arrays.asList( pathComponents );
    }
    
    public void setPathComponents (List<String> pathComponents) {
        this.pathComponents = pathComponents;
    }
    
    public void addPath (String path) {
        List<String> pathComponents = getPathComponents();
        
        if (path != null) {
            if (pathComponents == null) {
                pathComponents = new LinkedList<String>();
            }
            
            pathComponents.add( path );
        }
        
        setPathComponents( pathComponents );
    }
    
    
    
    public Map<String,String[]> getParameters () {
        return parameters;
    }
    
    public String getParameter (String name) {
        return getParameters(name)[0];
    }
    
    public String[] getParameters (String name) {
        return parameters.get(name);
    }
    
    public void setParameters (String parametersString) {
        Map<String,String[]> parameters = new LinkedHashMap<String,String[]>();
        
        if (parametersString != null) {
            List<String> entries = Arrays.asList( parametersString.split( "&" ) );
            Iterator<String> iterator = entries.iterator();
            while (iterator.hasNext()) {
                String parameterString = iterator.next();
                String name  = null;
                String value = null;
                
                int indexOfEquals = parameterString.indexOf("=");
                if (indexOfEquals < 0) {
                    name = parameterString;
                }
                else {
                    name  = parameterString.substring( 0, indexOfEquals );
                    value = parameterString.substring( indexOfEquals + 1 );
                }
                
                if (name != null) {
                    String[] currentValue = parameters.get( name );
                    if (currentValue == null) {
                        parameters.put( name, new String[]{ value } );
                    }
                    else {
                        List<String> list = new LinkedList<String>();
                        list.addAll( Arrays.asList( currentValue ) );
                        list.add( value );
                        parameters.put( name, list.toArray( new String[]{} ) );
                    }
                }
            }
        }
        
        setParameters( parameters );
    }
    
    public void setParameters (Map<String,String[]> parameters) {
        this.parameters = parameters;
    }
    
    public String[] setParameter (String name, String... values) {
        return parameters.put(name, values);
    }
    
    public String[] removeParameters (String name) {
        return parameters.remove(name);
    }
    
    public String[] removeParameters (String name, String... values) {
        List<String> removed = new LinkedList<String>();
        List<String> remaining = new LinkedList<String>();
        
        String[] currentValues = getParameters(name);
        for (int i = 0; i < currentValues.length; i++) {
            String currentValue = currentValues[ i ];
            
            boolean keep = true;
            for (int j = 0; j < values.length; j++) {
                String value = values[ j ];
                if (currentValue.equals(value)) {
                    keep = false;
                }
            }
            
            if (keep) {
                remaining.add(currentValue);
            }
            else {
                removed.add(currentValue);
            }
        }
        
        setParameter(name, remaining.toArray(new String[]{}));
        
        return removed.toArray(new String[]{});
    }
    
    
    
    
    public HttpURI clone () {
        Logger.getLogger( HttpURI.class ).debug("STARTING TO CLONE");
        HttpURI uri = new HttpURI();
        uri.setHost( getHost() );
        uri.setPort( getPort() );
        uri.setPath( getPath() );
        uri.setParameters( getParameters() );
        Logger.getLogger( HttpURI.class ).debug("FINISHED TO CLONE");
        return uri;
    }
    
    
    
    /**
     * without
     */
    
    public HttpURI without (String name) {
        HttpURI uri = clone();
        uri.removeParameters(name);
        return uri;
    }
    
    public HttpURI without (String name, String... values) {
        HttpURI uri = clone();
        uri.removeParameters(name, values);
        return uri;
    }
    
    public HttpURI without (String name, int... values) {
        String[] stringValues = new String[ values.length ];
        for (int i = 0; i < values.length; i++) {
            stringValues[ i ] = String.valueOf( values[ i ] );
        }
        HttpURI uri = clone();
        uri.removeParameters(name, stringValues);
        return uri;
    }
    
    public HttpURI with (String name, String... values) {
        HttpURI uri = without(name);
        uri.setParameter(name, values);
        return uri;
    }
    
    public HttpURI with (String name, int... values) {
        String[] stringValues = new String[ values.length ];
        for (int i = 0; i < values.length; i++) {
            stringValues[ i ] = String.valueOf( values[ i ] );
        }
        return with( name, stringValues );
    }
    
    
    
    public static String uriFormatted (String string) {
        byte[] bytes = string.getBytes();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            buffer.append( uriFormatted( bytes[ i ] ) );
        }
        return buffer.toString();
    }
    
    public static String uriFormatted (byte b) {
        if ( ( b & 0x80 ) > 0) {
            // Escape it!
            return escape( b );
        }
        else {
            String s = "";
            s += (char) b;
            return s;
        }
    }
    
    public static String escape (byte b) {
        
        String[] alphabet = {
            "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "A", "B", "C", "D", "E", "F"
        };
        
        String escaped = "%";
        
        byte ch = (byte) (b & 0xF0); // Strip off high nibble
        ch = (byte) (ch >>> 4); // shift the bits down
        ch = (byte) (ch & 0x0F); // must do this is high order bit is on!
        escaped += alphabet[ (int) ch ]; // convert the nibble to a String Character
        
        ch = (byte) (b & 0x0F); // Strip off low nibble 
        escaped += alphabet[ (int) ch ]; // convert the nibble to a String Character
        
        return escaped;
    }
    
    public String toString () {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("http://");
        
        // host
        buffer.append( getHost() );
        
        // port
        if (getPort() != null) {
            buffer.append( ":" + getPort() );
        }
        
        // path
        if (getPath() != null) {
            buffer.append( getPath() );
        }
        
        Map<String,String[]> parameters = getParameters();
        if (parameters != null) {
            boolean first = true;
            Iterator<Map.Entry<String,String[]>> entriesIterator = parameters.entrySet().iterator();
            while (entriesIterator.hasNext()) {
                Map.Entry<String,String[]> entry = entriesIterator.next();
                String   name   = entry.getKey();
                String[] values = entry.getValue();
                
                for (int i = 0; i < values.length; i++) {
                    if (first) {
                        buffer.append("?");
                        first = false;
                    }
                    else {
                        buffer.append("&");
                    }
                    buffer.append( uriFormatted( name ) );
                    buffer.append("=");
                    buffer.append( uriFormatted( values[i] ) );
                }
            }
        }
        
        return buffer.toString();
    }
    
}