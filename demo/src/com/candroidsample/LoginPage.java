package com.candroidsample;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends Activity
{
	Button loginButton;
	EditText usernameText;
	EditText passwordText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);

		loginButton = (Button)findViewById(R.id.loginbt);
		loginButton.setOnClickListener(new Button.OnClickListener()
        { 
            @Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
            	
//            	Intent intent = new Intent();
//				intent.setClass(HomePage.this,
//						CaldroidSampleActivity.class);
//				startActivity(intent);
//				finish();
			}         

        });
		usernameText = (EditText)findViewById(R.id.user);
		passwordText = (EditText)findViewById(R.id.pass);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_page, menu);
		return true;
	}

}
