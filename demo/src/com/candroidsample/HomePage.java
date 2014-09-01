package com.candroidsample;


import static com.candroidsample.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.candroidsample.CommonUtilities.EXTRA_MESSAGE;
import static com.candroidsample.CommonUtilities.SENDER_ID;


import homedetail.EventDelivery;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.candroidsample.CaldroidSampleActivity;
import com.candroidsample.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class HomePage extends Activity
{
	GoogleCloudMessaging gcm;
	private Context context;
	private String strRegId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		context = getApplicationContext();
		gcm = GoogleCloudMessaging.getInstance(this);

		setGCM_RegID();
		
		setUI();
	}
	
	public void setUI()
	{
		Button bt1 = (Button)findViewById(R.id.bt1);
		bt1.setOnClickListener(new Button.OnClickListener()
        { 
            @Override
			public void onClick(View arg0)
			{
				
			}         

        }); 
		
		Button bt2 = (Button)findViewById(R.id.bt2);
		bt2.setOnClickListener(new Button.OnClickListener()
        { 
            @Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
            	Intent intent = new Intent();
				intent.setClass(HomePage.this,
						CaldroidSampleActivity.class);
				startActivity(intent);
				finish();
			}         

        }); 
		Button bt3 = (Button)findViewById(R.id.bt3);
		bt3.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();

				bundle.putInt("Index", 2);

				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(HomePage.this,
						ShowTravelList.class);
				
				startActivity(intent);
			}
			
		});
		Button bt4 = (Button)findViewById(R.id.bt4);
		bt4.setOnClickListener(new Button.OnClickListener()
        { 
            @Override
			public void onClick(View arg0)
			{
            	Intent intent = new Intent();
            	
				intent.setClass(HomePage.this,
						EventDelivery.class);
				
				startActivity(intent);
				// TODO Auto-generated method stub
			}         

        });
		Button bt5 = (Button)findViewById(R.id.bt5);
		bt5.setOnClickListener(new Button.OnClickListener()
        { 
            @Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
            	
			}         

        }); 
	}
	
	public void setGCM_RegID() 
	{
		registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
		// register with Google.
		new AsyncTask<Void, String, String>() 
		{

			@Override
			protected String doInBackground(Void... params) 
			{
				String msg = "";
				try 
				{
					if (gcm == null) 
					{
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					
					strRegId = gcm.register(SENDER_ID);
					
					System.out.println("id"+strRegId);
				    
					msg = "Device registered, registration id=" + strRegId;

					// send id to our server
					boolean registered = ServerUtilities.register(context,
							strRegId);

				} 
				catch (IOException ex) 
				{
					msg = "Error :" + ex.getMessage();
					
					System.out.println(msg);
				}
				
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) 
			{
				// tvRegisterMsg.append(msg + "\n");
			}

		}.execute(null, null, null);
	}
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		unregisterReceiver(mHandleMessageReceiver);
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() 
	{
		private final static String MY_MESSAGE = "com.stu.phonebook.DISPLAY_MESSAGE";

		@Override
		public void onReceive(Context context, Intent intent) 
		{

			if (MY_MESSAGE.equals(intent.getAction())) 
			{
				final String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
				
			}
		}
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}

}
