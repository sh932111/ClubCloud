package com.candroidsample;

import getdb.DBManager;
import getfunction.GetServerMessage;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			final String resString = msg.getData().getString("result");
			String messageString = msg.getData().getString("Message");

			AlertDialog.Builder dialog = new AlertDialog.Builder(Register1.this);
			dialog.setTitle("訊息"); // 設定dialog 的title顯示內容
			dialog.setIcon(android.R.drawable.ic_dialog_alert);// 設定dialog 的ICO
			String mesString = messageString;

			dialog.setMessage(mesString);

			dialog.setCancelable(false); // 關閉 Android 系統的主要功能鍵(menu,home等...)
			dialog.setPositiveButton("確定",
					new DialogInterface.OnClickListener()
					{
						public void onClick(DialogInterface dialog, int which)
						{
							// 按下"收到"以後要做的事情

							if (resString.equals("1"))
							{
								Intent intent = new Intent();

								Bundle bundle = new Bundle();

								bundle.putString("user_id", EID
										+ idNumberEdit.getText().toString());

								bundle.putString("city", CITY);
								bundle.putString("city_detail", DETAILCITY);
								bundle.putString("city_id", CITY_id);
								bundle.putString("city_detail_id", DETAILCITY_id);

								intent.putExtras(bundle);

								intent.setClass(Register1.this, Register2.class);
								startActivity(intent);
							}
						}
					});
			dialog.show();
		}
	};

	public class sendPostRunnable implements Runnable
	{
		String URL = null;
		String ID = null;

		public sendPostRunnable(String url, String id)
		{
			this.URL = url;
			this.ID = id;
		}

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			GetServerMessage demo = new GetServerMessage();

			List<NameValuePair> parems = new ArrayList<NameValuePair>();

			if (ID.length() > 0)
			{
				parems.add(new BasicNameValuePair("user_id", ID));
			}

			Dictionary<String, String> dic = demo.stringQuery(URL, parems);

			String message = dic.get("Message");
			String result = dic.get("result");

			Bundle countBundle = new Bundle();
			countBundle.putString("Message", message);
			countBundle.putString("result", result);

			Message msg = new Message();
			msg.setData(countBundle);

			mHandler.sendMessage(msg);
		}

	}

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
		CITY = "花蓮縣";
		DETAILCITY = "花蓮市";
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

				sendPostRunnable post = new sendPostRunnable(
						"http://192.168.1.31:8888/ClubCloud/Register1.php",
						user_id);

				Thread t = new Thread(post);

				t.start();

				// TODO Auto-generated method stub
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

		int rows_num = mCursor.getCount(); // 取得資料表列數
		if (rows_num != 0)
		{
			mCursor.moveToFirst(); // 將指標移至第一筆資料
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
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>
		(this,android.R.layout.simple_spinner_dropdown_item,cs1);
		
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

		int rows_num = dCursor.getCount(); // 取得資料表列數
		if (rows_num != 0)
		{
			dCursor.moveToFirst(); // 將指標移至第一筆資料
			for (int i = 0; i < rows_num; i++)
			{
				columnArray1.add(dCursor.getString(1));
				columnArray2.add(dCursor.getString(2));
				if (i==0)
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
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>
		(this,android.R.layout.simple_spinner_dropdown_item,cs2);
		
		city_detail_spinner.setAdapter(adapter);

		city_detail_spinner.setOnItemSelectedListener(spnPerferListener);
		
		startManagingCursor(dCursor);
		
	}
	
}
