package message;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import bugzilla.common.message.MessageReceiver;
import bugzilla.exception.GenerateDocumentException;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageReceiverException;
import bugzilla.common.bug.Bug;
import bugzilla.common.UnitTestStep;
import bugzilla.utilities.JacksonAdapter;

import generator.ExcelGenerator;
import generator.ReleaseNoteGenerator;
import generator.SubsystemTestGenerator;
import generator.UnitTestGenerator;

import log.DocumentLogger;

/**
 * Processes document requests received in the message folder, processes them and sends a response message containing the file path of the generated document.
 * 
 * @author Tom Hewitt
 * @since 2.3.0
 */
@SuppressWarnings("unchecked")
public class DocumentReceiver extends MessageReceiver
{	
	public DocumentReceiver()
	{
		this.setFileTypes(Arrays.asList(".documentrequest"));
	}

	@Override
	public void processMessage(File file) throws MessageReceiverException
	{
		try
		{
			while(!file.renameTo(file)) 
		        Thread.sleep(10);
			
			String contents = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())), StandardCharsets.UTF_8);

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(contents);

			String operation = jsonObject.get("operation").toString();
			
			DocumentLogger.getInstance().print("Message received");
			
			switch(operation)
			{
				case "releasenote":
				{
					String subsystem		= jsonObject.get("subsystem").toString();
					String classification 	= jsonObject.get("classification").toString();
					String documentTitle 	= jsonObject.get("documenttitle").toString();
					String documentNumber 	= jsonObject.get("documentnumber").toString();
					String issue 			= jsonObject.get("issue").toString();
					String issueDate 		= jsonObject.get("issuedate").toString();
					String issueStatus 		= jsonObject.get("issuestatus").toString();
					String filename 		= jsonObject.get("filename").toString();

					List<Bug> bugs = JacksonAdapter.fromJson(jsonObject.get("bugs").toString(), Bug.class);
					
					ReleaseNoteGenerator r = new ReleaseNoteGenerator.Builder().withClassification(classification)
																				.withDocumentNumber(documentNumber)
																				.withDocumentTitle(documentTitle)
																				.withFilename(filename)
																				.withIssue(issue)
																				.withIssueDate(issueDate)
																				.withIssueStatus(issueStatus)
																				.withBugs(bugs)
																				.build();							
					String filePath = "";
					
					switch (subsystem)
					{
						case "CRM":
							DocumentLogger.getInstance().print("Generating CRM release note");
							filePath = r.generateCrmReleaseNote();
							break;
							
						default:
							DocumentLogger.getInstance().print("Unrecognised subsystem: " + subsystem);
							DocumentSender.sendResponseMessage(false, "Unrecognised subsystem: " + subsystem, "");
							return;
					}
					
					DocumentSender.sendResponseMessage(true, "", filePath);
					break;
				}
					
				case "excel":
				{
					List<Bug> bugs = JacksonAdapter.fromJson(jsonObject.get("Bugs").toString(), Bug.class);
					String filePath = ExcelGenerator.generateExcelDocument(bugs);
					DocumentSender.sendResponseMessage(true, "", filePath);
					break;
				}
					
				case "unittest":
				{
					String classification 	= jsonObject.get("classification").toString();
					String documentTitle 	= jsonObject.get("documenttitle").toString();
					String fileLocation 	= jsonObject.get("filelocation").toString();
					String filename 		= jsonObject.get("filename").toString();
					String developerUsername= jsonObject.get("developerusername").toString();
					String aim				= jsonObject.get("aim").toString();
					String testEnvironment	= jsonObject.get("testenvironment").toString();

					List<UnitTestStep> testSteps = JacksonAdapter.fromJson(jsonObject.get("testSteps").toString(), UnitTestStep.class);
					String filePath = new UnitTestGenerator.Builder().withAim(aim)
																	.withClassification(classification)
																	.withDeveloperUsername(developerUsername)
																	.withDocumentTitle(documentTitle)
																	.withFileLocation(fileLocation)
																	.withFilename(filename)
																	.withTestEnvironment(testEnvironment)
																	.withUnitTestSteps(testSteps)
																	.build()
																	.generateUnitTest();												
									
					DocumentSender.sendResponseMessage(true, "", filePath);
					break;
				}
				
				case "subsystemtest":
				{
					String classification 	= jsonObject.get("classification").toString();
					String documentTitle 	= jsonObject.get("documenttitle").toString();
					String documentNumber 	= jsonObject.get("documentnumber").toString();
					String fileLocation 	= jsonObject.get("filelocation").toString();
					String filename 		= jsonObject.get("filename").toString();
					String testEnvironment	= jsonObject.get("testenvironment").toString();
					String issue 			= jsonObject.get("issue").toString();
					String issueStatus 		= jsonObject.get("issuestatus").toString();
					String developerName	= jsonObject.get("developerusername").toString();
					String releaseNumber	= jsonObject.get("releasenumber").toString();
					List<Bug> bugs = JacksonAdapter.fromJson(jsonObject.get("bugs").toString(), Bug.class);

					String filePath = new SubsystemTestGenerator.Builder().withClassification(classification)
																			.withDeveloperName(developerName)
																			.withDocumentNumber(documentNumber)
																			.withDocumentTitle(documentTitle)
																			.withFileLocation(fileLocation)
																			.withFilename(filename)
																			.withIssue(issue)
																			.withIssueStatus(issueStatus)
																			.withBugs(bugs)
																			.withReleaseNumber(releaseNumber)
																			.withTestEnvironment(testEnvironment)
																			.build()
																			.generateSubsystemTest();
					
					DocumentSender.sendResponseMessage(true, "", filePath);
					break;
				}
					
				default:
					DocumentLogger.getInstance().print("Unknown operation: " + operation);
					DocumentSender.sendResponseMessage(false, "Unknown operation: " + operation, "");
					break;
			}
		}
		catch (JsonTransformationException | GenerateDocumentException e)
		{
			DocumentLogger.getInstance().printStackTrace(e);
			DocumentSender.sendResponseMessage(false, e.getMessage(), "");
		} 
		catch (Exception e)
		{
			throw new MessageReceiverException(e.getMessage());
		}
	}	
}
