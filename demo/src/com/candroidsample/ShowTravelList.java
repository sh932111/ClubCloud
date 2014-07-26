package com.candroidsample;

import getfunction.DB;
import getfunction.MyAdapter;
import getfunction.PushDB;
import getfunction.SysUtil;

import java.util.ArrayList;

import android.R.integer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowTravelList extends Activity
{
	private ListView listView;

	private Cursor mCursor;

	String time_string;

	CharSequence[] cs1;
	CharSequence[] cs2;
	CharSequence[] cs3;
	CharSequence[] cs4;
	CharSequence[] cs5;

	int list_index;

	// 1.caldroid 2.push
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_show_travel_list);

		list_index = 0;

		Intent intent = this.getIntent();

		Bundle bundle = intent.getExtras();// 取得Bundle

		list_index = bundle.getInt("Index");
		TextView txt_1 = (TextView) findViewById(R.id.datetext);

		if (list_index == 1)
		{
			time_string = bundle.getString("Time");

			txt_1.setText(time_string);
		}
		else
		{
			txt_1.setText("訊息查詢");
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

					bundle.putString("Date", time_string);
					bundle.putLong("ID", 0);

					Intent intent = new Intent();
					intent.putExtras(bundle);
					intent.setClass(ShowTravelList.this, ShowTravelDetail.class);
					startActivity(intent);
				}
			});

		}

		setList();
	}

	protected void onRestart()
	{
		// TODO Auto-generated method stub
		super.onRestart();

		setList();
	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();

		stopManagingCursor(mCursor);

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
			DB mDbHelper = new DB(this);
			mDbHelper.open();

			mCursor = mDbHelper.get(time_string);

		}
		else
		{
			PushDB mDbHelper = new PushDB(this);

			mDbHelper.open();

			mCursor = mDbHelper.getAll();

			// mDbHelper.close();
		}
	}

	public void setList()
	{
		listView = (ListView) findViewById(R.id.listView);

		loadData();

		ArrayList<String> columnArray1 = new ArrayList<String>();
		ArrayList<String> columnArray3 = new ArrayList<String>();
		ArrayList<String> columnArray4 = new ArrayList<String>();
		ArrayList<String> columnArray5 = new ArrayList<String>();

		int rows_num = mCursor.getCount(); // 取得資料表列數
		if (rows_num != 0)
		{
			mCursor.moveToFirst(); // 將指標移至第一筆資料
			for (int i = 0; i < rows_num; i++)
			{
				columnArray1.add(mCursor.getString(1));
				columnArray3.add(mCursor.getString(3));
				columnArray4.add(mCursor.getString(4));
				columnArray5.add(mCursor.getString(5));
				mCursor.moveToNext();
			}
		}
		cs1 = columnArray1.toArray(new CharSequence[columnArray1.size()]);
		cs3 = columnArray3.toArray(new CharSequence[columnArray1.size()]);
		cs4 = columnArray4.toArray(new CharSequence[columnArray1.size()]);
		cs5 = columnArray5.toArray(new CharSequence[columnArray1.size()]);

		MyAdapter adapter = new MyAdapter(ShowTravelList.this, cs1, cs3, cs4,
				cs5);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				mCursor.moveToPosition(position);

				Long SelectID = mCursor.getLong(0);
				String title = mCursor.getString(1);
				String message = mCursor.getString(2);
				String dateString = mCursor.getString(3);
				String timeString = mCursor.getString(4);
				String check = mCursor.getString(5);

				Bundle bundle = new Bundle();

				bundle.putLong("ID", SelectID);
				bundle.putString("Title", title);
				bundle.putString("Message", message);
				bundle.putString("Date", dateString);
				bundle.putString("Time", timeString);
				bundle.putString("Check", check);

				if (list_index == 1)
				{
					Intent intent = new Intent(ShowTravelList.this,
							ShowTravelDetail.class);

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

		startManagingCursor(mCursor);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
				&& list_index == 1)
		{
			SysUtil mSysUtil = new SysUtil(ShowTravelList.this);
			mSysUtil.exit();
			finish();

			// moveTaskToBack(true);
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
