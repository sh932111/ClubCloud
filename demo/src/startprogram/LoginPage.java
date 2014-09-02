package startprogram;

import getdb.UserDB;
import getfunction.DialogShow;
import getfunction.GetServerMessage;
import getfunction.HomeUtil;
import getfunction.SendPostRunnable;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.candroidsample.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends Activity
{
	Button loginButton;
	EditText usernameText;
	EditText passwordText;
	public static final String PREF = "get_pref";
	public static final String GET_ID = "get_id";
	String device_token;
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			final String resString = msg.getData().getString("result");
			
			final String username = msg.getData().getString("username");
			final String password = msg.getData().getString("password");
			final String name = msg.getData().getString("name");
			final String user_id = msg.getData().getString("user_id");
			final String device_os = msg.getData().getString("device_os");
			final String device_t = msg.getData().getString("device_token");
			
			String messageString = msg.getData().getString("Message");

			AlertDialog.Builder dialog = new AlertDialog.Builder(LoginPage.this);
			dialog.setTitle(getString(R.string.dialog_title1)); 
			dialog.setIcon(android.R.drawable.ic_dialog_alert);
			String mesString = messageString;
			dialog.setMessage(mesString);
			dialog.setCancelable(false);
			dialog.setPositiveButton(getString(R.string.dialog_check),
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{

							if (resString.equals("1"))
							{
								UserDB mDbHelper = new UserDB(LoginPage.this);

								mDbHelper.open();
								
								mDbHelper.create(username, password, name, user_id, device_t, device_os);
					        	
								mDbHelper.close();
								
								HomeUtil mSysUtil= new HomeUtil(LoginPage.this);  
					            
								mSysUtil.exit();
							}
						}
					});
			dialog.show();
		}
	};

	public class sendPostRunnable implements Runnable
	{
		String URL = null;
		String userName = null;
		String passWord = null;

		public sendPostRunnable(String url, String username,String password)
		{
			this.URL = url;
			this.userName = username;
			this.passWord = password;
		}

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
//			GetServerMessage demo = new GetServerMessage();
//
//			List<NameValuePair> parems = new ArrayList<NameValuePair>();
//
//			parems.add(new BasicNameValuePair("username", userName));
//			parems.add(new BasicNameValuePair("password", passWord));
//			parems.add(new BasicNameValuePair("device_token", device_token));
//			parems.add(new BasicNameValuePair("device_os", "android"));
//			
//			Dictionary<String, String> dic = demo.LoginQuery(URL, parems);
//			
//			String message = dic.get("Message");
//			String result = dic.get("result");
//			String username = dic.get("username"); 
//			String password = dic.get("password"); 
//			String name = dic.get("name"); 
//			String user_id = dic.get("user_id"); 
//			String device_token = dic.get("device_token"); 
//			String device_os = dic.get("device_os"); 
//			String user_city = dic.get("user_city"); 
//			String user_city_detail = dic.get("user_city_detail"); 
//			String city_id = dic.get("city_id"); 
//			String city_detail_id = dic.get("city_detail_id"); 
//			
//			Bundle countBundle = new Bundle();
//			countBundle.putString("Message", message);
//			countBundle.putString("result", result);
//			countBundle.putString("username", username);
//			countBundle.putString("password", password);
//			countBundle.putString("name", name);
//			countBundle.putString("user_id", user_id);
//			countBundle.putString("device_token", device_token);
//			countBundle.putString("device_os", device_os);
//			countBundle.putString("user_city", user_city);
//			countBundle.putString("user_city_detail", user_city_detail);
//			countBundle.putString("city_id", city_id);
//			countBundle.putString("city_detail_id", city_detail_id);
//
//			Message msg = new Message();
//			msg.setData(countBundle);
//
//			mHandler.sendMessage(msg);
		}

	}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);

		usernameText = (EditText)findViewById(R.id.user);
		passwordText = (EditText)findViewById(R.id.pass);
		
		SharedPreferences get_pref = getSharedPreferences(PREF, 0);

		String get_id = get_pref.getString(GET_ID, "");

		if (!"".equals(get_id))
		{
			device_token = get_id;
		}
		
		loginButton = (Button)findViewById(R.id.loginbt);
		loginButton.setOnClickListener(new Button.OnClickListener()
        { 
            @Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
            	List<NameValuePair> parems = new ArrayList<NameValuePair>();

    			parems.add(new BasicNameValuePair("username", usernameText.getText().toString()));
    			parems.add(new BasicNameValuePair("password", passwordText.getText().toString()));
    			parems.add(new BasicNameValuePair("device_token", device_token));
    			parems.add(new BasicNameValuePair("device_os", "android"));
            	
            	SendPostRunnable post = new SendPostRunnable(getString(R.string.IP)+
						getString(R.string.Login), parems,
						new SendPostRunnable.Callback()
						{
							@Override
							public void service_result(Message msg)
							{
								// TODO Auto-generated method stub
								Bundle countBundle = msg.getData();

								@SuppressWarnings("unchecked")
								HashMap<String, String> resultData = (HashMap<String, String>) countBundle.getSerializable("resultData");
								
								String messageString = resultData.get("Message");
								final String resString = resultData.get("result");

								final String username = resultData.get("username");
								final String password = resultData.get("password");
								final String name = resultData.get("name");
								final String user_id = resultData.get("user_id");
								final String device_os = resultData.get("device_os");
								final String device_t = resultData.get("device_token");
								final String user_city = resultData.get("user_city");
								final String user_city_detail = resultData.get("user_city_detail");
								final String city_id = resultData.get("city_id");
								final String city_detail_id = resultData.get("city_detail_id");
							}
						});
				
				 Thread t = new Thread(post);
					
				 t.start();
  
			}         

        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_page, menu);
		return true;
	}

}
