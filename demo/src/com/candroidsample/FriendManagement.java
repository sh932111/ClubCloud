package com.candroidsample;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FriendManagement extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_management);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_management, menu);
		return true;
	}

}
