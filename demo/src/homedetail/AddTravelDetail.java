package homedetail;

import getdb.TravelDB;
import getfunction.FolderFunction;
import getfunction.ImageFunction;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import pagefunction.PageUtil;

import uifunction.ShowScrollView;
import uifunction.ShowToolbar;

import com.candroidsample.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TimePicker;

public class AddTravelDetail extends Activity
{	
	ShowScrollView showScrollView;

	private DisplayMetrics mPhone;

	static final int TIME_DIALOG_ID = 1;

	private int mHour;
	private int mMinute;

	Bitmap resImage = null;

	private final static int CAMERA = 66;
	private final static int Album = 67;

	String image_path;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_travel_detail);
		
		Intent intent = this.getIntent();

		Bundle bundle = intent.getExtras();
		
		String date_string = bundle.getString("Date");

		mPhone = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(mPhone);

		final Calendar c = Calendar.getInstance();
		
		mHour = c.get(Calendar.HOUR_OF_DAY);
		
		mMinute = c.get(Calendar.MINUTE);
	
		float h_size = 1920 / getResources().getDisplayMetrics().heightPixels;

		float s_size = 350 / h_size;

		showScrollView = new ShowScrollView();
		showScrollView.showView(
				(LinearLayout) findViewById(R.id.LinearLayout1), this,
				null, getResources().getDisplayMetrics().heightPixels
						- getResources().getDisplayMetrics().widthPixels
						/ ShowToolbar.getMenuNum(this) - (int) s_size);

		showScrollView.chooseDayBt.setVisibility(View.INVISIBLE);
		showScrollView.chooseImgBt.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				showDialog();
			}

		});

		showScrollView.chooseTimeBt
				.setOnClickListener(new Button.OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						showDialog(TIME_DIALOG_ID); 
					}

				});
		showScrollView.dateView.setText(date_string);
		
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
						PageUtil mSysUtil = new PageUtil(AddTravelDetail.this);
						mSysUtil.exit(msg + 1);
						finish();

					}
				});
		
		Button addButton = (Button) findViewById(R.id.add);
		
		addButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				
				Date now = new Date();
				
				Long id = Long.parseLong(sdf.format(now.getTime()));
				
				String check = "0";
				
				if (resImage != null)
				{
					check = "2";
					
					String app_path = getExternalFilesDir(null).getAbsolutePath() + "/"+"pushphoto"+"/" + id + ".png";
					
					FolderFunction setfolder = new FolderFunction();

					setfolder.saveImage(resImage,  app_path);	
				}
				
				TravelDB mDbHelper = new TravelDB(AddTravelDetail.this);
				
				mDbHelper.open();

				mDbHelper.create(id, 
						showScrollView.titleView.getText().toString(), 
						showScrollView.listView.getText().toString(), 
						showScrollView.dateView.getText().toString(), 
						showScrollView.timeView.getText().toString(), 
						"1", 
						check);

				mDbHelper.close();
				
				finish();
			}
		});	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_travel_detail, menu);
		return true;
	}
	public void showDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				AddTravelDetail.this);
		dialog.setTitle(getString(R.string.dialog_title1));
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		String mesString = getString(R.string.check_img_src);
		dialog.setMessage(mesString);
		dialog.setCancelable(false);
		dialog.setPositiveButton(getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,
							int which)
					{

					}
				});
		dialog.setNegativeButton(getString(R.string.dialog_album),
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,
							int which)
					{
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								Intent.ACTION_GET_CONTENT, null);
						intent.setType("image/*");
						startActivityForResult(intent, Album);
					}
				});
		dialog.setNeutralButton(getString(R.string.dialog_camera),
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,
							int which)
					{
						// TODO Auto-generated method stub

						Intent intent = new Intent(
								"android.media.action.IMAGE_CAPTURE");
						startActivityForResult(intent, CAMERA);
					}
				});
		dialog.show();
	}
	protected void onActivityResult(int rsquestCode, int resultCode, Intent data)
	{
		if ((CAMERA == rsquestCode || Album == rsquestCode) && data != null)
		{
			Uri uri = data.getData();

			if (uri != null)
			{
				ContentResolver cr = this.getContentResolver();

				Cursor cursor = cr.query(uri, null, null, null, null);
				cursor.moveToFirst();

				image_path = cursor.getString(1);
				
				try
				{
					Bitmap bitmap = BitmapFactory.decodeStream(cr
							.openInputStream(uri));
					
					ImageFunction getFunction = new ImageFunction();

					if (bitmap.getWidth() > bitmap.getHeight())
						resImage = getFunction.ScalePic(bitmap, mPhone.widthPixels);

					else
						resImage = getFunction.ScalePic(bitmap, mPhone.widthPixels);

					showScrollView.showImageView.setImageBitmap(resImage);
				}
				catch (FileNotFoundException e)
				{

				}
			}
			
			// super.onActivityResult(rsquestCode, resultCode, data);
		}

	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener()
	{

		@Override
		public void onTimeSet(TimePicker arg0, int arg1, int arg2)
		{
			// TODO Auto-generated method stub
			mHour = arg1;
			mMinute = arg2;

			showScrollView.timeView.setText(arg1 + " :"
					+ arg2);
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

}
