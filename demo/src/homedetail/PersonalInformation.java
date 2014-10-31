package homedetail;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import uifunction.ShowEventDailog;
import uifunction.ShowToolbar;
import getfunction.DBTools;
import getfunction.FolderFunction;
import getfunction.ImageFunction;
import getfunction.PageUtil;
import httpfunction.DownloadImageRunnable;
import httpfunction.SendPostRunnable;
import httpfunction.UploadImage;

import com.candroidsample.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonalInformation extends CloudActivity
{
	
	private DisplayMetrics mPhone;
	private ImageView userImage;
	private final static int CAMERA = 66;
	private final static int Album = 67;
	Bitmap resImage = null;
	private Calendar m_Calendar = Calendar.getInstance();

	boolean check;

	private int myYear, myMonth, myDay;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_information);
		check = true;
		mPhone = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(mPhone);

		userImage = (ImageView) findViewById(R.id.userImage);

		Button button01 = (Button) findViewById(R.id.openQR);

		button01.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN"); // 開啟條碼掃描器
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // 設定QR Code參數
				startActivityForResult(intent, 1); // 要求回傳1
			}

		});
		Button button02 = (Button) findViewById(R.id.changeImg);

		button02.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				showDialog();
			}

		});

		Button button03 = (Button) findViewById(R.id.openlog);

		button03.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				DatePickerDialog dialog = new DatePickerDialog(
						PersonalInformation.this, myDateSetListener, m_Calendar
								.get(Calendar.YEAR), m_Calendar
								.get(Calendar.MONTH), m_Calendar
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
				DatePicker dp = findDatePicker((ViewGroup) dialog.getWindow()
						.getDecorView());
				if (dp != null)
				{
					((ViewGroup) ((ViewGroup) dp.getChildAt(0)).getChildAt(0))
							.getChildAt(2).setVisibility(View.GONE);
				}

			}

		});
		getData();

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
						PageUtil mSysUtil = new PageUtil(
								PersonalInformation.this);
						mSysUtil.exit(msg + 1);
						finish();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personal_information, menu);
		return true;
	}

	public void getData()
	{

		ArrayList<String> array_list = DBTools.getUserAllData(PersonalInformation.this);

		if (array_list != null)
		{
			setImage(array_list.get(0));
			TextView usernameTextView = (TextView) findViewById(R.id.username);
			usernameTextView.setText(array_list.get(0));
			TextView userTextView = (TextView) findViewById(R.id.user);
			userTextView.setText(array_list.get(2));
			TextView phoneTextView = (TextView) findViewById(R.id.phone);
			phoneTextView.setText(array_list.get(10));
			TextView cityTextView = (TextView) findViewById(R.id.city);
			cityTextView.setText(array_list.get(6));
			TextView areaTextView = (TextView) findViewById(R.id.area);
			areaTextView.setText(array_list.get(7));
		}
	}

	public void setImage(String index)
	{
		ImageFunction get_image = new ImageFunction();

		String app_path = this.getExternalFilesDir(null).getAbsolutePath()
				+ "/" + "userphoto" + "/" + index + ".png";

		userImage.setImageBitmap(get_image.getBitmapFromSDCard(app_path));
	}

	public void showDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				PersonalInformation.this);
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

	public void post(String table_id, String username)
	{
		List<NameValuePair> parems = new ArrayList<NameValuePair>();

		parems.add(new BasicNameValuePair("id", table_id));
		parems.add(new BasicNameValuePair("username", username));
		SendPostRunnable post = new SendPostRunnable(getString(R.string.IP)
				+ getString(R.string.pushRollCall), parems,
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
						//
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								PersonalInformation.this);
						dialog.setTitle(getString(R.string.dialog_title1));
						dialog.setIcon(android.R.drawable.ic_dialog_alert);
						String mesString = messageString;
						dialog.setMessage(mesString);
						dialog.setCancelable(false);
						dialog.setPositiveButton(
								getString(R.string.dialog_check),
								new DialogInterface.OnClickListener()
								{
									public void onClick(DialogInterface dialog,
											int which)
									{
										String data_id = null;
										String image = null;
										boolean resString = false;

										try
										{
											resString = result
													.getBoolean("result");
											data_id = result
													.getString("data_id");
											image = result.getString("image");
										}
										catch (JSONException e)
										{
											// TODO Auto-generated catch
											// block
											e.printStackTrace();
										}

										if (resString)
										{
											if (image.equals("1"))
											{

												DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(
														data_id,
														PersonalInformation.this,
														"pushphoto",
														getResources()
																.getString(
																		R.string.downloadRequestImage),
														new DownloadImageRunnable.Callback()
														{
															@Override
															public void service_result()
															{
																// TODO
																// Auto-generated
																// method stub

															}

															@Override
															public void err_service_result()
															{
																// TODO Auto-generated method stub
																
															}
														});
												dImageRunnable.downLoadImage();
											}

											DBTools.saveEventData(PersonalInformation.this, result, "event");
										}
									}
								});
						dialog.show();
					}
				});

		Thread t = new Thread(post);

		t.start();

	}

	@SuppressLint("NewApi")
	private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener()
	{

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth)
		{
			if (check)
			{
				check = false;
				
				ShowEventDailog dailog = new ShowEventDailog();
				Bundle args = new Bundle();

				args.putString("YEAR", String.valueOf(year));
				args.putString("MONTH", String.valueOf(monthOfYear + 1));

				dailog.setArguments(args);
				dailog.show(getFragmentManager(), "dialog");
				// call dialog
			} else
			{
				check = true;
			}
			// TODO Auto-generated method stub

		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		String user = DBTools.getUserName(PersonalInformation.this);

		if (requestCode == 1)
		{ // startActivityForResult回傳值
			if (resultCode == RESULT_OK)
			{
				String contents = data.getStringExtra("SCAN_RESULT"); // 取得QR

				post(contents, user);
			}
		}
		if ((CAMERA == requestCode || Album == requestCode) && data != null)
		{
			Uri uri = data.getData();

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

					userImage.setImageBitmap(resImage);

					String app_path = getExternalFilesDir(null)
							.getAbsolutePath()
							+ "/"
							+ "userphoto"
							+ "/"
							+ user
							+ ".png";

					boolean check = FolderFunction.saveImage(resImage, app_path);

					if (check)
					{
						UploadImage uploadImage = new UploadImage(
								getString(R.string.IP)
										+ getString(R.string.uploadUserImage),
								app_path, String.valueOf(user));

						Thread t2 = new Thread(uploadImage);

						t2.start();
					}

				}
				catch (FileNotFoundException e)
				{

				}
			}
		}
	}

	private DatePicker findDatePicker(ViewGroup group)
	{
		if (group != null)
		{
			for (int i = 0, j = group.getChildCount(); i < j; i++)
			{
				View child = group.getChildAt(i);
				if (child instanceof DatePicker)
				{
					return (DatePicker) child;
				} else if (child instanceof ViewGroup)
				{
					DatePicker result = findDatePicker((ViewGroup) child);
					if (result != null)
						return result;
				}
			}
		}
		return null;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) 
		{
			finish();
             
			return true;
		}

	  return super.onKeyDown(keyCode, event);
	}
}
