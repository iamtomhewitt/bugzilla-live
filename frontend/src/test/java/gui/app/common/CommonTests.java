package gui.app.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ui.app.common.GuiConstants;
import ui.app.common.GuiMethods;

public class CommonTests
{
	@Test
	public void bugRegexWorksCorrectly()
	{
		String number = "12345";
		String number2 = "12a34.5";

		boolean match = number.matches(GuiConstants.BUG_REGEX);
		boolean match2 = number2.matches(GuiConstants.BUG_REGEX);
		
		assertEquals(true, match);
		assertEquals(false, match2);
	}
	
	@Test
	public void canCreateDisplayNameFromEmail()
	{
		String email = "Tom@example.com";
		String name = GuiMethods.createDisplayName(email);
		
		assertEquals("Tom", name);
	}
}
