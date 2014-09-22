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

import getdb.PushDB;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;

/**
 * IntentService responsible for handling GCM messages.
 */
@SuppressLint("SimpleDateFormat")
public class GCMIntentService extends GCMBaseIntentService
{

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
		}
		else
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
		Log.i(TAG, "Received message");
		System.out.println("Received message");

		Bundle bundle = intent.getExtras();

		String _id = bundle.getString("data_id");
		
		System.out.println(_id);
		
		String title = bundle.getString("title");

		String detail = bundle.getString("detail"); // getString(R.string.gcm_message);
		
		String time = bundle.getString("time");

		String time_detail = bundle.getString("time_detail"); // getString(R.string.gcm_message);

		String image = bundle.getString("image");
		
		     
		PushDB mDbHelper = new PushDB(this);
		
		mDbHelper.open();

		mDbHelper.create(Long.parseLong(_id),title, detail, time ,time_detail , "1",image);
		
		mDbHelper.close();
		
		displayMessage(context, detail);
		// notifies user
		generateNotification(context, detail, title);
	}

	@Override
	protected void onDeletedMessages(Context context, int total)
	{
		Log.i(TAG, "Received deleted messages notification");
		System.out.println("Received deleted messages notification");

		String message = getString(R.string.gcm_deleted, total);

		displayMessage(context, message);
		// notifies user
		generateNotification(context, message, "delete");
	}

	@Override
	public void onError(Context context, String errorId)
	{
		Log.i(TAG, "Received error: " + errorId);
		System.out.println("Received error");

		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId)
	{
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		System.out.println("Received recoverable error");

		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message,
			String title)
	{
		int icon = R.drawable.ic_launcher;

		System.out.println(message);

		long when = System.currentTimeMillis();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Notification notification = new Notification(icon, message, when);

		// String title = context.getString(R.string.app_name);

		Intent notificationIntent = new Intent(context, HomePage.class);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, title, message, intent);

		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, notification);
	}

}
