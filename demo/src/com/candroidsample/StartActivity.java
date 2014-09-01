package com.candroidsample;

import getdb.DBManager;
import getdb.UserDB;
import getfunction.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import static com.candroidsample.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.candroidsample.CommonUtilities.EXTRA_MESSAGE;
import static com.candroidsample.CommonUtilities.SENDER_ID;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts.Settings;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity
{
	public static final String PREF = "get_pref";
	public static final String GET_ID = "get_id";

	GoogleCloudMessaging gcm;
	private Context context;
	private String strRegId;

	//public DBManager dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		buildFolder();
		
		DBManager dbHelper = new DBManager(StartActivity.this);

		dbHelper.openDatabase();

		dbHelper.closeDatabase();

		context = getApplicationContext();
		gcm = GoogleCloudMessaging.getInstance(this);

		setGCM_RegID();

		UserDB mDbHelper = new UserDB(StartActivity.this);

		mDbHelper.open();

		int count = mDbHelper.isTableExists();

		mDbHelper.close();

		if (count != 0)
		{
			HomeUtil mSysUtil = new HomeUtil(StartActivity.this);

			mSysUtil.exit();
		}

		setUI();
	}

	public void buildFolder()
	{
		FolderFunction setfolder = new FolderFunction();

		setfolder.folderBuild("ClubCloud");
		setfolder.folderBuild("ClubCloud/userphoto");
		setfolder.folderBuild("ClubCloud/pushphoto");
		
		Bitmap bmImage = BitmapFactory.decodeResource(getResources(),
				R.drawable.user);

		String extStorage = Environment.getExternalStorageDirectory()
				.toString();
		
		String locaction = "ClubCloud/" + "user.PNG";
		
		setfolder.saveImage(bmImage, extStorage, locaction);
	}
	
	public void setUI()
	{
		Button bt1 = (Button) findViewById(R.id.button1);
		bt1.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, Register1.class);
				startActivity(intent);

			}

		});
		Button bt2 = (Button) findViewById(R.id.button2);
		bt2.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, LoginPage.class);
				startActivity(intent);
			}

		});
	}
	
	public void setGCM_RegID()
	{
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
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

					SharedPreferences get_pref = getSharedPreferences(PREF, 0);

					get_pref.edit().putString(GET_ID, strRegId).commit();

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
				final String newMessage = intent.getExtras().getString(
						EXTRA_MESSAGE);

			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

}
