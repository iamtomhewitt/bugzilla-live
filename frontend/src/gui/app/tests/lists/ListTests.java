package gui.app.tests.lists;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import common.Folders;
import common.exception.JsonTransformationException;
import common.exception.MessageSenderException;
import common.message.list.CreateListRequest;
import common.message.list.DeleteListRequest;
import common.message.list.ModifyListRequest;
import gui.app.message.GuiMessageSender;

public class ListTests
{
    @After
    @Before
    public void clearMessageFolder()
    {
        for (File f : new File(Folders.MESSAGE_FOLDER).listFiles())
        {
            System.out.println("Deleting: " + f.getAbsolutePath());
            f.delete();
        }
    }
    
    // Object creation tests
    @Test
    public void testCreateCreateListRequestObject()
    {
        CreateListRequest request = new CreateListRequest("List Name", "12345");
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "listrequest");
        assertEquals(request.getFileExtension().endsWith(".listrequest"), true);
        assertEquals(request.getOperation(), "create");
        assertEquals(request.getFilename(), "List Name");
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    @Test
    public void testCreateModifyListRequestObject()
    {
        ModifyListRequest request = new ModifyListRequest("List Name", "12346", "12345");
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "listrequest");
        assertEquals(request.getFileExtension().endsWith(".listrequest"), true);
        assertEquals(request.getOperation(), "modify");
        assertEquals(request.getFilename(), "List Name");
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    @Test
    public void testCreateDeleteListRequestObject()
    {
        DeleteListRequest request = new DeleteListRequest("List Name");
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "listrequest");
        assertEquals(request.getFileExtension().endsWith(".listrequest"), true);
        assertEquals(request.getOperation(), "delete");
        assertEquals(request.getFilename(), "List Name");
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    // Sending tests
    @Test
    public void testSendCreateListRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File(Folders.MESSAGE_FOLDER);
        
        CreateListRequest request = new CreateListRequest("List Name", "12345");      
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".listrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
    
    @Test
    public void testSendModifyListRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File(Folders.MESSAGE_FOLDER);
        
        ModifyListRequest request = new ModifyListRequest("List Name", "12345", "12347");      
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".listrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
    
    @Test
    public void testSendDeleteListRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File(Folders.MESSAGE_FOLDER);
        
        DeleteListRequest request = new DeleteListRequest("List Name");      
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".listrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }

    // Mock response tests
    @Test
    public void testMockListSuccessResponse() throws ParseException, IOException
    {
        File mock = new File("tests/mocks/list/response/success.listresponse");
     
        String responseContent = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(responseContent);
        
        assertEquals("notification", message.get("operation").toString());
        assertEquals("yes", message.get("successful").toString());
    }
    
    @Test
    public void testMockListFailureResponse() throws ParseException, IOException
    {
        File mock = new File("tests/mocks/list/response/failure.listresponse");
     
        String responseContent = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(responseContent);
        
        assertEquals("notification", message.get("operation").toString());
        assertEquals("no", message.get("successful").toString());
        assertEquals(false, message.get("failurereason").toString().isEmpty());
    }
    
}
