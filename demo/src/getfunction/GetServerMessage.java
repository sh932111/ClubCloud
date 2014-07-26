package getfunction;

import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class GetServerMessage
{

	public Dictionary<String, String> stringQuery(String url, List<NameValuePair>parems) 
	{
		try
        {
            HttpPost method = new HttpPost(url);
            
            method.setEntity(new UrlEncodedFormEntity(parems,HTTP.UTF_8));
            
            HttpResponse response = new DefaultHttpClient().execute(method);
            
            HttpEntity entity = response.getEntity();
            
            if(entity != null)
            {
            	JSONObject result = new JSONObject(EntityUtils.toString(entity));
            	
            	if (result != null && result.has("result"))
            	{
            	      if(result.optBoolean("result", false))
            	      {
            	    	  String res_mseeage = (String) result.get("Message");
                          
                          Dictionary<String, String> dict =  new Hashtable<String, String>();
                          
                          dict.put("result","1");
                          dict.put("Message",res_mseeage);
                          
                          return dict;
            	              // Now parse and get all your data
            	      }
            	      else
            	      {
            	    	  String res_errorMessage = (String) result.get("Message");
                          
                          Dictionary<String, String> dict =  new Hashtable<String, String>();
                          
                          dict.put("result","0");
                          dict.put("Message",res_errorMessage);
                          
                          return dict;
            	    	  
            	    	  //System.out.println("error");
            	         // If you are running is thread, then run on UI thread and show Toast
            	     }
            	 }
                
                else 
                {
                    Dictionary<String, String> dict =  new Hashtable<String, String>();

                    dict.put("result","0");
                    dict.put("Message","json error");
                    
                    return dict;
      	    	  
				}
            }
            
            else
            {
            	Dictionary<String, String> dict =  new Hashtable<String, String>();

                dict.put("result","0");
            	dict.put("Message", "service error");
                
                return dict;
            }
         }
         catch(Exception e)
         {
        	 Dictionary<String, String> dict =  new Hashtable<String, String>();

             dict.put("result","0");
         	 dict.put("Message", "internet error");
             
             return dict;
         }
	}
	public Dictionary<String, String> LoginQuery(String url, List<NameValuePair>parems) 
	{
		try
        {
            HttpPost method = new HttpPost(url);
            
            method.setEntity(new UrlEncodedFormEntity(parems,HTTP.UTF_8));
            
            HttpResponse response = new DefaultHttpClient().execute(method);
            
            HttpEntity entity = response.getEntity();
            
            if(entity != null)
            {
            	JSONObject result = new JSONObject(EntityUtils.toString(entity));
            	
            	if (result != null && result.has("result"))
            	{
            	      if(result.optBoolean("result", false))
            	      {
            	    	  String res_mseeage = (String) result.get("Message");
            	    	  String username = (String) result.get("username");
            	    	  String password = (String) result.get("password");
            	    	  String name = (String) result.get("name");
            	    	  String user_id = (String) result.get("user_id");
            	    	  String device_token = (String) result.get("device_token");
            	    	  String device_os = (String) result.get("device_os");
                          
                          Dictionary<String, String> dict =  new Hashtable<String, String>();
                          
                          dict.put("result","1");
                          dict.put("Message",res_mseeage);
                          dict.put("username",username);
                          dict.put("password",password);
                          dict.put("name",name);
                          dict.put("user_id",user_id);
                          dict.put("device_token",device_token);
                          dict.put("device_os",device_os);
                          
                          return dict;
            	              // Now parse and get all your data
            	      }
            	      else
            	      {
            	    	  String res_errorMessage = (String) result.get("Message");
                          
                          Dictionary<String, String> dict =  new Hashtable<String, String>();
                          
                          dict.put("result","0");
                          dict.put("Message",res_errorMessage);
                          
                          return dict;
            	    	  
            	    	  //System.out.println("error");
            	         // If you are running is thread, then run on UI thread and show Toast
            	     }
            	 }
                
                else 
                {
                    Dictionary<String, String> dict =  new Hashtable<String, String>();

                    dict.put("result","0");
                    dict.put("Message","json error");
                    
                    return dict;
      	    	  
				}
            }
            
            else
            {
            	Dictionary<String, String> dict =  new Hashtable<String, String>();

                dict.put("result","0");
            	dict.put("Message", "service error");
                
                return dict;
            }
         }
         catch(Exception e)
         {
        	 Dictionary<String, String> dict =  new Hashtable<String, String>();

             dict.put("result","0");
         	 dict.put("Message", "internet error");
             
             return dict;
         }
	}
}
