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
        factory = null;
        Document document = builder.newDocument();
        builder=null;
        Element service = document.createElement( "service" );
        service.setAttribute( "xmlns", "http://www.w3.org/2007/app" );
        Workspace workspace;
        Iterator<Workspace> workspaces = getWorkspaces().iterator();
        while (workspaces.hasNext()) {
            workspace = workspaces.next();
            service.appendChild( workspace.asElement( document ) );
            workspace=null;
        }
        workspaces=null;
        document.appendChild( service );
        
        return document;
    }
    
}
