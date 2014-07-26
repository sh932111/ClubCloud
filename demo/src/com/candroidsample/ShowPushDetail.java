package com.candroidsample;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowPushDetail extends Activity
{
	TextView datetext;
	TextView timetext;
	TextView titletext;
	TextView detailtext;

	Button deleteButton;
	Button insertButton;

	Long Id;
	String Title;
	String Detail;
	String Time;
	String Date;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_push_detail);

		findView();

	}

	public void findView()
	{
		datetext = (TextView) findViewById(R.id.datetext);
		timetext = (TextView) findViewById(R.id.timetext);
		titletext = (TextView) findViewById(R.id.titletext);
		detailtext = (TextView) findViewById(R.id.detailtext);

		insertButton = (Button) findViewById(R.id.check);
		insertButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				DB mDbHelper = new DB(ShowPushDetail.this);

				mDbHelper.open();

				mDbHelper.create(Id, Title, Detail, Date, Time, "1");

				mDbHelper.close();

				AlertDialog.Builder dialog = new AlertDialog.Builder(
						ShowPushDetail.this);
				dialog.setTitle("訊息"); // 設定dialog 的title顯示內容
				dialog.setIcon(android.R.drawable.ic_dialog_alert);// 設定dialog
																	// 的ICO
				String mesString = "新增成功！是否要刪除此推撥訊息？";
				dialog.setMessage(mesString);
				dialog.setCancelable(false); // 關閉 Android
												// 系統的主要功能鍵(menu,home等...)
				dialog.setNegativeButton("不刪除",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{
								// TODO Auto-generated method stub
								
							}

						});
				dialog.setPositiveButton("刪除",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{
								PushDB mDbHelper = new PushDB(
										ShowPushDetail.this);

								mDbHelper.open();

								mDbHelper.delete(Id);

								mDbHelper.close();

								finish();
							}
						});
				dialog.show();

			}
		});

		deleteButton = (Button) findViewById(R.id.cancel);
		deleteButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				PushDB mDbHelper = new PushDB(ShowPushDetail.this);

				mDbHelper.open();

				mDbHelper.delete(Id);

				mDbHelper.close();

				finish();
			}
		});
		setValue();
	}

	public void setValue()
	{
		Intent intent = this.getIntent();

		Bundle bundle = intent.getExtras();// 取得Bundle

		Id = bundle.getLong("ID");

		Title = bundle.getString("Title");
		Detail = bundle.getString("Message");
		Time = bundle.getString("Time");
		Date = bundle.getString("Date");

		titletext.setText("標題 : " + Title);
		detailtext.setText(Detail);
		timetext.setText("時間 : " + Time);
		datetext.setText("日期 : " + Date);

		PushDB mDbHelper = new PushDB(ShowPushDetail.this);

		mDbHelper.open();

		mDbHelper.updateLook(Id, "0");

		mDbHelper.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_push_detail, menu);
		return true;
	}

}
