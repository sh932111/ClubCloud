package homedetail;

import getdb.UserDB;

import com.candroidsample.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EventDelivery extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_delivery);
		
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
