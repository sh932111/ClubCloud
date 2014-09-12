package homedetail;


import pagefunction.PageUtil;

import com.candroidsample.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;

public class CloudActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cloud);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cloud, menu);
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
		{
			PageUtil mSysUtil= new PageUtil(CloudActivity.this);  
            mSysUtil.exit(0);
			finish();
             
			return true;
		}

	  return super.onKeyDown(keyCode, event);
	}
}
