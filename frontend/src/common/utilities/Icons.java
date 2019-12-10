package common.utilities;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Icon locations and creation methods for all icons in the application.
 * 
 * @author Tom Hewitt
 * @since 1.0.0
 */
public class Icons
{
	public static final String ICON 		= "file:"+"Icon.png";
	public static final String BACKGROUND 	= "file:"+"Background.png";
	public static final String FIREFOX 		= "file:"+"Firefox.png";
	public static final String ADD 			= "file:"+"Add.png";
	public static final String REMOVE 		= "file:"+"Remove.png";
	public static final String HELP 		= "file:"+"Help.png";
	public static final String LISTS 		= "file:"+"Lists.png";
	public static final String REFRESH 		= "file:"+"Refresh.png";
	public static final String EXCEL 		= "file:"+"Excel.png";
	public static final String COMMENT		= "file:"+"Comment.png";
	public static final String ABOUT		= "file:"+"About.png";
	public static final String THEME		= "file:"+"Theme.png";
	public static final String CHANGE_STATUS= "file:"+"Change Status.png";

	public static final int ICON_SIZE = 25;
	
	public static ImageView createThemeIcon()
	{
		return createIcon(Icons.THEME);
	}
	
	public static ImageView createAddIcon()
	{
		return createIcon(Icons.ADD);
	}
	
	public static ImageView createChangeStatusIcon()
	{
		return createIcon(Icons.CHANGE_STATUS);
	}
	
	public static ImageView createRefreshIcon()
	{
		return createIcon(Icons.REFRESH);
	}
	
	public static ImageView createHelpIcon()
	{
		return createIcon(Icons.HELP);
	}
	
	public static ImageView createExcelIcon()
	{
		return createIcon(Icons.EXCEL);
	}

	public static ImageView createBugzillaIcon()
	{
		return createIcon(Icons.ICON);
	}
	
	public static ImageView createListIcon()
	{
		return createIcon(Icons.LISTS);
	}
	
	public static ImageView createFirefoxIcon()
	{
		return createIcon(Icons.FIREFOX);
	}
	
	public static ImageView createRemoveIcon()
	{
		return createIcon(Icons.REMOVE);
	}
	
	public static ImageView createCommentIcon()
	{
		return createIcon(Icons.COMMENT);
	}
	
	public static ImageView createAboutIcon()
	{
		return createIcon(Icons.ABOUT);
	}
	
	private static ImageView createIcon(String icon)
	{
		Image iconImage = new Image(icon);
		ImageView view = new ImageView(iconImage);
		view.setFitHeight(Icons.ICON_SIZE);
		view.setFitWidth(Icons.ICON_SIZE);
		return view;
	}
}
