package com.candroidsample;

import getdb.UserDB;
import getfunction.GetServerMessage;
import getfunction.HomeUtil;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.candroidsample.Register1.sendPostRunnable;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
			dialog.setTitle("訊息"); // 設定dialog 的title顯示內容
			dialog.setIcon(android.R.drawable.ic_dialog_alert);// 設定dialog 的ICO
			String mesString = messageString;

			dialog.setMessage(mesString);

			dialog.setCancelable(false); // 關閉 Android 系統的主要功能鍵(menu,home等...)
			dialog.setPositiveButton("確定",
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
								
								
//								Intent intent = new Intent();
//								
//								intent.setClass(LoginPage.this, HomePage.class);
//
//								startActivity(intent);
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
			GetServerMessage demo = new GetServerMessage();

			List<NameValuePair> parems = new ArrayList<NameValuePair>();

			parems.add(new BasicNameValuePair("username", userName));
			parems.add(new BasicNameValuePair("password", passWord));
			parems.add(new BasicNameValuePair("device_token", device_token));
			parems.add(new BasicNameValuePair("device_os", "android"));
			
			Dictionary<String, String> dic = demo.LoginQuery(URL, parems);
			
			String message = dic.get("Message");
			String result = dic.get("result");
			String username = dic.get("username"); 
			String password = dic.get("password"); 
			String name = dic.get("name"); 
			String user_id = dic.get("user_id"); 
			String device_token = dic.get("device_token"); 
			String device_os = dic.get("device_os"); 
			
			Bundle countBundle = new Bundle();
			countBundle.putString("Message", message);
			countBundle.putString("result", result);
			countBundle.putString("username", username);
			countBundle.putString("password", password);
			countBundle.putString("name", name);
			countBundle.putString("user_id", user_id);
			countBundle.putString("device_token", device_token);
			countBundle.putString("device_os", device_os);

			Message msg = new Message();
			msg.setData(countBundle);

			mHandler.sendMessage(msg);
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
            	sendPostRunnable post = new sendPostRunnable(
						"http://192.168.1.31:8888/ClubCloud/Login.php",
						usernameText.getText().toString(),passwordText.getText().toString());

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
