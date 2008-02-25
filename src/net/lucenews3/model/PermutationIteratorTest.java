package net.lucenews3.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PermutationIteratorTest {

	@SuppressWarnings("unchecked")
	public static void main(String... arguments) throws Exception {
		List<String> a = new ArrayList<String>();
		a.add("A");
		a.add("B");
		a.add("C");
		List<String> b = new ArrayList<String>();
		b.add("D");
		b.add("E");
		b.add("F");
		List<String> c = new ArrayList<String>();
		c.add("G");
		c.add("H");
		c.add("I");
		
		Collection<List<String>> cc = new PermutationCollection<String>(a, b, c);
		System.out.println(cc.size());
		PermutationIterator<String> i = new PermutationIterator<String>(a, b, c);
		//while (i.hasNext()) {
		//	List<String> permutation = i.next();
		//	System.out.println(permutation);
		//}
	}
	
}
