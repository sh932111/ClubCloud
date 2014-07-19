package com.candroidsample;

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
import android.widget.ScrollView;
import android.widget.TextView;

public class Register2 extends Activity
{
	Button bt1;
	Button bt2;
	Button bt3;

	EditText edit_text1;
	EditText edit_text2;
	EditText edit_text3;
	EditText edit_text4;
	
	String user_id;
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			final String resString = msg.getData().getString("result");
			String messageString = msg.getData().getString("Message");

			AlertDialog.Builder dialog = new AlertDialog.Builder(Register2.this);
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
								UserDB mDbHelper = new UserDB(Register2.this);
								
								mDbHelper.open();
								
								String str1 = edit_text1.getText().toString();
				            	String str2 = edit_text2.getText().toString();
				            	String str3 = edit_text3.getText().toString();
				            	
				            	mDbHelper.create(str2, str3, str1, user_id, device_token, "android");
				            	
								mDbHelper.close();
								
								Intent intent = new Intent();
								intent.setClass(Register2.this, HomePage.class);
								startActivity(intent);
							}
						}
					});
			dialog.show();
		}
	};

	public class sendPostRunnable implements Runnable
	{
		String URL = null;
		
		List<NameValuePair> parems = null;
		
		public sendPostRunnable(String url, List<NameValuePair> p)
		{
			this.URL = url;
			this.parems = p;
		}

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			GetServerMessage demo = new GetServerMessage();

			Dictionary<String, String> dic = demo.stringQuery(URL, parems);

			String message = dic.get("Message");
			String result = dic.get("result");

			Bundle countBundle = new Bundle();
			countBundle.putString("Message", message);
			countBundle.putString("result", result);

			Message msg = new Message();
			msg.setData(countBundle);

			mHandler.sendMessage(msg);
		}

	}

	public static final String PREF = "get_pref";
	public static final String GET_ID = "get_id";
	String device_token;
	
	ScrollView scrollView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register2);
		
		SharedPreferences get_pref = getSharedPreferences(PREF, 0);
		
		String get_id = get_pref.getString(GET_ID, "");
		
		if (!"".equals(get_id))
		{
			device_token = get_id;
		}
		
		Bundle bundle = getIntent().getExtras();
		
		user_id = bundle.getString("user_id");
		
		TextView txt1 = (TextView)findViewById(R.id.txt1);
		
		txt1.setText("身分證："+ user_id);
		
		scrollView = (ScrollView)findViewById(R.id.scrollView1);
		
		bt1 = (Button)findViewById(R.id.button1);
		bt1.setOnClickListener(new Button.OnClickListener()
        { 
            @Override
			public void onClick(View arg0)
			{
            	scrollView.setVisibility(View.VISIBLE);
			}
        });
		bt2 = (Button)findViewById(R.id.button2);
		bt2.setOnClickListener(new Button.OnClickListener()
        { 
            @Override
			public void onClick(View arg0)
			{
            	scrollView.setVisibility(View.VISIBLE);
			}
        });
		bt3 = (Button)findViewById(R.id.button3);
		bt3.setOnClickListener(new Button.OnClickListener()
        { 
            @Override
			public void onClick(View arg0)
			{
            	edit_text1 = (EditText)findViewById(R.id.editText1);
            	String str1 = edit_text1.getText().toString();
            	edit_text2 = (EditText)findViewById(R.id.editText2);
            	String str2 = edit_text2.getText().toString();
            	edit_text3 = (EditText)findViewById(R.id.editText3);
            	String str3 = edit_text3.getText().toString();
            	edit_text4 = (EditText)findViewById(R.id.editText4);
            	String str4 = edit_text4.getText().toString();
            	
            	if (str3.equals(str4) && str1.length() > 0 && str2.length() > 0 && str3.length() > 0 && str4.length() > 0)
				{
            		List<NameValuePair> parems = new ArrayList<NameValuePair>();

            		parems.add(new BasicNameValuePair("name", str1));
            		parems.add(new BasicNameValuePair("username", str2));
            		parems.add(new BasicNameValuePair("password", str3));
            		parems.add(new BasicNameValuePair("user_id", user_id));
            		parems.add(new BasicNameValuePair("device_token", device_token));
            		parems.add(new BasicNameValuePair("device_os", "android"));
            		
            		sendPostRunnable post = new sendPostRunnable(
    						"http://192.168.1.31:8888/ClubCloud/Register2.php",
    						parems);

    				Thread t = new Thread(post);

    				t.start();
            		
				}
            	else 
            	{
        			AlertDialog.Builder dialog = new AlertDialog.Builder(Register2.this);
        			dialog.setTitle("訊息"); // 設定dialog 的title顯示內容
        			dialog.setIcon(android.R.drawable.ic_dialog_alert);// 設定dialog 的ICO
        			String mesString = "密碼兩次輸入錯誤或是資料有遺漏";
        			dialog.setMessage(mesString);
        			dialog.setCancelable(false); // 關閉 Android 系統的主要功能鍵(menu,home等...)dialog.setPositiveButton("確定",
        			dialog.setPositiveButton("確定",
        					new DialogInterface.OnClickListener()
        					{
        						public void onClick(DialogInterface dialog, int which)
        						{

        						}
        					});
        			dialog.show();
				}

			}
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register2, menu);
		return true;
	}

}
