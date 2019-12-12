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
	public static final String ICON 		= "Icon.png";
	public static final String BACKGROUND 	= "Background.png";
	public static final String FIREFOX 		= "Firefox.png";
	public static final String ADD 			= "Add.png";
	public static final String REMOVE 		= "Remove.png";
	public static final String HELP 		= "Help.png";
	public static final String LISTS 		= "Lists.png";
	public static final String REFRESH 		= "Refresh.png";
	public static final String EXCEL 		= "Excel.png";
	public static final String COMMENT		= "Comment.png";
	public static final String ABOUT		= "About.png";
	public static final String THEME		= "Theme.png";
	public static final String CHANGE_STATUS= "Change Status.png";

	public static final int ICON_SIZE = 25;
	
	public ImageView createThemeIcon()
	{
		return createIcon(new Icons().THEME);
	}
	
	public ImageView createAddIcon()
	{
		return createIcon(new Icons().ADD);
	}
	
	public ImageView createChangeStatusIcon()
	{
		return createIcon(new Icons().CHANGE_STATUS);
	}
	
	public ImageView createRefreshIcon()
	{
		return createIcon(new Icons().REFRESH);
	}
	
	public ImageView createHelpIcon()
	{
		return createIcon(new Icons().HELP);
	}
	
	public ImageView createExcelIcon()
	{
		return createIcon(new Icons().EXCEL);
	}

	public ImageView createBugzillaIcon()
	{
		return createIcon(new Icons().ICON);
	}
	
	public ImageView createListIcon()
	{
		return createIcon(new Icons().LISTS);
	}
	
	public ImageView createFirefoxIcon()
	{
		return createIcon(new Icons().FIREFOX);
	}
	
	public ImageView createRemoveIcon()
	{
		return createIcon(new Icons().REMOVE);
	}
	
	public ImageView createCommentIcon()
	{
		return createIcon(new Icons().COMMENT);
	}
	
	public ImageView createAboutIcon()
	{
		return createIcon(new Icons().ABOUT);
	}
	
	private ImageView createIcon(String icon)
	{
		String url = getClass().getResource("images/"+icon).toExternalForm();
		Image iconImage = new Image(url);
		ImageView view = new ImageView(iconImage);
		view.setFitHeight(new Icons().ICON_SIZE);
		view.setFitWidth(new Icons().ICON_SIZE);
		return view;
	}
}