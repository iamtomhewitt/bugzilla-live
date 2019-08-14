package bugzilla.message.OR;

import org.json.simple.JSONObject;

public class ChangeORStatusRequest extends ORRequest
{
	private String status;
	private String comment;
	private String number;
	
	public ChangeORStatusRequest()
	{
		// Used for builder
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toJson()
	{
		JSONObject message = new JSONObject();
		
		JSONObject commentObj = new JSONObject();
		commentObj.put("comment", comment);
		
		message.put("message", this.getMessage());
		message.put("operation", this.getOperation());
		message.put("username", this.getUsername());
		message.put("password", this.getPassword());
		message.put("apiKey", this.getApiKey());
		message.put("number", this.number);
		message.put("status", status);
		message.put("comment", commentObj);

		return message.toJSONString();
	}
	
	public static class Builder
	{
		private String number;
		private String status;
		private String comment;
		private String username;
		private String password;
		private String apiKey;

		public Builder withORNumber(String number)
		{
			this.number = number;
			return this;
		}
		
		public Builder withComment(String comment)
		{
			this.comment = comment;
			return this;
		}
		
		public Builder withStatus(String status)
		{
			this.status = status;
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

		public ChangeORStatusRequest build()
		{
			ChangeORStatusRequest request = new ChangeORStatusRequest();
			request.setMessage("orrequest");
			request.setFileExtension(".orrequest");
			request.setOperation("changestatus");
			request.setUsername(this.username);
			request.setPassword(this.password);
			request.setApiKey(this.apiKey);
			request.status = this.status;
			request.comment = this.comment;
			request.number = this.number;
			return request;
		}
	}
}
