package net.lucenews3.http;

import java.util.Enumeration;

public abstract class HttpServletCommunicationBridge {

	private HttpCommunication communication;

	public HttpServletCommunicationBridge(HttpCommunication communication) {
		this.communication = communication;
	}

	public boolean containsHeader(String name) {
		boolean result;
		final KeyValueMap<String, String> headersByName = communication.getHeaders().byKey();
		result = headersByName.containsKey(name);
		return result;
	}

	public long getDateHeader(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setDateHeader(String name, long date) {
		final HeaderList headers = communication.getHeaders();
		final KeyValueMap<String, String> headersByName = headers.byKey();
		if (headersByName.containsKey(name)) {
			headersByName.remove(name);
		}
		// TODO
		headers.add(new HeaderImpl(name, String.valueOf(date)));
	}

	public void addDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		
	}

	public String getHeader(String name) {
		String result;
		KeyValueMap<String, String> headersByName = communication.getHeaders()
				.byKey();
		if (headersByName.containsKey(name)) {
			result = headersByName.get(name).get(0);
		} else {
			result = null;
		}
		return result;
	}

	public void setHeader(String name, String value) {
		final HeaderList headers = communication.getHeaders();
		final KeyValueMap<String, String> headersByName = headers.byKey();
		if (headersByName.containsKey(name)) {
			headersByName.remove(name);
		}
		headers.add(new HeaderImpl(name, value));
	}

	public void addHeader(String name, String value) {
		final HeaderList headers = communication.getHeaders();
		headers.add(new HeaderImpl(name, value));
	}

	public Enumeration<String> getHeaderNames() {
		return new IteratorEnumeration<String>(communication.getHeaders().byKey()
				.keySet().iterator());
	}

	public Enumeration<String> getHeaders(String name) {
		Enumeration<String> result;
		final KeyValueMap<String, String> headersByName = communication.getHeaders()
				.byKey();
		if (headersByName.containsKey(name)) {
			result = new IteratorEnumeration<String>(headersByName.get(name)
					.iterator());
		} else {
			result = new EmptyEnumeration<String>();
		}
		return result;
	}

	public int getIntHeader(String name) {
		int result;
		final KeyValueMap<String, String> headersByName = communication.getHeaders()
				.byKey();
		if (headersByName.containsKey(name)) {
			result = Integer.parseInt(headersByName.get(name).get(0));
		} else {
			result = -1;
		}
		return result;
	}

	public void setIntHeader(String name, int value) {
		final HeaderList headers = communication.getHeaders();
		final KeyValueMap<String, String> headersByName = headers.byKey();
		if (headersByName.containsKey(name)) {
			headersByName.remove(name);
		}
		headers.add(new HeaderImpl(name, String.valueOf(value)));
	}

	public void addIntHeader(String name, int value) {
		final HeaderList headers = communication.getHeaders();
		headers.add(new HeaderImpl(name, String.valueOf(value)));
	}
	
}
