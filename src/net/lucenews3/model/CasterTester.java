package net.lucenews3.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CasterTester {

	private Caster caster;
	
	@Before
	public void setup() {
		this.caster = new CasterImpl();
	}
	
	@Test
	public void testBooleanPrimitive() {
		Assert.assertEquals(Boolean.TRUE, caster.cast(true, boolean.class));
	}
	
	@Test
	public void testBooleanReference() {
		Assert.assertEquals(Boolean.TRUE, caster.cast(true, Boolean.class));
	}
	
	@Test
	public void testCharPrimitive() {
		Assert.assertEquals('A', caster.cast('A', char.class));
	}
	
	@Test
	public void testCharReference() {
		Assert.assertEquals('A', caster.cast('A', Character.class));
	}
	
	@Test
	public void testBytePrimitive() {
		Assert.assertEquals((byte) 0x01, caster.cast((byte) 0x01, byte.class));
	}
	
	@Test
	public void testByteReference() {
		Assert.assertEquals((byte) 0x01, caster.cast((byte) 0x01, Byte.class));
	}
	
	@Test
	public void testShortPrimitive() {
		Assert.assertEquals((short) 0x01, caster.cast((short) 0x01, short.class));
	}
	
	@Test
	public void testShortReference() {
		Assert.assertEquals((short) 0x01, caster.cast((short) 0x01, Short.class));
	}
	
	@Test
	public void testIntPrimitive() {
		Assert.assertEquals(0x01, caster.cast(0x01, int.class));
	}
	
	@Test
	public void testIntReference() {
		Assert.assertEquals(0x01, caster.cast(0x01, Integer.class));
	}
	
	@Test
	public void testLongPrimitive() {
		Assert.assertEquals(0x01L, caster.cast(0x01L, long.class));
	}
	
	@Test
	public void testLongReference() {
		Assert.assertEquals(0x01L, caster.cast(0x01L, Long.class));
	}
	
	@Test
	public void testFloatPrimitive() {
		Assert.assertEquals(0.1f, caster.cast(0.1f, float.class));
	}
	
	@Test
	public void testFloatReference() {
		Assert.assertEquals(0.1f, caster.cast(0.1f, Float.class));
	}
	
	@Test
	public void testDoublePrimitive() {
		Assert.assertEquals(0.1, caster.cast(0.1, double.class));
	}
	
	@Test
	public void testDoubleReference() {
		Assert.assertEquals(0.1, caster.cast(0.1, Double.class));
	}
	
	@Test
	public void testFloatToDouble() {
		Assert.assertEquals(1.5, caster.cast(1.5f, double.class));
	}
	
	@Test
	public void testCharToInt() {
		Assert.assertEquals(0x41, caster.cast('A', int.class));
	}
	
}
