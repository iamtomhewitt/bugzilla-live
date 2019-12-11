package common.message;

import java.io.IOException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * An abstract class making requests to the Node Express backend.
 *
 * @author Tom Hewitt
 */
public class ApiRequestor {
	
	public static String request(String endpoint, String query) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			String url = "http://localhost:3001" + endpoint;
			
			if (!query.isEmpty()) {
				url += URLEncoder.encode(query, "UTF-8");
			}
			
			HttpGet request = new HttpGet(url);

			HttpResponse result = httpClient.execute(request);

			String json = EntityUtils.toString(result.getEntity(), "UTF-8");

			return json;

		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public static String request(String endpoint) {
		return request(endpoint, "");
	}
}
