package com.candroidsample;

//import java.text.SimpleDateFormat;
import getdb.DB;
import getfunction.HomeUtil;
import homedetail.ShowTravelList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import android.R.color;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
		
@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends FragmentActivity
{
	private Cursor mCursor;
	private CaldroidFragment caldroidFragment;
	private CaldroidFragment dialogCaldroidFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setCaldroid(savedInstanceState);
	}

	public void setCaldroid(Bundle savedInstanceState)
	{
		// final SimpleDateFormat formatter = new
		// SimpleDateFormat("dd MMM yyyy");
		
		caldroidFragment = new CaldroidFragment();

		if (savedInstanceState != null)
		{
			caldroidFragment.restoreStatesFromKey(savedInstanceState,
					"CALDROID_SAVED_STATE");
		}
		// If activity is created from fresh
		else
		{
			Bundle args = new Bundle();

			Calendar cal = Calendar.getInstance();
			args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
			args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
			args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
			args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

			caldroidFragment.setArguments(args);
		}


		setCustomResourceForDates();

		// Attach to the activity
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		
		t.replace(R.id.calendar1, caldroidFragment);
		
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() 
		{
			@Override
			public void onSelectDate(Date date, View view)
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

				String time = sdf.format(date);

				Bundle bundle = new Bundle();

				bundle.putString("Time", time);
				bundle.putInt("Index", 1);

				Intent intent = new Intent();
				intent.putExtras(bundle);
				intent.setClass(CaldroidSampleActivity.this,
						ShowTravelList.class);
				
				startActivity(intent);
				finish();
			}

			@Override
			public void onChangeMonth(int month, int year)
			{
				// ������ɱҰ�
			}

			@Override
			public void onLongClickDate(Date date, View view)
			{
				// ����|�Ұ�
			}

			@Override
			public void onCaldroidViewCreated()
			{
				if (caldroidFragment.getLeftArrowButton() != null)
				{
					// �s�W������Ұ�
				}
			}

		};
		caldroidFragment.setCaldroidListener(listener);
	}
	protected void onRestart() 
    {
    	  // TODO Auto-generated method stub
    	super.onRestart();
    	
    }
	private void setCustomResourceForDates()
	{	

		DB mDbHelper = new DB(CaldroidSampleActivity.this);

		mDbHelper.open();

		mCursor = mDbHelper.getAllDate();
		
		int rows_num = mCursor.getCount();

		if (rows_num != 0)
		{
			mCursor.moveToFirst(); // �N��в��ܲĤ@�����

			for (int i = 0; i < rows_num; i++)
			{
				String get_date_string = mCursor.getString(0);

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				// �i���ഫ

				Date date = null;
				try
				{
					date = sdf.parse(get_date_string);
				} catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Date now = new Date();

				Calendar calendar1 = Calendar.getInstance();
		        
				calendar1.setTime(now);
				Calendar calendar2 = Calendar.getInstance();
		        
				calendar2.setTime(date);
				
				int day = daysOfTwo(calendar1, calendar2);
				
				Calendar cal = Calendar.getInstance();

				cal.add(Calendar.DATE, day);
				Date blueDate = cal.getTime();

				if (caldroidFragment != null)
				{
					if (day < 0)
					{
						caldroidFragment.setBackgroundResourceForDate(R.color.green,
								blueDate);

						caldroidFragment.setTextColorForDate(R.color.white,
								blueDate);
					}
					else if (day == 0)
					{
						caldroidFragment.setBackgroundResourceForDate(R.color.red,
								blueDate);

						caldroidFragment.setTextColorForDate(R.color.white,
								blueDate);
					}
					else 
					{
						caldroidFragment.setBackgroundResourceForDate(R.color.blue,
								blueDate);

						caldroidFragment.setTextColorForDate(R.color.white,
								blueDate);
					}
				}

				mCursor.moveToNext(); // �N��в��ܤU�@�����
			}
		}
		mDbHelper.close();

	}
	public int daysOfTwo(Calendar befor, Calendar after)
	{
		int day1 = befor.get(Calendar.DAY_OF_YEAR);
		int day2 = after.get(Calendar.DAY_OF_YEAR);
		return day2 - day1;
		}
	/**
	 * Save current states of the Caldroid here
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);

		if (caldroidFragment != null)
		{
			caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
		}

		if (dialogCaldroidFragment != null)
		{
			dialogCaldroidFragment.saveStatesToKey(outState,
					"DIALOG_CALDROID_SAVED_STATE");
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
		{
			HomeUtil mSysUtil= new HomeUtil(CaldroidSampleActivity.this);  
            mSysUtil.exit();
			finish();
             
			return true;
		}

	  return super.onKeyDown(keyCode, event);
	}

}
