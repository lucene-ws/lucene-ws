package net.lucenews.test.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

public class XoxoUtility {

	private DomUtility dom;
	
	public XoxoUtility() {
		dom = new DomUtility();
	}
	
	public Map<String, String> toMap(Element dl) {
		Map<String, String> result = new HashMap<String, String>();
		
		List<Element> childNodes = dom.elementsByPath(dl, "./*");
		String key = null;
		for (Element child : childNodes) {
			String tag = child.getTagName();
			if (tag.equals("dt")) {
				if (key != null) {
					result.put(key, null);
				}
				key = dom.innerText(child);
			} else if (tag.equals("dd")) {
				String value = dom.innerText(child);
				result.put(key, value);
				key = null;
			} else {
				throw new RuntimeException("Inconsistent xoxo list");
			}
		}
		
		return result;
	}
	
}
