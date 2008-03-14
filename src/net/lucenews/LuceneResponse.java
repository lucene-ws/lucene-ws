package net.lucenews;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class LuceneResponse extends HttpServletResponseWrapper {
    private ServletOutputStream outputStream;
    private PrintWriter writer;
    private int status;
    
    
    protected LuceneResponse (HttpServletResponse response) {
        super( response );
        setStatus( HttpServletResponse.SC_OK );
    }
    
    
    
    public static LuceneResponse newInstance (HttpServletResponse response) {
        return new LuceneResponse( response );
    }
    
    @Override
    @Deprecated
    public String encodeRedirectUrl (String url) {
        return encodeRedirectURL( url );
    }
    
    @Override
    @Deprecated
    public String encodeUrl (String url) {
        return encodeURL( url );
    }
    
    @Override
    @Deprecated
    public void setStatus (int sc, String sm) {
        setStatus( sc );
        
        if (sm != null) {
            try {
                sendError( sc, sm );
            }
            catch (IOException ioe) {
            }
        }
    }
    
    
    
    /**public ServletOutputStream getOutputStream ()
    throws IOException
    {
    if( outputStream == null )
    outputStream = super.getOutputStream();
    return outputStream;
    }
    
    public PrintWriter getWriter ()
    throws IOException
    {
    if( outputStream == null )
    outputStream = super.getOutputStream();
    
    if( writer == null )
    writer = new PrintWriter( outputStream, false );
    
    return writer;
    
    }
    
    public void flushBuffer ()
    {
    if( writer != null )
    writer.flush();
    }*/
    
    
    @Override
    public void setStatus (int status) {
        this.status = status;
        super.setStatus( status );
    }
    
    public int getStatus () {
        return status;
    }
}
