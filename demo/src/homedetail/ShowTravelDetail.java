package homedetail;

import java.io.FileNotFoundException;
import java.util.Calendar;

import pagefunction.PageUtil;
import uifunction.ShowScrollView;
import uifunction.ShowToolbar;
import getdb.TravelDB;
import getfunction.DBTools;
import getfunction.FolderFunction;
import getfunction.ImageFunction;
import httpfunction.DownloadImageRunnable;

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

public class ShowTravelDetail extends Activity
{
	ShowScrollView showScrollView;

	Long id;

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
		setContentView(R.layout.activity_show_travel_detail2);

		mPhone = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(mPhone);

		final Calendar c = Calendar.getInstance();
		
		mHour = c.get(Calendar.HOUR_OF_DAY);
		
		mMinute = c.get(Calendar.MINUTE);
		
		Intent intent = this.getIntent();

		Bundle bundle = intent.getExtras();

		id = bundle.getLong("ID");

		Bundle get_bundle = DBTools.getTravelDetail(ShowTravelDetail.this, id);

		String image_check = get_bundle.getString("Image");
		
		DBTools.updateTravelLook(ShowTravelDetail.this, id);
		
		float h_size = 1920 / getResources().getDisplayMetrics().heightPixels;

		float s_size = 350 / h_size;

		showScrollView = new ShowScrollView();
		showScrollView.showView(
				(LinearLayout) findViewById(R.id.LinearLayout1), this,
				get_bundle, getResources().getDisplayMetrics().heightPixels
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
		

		if (Integer.parseInt(image_check) == 1)
		{
			DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(
					String.valueOf(id), this, "pushphoto", getResources()
							.getString(R.string.downloadRequestImage),
					new DownloadImageRunnable.Callback()
					{
						@Override
						public void service_result()
						{
							// TODO Auto-generated method stub
							ImageFunction get_image = new ImageFunction();

							String app_path = getExternalFilesDir(null)
									.getAbsolutePath()
									+ "/"
									+ "pushphoto"
									+ "/" + id + ".png";

							showScrollView.showImageView
									.setImageBitmap(get_image
											.getBitmapFromSDCard(app_path));
							DBTools.updateTravelImage(ShowTravelDetail.this, id);
						}
					});
			dImageRunnable.downLoadImage();
		}
		else if(Integer.parseInt(image_check) == 2)
		{
			ImageFunction get_image = new ImageFunction();

			String app_path = getExternalFilesDir(null)
					.getAbsolutePath()
					+ "/"
					+ "pushphoto"
					+ "/" + id + ".png";

			showScrollView.showImageView
					.setImageBitmap(get_image
							.getBitmapFromSDCard(app_path));
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
						PageUtil mSysUtil = new PageUtil(ShowTravelDetail.this);
						mSysUtil.exit(msg + 1);
						finish();

					}
				});
		
		Button cancel = (Button) findViewById(R.id.cancel);
		
		cancel.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				DBTools.deleteDB(ShowTravelDetail.this, id, 2);
				
				finish();
			}
		});

		Button check = (Button) findViewById(R.id.check);

		check.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				updateDB(id);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_travel_detail2, menu);
		return true;
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
	public void updateDB(Long id)
	{
		String app_path = getExternalFilesDir(null).getAbsolutePath() + "/"+"pushphoto"+"/" + id + ".png";
		
		if (resImage != null)
		{
			FolderFunction setfolder = new FolderFunction();

			setfolder.saveImage(resImage,  app_path);	
		}
		
		String timeString = mHour + ":" + mMinute;

		DBTools.updateTravelAll(ShowTravelDetail.this, id, showScrollView.titleView.getText().toString(), showScrollView.listView.getText().toString(),  showScrollView.dateView.getText().toString() ,timeString ,"2");
	}
	public void showDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				ShowTravelDetail.this);
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
}
