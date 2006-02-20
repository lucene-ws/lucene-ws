package net.lucenews.atom;

import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

public class IntrospectionDocument
{
	
	private List<Workspace> m_workspaces;
	
	
	
	public IntrospectionDocument ()
	{
		m_workspaces = new LinkedList<Workspace>();
	}
	
	
	public List<Workspace> getWorkspaces ()
	{
		return m_workspaces;
	}
	
	public void addWorkspace (Workspace workspace)
	{
		m_workspaces.add( workspace );
	}
	
	
	public Document asDocument ()
		throws ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();
		
		Element service = document.createElement( "service" );
		service.setAttribute( "xmlns", "http://purl.org/atom/app#" );
		
		Iterator<Workspace> workspaces = getWorkspaces().iterator();
		while( workspaces.hasNext() )
			service.appendChild( workspaces.next().asElement( document ) );
		
		document.appendChild( service );
		
		return document;
	}
	
}
