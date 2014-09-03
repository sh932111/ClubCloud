package homedetail;

import getdb.DB;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.candroidsample.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class ShowTravelDetail extends Activity
{
	TextView timeTextView;

	EditText titleText;
	EditText detailText;
	
	static final int TIME_DIALOG_ID = 1;

	private int mHour;
	private int mMinute;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_travel_detail);

		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);

		titleText = (EditText)findViewById(R.id.title_label);
		detailText = (EditText)findViewById(R.id.detail_label);
		
		Intent intent = this.getIntent();

		Bundle bundle = intent.getExtras();

		final String timeString = bundle.getString("Date");

		final Long IDString = bundle.getLong("ID");
		
		TextView txt_1 = (TextView) findViewById(R.id.datetext);

		txt_1.setText(getString(R.string.text_date) + timeString);
		
		timeTextView = (TextView) findViewById(R.id.timetext);

		Button bt = (Button) findViewById(R.id.choosetime);

		bt.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID); 
			}
		});
		
		
		Button check = (Button) findViewById(R.id.check);

		if (IDString != 0)
		{
			titleText.setText(bundle.getString("Title"));
			detailText.setText(bundle.getString("Message"));
			timeTextView.setText(getString(R.string.text_time) + bundle.getString("Time"));
	    	
			String checkString = bundle.getString("Check");
			
			if (checkString.equals("1"))
			{
				DB mDbHelper = new DB(ShowTravelDetail.this);
				
				mDbHelper.open();

				mDbHelper.updateLook(IDString, "0");
				
				mDbHelper.close();
			}
			
			check.setText(getString(R.string.edit));
			
			check.setOnClickListener(new Button.OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					updateDB(IDString ,timeString);
					finish();
				}
			});
		}
		else 
		{
			check.setOnClickListener(new Button.OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					saveDB(timeString);
					finish();
				}
			});
		}
		Button cancel = (Button) findViewById(R.id.cancel);
		
		if (IDString != 0)
		{
			cancel.setText(getString(R.string.delete));
		
			cancel.setOnClickListener(new Button.OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					DB mDbHelper = new DB(ShowTravelDetail.this);
					
					mDbHelper.open();

					mDbHelper.delete(IDString);

					mDbHelper.close();
					
					finish();
				}
			});
		}
		else 
		{
			cancel.setOnClickListener(new Button.OnClickListener()
			{
				@Override
				public void onClick(View arg0)
				{
					finish();
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_travel_detail, menu);
		return true;
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker arg0, int arg1, int arg2)
		{
			// TODO Auto-generated method stub
			mHour = arg1;
			mMinute = arg2;
			
			timeTextView.setText(getString(R.string.text_time) + arg1 + " :" + arg2);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case TIME_DIALOG_ID:
			TimePickerDialog tpd = new TimePickerDialog(this, mTimeSetListener,
					mHour, mMinute, false);
			return tpd;
		}
		return null;
	}
	
	@SuppressLint("SimpleDateFormat")
	public void saveDB(String date_string)
	{
		String timeString = mHour + ":" + mMinute;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
		Date now = new Date();
		
		DB mDbHelper = new DB(ShowTravelDetail.this);
		
		mDbHelper.open();

		mDbHelper.create(Long.parseLong(sdf.format(now.getTime())),titleText.getText().toString(), detailText.getText().toString(), date_string ,timeString , "1");
		
		mDbHelper.close();
	}
	public void updateDB(Long id ,String date_string)
	{
		String timeString = mHour + ":" + mMinute;
		
		DB mDbHelper = new DB(ShowTravelDetail.this);
		
		mDbHelper.open();

		mDbHelper.updateAll(id, titleText.getText().toString(), detailText.getText().toString(),  date_string ,timeString );

		mDbHelper.close();
	}
}
