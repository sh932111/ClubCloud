package homedetail;

import getfunction.DBTools;
import getfunction.PageUtil;

import java.util.ArrayList;

import uifunction.ShowToolbar;

import com.candroidsample.R;

import adapter.MessageAdapter;
import android.os.Bundle;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowTravelList extends CloudActivity
{
	private ListView listView;

	String time_string;

	ArrayList<Bundle> arrayList;
	
	MessageAdapter Adapter;
	
	String Y = "";
	String M = "";
	String D = ""; 
	
	int list_index;

	// 1.caldroid 2.push
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_show_travel_list);

		list_index = 0;

		Intent intent = this.getIntent();

		Bundle bundle = intent.getExtras();

		list_index = bundle.getInt("Index");

		TextView txt_1 = (TextView) findViewById(R.id.datetext);

		if (list_index == 1)
		{
			Y =  bundle.getString("Year");
			M =  bundle.getString("Month");
			D =  bundle.getString("Day");
			time_string = Y +"/"+M+"/"+D;

			txt_1.setText(time_string);
		}
		else
		{
			txt_1.setText(getString(R.string.title_activity_show_push_list));
		}

		Button bt = (Button) findViewById(R.id.addbutton);

		if (list_index != 1)
		{
			bt.setVisibility(View.GONE);
		}
		else
		{
			bt.setOnClickListener(new Button.OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					Bundle bundle = new Bundle();

					bundle.putString("Year", Y);
					bundle.putString("Month", M);
					bundle.putString("Day", D);
					bundle.putLong("ID", 0);

					Intent intent = new Intent();
					intent.putExtras(bundle);
					intent.setClass(ShowTravelList.this, AddTravelDetail.class);
					startActivity(intent);
				}
			});

		}

		setList();

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
						PageUtil mSysUtil = new PageUtil(ShowTravelList.this);
						mSysUtil.exit(msg + 1);
						finish();

					}
				});
	}

	protected void onRestart()
	{
		// TODO Auto-generated method stub
		super.onRestart();
		
		loadData();

		Adapter.refresh(arrayList);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		loadData();

		Adapter.refresh(arrayList);
	}

	
	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_travel_list, menu);
		return true;
	}

	public void loadData()
	{
		if (list_index == 1)
		{
			arrayList = DBTools.getTravelToDate(ShowTravelList.this, time_string);
		}
		else
		{
			arrayList = DBTools.getPushAll(ShowTravelList.this);
		}
	}

	public void setList()
	{
		listView = (ListView) findViewById(R.id.listView);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, getResources()
						.getDisplayMetrics().heightPixels
						- getResources().getDisplayMetrics().heightPixels / 3);

		listView.setLayoutParams(layoutParams);

		loadData();

		Adapter = new MessageAdapter(ShowTravelList.this, arrayList);
		
		listView.setAdapter(Adapter);
		//
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Long SelectID = arrayList.get(position).getLong("ID");
				
				Bundle bundle = new Bundle();

				bundle.putLong("ID", SelectID);
				
				if (list_index == 1)
				{
					Intent intent = new Intent(ShowTravelList.this,
							NotEditActivity.class);

					intent.putExtras(bundle);

					startActivity(intent);
				}
				else
				{

					Intent intent = new Intent(ShowTravelList.this,
							ShowPushDetail.class);

					intent.putExtras(bundle);

					startActivity(intent);
				}

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
				&& list_index == 1)
		{
			PageUtil mSysUtil = new PageUtil(ShowTravelList.this);
			mSysUtil.exit(2);
			finish();

			// moveTaskToBack(true);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
