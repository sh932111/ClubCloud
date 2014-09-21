package startprogram;

import getdb.UserDB;
import getfunction.FolderFunction;
import httpfunction.DownloadImage;
import httpfunction.DownloadImageRunnable;
import httpfunction.SendPostRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import pagefunction.PageUtil;

import com.candroidsample.R;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginPage extends Activity
{
	Button loginButton;
	EditText usernameText;
	EditText passwordText;
	public static final String PREF = "get_pref";
	public static final String GET_ID = "get_id";
	String device_token;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);

		usernameText = (EditText) findViewById(R.id.user);
		passwordText = (EditText) findViewById(R.id.pass);

		SharedPreferences get_pref = getSharedPreferences(PREF, 0);

		String get_id = get_pref.getString(GET_ID, "");

		if (!"".equals(get_id))
		{
			device_token = get_id;
		}

		loginButton = (Button) findViewById(R.id.loginbt);
		loginButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				List<NameValuePair> parems = new ArrayList<NameValuePair>();

				parems.add(new BasicNameValuePair("username", usernameText
						.getText().toString()));
				parems.add(new BasicNameValuePair("password", passwordText
						.getText().toString()));
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
												}
												catch (JSONException e)
												{
													// TODO Auto-generated catch
													// block
													e.printStackTrace();
												}

												if (resString)
												{
													DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(username, LoginPage.this,"pushphoto",getResources().getString(R.string.downloadUserImage));
													dImageRunnable.downLoadImage();
													

													UserDB mDbHelper = new UserDB(
															LoginPage.this);

													mDbHelper.open();

													mDbHelper.create(username,
															password, name,
															user_id, device_t,
															device_os,
															user_city,
															user_city_detail,
															city_id,
															city_detail_id);

													mDbHelper.close();

													PageUtil mSysUtil = new PageUtil(
															LoginPage.this);

													mSysUtil.exit(0);
													
												}
											}
										});
								dialog.show();
							}
						});

				Thread t = new Thread(post);

				t.start();

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

	
}
