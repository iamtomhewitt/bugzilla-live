package common.message;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import common.Errors;
import common.MessageBox;

/**
 * An abstract class making requests to the Node Express backend.
 *
 * @author Tom Hewitt
 */
public class ApiRequestor 
{	
	public static String request(String endpoint) 
	{
		String url = "http://localhost:3001" + endpoint;
		return makeRequest(url);
	}
	
	public static String requestExternal(String url)
	{
		return makeRequest(url);
	}
	
	private static String makeRequest(String url)
	{
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) 
		{			
			HttpGet request = new HttpGet(url);

			HttpResponse result = httpClient.execute(request);

			String json = EntityUtils.toString(result.getEntity(), "UTF-8");

			return json;

		} 
		catch (IOException | ParseException e) 
		{
			MessageBox.showExceptionDialog(Errors.REQUEST, e);
		}
		return null;
	}
}