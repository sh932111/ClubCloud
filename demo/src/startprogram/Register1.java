package startprogram;

import getfunction.DialogShow;
import httpfunction.SendPostRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import uifunction.ChooseCityDailog;
import uifunction.ChooseCityDailog.chooseCityListener;

import com.candroidsample.R;

import android.os.Bundle;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Register1 extends Activity implements chooseCityListener
{
	private ProgressDialog pd; 

	private Spinner id_spinner;
	
	String EID;

	private EditText idNumberEdit;

	String device_token;

	String CITY="";
	String DETAILCITY="";

	String CITY_id="";
	String DETAILCITY_id="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register1);
		Intent intent = this.getIntent();

		Bundle bundle = intent.getExtras();

		device_token = bundle.getString("device_token");

		findView();
	}

	@SuppressLint("NewApi")
	public void findView()
	{
		
		id_spinner = (Spinner) findViewById(R.id.spinnner);
		idNumberEdit = (EditText) findViewById(R.id.editText1);
		idNumberEdit.setText("109212490");
		
		ArrayAdapter<CharSequence> adapterBalls = ArrayAdapter
				.createFromResource(this, R.array.EID,
						android.R.layout.simple_spinner_item);
		adapterBalls
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		id_spinner.setAdapter(adapterBalls);

		id_spinner.setOnItemSelectedListener(spnPerferListener);

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
		
		Button bt1 = (Button) findViewById(R.id.button1);
		bt1.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if (CITY.length() > 0 && DETAILCITY.length() > 0)
				{
					pd = ProgressDialog.show(Register1.this, "請稍後", "載入中，請稍後..."); 
					
					String user_id = EID + idNumberEdit.getText().toString();
	
					List<NameValuePair> parems = new ArrayList<NameValuePair>();
	
					if (user_id.length() > 0)
					{
						parems.add(new BasicNameValuePair("user_id", user_id));
					}
	
					SendPostRunnable post = new SendPostRunnable(getString(R.string.IP)+
							getString(R.string.Register1), parems,
							new SendPostRunnable.Callback()
							{
								@Override
								public void service_result(Message dic)
								{
									pd.dismiss();
									
									Bundle countBundle = dic.getData();
	
									@SuppressWarnings("unchecked")
									HashMap<String, Object> resultData = (HashMap<String, Object>) countBundle.getSerializable("resultData");
									
									final JSONObject result = (JSONObject) resultData.get("Data");
									
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
									DialogShow show = new DialogShow();
									show.showStyle1(Register1.this,
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
														resString = result.getBoolean("result");
														
													}
													catch (JSONException e)
													{
														// TODO Auto-generated catch block
														e.printStackTrace();
													}
													
													if (resString)
													{
														goNextPage();
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
					
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register1, menu);
		return true;
	}

	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();

	}

	private Spinner.OnItemSelectedListener spnPerferListener = new Spinner.OnItemSelectedListener()
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			if (id_spinner == arg0)
			{
				EID = arg0.getSelectedItem().toString();
			}
						// TODO Auto-generated method stub
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0)
		{
			// TODO Auto-generated method stub

		}
	};


	public void goNextPage()
	{
		Intent intent = new Intent();

		Bundle bundle = new Bundle();

		bundle.putString("user_id", EID + idNumberEdit.getText().toString());

		bundle.putString("city", CITY);
		bundle.putString("city_detail", DETAILCITY);
		bundle.putString("city_id", CITY_id);
		bundle.putString("city_detail_id", DETAILCITY_id);
		bundle.putString("device_token", device_token);

		intent.putExtras(bundle);

		intent.setClass(Register1.this, Register2.class);
		startActivity(intent);
	}

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

