package net.lucenews3.atom;

import java.util.List;

public interface Collection extends Common {

	public String getTitle();
	
	public void setTitle(String title);
	
	public String getHref();
	
	public void setHref(String href);
	
	public List<String> getAccepts();
	
	public void setAccepts(List<String> accepts);
	
	public CategoryList getCategories();
	
	public void setCategories(CategoryList categories);
	
}
