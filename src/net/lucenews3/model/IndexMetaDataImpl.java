package net.lucenews3.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class IndexMetaDataImpl implements IndexMetaData {

	private String name;
	private String primaryField;
	private List<String> defaultFields;
	private Properties properties;

	public IndexMetaDataImpl() {
		this.defaultFields = new ArrayList<String>();
		this.properties = new Properties();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrimaryField() {
		return primaryField;
	}

	public void setPrimaryField(String primaryField) {
		this.primaryField = primaryField;
	}

	public List<String> getDefaultFields() {
		return defaultFields;
	}

	public void setDefaultFields(List<String> defaultFields) {
		this.defaultFields = defaultFields;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
}
