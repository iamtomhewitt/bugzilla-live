package bugzilla.common.bug;

import java.nio.charset.StandardCharsets;

/**
 * Data class for a bug comment.
 *
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class BugComment
{
	private String commenter;
	private String time;
	private String comment;

	public BugComment()
	{
		// Used for Jackson
	}

	public String getCommenter()
	{
		return commenter;
	}

	public void setCommenter(String commenter)
	{
		this.commenter = commenter;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		comment = comment.replaceAll("�€�", "'");
		comment = comment.replaceAll("�€“", "-");
		this.comment = new String(comment.getBytes(), StandardCharsets.UTF_8);
	}
}
