package net.lucenews.atom;

import java.text.*;
import java.util.*;

public abstract class Base {
    // Required
    private String id;
    private String title;
    private String updated;
    // Recommended
    private List<Author> authors;
    private List<Link>   links;
    // Optional
    private Category          category;
    private List<Contributor> contributors;
    
    
    public Base () {
        links        = new LinkedList<Link>();
        authors      = new LinkedList<Author>();
        contributors = new LinkedList<Contributor>();
    }
    
    
    public String getID () {
        return id;
    }
    
    public void setID (String id) {
        this.id = id;
    }
    
    
    
    public String getTitle () {
        return title;
    }
    
    public void setTitle (String title) {
        this.title = title;
    }
    
    
    
    public String getUpdated () {
        return updated;
    }
    
    public void setUpdated (String updated) {
        this.updated = updated;
    }
    
    public void setUpdated (Calendar calendar) {
        setUpdated( calendar.getTime(), calendar.getTimeZone() );
    }
    
    public void setUpdated (Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        setUpdated(calendar);
    }
    
    public void setUpdated (Date date, TimeZone timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        
        StringBuffer buffer = new StringBuffer();
        
        formatter.format( date, buffer, new FieldPosition( DateFormat.TIMEZONE_FIELD ) );
        
        if (timezone == null) {
            buffer.append("Z");
        }
        else {
            int totalMinutes = timezone.getRawOffset() / 60000;
            
            int hours   = totalMinutes / 60;
            int minutes = totalMinutes - hours * 60;
            String h = Math.abs(hours) >= 10 ? String.valueOf( Math.abs( hours ) ) : "0" + Math.abs( hours );
            String m = minutes >= 10 ? String.valueOf( minutes ) : "0" + minutes;
            
            if (hours < 0) {
                buffer.append( "-" );
            }
            else {
                buffer.append( "+" );
            }
            
            buffer.append( h + ":" + m );
        }
        
        this.updated = String.valueOf( buffer );
    }
    
    
    
    public static String asString (Calendar calendar) {
        return asString( calendar.getTime(), calendar.getTimeZone() );
    }
    
    public static String asString (Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return asString( calendar );
    }
    
    public static String asString (Date date, TimeZone timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        
        StringBuffer buffer = new StringBuffer();
        
        formatter.format( date, buffer, new FieldPosition( DateFormat.TIMEZONE_FIELD ) );
        
        if (timezone == null) {
            buffer.append("Z");
        }
        else {
            int totalMinutes = timezone.getRawOffset() / 60000;
            
            int hours   = totalMinutes / 60;
            int minutes = totalMinutes - hours * 60;
            totalMinutes=0;
            String h = Math.abs(hours) >= 10 ? String.valueOf( Math.abs( hours ) ) : "0" + Math.abs( hours );
            String m = minutes >= 10 ? String.valueOf( minutes ) : "0" + minutes;
            
            if (hours < 0) {
                buffer.append( "-" );
            }
            else {
                buffer.append( "+" );
            }
            
            buffer.append( h + ":" + m );
        }
        
        return buffer.toString();
    }
    
    
    
    public List<Author> getAuthors () {
        return authors;
    }
    
    public void addAuthor (Author author) {
        authors.add( author );
    }
    
    
    
    public List<Contributor> getContributors () {
        return contributors;
    }
    
    public void addContributor (Contributor contributor) {
        contributors.add( contributor );
    }
    
    
    
    
    public List<Link> getLinks () {
        return links;
    }
    
    public void addLink (Link link) {
        links.add( link );
    }
    
    
    
    public Category getCategory () {
        return category;
    }
    
    public void setCategory (Category category) {
        this.category = category;
    }
    
}
