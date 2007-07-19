package net.lucenews.atom;

import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class IntrospectionDocument {
    
    private List<Workspace> workspaces;
    
    
    
    public IntrospectionDocument () {
        workspaces = new LinkedList<Workspace>();
    }
    
    
    public List<Workspace> getWorkspaces () {
        return workspaces;
    }
    
    public void addWorkspace (Workspace workspace) {
        workspaces.add( workspace );
    }
    
    
    public Document asDocument () throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        
        Element service = document.createElement( "service" );
        service.setAttribute( "xmlns", "http://www.w3.org/2007/app" );
        
        Iterator<Workspace> workspaces = getWorkspaces().iterator();
        while (workspaces.hasNext()) {
            Workspace workspace = workspaces.next();
            service.appendChild( workspace.asElement( document ) );
        }
        
        document.appendChild( service );
        
        return document;
    }
    
}
