package gui.app.tests.exception;

import org.json.simple.JSONObject;
import org.junit.Test;

import common.bug.Bug;
import common.exception.JsonTransformationException;
import common.exception.MessageProcessorException;
import common.utilities.JacksonAdapter;

import gui.app.processor.ListProcessor;

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
}
