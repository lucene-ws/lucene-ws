package net.lucenews3.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MethodInvokerImplTest {

	private MethodInvoker invoker;
	
	public class Animal {
		
		public String getSound() {
			return "...";
		}
		
	}
	
	public class Dog extends Animal {
		
		@Override
		public String getSound() {
			return "woof";
		}
		
		public String enjoys(boolean all, String... foods) {
			return "kind of";
		}
		
	}
	
	public class GermanShepherd extends Dog {
		
		@Override
		public String getSound() {
			return "grrr";
		}
		
		@Override
		public String enjoys(boolean all, String... foods) {
			return "sometimes";
		}
		
	}
	
	@Before
	public void setup() {
		this.invoker = new MethodInvokerImpl();
	}
	
	@Test
	public void testAnimalSound() {
		Animal animal = new Animal();
		Assert.assertEquals("...", invoker.invoke(animal, "getSound"));
	}
	
	@Test
	public void testDogSound() {
		Dog dog = new Dog();
		Assert.assertEquals("woof", invoker.invoke(dog, "getSound"));
	}
	
	@Test
	public void testGermanShepherdSound() {
		GermanShepherd germanShepherd = new GermanShepherd();
		Assert.assertEquals("grrr", germanShepherd.getSound());
	}
	
	@Test
	public void testDogEnjoys() {
		Dog dog = new Dog();
		Assert.assertEquals("kind of", invoker.invoke(dog, "enjoys", true, "tuna", "pizza"));
	}
	
	@Test
	public void testGermanShepherdEnjoys() {
		GermanShepherd germanShepherd = new GermanShepherd();
		Assert.assertEquals("sometimes", invoker.invoke(germanShepherd, "enjoys", false));
	}
	
}
