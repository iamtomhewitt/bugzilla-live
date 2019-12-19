package gui.app.theme;

public class Sizes
{
	public final static int BUTTON_WIDTH_SMALL		= 100;
	public final static int BUTTON_WIDTH_MEDIUM 	= 200;
	public final static int BUTTON_WIDTH_LARGE 		= 300;
	public final static int BUTTON_HEIGHT_SMALL 	= 30;

	public final static int INPUT_WIDTH_SMALL 		= 100;
	public final static int INPUT_WIDTH_MEDIUM 		= 150;
	public final static int INPUT_WIDTH_LARGE 		= 200;
	public final static int INPUT_WIDTH_X_LARGE 	= 500;
	public final static int INPUT_HEIGHT_SMALL 		= 30;
	
	public final static int WRAPPING_WIDTH_MEDIUM 	= 350;
	
	public final static int ROW_SIZE_SMALL			= 25;
	public final static int ROW_SIZE_MEDIUM			= 50;
	public final static int ROW_SIZE_LARGE			= 70;
	
	public final static int FONT_SIZE_SMALL			= 12;
	public final static int FONT_SIZE_MEDIUM		= 20;
	public final static int FONT_SIZE_LARGE			= 30;
	
	public enum Size
	{
		SMALL, MEDIUM, LARGE, X_LARGE
	}
	
	public static int calculateButtonHeight(Size size)
	{
		return Sizes.BUTTON_HEIGHT_SMALL;
	}

	public static int calculateButtonWidth(Size size)
	{
		switch (size)
		{
			case SMALL:
					return Sizes.BUTTON_WIDTH_SMALL;

			case MEDIUM:
					return Sizes.BUTTON_WIDTH_MEDIUM;

			case LARGE:
					return Sizes.BUTTON_WIDTH_LARGE;

			default:
					return Sizes.BUTTON_WIDTH_MEDIUM;
		}
	}
	
	public static int calculateTextFieldHeight(Size size)
	{
		return Sizes.INPUT_HEIGHT_SMALL;
	}

	public static int calculateTextFieldWidth(Size size)
	{
		switch (size)
		{
			case SMALL:
					return Sizes.INPUT_WIDTH_SMALL;

			case MEDIUM:
					return Sizes.INPUT_WIDTH_MEDIUM;

			case LARGE:
					return Sizes.INPUT_WIDTH_LARGE;

			case X_LARGE:
					return Sizes.INPUT_WIDTH_X_LARGE;

			default:
					return Sizes.INPUT_WIDTH_MEDIUM;
		}
	}
	
}
