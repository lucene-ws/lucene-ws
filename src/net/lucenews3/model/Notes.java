package net.lucenews3.model;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class Notes {

	public static Map<Object, Map<String, Object>> notesByObject = new IdentityHashMap<Object, Map<String,Object>>();
	
	public static Map<String, Object> get(Object object) {
		return notesByObject.get(object);
	}
	
	public static void set(Object object, Map<String, Object> notes) {
		notesByObject.put(object, notes);
	}
	
	public static boolean hasNotes(Object object) {
		return notesByObject.containsKey(object);
	}
	
	public static Object get(Object object, String name) {
		if (notesByObject.containsKey(object)) {
			return notesByObject.get(object).get(name);
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T get(Object object, String name, Class<T> type) {
		if (notesByObject.containsKey(object)) {
			return (T) notesByObject.get(object).get(name);
		} else {
			return null;
		}
	}
	
	public static void put(Object object, String name, Object value) {
		if (notesByObject.containsKey(object)) {
			notesByObject.get(object).put(name, value);
		} else {
			Map<String, Object> notes = new HashMap<String, Object>();
			notes.put(name, value);
			notesByObject.put(object, notes);
		}
	}
	
	public static void remove(Object object) {
		notesByObject.remove(object);
	}
	
}
