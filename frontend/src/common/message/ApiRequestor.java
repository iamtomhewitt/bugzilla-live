package common.message;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * An abstract class making requests to the Node Express backend.
 *
 * @author Tom Hewitt
 */
public class ApiRequestor {
	
	public static JSONObject request(String url) {
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
			HttpGet request = new HttpGet(url);

			HttpResponse result = httpClient.execute(request);

			String jsonString = EntityUtils.toString(result.getEntity(), "UTF-8");
			
			JSONObject json = (JSONObject) new JSONParser().parse(jsonString);

			return json;

		} catch (IOException | ParseException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
