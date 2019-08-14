package bugzilla.message.document;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import bugzilla.common.UnitTestStep;
import bugzilla.exception.JsonTransformationException;
import bugzilla.utilities.JacksonAdapter;

/**
 * A request message to create a unit test document.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public class UnitTestRequest extends DocumentRequest
{
	private String developerUsername;
	private String aim;
	private String testEnvironment;
	
	private List<UnitTestStep> testSteps;
	
	private UnitTestRequest()
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
			message.put("filelocation", this.getFileLocation());
			message.put("filename", this.getFilename());
			message.put("documenttitle", this.getDocumentTitle());
			message.put("classification", this.getClassification());
			message.put("developerusername", this.developerUsername);
			message.put("aim", this.aim);
			message.put("testenvironment", this.testEnvironment);
	
			if (this.testSteps != null)
			{
				String testStepsAsJson = JacksonAdapter.toJson(this.testSteps);
	
				JSONParser parser = new JSONParser();
				JSONArray testStepsArray = (JSONArray) parser.parse(testStepsAsJson);
	
				message.put("testSteps", testStepsArray);
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
		private String fileLocation;
		private String filename;
		private String documentTitle;
		private String classification;
		private String developerUsername;
		private String aim;
		private String testEnvironment;
		
		private List<UnitTestStep> testSteps;
		
		public Builder withFileLocation(String fileLocation)
		{
			this.fileLocation = fileLocation;
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
		
		public Builder withDeveloperUsername(String developerUsername)
		{
			this.developerUsername = developerUsername;
			return this;
		}
		
		public Builder withAim(String aim)
		{
			this.aim = aim;
			return this;
		}
		
		public Builder withTestEnvironment(String testEnvironment)
		{
			this.testEnvironment = testEnvironment;
			return this;
		}
		
		public Builder withTestSteps(List<UnitTestStep> testSteps)
		{
			this.testSteps = testSteps;
			return this;
		}
		
		public UnitTestRequest build()
		{
			UnitTestRequest request = new UnitTestRequest();
			request.setMessage("documentrequest");
			request.setFileExtension(".documentrequest");
			request.setOperation("unittest");
			request.setFileLocation(this.fileLocation);
			request.setFilename(this.filename);
			request.setDocumentTitle(this.documentTitle);
			request.setClassification(this.classification);
			request.testSteps = this.testSteps;
			request.developerUsername = this.developerUsername;
			request.aim = this.aim;
			request.testEnvironment = this.testEnvironment;
			return request;
		}	
	}
}
