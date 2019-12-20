package common.message;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import common.error.RequestException;

/**
 * An abstract class making requests to the Node Express backend.
 *
 * @author Tom Hewitt
 */
public class ApiRequestor
{
	public static String request(String url) throws RequestException
	{
		try
		{
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			
			HttpGet request = new HttpGet(url);
	
			HttpResponse result = httpClient.execute(request);
	
			String json = EntityUtils.toString(result.getEntity(), "UTF-8");
	
			return json;
		}
		catch (Exception e)
		{
			throw new RequestException(e.getMessage());
		}
	}
}