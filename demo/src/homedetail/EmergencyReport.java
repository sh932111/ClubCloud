package homedetail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;

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
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class EmergencyReport extends Activity implements LocationListener
{
	private ListView listView;
	TextView nodata;
	ArrayList<Bundle> mArrayList;
	String Latitude;
	String Longitude;
	private boolean getService = false;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency_report);

		LocationManager status = (LocationManager) (this
				.getSystemService(EmergencyReport.this.LOCATION_SERVICE));
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| status.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			// 如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
			getService = true;
			locationServiceInitial();
		} else
		{
			Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(
					android.provider.Settings.ACTION_WIFI_SETTINGS);
			startActivity(intent);
		}

		listView = (ListView) findViewById(R.id.emergency_list);
		nodata = (TextView) findViewById(R.id.noData);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, getResources()
						.getDisplayMetrics().heightPixels
						- getResources().getDisplayMetrics().heightPixels / 3);

		listView.setLayoutParams(layoutParams);

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR); // 取出年
		int month = c.get(Calendar.MONTH) + 1; // 取出月，月份的編號是由0~11 故+1

		EventDB db = new EventDB(EmergencyReport.this);

		db.open();

		mArrayList = db.getType("emergency", String.valueOf(year),
				String.valueOf(month));

		EventAdpter adapter = new EventAdpter(EmergencyReport.this, mArrayList,
				"", 2, new EventAdpter.Callback()
				{
					@Override
					public void responsePeace(int i)
					{
						// TODO Auto-generated method stub
						post("2", i);
					}

					@Override
					public void responseHelp(int i)
					{
						// TODO Auto-generated method stub
						post("3", i);
					}
				});

		listView.setAdapter(adapter);

		db.close();

		if (mArrayList.size() != 0)
		{
			nodata.setVisibility(View.GONE);
		}

		ShowToolbar showToolbar = new ShowToolbar();
		showToolbar.showToolbar(
				(LinearLayout) findViewById(R.id.LinearLayout1),
				this,
				getResources().getDisplayMetrics().widthPixels
						/ ShowToolbar.getMenuNum(this), 1,
				new ShowToolbar.Callback()
				{
					@Override
					public void service_result(int msg)
					{
						// TODO Auto-generated method stub
						PageUtil mSysUtil = new PageUtil(EmergencyReport.this);
						mSysUtil.exit(msg + 1);
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

	public void post(String type, int index)
	{
		// 22.901640, 120.306044
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
											// finish();
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

	@Override
	public void onLocationChanged(Location location)
	{
		// TODO Auto-generated method stub
		getLocation(location);
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
	
	private void locationServiceInitial()
	{
		lms = (LocationManager) getSystemService(LOCATION_SERVICE);	//取得系統定位服務
		Criteria criteria = new Criteria();	//資訊提供者選取標準
		bestProvider = lms.getBestProvider(criteria, true);	//選擇精準度最高的提供者
		Location location = lms.getLastKnownLocation(bestProvider);
		getLocation(location);
	}

	private void getLocation(Location location)
	{ // 將定位資訊顯示在畫面中
		if (location != null)
		{
			Double longitude = location.getLongitude(); // 取得經度
			Double latitude = location.getLatitude(); // 取得緯度

			Longitude = String.valueOf(longitude);
			Latitude = String.valueOf(latitude);
		} 
		else
		{
			Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(getService) {
			lms.requestLocationUpdates(bestProvider, 1000, 1, this);
			//服務提供者、更新頻率60000毫秒=1分鐘、最短距離、地點改變時呼叫物件
		}
	}
 
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(getService) {
			lms.removeUpdates(this);	//離開頁面時停止更新
		}
	}
}
