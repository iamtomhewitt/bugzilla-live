package common.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.UnitTestStep;
import common.bug.Bug;
import common.bug.BugAttachment;
import common.bug.BugComment;
import common.exception.JsonTransformationException;

/**
 * Uses Jackson to convert a JSON string into object types.
 *
 * @author Tom Hewitt
 * @since 2.3.0
 */

@SuppressWarnings("rawtypes")
public class JacksonAdapter
{
	/**
	 * Convert an object to a JSON string, for example, an <code>Bug</code>.
	 */
	public static <T> String toJson(T object) throws JsonTransformationException
	{
		ObjectMapper mapper = new ObjectMapper();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try 
		{
			mapper.writeValue(out, object);
		} 
		catch (IOException e) 
		{
			throw new JsonTransformationException(e.getMessage());
		}
		
		String json = out.toString();
		return json;
	}

	/**
	 * Converts a JSON string into an object specified by a class.
	 * <p>
	 * For example, to convert JSON to an <code>Bug</code>: <br>
	 * <code>List<.Bug> listOfBugs = fromJSON("yourJSON", Bug.class);</code>
	 */
	public static List fromJson(String json, Class c) throws JsonTransformationException
	{
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try
		{
			if (c == Bug.class)
			{
				return Arrays.asList(mapper.readValue(json, Bug[].class));
			}
			if (c == BugComment.class)
			{
				return Arrays.asList(mapper.readValue(json, BugComment[].class));
			}
			if (c == BugAttachment.class)
			{
				return Arrays.asList(mapper.readValue(json, BugAttachment[].class));
			}
			if (c == UnitTestStep.class)
			{
				return Arrays.asList(mapper.readValue(json, UnitTestStep[].class));
			}
		}
		catch (IOException e)
		{
			throw new JsonTransformationException(e.getMessage());
		}

		throw new JsonTransformationException("Incorrect class specified: " + c.toString());
	}
}
