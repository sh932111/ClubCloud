package startprogram;

import getdb.DBManager;
import getfunction.DialogShow;
import httpfunction.SendPostRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.candroidsample.R;

import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Register1 extends Activity
{
	private Cursor mCursor;
	private Cursor dCursor;

	CharSequence[] cs1;
	CharSequence[] cs2;

	CharSequence[] cs1_id;
	CharSequence[] cs2_id;

	private Spinner spinner;
	private Spinner city_spinner;
	private Spinner city_detail_spinner;

	String EID;

	String CITY;
	String DETAILCITY;

	String CITY_id;
	String DETAILCITY_id;

	private EditText idNumberEdit;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register1);

		findView();
	}

	public void findView()
	{
		loadData();
		
		CITY = getString(R.string.default_city);
		DETAILCITY = getString(R.string.default_area);
		CITY_id = "U";
		DETAILCITY_id = "970";
		
		spinner = (Spinner) findViewById(R.id.spinnner);
		idNumberEdit = (EditText) findViewById(R.id.editText1);

		ArrayAdapter<CharSequence> adapterBalls = ArrayAdapter
				.createFromResource(this, R.array.EID,
						android.R.layout.simple_spinner_item);
		adapterBalls
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner.setAdapter(adapterBalls);

		spinner.setOnItemSelectedListener(spnPerferListener);

		Button bt1 = (Button) findViewById(R.id.button1);
		bt1.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
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

		});
		setSpinner();
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

		stopManagingCursor(mCursor);
		stopManagingCursor(dCursor);

	}

	public void loadData()
	{
		DBManager dbManager = new DBManager(Register1.this);

		dbManager.openDatabase();

		mCursor = dbManager.getCityAll();
	}

	private Spinner.OnItemSelectedListener spnPerferListener = new Spinner.OnItemSelectedListener()
	{
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3)
		{
			if (spinner == arg0)
			{
				EID = arg0.getSelectedItem().toString();
				System.out.println(EID);
			}
			else if (city_spinner == arg0)
			{
				CITY = arg0.getSelectedItem().toString();

				CITY_id = cs1_id[arg2].toString();

				setDetail();
			}
			else if (city_detail_spinner == arg0)
			{
				DETAILCITY = arg0.getSelectedItem().toString();

				DETAILCITY_id = cs2_id[arg2].toString();

			}

			// TODO Auto-generated method stub
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0)
		{
			// TODO Auto-generated method stub

		}
	};

	public void setSpinner()
	{
		ArrayList<String> columnArray1 = new ArrayList<String>();
		ArrayList<String> columnArray2 = new ArrayList<String>();

		int rows_num = mCursor.getCount();
		if (rows_num != 0)
		{
			mCursor.moveToFirst();
			for (int i = 0; i < rows_num; i++)
			{
				columnArray1.add(mCursor.getString(0));
				columnArray2.add(mCursor.getString(1));

				mCursor.moveToNext();
			}
		}
		cs1 = columnArray1.toArray(new CharSequence[columnArray1.size()]);
		cs1_id = columnArray2.toArray(new CharSequence[columnArray2.size()]);

		city_spinner = (Spinner) findViewById(R.id.city_spinnner);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_dropdown_item, cs1);

		city_spinner.setAdapter(adapter);

		city_spinner.setOnItemSelectedListener(spnPerferListener);
		startManagingCursor(mCursor);

		setDetail();
	}

	public void setDetail()
	{
		DBManager dbManager = new DBManager(Register1.this);

		dbManager.openDatabase();

		dCursor = dbManager.get(CITY);

		ArrayList<String> columnArray1 = new ArrayList<String>();
		ArrayList<String> columnArray2 = new ArrayList<String>();

		int rows_num = dCursor.getCount();
		if (rows_num != 0)
		{
			dCursor.moveToFirst();
			for (int i = 0; i < rows_num; i++)
			{
				columnArray1.add(dCursor.getString(1));
				columnArray2.add(dCursor.getString(2));
				if (i == 0)
				{
					DETAILCITY = dCursor.getString(1);
					DETAILCITY_id = dCursor.getString(2);
				}
				dCursor.moveToNext();
			}
		}

		cs2 = columnArray1.toArray(new CharSequence[columnArray1.size()]);
		cs2_id = columnArray2.toArray(new CharSequence[columnArray2.size()]);

		city_detail_spinner = (Spinner) findViewById(R.id.city_detail_spinnner);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_dropdown_item, cs2);

		city_detail_spinner.setAdapter(adapter);

		city_detail_spinner.setOnItemSelectedListener(spnPerferListener);

		startManagingCursor(dCursor);

	}

	public void goNextPage()
	{
		Intent intent = new Intent();

		Bundle bundle = new Bundle();

		bundle.putString("user_id", EID + idNumberEdit.getText().toString());

		bundle.putString("city", CITY);
		bundle.putString("city_detail", DETAILCITY);
		bundle.putString("city_id", CITY_id);
		bundle.putString("city_detail_id", DETAILCITY_id);

		intent.putExtras(bundle);

		intent.setClass(Register1.this, Register2.class);
		startActivity(intent);
	}

}
