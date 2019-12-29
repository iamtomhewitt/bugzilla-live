package common.message;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import common.error.RequestException;

/**
 * An abstract class making requests to the Node Express backend.
 *
 * @author Tom Hewitt
 */
public class ApiRequestor
{
	public enum ApiRequestType { GET, POST };
	
	public static JSONObject request(ApiRequestType type, String url) throws RequestException
	{
		try
		{
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			
			HttpUriRequest request = type == ApiRequestType.GET ? new HttpGet(url) : new HttpPost(url);

			HttpResponse result = httpClient.execute(request);
	
			String json = EntityUtils.toString(result.getEntity(), "UTF-8");
			
			// The response was an array, surround it with a tag so that a JSONObject can be created from it
			if (!json.startsWith("{"))
			{
				json = "{\"array\":" + json + "}";
			}
	
			return new JSONObject(json);
		}
		catch (Exception e)
		{
			throw new RequestException(e.getMessage());
		}
	}
}