package ui.app.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import common.utility.UiConstants;

public class CommonTests
{
	@Test
	public void bugRegexWorksCorrectly()
	{
		String number = "12345";
		String number2 = "12a34.5";

		boolean match = number.matches(UiConstants.BUG_REGEX);
		boolean match2 = number2.matches(UiConstants.BUG_REGEX);
		
		assertEquals(true, match);
		assertEquals(false, match2);
	}
}