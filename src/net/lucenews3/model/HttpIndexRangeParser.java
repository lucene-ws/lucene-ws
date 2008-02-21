package net.lucenews3.model;

import javax.servlet.http.HttpServletRequest;

public class HttpIndexRangeParser implements IndexRangeParser<HttpServletRequest> {

	public static final int DEFAULT_ORDINAL = 1;
	public static final int DEFAULT_SIZE = 10;
	public static final String DEFAULT_ORDINAL_PARAMETER_NAME = "page";
	public static final String DEFAULT_SIZE_PARAMETER_NAME = "itemsPerPage";
	
	private int defaultOrdinal;
	private int defaultSize;
	private String ordinalParameterName;
	private String sizeParameterName;
	
	public HttpIndexRangeParser() {
		this.defaultOrdinal = DEFAULT_ORDINAL;
		this.defaultSize = DEFAULT_SIZE;
		this.ordinalParameterName = DEFAULT_ORDINAL_PARAMETER_NAME;
		this.sizeParameterName = DEFAULT_SIZE_PARAMETER_NAME;
	}
	
	public HttpIndexRangeParser(int defaultOrdinal, int defaultSize) {
		this();
		this.defaultOrdinal = defaultOrdinal;
		this.defaultSize = defaultSize;
	}
	
	@Override
	public IndexRange parse(HttpServletRequest request) {
		String ordinalValue = request.getParameter(ordinalParameterName);
		
		int ordinal;
		if (ordinalValue == null) {
			ordinal = 1;
		} else {
			ordinal = Integer.parseInt(ordinalValue);
		}
		
		String sizeValue = request.getParameter(sizeParameterName);
		
		int size;
		if (sizeValue == null) {
			size = 20;
		} else {
			size = Integer.parseInt(sizeValue);
		}
		
		return new Page(ordinal, size);
	}

	public int getDefaultOrdinal() {
		return defaultOrdinal;
	}

	public void setDefaultOrdinal(int defaultOrdinal) {
		this.defaultOrdinal = defaultOrdinal;
	}

	public int getDefaultSize() {
		return defaultSize;
	}

	public void setDefaultSize(int defaultSize) {
		this.defaultSize = defaultSize;
	}

	public String getOrdinalParameterName() {
		return ordinalParameterName;
	}

	public void setOrdinalParameterName(String ordinalParameterName) {
		this.ordinalParameterName = ordinalParameterName;
	}

	public String getSizeParameterName() {
		return sizeParameterName;
	}

	public void setSizeParameterName(String sizeParameterName) {
		this.sizeParameterName = sizeParameterName;
	}

}
