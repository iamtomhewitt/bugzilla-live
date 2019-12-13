package common.bug;

import java.nio.charset.StandardCharsets;

/**
 * Data class for a bug comment.
 *
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class BugComment
{
	private String creator;
	private String time;
	private String text;

	public BugComment()
	{
		// Used for Jackson
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		text = text.replaceAll("�€�", "'");
		text = text.replaceAll("�€“", "-");
		this.text = new String(text.getBytes(), StandardCharsets.UTF_8);
	}
}
