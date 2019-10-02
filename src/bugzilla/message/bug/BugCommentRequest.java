package bugzilla.message.bug;

import org.json.simple.JSONObject;

/**
 * A request to add a comment to a bug.
 * 
 * @author Tom Hewitt
 */
public class BugCommentRequest extends BugRequest
{
	private String number;
	private String comment;

	private BugCommentRequest()
	{
		// Used for builder
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONObject message = new JSONObject();
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("number", this.number);
		message.put("comment", this.comment);
		message.put("username", this.getUsername());
		message.put("password", this.getPassword());
		message.put("apiKey", this.getApiKey());

		return message.toJSONString();
	}

	public static class Builder
	{
		private String number;
		private String comment;
		private String username;
		private String password;
		private String apiKey;

		public Builder withBugNumber(String number)
		{
			this.number = number;
			return this;
		}
		
		public Builder withComment(String comment)
		{
			this.comment = comment;
			return this;
		}
		
		public Builder withUsername(String username)
		{
			this.username = username;
			return this;
		}
		
		public Builder withPassword(String password)
		{
			this.password = password;
			return this;
		}
		
		public Builder withApiKey(String apiKey)
		{
			this.apiKey = apiKey;
			return this;
		}

		public BugCommentRequest build()
		{
			BugCommentRequest request = new BugCommentRequest();
			request.setMessage("bugrequest");
			request.setFileExtension(".bugrequest");
			request.setOperation("addcomment");
			request.setUsername(this.username);
			request.setPassword(this.password);
			request.setApiKey(this.apiKey);
			request.number = this.number;
			request.comment = this.comment;
			return request;
		}
	}
}
