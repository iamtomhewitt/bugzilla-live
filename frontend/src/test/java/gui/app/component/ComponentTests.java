package gui.app.component;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import common.bug.Bug;
import gui.app.common.GuiMethods;

public class ComponentTests
{
	@Test
	public void canSortBugsAscending()
	{
		Bug bug1 = new Bug();
		bug1.setId("1");
		
		Bug bug2 = new Bug();
		bug2.setId("3");
		
		Bug bug3 = new Bug();		
		bug3.setId("2");
		
		List<Bug> bugs = Arrays.asList(bug1, bug2, bug3);
		
		bugs = GuiMethods.sortBugs(bugs, false, "id");
		
		assertEquals("1", bugs.get(0).getId());
		assertEquals("2", bugs.get(1).getId());
		assertEquals("3", bugs.get(2).getId());
	}
	
	@Test
	public void canSortBugsDescending()
	{
		Bug bug1 = new Bug();
		bug1.setId("1");
		
		Bug bug2 = new Bug();
		bug2.setId("3");
		
		Bug bug3 = new Bug();		
		bug3.setId("2");
		
		List<Bug> bugs = Arrays.asList(bug1, bug2, bug3);
		
		bugs = GuiMethods.sortBugs(bugs, true, "id");
		
		assertEquals("3", bugs.get(0).getId());
		assertEquals("2", bugs.get(1).getId());
		assertEquals("1", bugs.get(2).getId());
	}
}
