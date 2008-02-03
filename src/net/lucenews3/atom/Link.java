package net.lucenews3.atom;

public interface Link extends Common {

	public String getHref();
	
	public void setHref(String href);
	
	public String getRel();
	
	public void setRel(String rel);
	
	public String getType();
	
	public void setType(String type);
	
	public String getHreflang();
	
	public void setHreflang(String hreflang);
	
	public String getTitle();
	
	public void setTitle(String title);
	
	public Integer getLength();
	
	public void setLength(Integer length);
	
}
