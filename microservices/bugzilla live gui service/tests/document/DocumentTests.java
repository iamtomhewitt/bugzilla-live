package document;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.common.Folders;
import bugzilla.common.UnitTestStep;
import bugzilla.message.document.ExcelRequest;
import bugzilla.message.document.ReleaseNoteRequest;
import bugzilla.message.document.SubsystemTestRequest;
import bugzilla.message.document.UnitTestRequest;
import bugzilla.utilities.JacksonAdapter;
import message.GuiMessageSender;

public class DocumentTests
{
    private List<Bug> mockBugs;
    
    @After
    @Before
    @SuppressWarnings("unchecked")
    public void clearMessageFolder() throws IOException, JsonTransformationException, ParseException
    {
        for (File f : new File(Folders.MESSAGE_FOLDER).listFiles())
        {
            System.out.println("Deleting: "+f.getAbsolutePath());
            f.delete();
        }
        
        // Setup some mock bugs
        File mock = new File("tests/mocks/document/mock bugs.json");
        String mockContents = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(mockContents);
        this.mockBugs = JacksonAdapter.fromJson(message.get("bugs").toString(), Bug.class);
    }
    
    // Object creation tests
    @Test
    public void testCreateExcelRequestObject() throws JsonTransformationException
    {
        ExcelRequest request = new ExcelRequest(mockBugs);
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "documentrequest");
        assertEquals(request.getFileExtension().endsWith(".documentrequest"), true);
        assertEquals(request.getOperation(), "excel");
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    @Test
    public void testCreateReleaseNoteRequestObject() throws JsonTransformationException
    {
        ReleaseNoteRequest request = new ReleaseNoteRequest.Builder().withClassification("classification")
                                                                    .withDocumentNumber("doc number")
                                                                    .withDocumentTitle("doc title")
                                                                    .withFilename("filename")
                                                                    .withIssue("issue")
                                                                    .withIssueStatus("status")
                                                                    .withBugs(mockBugs)
                                                                    .withSubsystem("subsystem")
                                                                    .build();
        assertNotNull(request);
        assertEquals(request.getMessage(), "documentrequest");
        assertEquals(request.getFileExtension().endsWith(".documentrequest"), true);
        assertEquals(request.getOperation(), "releasenote");
        assertEquals(request.getClassification().isEmpty(), false);
        assertEquals(request.getDocumentTitle().isEmpty(), false);
        assertEquals(request.getFilename().isEmpty(), false);
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    @Test
    public void testCreateSubsystemTestRequestObject() throws JsonTransformationException
    {
        SubsystemTestRequest request = new SubsystemTestRequest.Builder().withClassification("classification")
                                                                    .withDeveloperUsername("username")
                                                                    .withDocumentNumber("doc number")
                                                                    .withDocumentTitle("doc title")
                                                                    .withFilename("filename")
                                                                    .withIssue("issue")
                                                                    .withIssueStatus("status")
                                                                    .withBugs(mockBugs)
                                                                    .withReleaseNumber("release")
                                                                    .withSubsystem("subsystem")
                                                                    .withTestEnvironment("dev")
                                                                    .build();
        assertNotNull(request);
        assertEquals(request.getMessage(), "documentrequest");
        assertEquals(request.getFileExtension().endsWith(".documentrequest"), true);
        assertEquals(request.getOperation(), "subsystemtest");
        assertEquals(request.getClassification().isEmpty(), false);
        assertEquals(request.getDocumentTitle().isEmpty(), false);
        assertEquals(request.getFilename().isEmpty(), false);
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    @Test
    public void testCreateUnitTestRequestObject() throws JsonTransformationException
    {
        List<UnitTestStep> testSteps = new ArrayList<>();
        testSteps.add(new UnitTestStep("step", "action", "result"));
        
        UnitTestRequest request = new UnitTestRequest.Builder().withClassification("classification")
                                                                    .withAim("aim")
                                                                    .withClassification("classification")
                                                                    .withDeveloperUsername("username")
                                                                    .withDocumentTitle("doc title")
                                                                    .withFilename("filename")
                                                                    .withFileLocation("file location")
                                                                    .withTestSteps(testSteps)
                                                                    .withTestEnvironment("dev")
                                                                    .build();
        assertNotNull(request);
        assertEquals(request.getMessage(), "documentrequest");
        assertEquals(request.getFileExtension().endsWith(".documentrequest"), true);
        assertEquals(request.getOperation(), "unittest");
        assertEquals(request.getClassification().isEmpty(), false);
        assertEquals(request.getDocumentTitle().isEmpty(), false);
        assertEquals(request.getFileLocation().isEmpty(), false);
        assertEquals(request.getFilename().isEmpty(), false);
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    // Sending tests
    @Test
    public void testSendExcelRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File(Folders.MESSAGE_FOLDER);
        ExcelRequest request = new ExcelRequest(mockBugs);
        
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".documentrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
    
    @Test
    public void testSendReleaseNoteRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File(Folders.MESSAGE_FOLDER);
        ReleaseNoteRequest request = new ReleaseNoteRequest.Builder().withClassification("classification")
                                                                    .withDocumentNumber("doc number")
                                                                    .withDocumentTitle("doc title")
                                                                    .withFilename("filename")
                                                                    .withIssue("issue")
                                                                    .withIssueStatus("status")
                                                                    .withBugs(mockBugs)
                                                                    .withSubsystem("subsystem")
                                                                    .build();        
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".documentrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
    
    @Test
    public void testSendSubsystemTestRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File(Folders.MESSAGE_FOLDER);
        SubsystemTestRequest request = new SubsystemTestRequest.Builder().withClassification("classification")
                                                                        .withDeveloperUsername("username")
                                                                        .withDocumentNumber("doc number")
                                                                        .withDocumentTitle("doc title")
                                                                        .withFilename("filename")
                                                                        .withIssue("issue")
                                                                        .withIssueStatus("status")
                                                                        .withBugs(mockBugs)
                                                                        .withReleaseNumber("release")
                                                                        .withSubsystem("subsystem")
                                                                        .withTestEnvironment("dev")
                                                                        .build();      
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".documentrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
    
    @Test
    public void testSendUnitTestRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File(Folders.MESSAGE_FOLDER);
        List<UnitTestStep> testSteps = new ArrayList<>();
        testSteps.add(new UnitTestStep("step", "action", "result"));
        
        UnitTestRequest request = new UnitTestRequest.Builder().withClassification("classification")
                                                                    .withAim("aim")
                                                                    .withClassification("classification")
                                                                    .withDeveloperUsername("username")
                                                                    .withDocumentTitle("doc title")
                                                                    .withFilename("filename")
                                                                    .withFileLocation("file location")
                                                                    .withTestSteps(testSteps)
                                                                    .withTestEnvironment("dev")
                                                                    .build();   
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".documentrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }

    // Mock response tests
    @Test
    public void testMockSuccessResponse() throws ParseException, IOException
    {
        File mock = new File("tests/mocks/document/response/success.documentresponse");
        String responseContent = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(responseContent);
        
        assertEquals("yes", message.get("successful").toString());
        assertEquals(false, message.get("filename").toString().isEmpty());
    }
    
    @Test
    public void testMockFailureResponse() throws ParseException, IOException
    {
        File mock = new File("tests/mocks/document/response/failure.documentresponse");
        String responseContent = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(responseContent);
        
        assertEquals("no", message.get("successful").toString());
        assertEquals(false, message.get("failurereason").toString().isEmpty());
    }
}
