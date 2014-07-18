package com.candroidsample;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class Register2 extends Activity
{
	ScrollView scrollView;
	Button bt1;
	Button bt2;
	Button bt3;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register2);
		
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
            	Intent intent = new Intent();
				intent.setClass(Register2.this, HomePage.class);
				startActivity(intent);
			}
        });
		bt3 = (Button)findViewById(R.id.button3);
		bt3.setOnClickListener(new Button.OnClickListener()
        { 
            @Override
			public void onClick(View arg0)
			{
            
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
