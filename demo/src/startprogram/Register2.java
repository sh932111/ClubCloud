package startprogram;

import getfunction.*;
import httpfunction.DownloadImageRunnable;
import httpfunction.SendPostRunnable;
import httpfunction.UploadImage;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import utils.TimeUtils;

import com.candroidsample.R;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Register2 extends Activity
{
	// private String srcPath = "/storage/sdcard0/myFile.png";
	// private String actionUrl = getString(R.string.uploadUserImage);
	// private String Register2_url = getString(R.string.Register2);

	private ProgressDialog pd;

	// Button bt1;
	// Button bt2;
	Button bt3;

	private final static int CAMERA = 66;
	private final static int Album = 67;

	Button cameraButton;
	Button fbButton;

	EditText edit_text1;
	EditText edit_text2;
	EditText edit_text3;
	EditText edit_text4;
	EditText edit_text5;

	ImageView mImg;

	private DisplayMetrics mPhone;

	Bitmap resImage = null;

	String user_id;
	String image_path = "";
	String cityString;
	String citydetailString;
	String city_id;
	String citydetail_id;

	public static final String PREF = "get_pref";
	public static final String GET_ID = "get_id";

	String device_token;

	ScrollView scrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register2);

		Intent intent = this.getIntent();

		Bundle bundle = intent.getExtras();

		device_token = bundle.getString("device_token");

		user_id = bundle.getString("user_id");
		cityString = bundle.getString("city");
		citydetailString = bundle.getString("city_detail");
		city_id = bundle.getString("city_id");
		citydetail_id = bundle.getString("city_detail_id");
		
		findView();
	}

	@SuppressLint("NewApi")
	public void findView()
	{
		TextView txt1 = (TextView) findViewById(R.id.txt1);

		txt1.setText(getString(R.string.input_ID) + user_id);

		TextView txt2 = (TextView) findViewById(R.id.txt2);

		txt2.setText(getString(R.string.text_city2) + cityString
				+ citydetailString);

		cameraButton = (Button) findViewById(R.id.cameraBt);
		cameraButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				showPhotoAlert();
			}
		});

		scrollView = (ScrollView) findViewById(R.id.scrollView1);

		mImg = (ImageView) findViewById(R.id.img);

		edit_text1 = (EditText) findViewById(R.id.editText1);
		edit_text2 = (EditText) findViewById(R.id.editText2);
		edit_text3 = (EditText) findViewById(R.id.editText3);
		edit_text4 = (EditText) findViewById(R.id.editText4);
		edit_text5 = (EditText) findViewById(R.id.editText5);

		bt3 = (Button) findViewById(R.id.button3);
		bt3.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				String str1 = edit_text1.getText().toString();
				String str2 = edit_text2.getText().toString();
				String str3 = edit_text3.getText().toString();
				String str4 = edit_text4.getText().toString();
				String str5 = edit_text5.getText().toString();

				if (str3.equals(str4) && str1.length() > 0 && str2.length() > 0
						&& str3.length() > 0 && str4.length() > 0
						&& str5.length() > 0)
				{
					postImageAndData(str1, str2, str3, str5);
				} else
				{
					showAlert();
				}

			}
		});

		mPhone = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(mPhone);

		fbButton = (Button) findViewById(R.id.fbbutton);
		fbButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Session.openActiveSession(Register2.this, true,
						new Session.StatusCallback()
						{
							// callback when session changes state
							@Override
							public void call(Session session,
									SessionState state, Exception exception)
							{
								if (session.isOpened())
								{
									// make request to the /me API
									Request.newMeRequest(session,
											new Request.GraphUserCallback()
											{
												@Override
												public void onCompleted(
														GraphUser user,
														Response response)
												{
													if (user != null)
													{
														try
														{
															String id = user
																	.getInnerJSONObject()
																	.getString("id");
															edit_text1
																	.setText(user
																			.getInnerJSONObject()
																			.getString(
																					"name"));
															edit_text1.setFocusable(false);
															edit_text2
																	.setText(id);
															edit_text2.setFocusable(false);
															edit_text3.setText("fb");
															edit_text4.setText("fb");
															
															LinearLayout layout1 = (LinearLayout)findViewById(R.id.linear1);
															LinearLayout layout2 = (LinearLayout)findViewById(R.id.linear2);
															layout1.setVisibility(View.GONE);
															layout2.setVisibility(View.GONE);
															
															getIcon();
														}
														catch (JSONException e)
														{
															// TODO
															// Auto-generated
															// catch block
															e.printStackTrace();
														}
													}
												}
											}).executeAsync();
									
								}
								
							}
							
						});

			}
		});

	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register2, menu);
		return true;
	}

	public void getIcon()
	{
		Session.openActiveSession(Register2.this, true,
				new Session.StatusCallback()
				{
					// callback when session changes state
					@Override
					public void call(Session session,
							SessionState state, Exception exception)
					{
						if (session.isOpened())
						{
							Bundle params = new Bundle();
							params.putBoolean("redirect", false);
							params.putString("height", "200");
							params.putString("type", "normal");
							params.putString("width", "200");
							/* make the API call */
							new Request(
							    session,
							    "/me/picture",
							    params,
							    HttpMethod.GET,
								    new Request.Callback() {
								        public void onCompleted(Response response) {
								            /* handle the result */
								        	
								        	JSONObject jsonObject = response.getGraphObject().getInnerJSONObject();

								        	try
											{

								        		JSONObject data = jsonObject.getJSONObject("data");
								        		
								        		String url = data.getString("url");
								        		
								        		DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(
														edit_text2.getText().toString(),
														Register2.this,
														"userphoto",
														url,
														new DownloadImageRunnable.Callback()
														{
															@Override
															public void service_result()
															{
																ImageFunction get_image = new ImageFunction();

																String app_path = getExternalFilesDir(null).getAbsolutePath()
																		+ "/" + "userphoto" + "/" + edit_text2.getText().toString() + ".png";
																resImage = get_image.getBitmapFromSDCard(app_path);
																mImg.setImageBitmap(resImage);
															}
														});
												dImageRunnable.downLoadImage2();
											}
											catch (JSONException e)
											{
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
								        }
								    }
								).executeAsync();
							
							
						}
						
					}
					
				});

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

					mImg.setImageBitmap(resImage);
				}
				catch (FileNotFoundException e)
				{

				}
			}

			// super.onActivityResult(rsquestCode, resultCode, data);
		} else
		{
			Session.getActiveSession().onActivityResult(this, rsquestCode,
					resultCode, data);
		}

	}

	public void creatDB()
	{
		String str1 = edit_text1.getText().toString();
		String str2 = edit_text2.getText().toString();
		String str3 = edit_text3.getText().toString();
		String cellphone = edit_text5.getText().toString();

		DBTools.creatUserData(Register2.this, str2, str3, str1, user_id,
				device_token, "android", cityString, citydetailString, city_id,
				citydetail_id, cellphone);

		PageUtil mSysUtil = new PageUtil(Register2.this);

		mSysUtil.exit(0);
	}

	public void showAlert()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(Register2.this);
		dialog.setTitle(getString(R.string.dialog_title1));
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		String mesString = getString(R.string.dialog_password_msg);
		dialog.setMessage(mesString);
		dialog.setCancelable(false);
		dialog.setPositiveButton(getString(R.string.dialog_check),
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which)
					{

					}
				});
		dialog.show();
	}

	public void showPhotoAlert()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(Register2.this);
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

	public void postImageAndData(String str1, String str2, String str3,
			String cellphone)
	{
		String now = TimeUtils.getNowTime();

		pd = ProgressDialog.show(Register2.this, "請稍後", "載入中，請稍後...");

		List<NameValuePair> parems = new ArrayList<NameValuePair>();

		parems.add(new BasicNameValuePair("name", str1));
		parems.add(new BasicNameValuePair("username", str2));
		parems.add(new BasicNameValuePair("password", str3));
		parems.add(new BasicNameValuePair("user_id", user_id));
		parems.add(new BasicNameValuePair("device_token", device_token));
		parems.add(new BasicNameValuePair("device_os", "android"));
		parems.add(new BasicNameValuePair("user_city", cityString));
		parems.add(new BasicNameValuePair("user_city_detail", citydetailString));
		parems.add(new BasicNameValuePair("city_id", city_id));
		parems.add(new BasicNameValuePair("city_detail_id", citydetail_id));
		parems.add(new BasicNameValuePair("cellphone", cellphone));
		parems.add(new BasicNameValuePair("send_time", now));

		image_path = this.getExternalFilesDir(null).getAbsolutePath()
				+ "/userphoto/" + str2 + ".png";

		if (resImage != null)
		{
			FolderFunction.saveImage(resImage, image_path);
			
			UploadImage uploadImage = new UploadImage(getString(R.string.IP)
					+ getString(R.string.uploadUserImage), image_path, str2);

			Thread t2 = new Thread(uploadImage);
			t2.start();
		}

		SendPostRunnable post = new SendPostRunnable(getString(R.string.IP)
				+ getString(R.string.Register2), parems,
				new SendPostRunnable.Callback()
				{
					@Override
					public void service_result(Message dic)
					{
						pd.dismiss();

						Bundle countBundle = dic.getData();

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

						show.showStyle1(Register2.this,
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
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										if (resString)
										{
											creatDB();
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
