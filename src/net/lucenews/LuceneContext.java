package net.lucenews;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.lucenews.model.*;
import org.apache.log4j.*;

public class LuceneContext {
    
    private Map<String,Object> m_stash;
    private LuceneRequest      m_request;
    private LuceneResponse     m_response;
    private LuceneWebService   m_service;
    private Logger             m_log;
    
    
    
    public LuceneContext (HttpServletRequest request, HttpServletResponse response) {
        m_request  = LuceneRequest.newInstance( request );
        m_response = LuceneResponse.newInstance( response );
    }
    
    
    
    public LuceneContext (LuceneRequest request, LuceneResponse response) {
        m_stash    = new HashMap<String,Object>();
        m_request  = request;
        m_response = response;
    }
    
    public LuceneContext (LuceneRequest request, LuceneResponse response, LuceneWebService service) {
        m_stash    = new HashMap<String,Object>();
        m_request  = request;
        m_response = response;
        m_service  = service;
    }
    
    
    public Map<String,Object> stash () {
        return m_stash;
    }
    
    public LuceneRequest request () {
        return m_request;
    }
    
    public LuceneRequest req () {
        return request();
    }
    
    public LuceneResponse response () {
        return m_response;
    }
    
    public LuceneResponse res () {
        return response();
    }
    
    public LuceneWebService service () {
        return getService();
    }
    
    public LuceneWebService getService () {
        return m_service;
    }
    
    public void setService (LuceneWebService service) {
        m_service = service;
    }
    
    public Logger log () {
        return m_log;
    }
    
    public void log (Logger p_log) {
        m_log = p_log;
    }
}
