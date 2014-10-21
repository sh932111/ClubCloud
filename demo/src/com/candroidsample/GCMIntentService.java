/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.candroidsample;

import static com.candroidsample.CommonUtilities.SENDER_ID;
import static com.candroidsample.CommonUtilities.displayMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import utils.NotificationUtls;
import getdb.EventDB;
import getdb.PushDB;
import getdb.UserDB;
import getfunction.DBTools;
import getfunction.DialogShow;
import homedetail.EmergencyReport;
import httpfunction.SendPostRunnable;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * IntentService responsible for handling GCM messages.
 */
@SuppressLint("SimpleDateFormat")
public class GCMIntentService extends GCMBaseIntentService implements
		LocationListener
{
	String Latitude;
	String Longitude;
	String ID;
	ArrayList<Bundle> mArrayList;
	private boolean getService = false;

	private static final String TAG = "GCMIntentService";

	public GCMIntentService()
	{
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId)
	{
		Log.i(TAG, "Device registered: regId = " + registrationId);
		System.out.println("regId = " + registrationId);

		displayMessage(context, getString(R.string.gcm_registered));

		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId)
	{
		Log.i(TAG, "Device unregistered");
		System.out.println("Device unregister");

		displayMessage(context, getString(R.string.gcm_unregistered));

		if (GCMRegistrar.isRegisteredOnServer(context))
		{
			ServerUtilities.unregister(context, registrationId);
		} else
		{
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
			System.out.println("Ignoring unregister callback");

		}
	}

	@Override
	protected void onMessage(Context context, Intent intent)
	{
		LocationManager status = (LocationManager) (this
				.getSystemService(this.LOCATION_SERVICE));

		Bundle bundle = intent.getExtras();

		String _id = bundle.getString("data_id");

		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| status.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			// 如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
			getService = true;
			locationServiceInitial(_id);
		} else
		{
			Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
			Intent intent2 = new Intent(
					android.provider.Settings.ACTION_WIFI_SETTINGS);
			startActivity(intent2);
		}
		String title = bundle.getString("title");

		String detail = bundle.getString("detail"); // getString(R.string.gcm_message);

		String type = bundle.getString("type");

		if (type.equals("1"))
		{
			DBTools.savePushData(context, bundle);
			
		} 
		else if (type.equals("2"))
		{
			DBTools.saveEventData(context, bundle,"emergency");	
		}

		displayMessage(context, detail);
		// notifies user
		NotificationUtls.generateNotification(context, detail, title);
	}

	@Override
	protected void onDeletedMessages(Context context, int total)
	{
		String message = getString(R.string.gcm_deleted, total);

		displayMessage(context, message);
		// notifies user
		NotificationUtls.generateNotification(context, message, "delete");
	}

	@Override
	public void onError(Context context, String errorId)
	{
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId)
	{
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	
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

	private LocationManager lms;
	private String bestProvider = LocationManager.GPS_PROVIDER;

	private void locationServiceInitial(String id)
	{
		lms = (LocationManager) getSystemService(LOCATION_SERVICE); // 取得系統定位服務
		Criteria criteria = new Criteria(); // 資訊提供者選取標準
		bestProvider = lms.getBestProvider(criteria, true); // 選擇精準度最高的提供者
		Location location = lms.getLastKnownLocation(bestProvider);
		getLocation(location, id);
	}

	private void getLocation(Location location, String id)
	{ // 將定位資訊顯示在畫面中
		if (location != null)
		{
			Double longitude = location.getLongitude(); // 取得經度
			Double latitude = location.getLatitude(); // 取得緯度

			Longitude = String.valueOf(longitude);
			Latitude = String.valueOf(latitude);

			if (Longitude.length() > 0 && Latitude.length() > 0)
			{
				ID = id;
				timer.schedule(task, 5000);  
			}
		} else
		{
			Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
		}
	}

	public void postServer(String id)
	{
		List<NameValuePair> parems = new ArrayList<NameValuePair>();

		parems.add(new BasicNameValuePair("id", id));
		parems.add(new BasicNameValuePair("username", DBTools.getUserName(this)));
		parems.add(new BasicNameValuePair("user_status", "1"));
		parems.add(new BasicNameValuePair("latitude", Latitude));
		parems.add(new BasicNameValuePair("longitude", Longitude));

		SendPostRunnable post = new SendPostRunnable(getString(R.string.IP)
				+ getString(R.string.Response), parems,
				new SendPostRunnable.Callback()
				{
					@Override
					public void service_result(Message msg)
					{
						// TODO Auto-generated method stub
						Bundle countBundle = msg.getData();

						@SuppressWarnings("unchecked")
						HashMap<String, Object> resultData = (HashMap<String, Object>) countBundle
								.getSerializable("resultData");

						final JSONObject result = (JSONObject) resultData
								.get("Data");

						String messageString = null;

						try
						{
							messageString = result.getString("Message");
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

		Thread t = new Thread(post);

		t.start();
	}


	Timer timer = new Timer();
	Handler handler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case 1:
				postServer(ID);
				break;
			}
			super.handleMessage(msg);
		}

	};

	TimerTask task = new TimerTask()
	{
		public void run()
		{
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
}
