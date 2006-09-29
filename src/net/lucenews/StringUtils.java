package net.lucenews;

public class StringUtils {
    
    public StringUtils () {
    }
    
    
    /**
     * Joins the String values of provided objects with a comma.
     */
    
    public static String join (Object... objects) {
        StringBuffer buffer = new StringBuffer();
        
        for ( int i = 0; i < objects.length; i++ ) {
            if ( i > 0 ) {
                buffer.append(",");
            }
            buffer.append( String.valueOf( objects[ i ] ) );
        }
        
        return buffer.toString();
    }
    
    /**
     * Joins the String values of provided objects with commas.
     * 
     *     i.e. - { "foo", "bar", "jim", "fad" }
     *                becomes...
     *            "foo, bar, jim and fad"
     */
    
    public static String naturalJoin (Object... objects) {
        StringBuffer buffer = new StringBuffer();
        
        for ( int i = 0; i < objects.length; i++ ) {
            if ( i > 0 ) {
                if ( i == objects.length - 1 ) {
                    buffer.append(" and ");
                }
                else {
                    buffer.append(", ");
                }
            }
            buffer.append( String.valueOf( objects[ i ] ) );
        }
        
        return buffer.toString();
    }
    
}
