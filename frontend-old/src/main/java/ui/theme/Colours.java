package ui.theme;

import javafx.scene.paint.Color;

public class Colours {
	private static String CRITICAL = "#ff0000";
	private static String MAJOR = "#ffc000";
	private static String MINOR = "#ffff00";
	private static String NORMAL = "#91dcff";
	private static String TRIVIAL = "#d0d0d0";

	private static String FIXED = "#00ff00";
	private static String RESOLVED = "#00ff2a";

	private static String WORKS_FOR_ME = "#00ffc6";
	private static String NOFAULT = "#005bff";

	private static String SELECTED = "#000000";

	private static String BLANK = "#ffffff";

	private static String WINDOW = "#242a3a";
	private static String WINDOW_TEXT = "#FFFFFF";

	private static String INFO_PANE_BACKGROUND = "#242a3a";
	private static String INFO_PANE_HEADING = "#587293";
	private static String INFO_PANE_SUBHEADING = "#FFFFFF";

	public static String toHex(Color c) {
		return String.format("#%02X%02X%02X", (int) (c.getRed() * 255), (int) (c.getGreen() * 255), (int) (c.getBlue() * 255));
	}

	public static void setCritical(String s) {
		CRITICAL = s;
	}

	public static void setMajor(String s) {
		MAJOR = s;
	}

	public static void setMinor(String s) {
		MINOR = s;
	}

	public static void setNormal(String s) {
		NORMAL = s;
	}

	public static void setTrivial(String s) {
		TRIVIAL = s;
	}

	public static void setFixed(String s) {
		FIXED = s;
	}

	public static void setResolved(String s) {
		RESOLVED = s;
	}

	public static void setWorksForMe(String s) {
		WORKS_FOR_ME = s;
	}

	public static void setNoFault(String s) {
		NOFAULT = s;
	}

	public static void setSelected(String s) {
		SELECTED = s;
	}

	public static void setBlank(String s) {
		BLANK = s;
	}

	public static void setWindow(String s) {
		WINDOW = s;
	}

	public static void setWindowText(String s) {
		WINDOW_TEXT = s;
	}

	public static void setInfoPaneBackground(String s) {
		INFO_PANE_BACKGROUND = s;
	}

	public static void setInfoPaneHeading(String s) {
		INFO_PANE_HEADING = s;
	}

	public static void setInfoPaneSubheading(String s) {
		INFO_PANE_SUBHEADING = s;
	}

	public static String getCritical() {
		return CRITICAL;
	}

	public static String getMajor() {
		return MAJOR;
	}

	public static String getMinor() {
		return MINOR;
	}

	public static String getNormal() {
		return NORMAL;
	}

	public static String getTrivial() {
		return TRIVIAL;
	}

	public static String getFixed() {
		return FIXED;
	}

	public static String getResolved() {
		return RESOLVED;
	}

	public static String getWorksForMe() {
		return WORKS_FOR_ME;
	}

	public static String getNoFault() {
		return NOFAULT;
	}

	public static String getSelected() {
		return SELECTED;
	}

	public static String getBlank() {
		return BLANK;
	}

	public static String getWindow() {
		return WINDOW;
	}

	public static String getWindowText() {
		return WINDOW_TEXT;
	}

	public static String getInfoPaneBackground() {
		return INFO_PANE_BACKGROUND;
	}

	public static String getInfoPaneHeading() {
		return INFO_PANE_HEADING;
	}

	public static String getInfoPaneSubheading() {
		return INFO_PANE_SUBHEADING;
	}
}