package homedetail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import getdb.EventDB;
import getdb.UserDB;
import getfunction.DialogShow;
import getfunction.EventAdpter;
import httpfunction.SendPostRunnable;
import pagefunction.PageUtil;
import uifunction.ShowToolbar;

import com.candroidsample.R;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EmergencyReport extends Activity
{
	private ListView listView;
	TextView nodata;
	ArrayList<Bundle> mArrayList;
	String Latitude;
	String Longitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency_report);

		Latitude = "22.901640";
		Longitude = "120.306044";
		
		listView = (ListView) findViewById(R.id.emergency_list);
		nodata = ( TextView ) findViewById(R.id.noData); 
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, getResources()
						.getDisplayMetrics().heightPixels
						- getResources().getDisplayMetrics().heightPixels / 3);

		listView.setLayoutParams(layoutParams);
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);                      //取出年
		int month = c.get(Calendar.MONTH) + 1;           //取出月，月份的編號是由0~11 故+1
		
		EventDB db = new EventDB(EmergencyReport.this);
		
		db.open();
		
		mArrayList= db.getType("emergency", String.valueOf(year), String.valueOf(month));
		
		EventAdpter adapter = new EventAdpter(EmergencyReport.this, mArrayList, "" ,2 ,
				new EventAdpter.Callback()
				{
					@Override
					public void responsePeace(int i)
					{
						// TODO Auto-generated method stub
						post("2",i);
					}
					
					@Override
					public void responseHelp(int i)
					{
						// TODO Auto-generated method stub
						post("3",i);
					}
				});
		
		listView.setAdapter(adapter);
			
		db.close();
		
		if (mArrayList.size() != 0)
		{
			nodata.setVisibility(View.GONE);
		}
		
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
	public String getUserName()
	{
		UserDB userDB = new UserDB(EmergencyReport.this);

		userDB.open();

		ArrayList<String> array_list = userDB.getAllDate();

		userDB.close();

		return array_list.get(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.emergency_report, menu);
		return true;
	}
	public void post(String type,int index)
	{
		//22.901640, 120.306044
		List<NameValuePair> parems = new ArrayList<NameValuePair>();
		Long id = mArrayList.get(index).getLong("ID");
		
		parems.add(new BasicNameValuePair("id", String.valueOf(id)));
		parems.add(new BasicNameValuePair("username", getUserName()));
		parems.add(new BasicNameValuePair("user_status", type));
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

						DialogShow show = new DialogShow();

						show.showStyle1(EmergencyReport.this,
								getString(R.string.dialog_title1),
								messageString,
								getString(R.string.dialog_check),
								new DialogShow.Callback()
								{
									@Override
									public void work()
									{
										boolean resString = false;
										try
										{
											resString = result
													.getBoolean("result");

										}
										catch (JSONException e)
										{
											// TODO Auto-generated catch
											// block
											e.printStackTrace();
										}
										if (resString)
										{
											//finish();
										}
									}

									@Override
									public void cancel()
									{
										// TODO Auto-generated method stub
									}
								});
					}
				});

		Thread t = new Thread(post);

		t.start();
	}

}
