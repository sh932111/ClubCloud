package homedetail;

import pagefunction.PageUtil;
import uifunction.ShowToolbar;

import com.candroidsample.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;

public class EmergencyReport extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency_report);
	
		ShowToolbar showToolbar = new ShowToolbar();
		showToolbar.showToolbar((LinearLayout) findViewById(R.id.LinearLayout1), this ,getResources().getDisplayMetrics().widthPixels/ShowToolbar.getMenuNum(this) ,1,new ShowToolbar.Callback()
		{	
			@Override
			public void service_result(int msg)
			{
				// TODO Auto-generated method stub
				PageUtil mSysUtil= new PageUtil(EmergencyReport.this);  
	            mSysUtil.exit(msg+1);
				finish();
	     	}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emergency_report, menu);
		return true;
	}

}
