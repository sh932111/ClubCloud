package startprogram;

import getfunction.DBTools;
import getfunction.ImageFunction;
import getfunction.PageUtil;
import homedetail.PersonalInformation;
import homedetail.ShowPushDetail;
import httpfunction.DownloadImageRunnable;
import httpfunction.SendPostRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.TimeUtils;

import com.candroidsample.R;
import com.candroidsample.StartActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends Activity
{
	Button loginButton;
	Button fbButton;

	EditText usernameText;
	EditText passwordText;

	String userName= "";
	
	public static final String PREF = "get_pref";
	public static final String GET_ID = "get_id";

	String device_token;

	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);

		Intent intent = this.getIntent();

		Bundle bundle = intent.getExtras();

		device_token = bundle.getString("device_token");
		
		usernameText = (EditText) findViewById(R.id.user);
		passwordText = (EditText) findViewById(R.id.pass);

		loginButton = (Button) findViewById(R.id.loginbt);
		loginButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				post(usernameText.getText().toString(),passwordText.getText().toString());
			}

		});

		fbButton = (Button) findViewById(R.id.fbbutton);
		fbButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Session.openActiveSession(LoginPage.this, true,
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_page, menu);
		return true;
	}

	public void post(String user_name ,String pass_word)
	{
		userName = user_name;

		List<NameValuePair> parems = new ArrayList<NameValuePair>();
		
		pd = ProgressDialog.show(LoginPage.this, "請稍後", "載入中，請稍後...");
		
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
						
							System.out.println(result);
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						//
						AlertDialog.Builder dialog = new AlertDialog.Builder(
								LoginPage.this);
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

											DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(
													username,
													LoginPage.this, "userphoto", getResources()
															.getString(R.string.downloadUserImage),
													new DownloadImageRunnable.Callback()
													{
														@Override
														public void service_result()
														{

															Intent intent = new Intent();
															intent.setClass(LoginPage.this, PersonalInformation.class);
															startActivity(intent);
														}

														@Override
														public void err_service_result()
														{
															// TODO Auto-generated method stub

															Intent intent = new Intent();
															intent.setClass(LoginPage.this, PersonalInformation.class);
															startActivity(intent);
														}
													});
											dImageRunnable.downLoadImage();
											
											pullData();
											pullUserData(city_id,city_detail_id);
											
											DBTools.creatUserData(
													LoginPage.this,
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
	
	public void pullData()
	{
		List<NameValuePair> parems = new ArrayList<NameValuePair>();
		parems.add(new BasicNameValuePair("username", userName));

		SendPostRunnable post = new SendPostRunnable(getString(R.string.IP)
				+ getString(R.string.PullData), parems,
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

								DBTools.saveEventData(LoginPage.this, res,
										"event");
								
								final Long id = res.getLong("data_id");
								
								if(res.getString("image").equals("1"))
								{
									DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(
											String.valueOf(id),
											LoginPage.this, "pushphoto",
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
								
								DBTools.savePushData(LoginPage.this, res);
								
								if (res.getString("image").equals("1"))
								{
									DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(
											String.valueOf(id),
											LoginPage.this, "pushphoto",
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
													DBTools.updatePushDBImage(LoginPage.this, id);
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
