package exception;

import org.json.simple.JSONObject;
import org.junit.Test;

import bugzilla.common.bug.Bug;
import bugzilla.exception.JsonTransformationException;
import bugzilla.exception.MessageProcessorException;
import bugzilla.exception.MessageReceiverException;
import bugzilla.exception.MessageSenderException;
import bugzilla.utilities.JacksonAdapter;
import message.GuiMessageReceiver;
import message.GuiMessageSender;
import processor.ListProcessor;

public class ExceptionTests 
{    
    @Test(expected=JsonTransformationException.class)
    public void testThrowsJsonTransformationException() throws JsonTransformationException
    {        
        String json = "incorrect json";
        JacksonAdapter.fromJson(json, Bug.class);
    }
    
    @SuppressWarnings("unchecked")
    @Test(expected=MessageProcessorException.class)
    public void testThrowsMessageProcessorException() throws MessageProcessorException
    {        
        JSONObject message = new JSONObject();
        message.put("operation", "unsupported operation");
        new ListProcessor().process(message);
    }
    
    @Test(expected=MessageReceiverException.class)
    public void testThrowsMessageReceiverException() throws MessageReceiverException
    {        
        new GuiMessageReceiver().processMessage(null);
    }
    
    @Test(expected=MessageSenderException.class)
    public void testThrowsMessageSenderException() throws MessageSenderException, JsonTransformationException
    {        
        new GuiMessageSender().sendRequestMessage(null);
    }
}
