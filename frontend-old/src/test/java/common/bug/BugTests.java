package common.bug;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BugTests {

	@Test
	public void emailCanBeTurnedIntoAssignedToName()
	{
		String email = "John@example.com";
		String email2= "Tom.Hewitt@anotherexample.com";
		
		Bug bug = new Bug();
		bug.setAssignedTo(email);
		
		Bug bug2 = new Bug();
		bug2.setAssignedTo(email2);
		
		assertEquals("John", bug.getAssignedTo());
		assertEquals("Tom.Hewitt", bug2.getAssignedTo());
	}
	
	@Test
	public void severityStartsWithCapitalLetter()
	{
		String severity = "major";
		
		Bug bug = new Bug();
		bug.setSeverity(severity);
		
		assertEquals("Major", bug.getSeverity());
	}
	
	@Test
	public void dateStringCanBeParsed()
	{
		String date = "2019-12-17T22:15:19Z";
		
		Bug bug = new Bug();
		bug.setLastUpdated(date);
		
		assertEquals("17/12/2019 22:15", bug.getLastUpdated());
	}
}
