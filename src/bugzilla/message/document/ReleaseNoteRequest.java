package bugzilla.message.document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import bugzilla.common.OR.OR;
import bugzilla.exception.JsonTransformationException;
import bugzilla.utilities.JacksonAdapter;

/**
 * A request message to create a release note.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class ReleaseNoteRequest extends DocumentRequest
{
	private String subsystem;
	private String documentNumber;
	private String issue;
	private String issueStatus;
	
	private List<OR> ors;
	
	private ReleaseNoteRequest()
	{
		// Used for builder
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toJson() throws JsonTransformationException
	{
		try
		{
			JSONObject message = new JSONObject();
			message.put("message", this.getMessage());
			message.put("operation", this.getOperation());
			message.put("subsystem", this.subsystem);
			message.put("filelocation", this.getFileLocation());
			message.put("filename", this.getFilename());
			message.put("documenttitle", this.getDocumentTitle());
			message.put("documentnumber", this.documentNumber);
			message.put("classification", this.getClassification());
			message.put("issue", this.issue);
			message.put("issuestatus", this.issueStatus);
			message.put("issuedate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			message.put("subsystem", this.subsystem);
	
			if (this.ors != null)
			{
				String ORsAsJson = JacksonAdapter.toJson(this.ors);
	
				// First parse ORsAsJson so that it formats correctly when it is written to the file
				JSONParser parser = new JSONParser();
				JSONArray ORs = (JSONArray) parser.parse(ORsAsJson);
	
				message.put("ORs", ORs);
			}
	
			return message.toJSONString();
		}
		catch (Exception e)
		{
			throw new JsonTransformationException(e.getMessage());
		}
	}
	
	public static class Builder
	{
		private String subsystem;
		private String documentNumber;
		private String issue;
		private String issueStatus;
		private String filename;
		private String documentTitle;
		private String classification;
		
		private List<OR> ors;
		
		public Builder withSubsystem(String subsystem)
		{
			this.subsystem = subsystem;
			return this;
		}
		
		public Builder withDocumentNumber(String documentNumber)
		{
			this.documentNumber = documentNumber;
			return this;
		}
		
		public Builder withIssue(String issue)
		{
			this.issue = issue;
			return this;
		}
		
		public Builder withIssueStatus(String issueStatus)
		{
			this.issueStatus = issueStatus;
			return this;
		}
		
		public Builder withFilename(String filename)
		{
			this.filename = filename;
			return this;
		}
		
		public Builder withDocumentTitle(String documentTitle)
		{
			this.documentTitle = documentTitle;
			return this;
		}
		
		public Builder withClassification(String classification)
		{
			this.classification = classification;
			return this;
		}
		
		public Builder withORs(List<OR> ors)
		{
			this.ors = ors;
			return this;
		}
		
		public ReleaseNoteRequest build()
		{
			ReleaseNoteRequest request = new ReleaseNoteRequest();
			request.setMessage("documentrequest");
			request.setFileExtension(".documentrequest");
			request.setOperation("releasenote");
			request.setClassification(this.classification);
			request.setFilename(this.filename);
			request.setDocumentTitle(this.documentTitle);
			request.documentNumber = this.documentNumber;
			request.issue = this.issue;
			request.issueStatus = this.issueStatus;
			request.ors = this.ors;
			request.subsystem = this.subsystem;
			return request;
		}
	}
}
