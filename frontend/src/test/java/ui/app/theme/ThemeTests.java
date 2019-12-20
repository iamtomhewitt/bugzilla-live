package ui.app.theme;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import javafx.scene.paint.Color;
import ui.theme.Colours;
import ui.theme.Sizes;
import ui.theme.Sizes.Size;

public class ThemeTests
{
	@Test
	public void colourCanBeTurnedToHex()
	{
		Color red = Color.RED;		
		assertEquals("#FF0000", Colours.toHex(red));
	}
	
	@Test
	public void sizeCalculatorWorksForSmallButtons()
	{
		Size size = Size.SMALL;
		
		int width = Sizes.calculateButtonWidth(size);
		int height = Sizes.calculateButtonHeight(size);
		
		assertEquals(100, width);
		assertEquals(30, height);
	}
	
	@Test
	public void sizeCalculatorWorksForMediumButtons()
	{
		Size size = Size.MEDIUM;
		
		int width = Sizes.calculateButtonWidth(size);
		int height = Sizes.calculateButtonHeight(size);
		
		assertEquals(200, width);
		assertEquals(30, height);
	}
	
	@Test
	public void sizeCalculatorWorksForLargeButtons()
	{
		Size size = Size.LARGE;
		
		int width = Sizes.calculateButtonWidth(size);
		int height = Sizes.calculateButtonHeight(size);
		
		assertEquals(300, width);
		assertEquals(30, height);
	}

	@Test
	public void sizeCalculatorWorksForSmallTextField()
	{
		Size size = Size.SMALL;
		
		int width = Sizes.calculateTextFieldWidth(size);
		int height = Sizes.calculateTextFieldHeight(size);
		
		assertEquals(100, width);
		assertEquals(30, height);
	}
	
	@Test
	public void sizeCalculatorWorksForMediumTextField()
	{
		Size size = Size.MEDIUM;
		
		int width = Sizes.calculateTextFieldWidth(size);
		int height = Sizes.calculateTextFieldHeight(size);
		
		assertEquals(150, width);
		assertEquals(30, height);
	}

	@Test
	public void sizeCalculatorWorksForLargeTextField()
	{
		Size size = Size.LARGE;
		
		int width = Sizes.calculateTextFieldWidth(size);
		int height = Sizes.calculateTextFieldHeight(size);
		
		assertEquals(200, width);
		assertEquals(30, height);
	}

	@Test
	public void sizeCalculatorWorksForExtraLargeTextField()
	{
		Size size = Size.X_LARGE;
		
		int width = Sizes.calculateTextFieldWidth(size);
		int height = Sizes.calculateTextFieldHeight(size);
		
		assertEquals(500, width);
		assertEquals(30, height);
	}
}