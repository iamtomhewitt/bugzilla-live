package bugzilla.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import bugzilla.common.OR.OR;
import bugzilla.common.OR.ORAttachment;
import bugzilla.common.OR.ORComment;
import bugzilla.exception.JsonTransformationException;
import bugzilla.common.UnitTestStep;

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
	 * Convert an object to a JSON string, for example, an <code>OR</code>.
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
	 * For example, to convert JSON to an <code>OR</code>: <br>
	 * <code>List<.OR> listOfORs = fromJSON("yourJSON", OR.class);</code>
	 */
	public static List fromJson(String json, Class c) throws JsonTransformationException
	{
		ObjectMapper mapper = new ObjectMapper();
		
		try
		{
			if (c == OR.class)
			{
				return Arrays.asList(mapper.readValue(json, OR[].class));
			}
			if (c == ORComment.class)
			{
				return Arrays.asList(mapper.readValue(json, ORComment[].class));
			}
			if (c == ORAttachment.class)
			{
				return Arrays.asList(mapper.readValue(json, ORAttachment[].class));
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
