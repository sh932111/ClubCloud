package getfunction;

import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class GetServerMessage
{

	public HashMap<String, Object> stringQuery(String url,
			List<NameValuePair> parems)
	{
		try
		{
			HttpPost method = new HttpPost(url);

			method.setEntity(new UrlEncodedFormEntity(parems, HTTP.UTF_8));

			HttpResponse response = new DefaultHttpClient().execute(method);

			HttpEntity entity = response.getEntity();

			if (entity != null)
			{
				JSONObject result = new JSONObject(EntityUtils.toString(entity));

				if (result != null && result.has("result"))
				{
					if (result.optBoolean("result", false))
					{
						HashMap<String, Object> transcript = new HashMap<String, Object>();

						transcript.put("Data", result);

						return transcript;
						// Now parse and get all your data
					}
					else
					{
						HashMap<String, Object> transcript = new HashMap<String, Object>();

						transcript.put("Data", result);

						return transcript;

					}
				}

				else
				{
					JSONObject jsonObject = new JSONObject();
					
					jsonObject.put("result", false);
					jsonObject.put("Message", "json error");

					HashMap<String, Object> transcript = new HashMap<String, Object>();

					transcript.put("Data", jsonObject);

					return transcript;

				}
			}

			else
			{
				JSONObject jsonObject = new JSONObject();
				
				jsonObject.put("result", false);
				jsonObject.put("Message", "service error");

				HashMap<String, Object> transcript = new HashMap<String, Object>();

				transcript.put("Data", jsonObject);

				return transcript;
			}
		}
		catch (Exception e)
		{
			JSONObject jsonObject = new JSONObject();
			
			try
			{
				jsonObject.put("result", false);
				jsonObject.put("Message", "internet error");
			}
			catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			HashMap<String, Object> transcript = new HashMap<String, Object>();

			transcript.put("Data", jsonObject);

			return transcript;
		}
	}
}
