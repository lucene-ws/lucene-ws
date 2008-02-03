package net.lucenews3.atom;

import java.util.ArrayList;
import java.util.List;

public class CollectionImpl extends CommonImpl implements Collection {

	private String title;
	private String href;
	private List<String> accepts;
	private CategoryList categories;

	public CollectionImpl() {
		this(new CategoryListImpl());
	}
	
	public CollectionImpl(CategoryList categories) {
		this(categories, new ArrayList<String>());
	}
	
	public CollectionImpl(CategoryList categories, List<String> accepts) {
		this.categories = categories;
		this.accepts = accepts;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public List<String> getAccepts() {
		return accepts;
	}

	public void setAccepts(List<String> accepts) {
		this.accepts = accepts;
	}

	public CategoryList getCategories() {
		return categories;
	}

	public void setCategories(CategoryList categories) {
		this.categories = categories;
	}
	
}
