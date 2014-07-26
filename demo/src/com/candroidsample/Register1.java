package com.candroidsample;

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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class Register1 extends Activity
{

	private Spinner spinner;
	String EID;
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
								
								bundle.putString("user_id", EID + idNumberEdit.getText().toString());

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register1, menu);
		return true;
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
			}
			// TODO Auto-generated method stub

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0)
		{
			// TODO Auto-generated method stub

		}
	};
}
