package gui.app.tests.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import common.exception.JsonTransformationException;
import common.exception.MessageSenderException;
import common.message.config.ApplicationGetRequest;
import common.message.config.ApplicationSaveRequest;
import common.message.config.UserGetRequest;
import common.message.config.UserSaveRequest;
import gui.app.message.GuiMessageSender;

public class ConfigTests
{
    @After
    @Before
    public void clearMessageFolder()
    {
        for (File f : new File("").listFiles())
        {
            System.out.println("Deleting: "+f.getAbsolutePath());
            f.delete();
        }
    }
    
    // Object creation tests
    @Test
    public void testCreateApplicationGetRequestObject()
    {
        ApplicationGetRequest request = new ApplicationGetRequest();
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "configrequest");
        assertEquals(request.getFileExtension().endsWith(".configrequest"), true);
        assertEquals(request.getOperation(), "get");
        assertEquals(request.getPropertyType(), "application");
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    @Test
    public void testCreateUserGetRequestObject()
    {
        UserGetRequest request = new UserGetRequest();
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "configrequest");
        assertEquals(request.getFileExtension().endsWith(".configrequest"), true);
        assertEquals(request.getOperation(), "get");
        assertEquals(request.getPropertyType(), "user");
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    @Test
    public void testCreateApplicationSaveRequestObject()
    {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("refreshrate", "30");
        
        ApplicationSaveRequest request = new ApplicationSaveRequest(properties);
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "configrequest");
        assertEquals(request.getFileExtension().endsWith(".configrequest"), true);
        assertEquals(request.getOperation(), "save");
        assertNotNull(request.getProperties());
        assertEquals(request.getPropertyType(), "application");
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    @Test
    public void testCreateUserSaveRequestObject()
    {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("username", "");
        
        UserSaveRequest request = new UserSaveRequest(properties);
        
        assertNotNull(request);
        assertEquals(request.getMessage(), "configrequest");
        assertEquals(request.getFileExtension().endsWith(".configrequest"), true);
        assertEquals(request.getOperation(), "save");
        assertNotNull(request.getProperties());
        assertEquals(request.getPropertyType(), "user");
        assertNotNull(request.toJson());
        assertEquals(request.toJson().isEmpty(), false);
    }
    
    // Sending tests
    @Test
    public void testSendApplicationGetRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File("");
        
        ApplicationGetRequest request = new ApplicationGetRequest();      
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".configrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
    
    @Test
    public void testSendUserGetRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File("");
        
        UserGetRequest request = new UserGetRequest();      
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".configrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
    
    @Test
    public void testSendApplicationSaveRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File("");
        
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("refreshrate", "30");
        
        ApplicationSaveRequest request = new ApplicationSaveRequest(properties);     
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".configrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
    
    @Test
    public void testSendUserSaveRequest() throws JsonTransformationException, MessageSenderException
    {
        File folder = new File("");
        
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("username", "");
        
        UserSaveRequest request = new UserSaveRequest(properties);     
        new GuiMessageSender().sendRequestMessage(request);
        
        assertEquals(folder.listFiles().length, 1);
        assertEquals(folder.listFiles()[0].getAbsolutePath().endsWith(".configrequest"), true);
        assertNotEquals(folder.listFiles()[0].length(), 0);
    }
    
    // Mock response tests
    @Test
    public void testMockUserConfigSuccessResponse() throws ParseException, IOException
    {
        File mock = new File("tests/mocks/config/response/user config success.configresponse");
     
        String responseContent = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(responseContent);
        JSONArray properties = (JSONArray) message.get("properties");
        String operation = message.get("operation").toString();
        
        assertEquals("getconfiguser", operation);
        assertEquals(false, properties.isEmpty());
    }
    
    @Test
    public void testMockApplicationConfigSuccessResponse() throws ParseException, IOException
    {
        File mock = new File("tests/mocks/config/response/application config success.configresponse");
     
        String responseContent = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(responseContent);
        JSONArray properties = (JSONArray) message.get("properties");
        String operation = message.get("operation").toString();
        
        assertEquals("getconfigapplication", operation);
        assertEquals(false, properties.isEmpty());
    }
    
    @Test
    public void testMockUserConfigFailureResponse() throws ParseException, IOException
    {
        File mock = new File("tests/mocks/config/response/user config failure.configresponse");
     
        String responseContent = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(responseContent);
        String operation = message.get("operation").toString();
        String failureReason = message.get("failurereason").toString();
        String success = message.get("successful").toString();
        
        assertEquals("getconfiguser", operation);
        assertEquals(false, failureReason.isEmpty());
        assertEquals("no", success);
    }
    
    @Test
    public void testMockApplicationConfigSFailureResponse() throws ParseException, IOException
    {
        File mock = new File("tests/mocks/config/response/application config failure.configresponse");
        
        String responseContent = new String(Files.readAllBytes(Paths.get(mock.getAbsolutePath())));
        JSONObject message = (JSONObject) new JSONParser().parse(responseContent);
        String operation = message.get("operation").toString();
        String failureReason = message.get("failurereason").toString();
        String success = message.get("successful").toString();
        
        assertEquals("getconfigapplication", operation);
        assertEquals(false, failureReason.isEmpty());
        assertEquals("no", success);
    }
}
