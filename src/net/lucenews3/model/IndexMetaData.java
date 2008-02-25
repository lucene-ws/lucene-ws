package net.lucenews3.model;

import java.util.List;
import java.util.Properties;

public interface IndexMetaData {

	public String getName();
	
	public void setName(String name);
	
	public String getPrimaryField();
	
	public void setPrimaryField(String primaryField);
	
	public List<String> getDefaultFields();
	
	public void setDefaultFields(List<String> defaultFields);
	
	public Properties getProperties();
	
	public void setProperties(Properties properties);
	
}
