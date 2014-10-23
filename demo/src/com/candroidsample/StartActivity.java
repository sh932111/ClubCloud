package com.candroidsample;

import java.io.IOException;

import getfunction.*;
import homedetail.EmergencyReport;
import startprogram.LoginPage;
import startprogram.Register1;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import static com.candroidsample.CommonUtilities.DISPLAY_MESSAGE_ACTION;
//import static com.candroidsample.CommonUtilities.EXTRA_MESSAGE;
import static com.candroidsample.CommonUtilities.SENDER_ID;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends Activity implements LocationListener
{
	public static final String PREF = "get_pref";
	public static final String GET_ID = "get_id";
	Button bt1 ;
	Button bt2 ;
	Button bt3 ;
	TextView text;
	GoogleCloudMessaging gcm;
	private Context context;
	private String strRegId;

	private LocationManager lms;
	private String bestProvider = LocationManager.GPS_PROVIDER;


	//public DBManager dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		LocationManager status = (LocationManager) (this
				.getSystemService(StartActivity.this.LOCATION_SERVICE));
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| status.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			// 如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
			locationServiceInitial();
		} else
		{
			Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(
					android.provider.Settings.ACTION_WIFI_SETTINGS);
			startActivity(intent);
		}
		
		buildFolder();

		setUI();
		
		context = getApplicationContext();
		
		gcm = GoogleCloudMessaging.getInstance(this);

		setGCM_RegID();

		int count = DBTools.getUserData(StartActivity.this);

		if (count != 0)
		{
			bt1.setVisibility(View.INVISIBLE);
			bt2.setVisibility(View.INVISIBLE);
		//註冊過	
		}
		else
		{
			text.setVisibility(View.INVISIBLE);
			bt3.setVisibility(View.INVISIBLE);
		}
		
	}

	public void buildFolder()
	{
		FolderFunction setfolder = new FolderFunction();

		String app_path = this.getExternalFilesDir(null).getAbsolutePath() + "/";
		
		setfolder.folderBuild(app_path+"userphoto");
		setfolder.folderBuild(app_path+"pushphoto");
		
	}

	public void setUI()
	{
		text = (TextView)findViewById(R.id.textView2);
		
		bt1 = (Button) findViewById(R.id.button1);
		bt1.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("device_token", strRegId);
				intent.putExtras(bundle);
				intent.setClass(StartActivity.this, Register1.class);
				startActivity(intent);

			}

		});
		bt2 = (Button) findViewById(R.id.button2);
		bt2.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("device_token", strRegId);
				intent.putExtras(bundle);
				intent.setClass(StartActivity.this, LoginPage.class);
				startActivity(intent);
			}

		});
		bt3 = (Button) findViewById(R.id.button3);
		bt3.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub

				PageUtil mSysUtil = new PageUtil(StartActivity.this);

				mSysUtil.exit(0);
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

					System.out.println(strRegId);
					
					SharedPreferences get_pref = getSharedPreferences(PREF, 0);

					get_pref.edit().putString(GET_ID, strRegId).commit();

					msg = "Device registered, registration id=" + strRegId;

					// send id to our server
					//boolean registered = ServerUtilities.register(context,
							//strRegId);

				}
				catch (IOException ex)
				{
					msg = "Error :" + ex.getMessage();

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
//				final String newMessage = intent.getExtras().getString(
//						EXTRA_MESSAGE);

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

	@Override
	public void onLocationChanged(Location location)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider)
	{
		// TODO Auto-generated method stub
		
	}
	private void locationServiceInitial()
	{
		lms = (LocationManager) getSystemService(LOCATION_SERVICE); // 取得系統定位服務
		Location location = null;
		
		if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER); // 使用GPS定位座標
		} 
		else if (lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			location = lms
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // 使用GPS定位座標
		}
		// Criteria criteria = new Criteria(); //資訊提供者選取標準
		// bestProvider = lms.getBestProvider(criteria, true); //選擇精準度最高的提供者
		// Location location = lms.getLastKnownLocation(bestProvider);
	}
}
