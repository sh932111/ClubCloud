package homedetail;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import startprogram.Register1;
import startprogram.Register2;
import uifunction.ChooseCityDailog;
import uifunction.ShowScrollView;
import uifunction.ShowToolbar;
import uifunction.ChooseCityDailog.chooseCityListener;
import getdb.CityDB;
import getdb.UserDB;
import getfunction.DBTools;
import getfunction.DialogShow;
import getfunction.FolderFunction;
import getfunction.ImageFunction;
import getfunction.PageUtil;
import httpfunction.SendPostRunnable;
import httpfunction.UploadImage;

import com.candroidsample.R;
import com.google.android.gms.internal.fo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.LinearLayout.LayoutParams;

public class EventDelivery extends CloudActivity implements chooseCityListener
{
	ShowScrollView showScrollView;

	private DisplayMetrics mPhone;

	static final int TIME_DIALOG_ID = 1;

	private Calendar m_Calendar = Calendar.getInstance();

	private int mHour;
	private int mMinute;

	Bitmap resImage = null;

	private final static int CAMERA = 66;
	private final static int Album = 67;

	String CITY="";
	String DETAILCITY="";

	String CITY_id="";
	String DETAILCITY_id="";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_delivery);

		mPhone = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(mPhone);

		final Calendar c = Calendar.getInstance();

		mHour = c.get(Calendar.HOUR_OF_DAY);

		mMinute = c.get(Calendar.MINUTE);

		float h_size = 1920 / getResources().getDisplayMetrics().heightPixels;

		float s_size = 850 / h_size;

		showScrollView = new ShowScrollView();
		showScrollView.showView(
				(LinearLayout) findViewById(R.id.LinearLayout1), this, null,
				getResources().getDisplayMetrics().heightPixels
						- getResources().getDisplayMetrics().widthPixels
						/ ShowToolbar.getMenuNum(this) - (int) s_size);

		showScrollView.chooseImgBt
				.setOnClickListener(new Button.OnClickListener()
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
		showScrollView.chooseDayBt
				.setOnClickListener(new Button.OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						DatePickerDialog dialog = new DatePickerDialog(
								EventDelivery.this, datepicker, m_Calendar
										.get(Calendar.YEAR), m_Calendar
										.get(Calendar.MONTH), m_Calendar
										.get(Calendar.DAY_OF_MONTH));
						dialog.show();
					}

				});
		showScrollView.addressTitleView.setVisibility(View.INVISIBLE);
		showScrollView.addressView.setVisibility(View.INVISIBLE);

		Button post_bt = new Button(this);

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		post_bt.setText("傳送");

		post_bt.setLayoutParams(lp);

		post_bt.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				post();
			}
		});
		Button bt3 = (Button) findViewById(R.id.chooseCity);
		bt3.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				ChooseCityDailog dailog = new ChooseCityDailog();
				dailog.show(getFragmentManager(), "dialog");
			}

		});

		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout1);

		layout.addView(post_bt);

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
						PageUtil mSysUtil = new PageUtil(EventDelivery.this);
						mSysUtil.exit(msg + 1);
						finish();
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_delivery, menu);
		return true;
	}

	
	public void post()
	{
		String image_check = "0";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		Date now = new Date();

		Long id = Long.parseLong(sdf.format(now.getTime()));

		if (resImage != null)
		{
			String app_path = getExternalFilesDir(null).getAbsolutePath() + "/"+"pushphoto"+"/" + id + ".png";
			
			FolderFunction setfolder = new FolderFunction();

			boolean check = setfolder.saveImage(resImage,  app_path);	
			
			if (check)
			{
				UploadImage uploadImage = new UploadImage(getString(R.string.IP)
						+ getString(R.string.Request_upload), app_path,
						String.valueOf(id));

				Thread t2 = new Thread(uploadImage);

				t2.start();

				image_check = "1";
			}
		}

		ArrayList<String> array_list = DBTools.getUserAllData(EventDelivery.this);

		String post_title = showScrollView.titleView.getText().toString();
		String post_list = showScrollView.listView.getText().toString();
		String post_date = showScrollView.dateView.getText().toString();
		String post_time = showScrollView.timeView.getText().toString();
		EditText address_text = (EditText) findViewById(R.id.address);
		String address = address_text.getText().toString();

		if (array_list != null && post_title.length() > 0
				&& post_list.length() > 0 && post_date.length() > 0
				&& post_time.length() > 0 && address.length() > 0 
				&& CITY.length() > 0 && DETAILCITY.length() > 0)
		{
			List<NameValuePair> parems = new ArrayList<NameValuePair>();

			parems.add(new BasicNameValuePair("id", String.valueOf(id)));
			parems.add(new BasicNameValuePair("username", array_list.get(0)));
			parems.add(new BasicNameValuePair("name", array_list.get(2)));
			parems.add(new BasicNameValuePair("city_id", array_list.get(8)));
			parems.add(new BasicNameValuePair("area_id", array_list.get(9)));
			parems.add(new BasicNameValuePair("title", post_title));
			parems.add(new BasicNameValuePair("detail", post_list));
			parems.add(new BasicNameValuePair("date", post_date));
			parems.add(new BasicNameValuePair("time", post_time));
			parems.add(new BasicNameValuePair("city", CITY));
			parems.add(new BasicNameValuePair("area", DETAILCITY));
			parems.add(new BasicNameValuePair("liner", "某里"));
			parems.add(new BasicNameValuePair("address", address));
			parems.add(new BasicNameValuePair("image", image_check));

			SendPostRunnable post = new SendPostRunnable(getString(R.string.IP)
					+ getString(R.string.Request), parems,
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

							show.showStyle1(EventDelivery.this,
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
												finish();
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
		else
		{
			DialogShow dialogShow = new DialogShow();
			dialogShow.showStyle1(this, "錯誤", "有未填欄位", "確定",
					new DialogShow.Callback()
					{
						@Override
						public void work()
						{

						}

						@Override
						public void cancel()
						{
							// TODO Auto-generated method stub

						}
					});
		}

	}

	public void showDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(EventDelivery.this);
		dialog.setTitle(getString(R.string.dialog_title1));
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		String mesString = getString(R.string.check_img_src);
		dialog.setMessage(mesString);
		dialog.setCancelable(false);
		dialog.setPositiveButton(getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{

					}
				});
		dialog.setNegativeButton(getString(R.string.dialog_album),
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_GET_CONTENT,
								null);
						intent.setType("image/*");
						startActivityForResult(intent, Album);
					}
				});
		dialog.setNeutralButton(getString(R.string.dialog_camera),
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
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
			
			if (Build.VERSION.SDK_INT < 19)
			{
				
			}
			else 
			{
				
			}
			
			if (uri != null)
			{
				ContentResolver cr = this.getContentResolver();

				Cursor cursor = cr.query(uri, null, null, null, null);
				cursor.moveToFirst();
				
				try
				{
					Bitmap bitmap = BitmapFactory.decodeStream(cr
							.openInputStream(uri));

					ImageFunction getFunction = new ImageFunction();

					if (bitmap.getWidth() > bitmap.getHeight())
						resImage = getFunction.ScalePic(bitmap,
								mPhone.widthPixels);

					else
						resImage = getFunction.ScalePic(bitmap,
								mPhone.widthPixels);

					showScrollView.showImageView.setImageBitmap(resImage);
				}
				catch (FileNotFoundException e)
				{

				}
			}

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

			showScrollView.timeView.setText(arg1 + " :" + arg2);
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

	DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener()
	{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth)
		{

			// TODO Auto-generated method stub
			m_Calendar.set(Calendar.YEAR, year);
			m_Calendar.set(Calendar.MONTH, monthOfYear);
			m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			String myFormat = "yyyy/MM/dd"; // In which you need put here
			SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
			showScrollView.dateView.setText(sdf.format(m_Calendar.getTime()));
		}
	};
	@Override
	public void onChooseCityComplete(Bundle bundle)
	{
		CITY = bundle.getString("city");
		CITY_id = bundle.getString("city_id");
		DETAILCITY = bundle.getString("area");
		DETAILCITY_id = bundle.getString("area_id");
		
		TextView address = (TextView)findViewById(R.id.addressText);
		address.setText("選擇區域:"+CITY+DETAILCITY);
	}
}
