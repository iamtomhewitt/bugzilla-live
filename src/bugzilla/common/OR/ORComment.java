package bugzilla.common.OR;

import java.nio.charset.StandardCharsets;

/**
 * Data class for an OR comment.
 *
 * @author Tom Hewitt
 * @since 2.0.0
 */
public class ORComment
{
	private String commenter;
	private String time;
	private String comment;

	public ORComment()
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
