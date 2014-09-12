package homedetail;

import pagefunction.PageUtil;
import getdb.UserDB;
import getfunction.ShowToolbar;

import com.candroidsample.R;

import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;

public class EventDelivery extends CloudActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_delivery);
		
		ShowToolbar showToolbar = new ShowToolbar();
		showToolbar.showToolbar((LinearLayout) findViewById(R.id.LinearLayout1), this ,getResources().getDisplayMetrics().widthPixels/ShowToolbar.getMenuNum(this) ,1,new ShowToolbar.Callback()
		{
			
			@Override
			public void service_result(int msg)
			{
				// TODO Auto-generated method stub
				PageUtil mSysUtil= new PageUtil(EventDelivery.this);  
	            mSysUtil.exit(msg+1);
				finish();
			}
		});
		
		getData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_delivery, menu);
		return true;
	}

	public void getData()
	{
		UserDB userDB = new UserDB(EventDelivery.this);
		
		
	}
	
	
	
}
