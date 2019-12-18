package gui.app.theme;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import javafx.scene.paint.Color;

public class ThemeTests
{
	@Test
	public void colourCanBeTurnedToHex()
	{
		Color red = Color.RED;		
		assertEquals("#FF0000", Colours.toHex(red));
	}
}