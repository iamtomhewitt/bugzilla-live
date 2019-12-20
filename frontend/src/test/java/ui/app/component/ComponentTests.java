package ui.app.component;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import common.bug.Bug;
import ui.common.UiMethods;
import ui.component.BugCounter;

public class ComponentTests
{
	private List<Bug> bugs;
	
	@Before
	public void eachTest()
	{
		Bug bug1 = new Bug();
		bug1.setId("1");
		bug1.setSeverity("Major");
		bug1.setStatus("Closed");
		
		Bug bug2 = new Bug();
		bug2.setId("3");
		bug2.setSeverity("Major");
		bug2.setStatus("Open");

		Bug bug3 = new Bug();		
		bug3.setId("2");
		bug3.setSeverity("Normal");
		bug3.setStatus("Open");
		
		bugs = Arrays.asList(bug1, bug2, bug3);
	}
	
	@Test
	public void canSortBugsAscending()
	{			
		bugs = UiMethods.sortBugs(bugs, false, "id");
		
		assertEquals("1", bugs.get(0).getId());
		assertEquals("2", bugs.get(1).getId());
		assertEquals("3", bugs.get(2).getId());
	}
	
	@Test
	public void canSortBugsDescending()
	{
		bugs = UiMethods.sortBugs(bugs, true, "id");
		
		assertEquals("3", bugs.get(0).getId());
		assertEquals("2", bugs.get(1).getId());
		assertEquals("1", bugs.get(2).getId());
	}
	
	@Test
	public void bugCounterWorksCorrectly()
	{
		assertEquals(2, BugCounter.countSeverityBugs(bugs, "Major"));
		assertEquals(1, BugCounter.countSeverityBugs(bugs, "Normal"));
		assertEquals(2, BugCounter.countStatusBugs(bugs, "Open"));
		assertEquals(1, BugCounter.countStatusBugs(bugs, "Closed"));
	}
}
