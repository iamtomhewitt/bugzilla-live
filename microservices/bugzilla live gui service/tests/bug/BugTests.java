package bug;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bugzilla.common.OR.Bug;
import bugzilla.common.OR.BugAttachment;
import bugzilla.common.OR.BugComment;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageSenderException;
import bugzilla.common.Folders;
import bugzilla.message.OR.ChangeBugStatusRequest;
import bugzilla.message.OR.BugCommentRequest;
import bugzilla.message.OR.BugDetailRequest;
import bugzilla.message.OR.BugsRequest;
import bugzilla.message.OR.SubsystemBugsRequest;
import bugzilla.message.OR.UserBugsRequest;
import bugzilla.utilities.JacksonAdapter;
import message.GuiMessageSender;

@SuppressWarnings("unchecked")
public class BugTests
{
	private static final String USERNAME = "tom.hewitt";
	private static final String PASSWORD = "password";
	private static final String API_KEY = "key";
	
	// Object creation tests
	@After
	@Before
	public void clearMessageFolder()
	{
	    for (File f : new File(Folders.MESSAGE_FOLDER).listFiles())
	    {
	        System.out.println("Deleting: "+f.getAbsolutePath());
            f.delete();
	    }
	}
	
	@Test
	public void testCreateChangeORStatusRequestObject()
	{
	    ChangeBugStatusRequest request = new ChangeBugStatusRequest.Builder().withApiKey(API_KEY)
	                                                                        .withComment("Comment")
	                                                                        .withBugNumber("12345")
	                                                                        .withPassword(PASSWORD)
	                                                                        .withStatus("Diagnosed")
	                                                                        .withUsername(USERNAME)
	                                                                        .build();
	    assertNotNull(request);
        assertEquals(request.getMessage(), "orrequest");
        assertEquals(request.getFileExtension().endsWith(".orrequest"), true);
        assertEquals(request.getOperation(), "changestatus");
        assertEquals(request.getUsername(), USERNAME);
        assertEquals(request.getPassword(), PASSWORD);
        assertEquals(request.getApiKey(), API_KEY);
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
	}
	
	@Test
	public void testCreateORCommentRequestObject()
    {
        BugCommentRequest request = new BugCommentRequest.Builder().withApiKey(API_KEY)
                                                                    .withComment("A comment.")
                                                                    .withBugNumber("12345")
                                                                    .withPassword(PASSWORD)
                                                                    .withUsername(USERNAME)
                                                                    .build();                                
        assertNotNull(request);
        assertEquals(request.getMessage(), "orrequest");
        assertEquals(request.getFileExtension().endsWith(".orrequest"), true);
        assertEquals(request.getOperation(), "addcomment");
        assertEquals(request.getUsername(), USERNAME);
        assertEquals(request.getPassword(), PASSWORD);
        assertEquals(request.getApiKey(), API_KEY);
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
	
	@Test
    public void testCreateORDetailRequestObject()
    {
        BugDetailRequest request = new BugDetailRequest("12345", USERNAME, PASSWORD, API_KEY);
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "orrequest");
        assertEquals(request.getFileExtension().endsWith(".orrequest"), true);
        assertEquals(request.getOperation(), "detail");
        assertEquals(request.getUsername(), USERNAME);
        assertEquals(request.getPassword(), PASSWORD);
        assertEquals(request.getApiKey(), API_KEY);
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
	
	@Test
	public void testCreateORsRequestObject()
	{
	    List<String> numbers = Arrays.asList("12345", "22345");
	    
	    BugsRequest request = new BugsRequest(numbers, USERNAME, PASSWORD, API_KEY);
	    assertNotNull(request);
        assertEquals(request.getMessage(), "orrequest");
        assertEquals(request.getFileExtension().endsWith(".orrequest"), true);
        assertEquals(request.getOperation(), "numbers");
        assertEquals(request.getUsername(), USERNAME);
        assertEquals(request.getPassword(), PASSWORD);
        assertEquals(request.getApiKey(), API_KEY);
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
	}
	
	@Test
    public void testCreateSubsystemORsRequestObject()
    {
        SubsystemBugsRequest request = new SubsystemBugsRequest("Subsystem", USERNAME, PASSWORD, API_KEY);
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "orrequest");
        assertEquals(request.getFileExtension().endsWith(".orrequest"), true);
        assertEquals(request.getOperation(), "subsystem");
        assertEquals(request.getUsername(), USERNAME);
        assertEquals(request.getPassword(), PASSWORD);
        assertEquals(request.getApiKey(), API_KEY);
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
	
	@Test
    public void testCreateUserORsRequestObject()
    {
        UserBugsRequest request = new UserBugsRequest(USERNAME, USERNAME, PASSWORD, API_KEY);
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "orrequest");
        assertEquals(request.getFileExtension().endsWith(".orrequest"), true);
        assertEquals(request.getOperation(), "user");
        assertEquals(request.getUsername(), USERNAME);
        assertEquals(request.getPassword(), PASSWORD);
        assertEquals(request.getApiKey(), API_KEY);
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
	
	
	// Sending tests
	@Test
	public void testSendChangeORStatusRequest() throws JsonTransformationException, MessageSenderException
	{
	    File folder = new File(Folders.MESSAGE_FOLDER);
	    ChangeBugStatusRequest request = new ChangeBugStatusRequest.Builder().withApiKey(API_KEY)
                                                                            .withComment("Comment")
                                                                            .withBugNumber("12345")
                                                                            .withPassword(PASSWORD)
                                                                            .withStatus("Diagnosed")
                                                                            .withUsername(USERNAME)
                                                                            .build();
        new GuiMessageSender().sendRequestMessage(request);
	    
	    assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".orrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
	}
	
	@Test
	public void testSendORCommentRequest() throws JsonTransformationException, MessageSenderException
	{
	    File folder = new File(Folders.MESSAGE_FOLDER);
	    BugCommentRequest request = new BugCommentRequest.Builder().withApiKey(API_KEY)
                                                                .withComment("A comment.")
                                                                .withBugNumber("12345")
                                                                .withPassword(PASSWORD)
                                                                .withUsername(USERNAME)
                                                                .build();       
	    new GuiMessageSender().sendRequestMessage(request);
	       
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".orrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
	}
	
	@Test
    public void testSendORDetailRequest() throws JsonTransformationException, MessageSenderException
    {
	    File folder = new File(Folders.MESSAGE_FOLDER);
	    BugDetailRequest request = new BugDetailRequest("12345", USERNAME, PASSWORD, API_KEY);      

	    new GuiMessageSender().sendRequestMessage(request);
        
	    assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".orrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
	
	@Test
	public void testSendORsRequest() throws JsonTransformationException, MessageSenderException
    {
	    File folder = new File(Folders.MESSAGE_FOLDER);
	    List<String> numbers = Arrays.asList("12345", "22345");        
        BugsRequest request = new BugsRequest(numbers, USERNAME, PASSWORD, API_KEY);    
        
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".orrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
	
	@Test
    public void testSendSusbsystemORsRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File(Folders.MESSAGE_FOLDER);
        SubsystemBugsRequest request = new SubsystemBugsRequest("Subsystem", USERNAME, PASSWORD, API_KEY);  
        
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".orrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
	
	@Test
    public void testSendUserORsRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File(Folders.MESSAGE_FOLDER);
        UserBugsRequest request = new UserBugsRequest(USERNAME, USERNAME, PASSWORD, API_KEY);
        
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".orrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
	
	
	// Mock response tests
    @Test
	public void testMockORsSuccessResponse() throws JsonTransformationException, ParseException, IOException
	{
	    File mock = new File("tests/mocks/OR/response/ors.orresponse");
	    
	    String responseContent = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(responseContent);
        List<Bug> ors = JacksonAdapter.fromJson(message.get("ORs").toString(), Bug.class);

        assertNotNull(ors);
        assertEquals(ors.isEmpty(), false);
	}
    
    @Test
    public void testMockORsFailureResponse() throws ParseException, IOException
    {
        File mock = new File("tests/mocks/OR/response/ors failure.orresponse");
        JSONObject message = (JSONObject) new JSONParser().parse(new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath()))));

        String operation = message.get("operation").toString();
        String successful = message.get("successful").toString();   
        String reason = message.get("failurereason").toString();
        
        assertEquals(operation, "notification");
        assertEquals(successful, "no");
        assertEquals(reason.isEmpty(), false);
    }
    
    @Test
    public void testMockORDetailSuccessResponse() throws IOException, JsonTransformationException, ParseException
    {
        File mock = new File("tests/mocks/OR/response/detail.orresponse");
        
        String responseContent = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(responseContent);
        List<BugAttachment> attachments = JacksonAdapter.fromJson(message.get("attachments").toString(), BugAttachment.class);
        List<BugComment> comments    = JacksonAdapter.fromJson(message.get("comments").toString(), BugComment.class);

        assertNotNull(attachments);
        assertEquals(attachments.isEmpty(), false);
        assertNotNull(comments);
        assertEquals(comments.isEmpty(), false);
    }
    
    @Test
    public void testMockORDetailFailureResponse() throws ParseException, IOException
    {
        File mock = new File("tests/mocks/OR/response/detail failure.orresponse");
        JSONObject message = (JSONObject) new JSONParser().parse(new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath()))));
        
        String operation = message.get("operation").toString();
        String successful = message.get("successful").toString();   
        String reason = message.get("failurereason").toString();
        
        assertEquals(operation, "notification");
        assertEquals(successful, "no");
        assertEquals(reason.isEmpty(), false);
    }
}
