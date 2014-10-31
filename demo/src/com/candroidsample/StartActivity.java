package com.candroidsample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import getfunction.*;
import homedetail.ShowPushDetail;
import httpfunction.DownloadImageRunnable;
import httpfunction.SendPostRunnable;
import startprogram.Register1;
import utils.TimeUtils;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import static com.candroidsample.CommonUtilities.DISPLAY_MESSAGE_ACTION;
//import static com.candroidsample.CommonUtilities.EXTRA_MESSAGE;
import static com.candroidsample.CommonUtilities.SENDER_ID;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class StartActivity extends Activity implements LocationListener
{
	Button registerBt ;
	Button loginBt ;
	Button passBt ;
	Button fbBt ;
	String userName= "";

	private ProgressDialog pd;
	private ProgressDialog pd1;
	private ProgressDialog pd2;

	GoogleCloudMessaging gcm;
	Context gcmContext;
	Context mContext;
	private String device_token;

	private LocationManager lms;
	private String bestProvider = LocationManager.GPS_PROVIDER;

	LinearLayout login_layout ;
	LinearLayout pass_layout ;
	
	EditText usernameText;
	EditText passwordText;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		LocationManager status = (LocationManager) (this
				.getSystemService(StartActivity.this.LOCATION_SERVICE));
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| status.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			// 如果GPS或網路定位開啟，呼叫locationServiceInitial()更新位置
			locationServiceInitial();
		} else
		{
			Toast.makeText(this, "請開啟定位服務", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(
					android.provider.Settings.ACTION_WIFI_SETTINGS);
			startActivity(intent);
		}
		
		buildFolder();

		setUI();
		
		mContext = StartActivity.this;
		gcmContext = getApplicationContext();
		
		gcm = GoogleCloudMessaging.getInstance(this);

		setGCM_RegID();

		int count = DBTools.getUserData(StartActivity.this);

		if (count != 0)
		{
			login_layout.setVisibility(View.GONE);
		//註冊過	
		}
		else
		{
			pass_layout.setVisibility(View.GONE);
		}
		
	}

	public void buildFolder()
	{
		FolderFunction setfolder = new FolderFunction();

		String app_path = this.getExternalFilesDir(null).getAbsolutePath() + "/";
		
		setfolder.folderBuild(app_path+"userphoto");
		setfolder.folderBuild(app_path+"pushphoto");
		
	}

	public void setUI()
	{	
		login_layout = (LinearLayout)findViewById(R.id.loginLayout);
		pass_layout = (LinearLayout)findViewById(R.id.passLayout); 

		usernameText = (EditText) findViewById(R.id.user);
		passwordText = (EditText) findViewById(R.id.pass);

		registerBt = (Button) findViewById(R.id.button1);
		registerBt.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putString("device_token", device_token);
				intent.putExtras(bundle);
				intent.setClass(StartActivity.this, Register1.class);
				startActivity(intent);

			}

		});
		loginBt = (Button) findViewById(R.id.button2);
		loginBt.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				post(usernameText.getText().toString(),passwordText.getText().toString());
				
			}

		});
		passBt = (Button) findViewById(R.id.button3);
		passBt.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub

				PageUtil mSysUtil = new PageUtil(StartActivity.this);

				mSysUtil.exit(0);
			}

		});
		fbBt = (Button) findViewById(R.id.fbbutton);
		fbBt.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub

				Session.openActiveSession(StartActivity.this, true,
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
																	.getString(
																			"id");
															
															post(id,"fb");

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
	
	
	public void setGCM_RegID()
	{
		registerReceiver(mHandleMessageReceiver, new IntentFilter(
				DISPLAY_MESSAGE_ACTION));
		// register with Google.
		new AsyncTask<Void, String, String>()
		{

			@Override
			protected String doInBackground(Void... params)
			{
				String msg = "";
				try
				{
					if (gcm == null)
					{
						gcm = GoogleCloudMessaging.getInstance(gcmContext);
					}

					device_token = gcm.register(SENDER_ID);

					msg = "Device registered, registration id=" + device_token;

					// send id to our server
					//boolean registered = ServerUtilities.register(context,
							//strRegId);

				}
				catch (IOException ex)
				{
					msg = "Error :" + ex.getMessage();

				}

				return msg;
			}

			@Override
			protected void onPostExecute(String msg)
			{
				// tvRegisterMsg.append(msg + "\n");
			}

		}.execute(null, null, null);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		//unregisterReceiver(mHandleMessageReceiver);
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver()
	{
		private final static String MY_MESSAGE = "com.stu.phonebook.DISPLAY_MESSAGE";

		@Override
		public void onReceive(Context context, Intent intent)
		{

			if (MY_MESSAGE.equals(intent.getAction()))
			{
//				final String newMessage = intent.getExtras().getString(
//						EXTRA_MESSAGE);

			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location)
	{
		// TODO Auto-generated method stub
		
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
	private void locationServiceInitial()
	{
		lms = (LocationManager) getSystemService(LOCATION_SERVICE); // 取得系統定位服務
		Location location = null;
		
		if (lms.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			location = lms.getLastKnownLocation(LocationManager.GPS_PROVIDER); // 使用GPS定位座標
		} 
		else if (lms.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			location = lms
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); // 使用GPS定位座標
		}
		// Criteria criteria = new Criteria(); //資訊提供者選取標準
		// bestProvider = lms.getBestProvider(criteria, true); //選擇精準度最高的提供者
		// Location location = lms.getLastKnownLocation(bestProvider);
	}

	public void post(String user_name ,String pass_word)
	{
		if (user_name.length() > 0 && pass_word.length() > 0)
		{
			userName = user_name;

			List<NameValuePair> parems = new ArrayList<NameValuePair>();
			
			pd = ProgressDialog.show(StartActivity.this, "請稍後", "載入中，請稍後...");
			
			parems.add(new BasicNameValuePair("username", user_name));
			parems.add(new BasicNameValuePair("password", pass_word));
			parems.add(new BasicNameValuePair("device_token", device_token));
			parems.add(new BasicNameValuePair("device_os", "android"));

			SendPostRunnable post = new SendPostRunnable(
					getString(R.string.IP) + getString(R.string.Login),
					parems, new SendPostRunnable.Callback()
					{
						@Override
						public void service_result(Message msg)
						{
							// TODO Auto-generated method stub
							pd.dismiss();

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
									mContext);
							dialog.setTitle(getString(R.string.dialog_title1));
							dialog.setIcon(android.R.drawable.ic_dialog_alert);
							String mesString = messageString;
							dialog.setMessage(mesString);
							dialog.setCancelable(false);
							dialog.setPositiveButton(
									getString(R.string.dialog_check),
									new DialogInterface.OnClickListener()
									{
										public void onClick(
												DialogInterface dialog,
												int which)
										{
											String username = null;
											String password = null;
											String name = null;
											String user_id = null;
											String device_os = null;
											String device_t = null;
											String user_city = null;
											String user_city_detail = null;
											String city_id = null;
											String city_detail_id = null;
											String cellphone = null;
											boolean resString = false;

											try
											{
												resString = result
														.getBoolean("result");
												username = result
														.getString("username");
												password = result
														.getString("password");
												name = result
														.getString("name");
												user_id = result
														.getString("user_id");
												device_os = result
														.getString("device_os");
												device_t = result
														.getString("device_token");
												user_city = result
														.getString("user_city");
												user_city_detail = result
														.getString("user_city_detail");
												city_id = result
														.getString("city_id");
												city_detail_id = result
														.getString("city_detail_id");
												cellphone = result
														.getString("cellphone");
											}
											catch (JSONException e)
											{
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

											if (resString)
											{
												pd = ProgressDialog.show(StartActivity.this, "請稍後", "載入中，請稍後...");
												
												DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(
														username,
														mContext, "userphoto", getResources()
																.getString(R.string.downloadUserImage),
														new DownloadImageRunnable.Callback()
														{
															@Override
															public void service_result()
															{
																PageUtil mSysUtil = new PageUtil(StartActivity.this);

																mSysUtil.exit(0);
																pd.dismiss();

															}

															@Override
															public void err_service_result()
															{
																// TODO Auto-generated method stub
																PageUtil mSysUtil = new PageUtil(StartActivity.this);

																mSysUtil.exit(0);
																pd.dismiss();
															}
														});
												dImageRunnable.downLoadImage();
												
												pullData();
												pullUserData(city_id,city_detail_id);
												
												DBTools.creatUserData(
														mContext,
														username, password,
														name, user_id,
														device_t,
														device_os,
														user_city,
														user_city_detail,
														city_id,
														city_detail_id,
														cellphone);
												
											
											}
										}
									});
							dialog.show();
						}
					});

			Thread t = new Thread(post);

			t.start();
		}
		else 
		{
			DialogShow dialogShow = new DialogShow();
			dialogShow.showStyle1(StartActivity.this, "訊息", "帳號密碼未輸入", "確定！", new DialogShow.Callback()
			{

				@Override
				public void work()
				{
					// TODO Auto-generated method stub
					
				}

				@Override
				public void cancel()
				{
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	public void pullData()
	{
		List<NameValuePair> parems = new ArrayList<NameValuePair>();
		parems.add(new BasicNameValuePair("username", userName));
		
		pd1 = ProgressDialog.show(StartActivity.this, "請稍後", "載入中，請稍後...");
		
		SendPostRunnable post = new SendPostRunnable(getString(R.string.IP)
				+ getString(R.string.PullData), parems,
				new SendPostRunnable.Callback()
				{
					@Override
					public void service_result(Message msg)
					{
						// TODO Auto-generated method stub
						pd1.dismiss();
						
						Bundle countBundle = msg.getData();

						@SuppressWarnings("unchecked")
						HashMap<String, Object> resultData = (HashMap<String, Object>) countBundle
								.getSerializable("resultData");
						
						final JSONObject result = (JSONObject) resultData
								.get("Data");

						JSONArray list = null;
						String messageString = null;
						int num = 0;

						try
						{
							messageString = result.getString("Message");
							list = result.getJSONArray("data");
							num = result.getInt("num");
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						for (int i = 0; i < num; i++)
						{
							try
							{
								JSONObject res = list.getJSONObject(i);

								DBTools.saveEventData(mContext, res,
										"event");
								
								final Long id = res.getLong("data_id");
								
								if(res.getString("image").equals("1"))
								{
									DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(
											String.valueOf(id),
											mContext, "pushphoto",
											getResources().getString(
													R.string.downloadRequestImage));
									dImageRunnable.downLoadImage();
								}
							}
							catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				});

		Thread t = new Thread(post);

		t.start();
	}
	public void pullUserData(String city_id,String area_id)
	{
		pd2 = ProgressDialog.show(StartActivity.this, "請稍後", "載入中，請稍後...");
		
		String send_time = TimeUtils.getNowTime();
		
		List<NameValuePair> parems = new ArrayList<NameValuePair>();

		parems.add(new BasicNameValuePair("username", userName));
		parems.add(new BasicNameValuePair("send_time", send_time));
		parems.add(new BasicNameValuePair("city_id", city_id));
		parems.add(new BasicNameValuePair("area_id", area_id));

		SendPostRunnable post = new SendPostRunnable(getString(R.string.IP)
				+ getString(R.string.PullLogin), parems,
				new SendPostRunnable.Callback()
				{
					@Override
					public void service_result(Message msg)
					{
						// TODO Auto-generated method stub
						pd2.dismiss();
						Bundle countBundle = msg.getData();
						
						@SuppressWarnings("unchecked")
						HashMap<String, Object> resultData = (HashMap<String, Object>) countBundle
								.getSerializable("resultData");

						final JSONObject result = (JSONObject) resultData
								.get("Data");
						
						JSONArray list = null;
						int num = 0;

						try
						{
							list = result.getJSONArray("data");
							num = result.getInt("num");
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						for (int i = 0; i < num; i++)
						{
							try
							{
								JSONObject res = list.getJSONObject(i);
								
								final Long id = res.getLong("data_id");
								
								DBTools.savePushData(mContext, res);
								
								if (res.getString("image").equals("1"))
								{
									DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(
											String.valueOf(id),
											mContext, "pushphoto",
											getResources().getString(
													R.string.downloadRequestImage),
											new DownloadImageRunnable.Callback()
											{
												@Override
												public void service_result()
												{
													// TODO
													// Auto-generated
													// method stub
													DBTools.updatePushDBImage(mContext, id);
												}

												@Override
												public void err_service_result()
												{
													// TODO Auto-generated method stub
													
												}
											});
									dImageRunnable.downLoadImage();
								}
								
							}
							catch (JSONException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});

		Thread t = new Thread(post);

		t.start();
	}
	
	protected void onActivityResult(int rsquestCode, int resultCode, Intent data)
	{
		Session.getActiveSession().onActivityResult(this, rsquestCode,
				resultCode, data);
	}

}
