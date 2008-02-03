package net.lucenews3.atom;

public interface Category extends Common {

	public String getTerm();
	
	public void setTerm(String term);
	
	public String getScheme();
	
	public void setScheme(String scheme);
	
	public String getLabel();
	
	public void setLabel(String label);
	
}
