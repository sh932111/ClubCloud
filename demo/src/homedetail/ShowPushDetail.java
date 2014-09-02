package homedetail;


import com.candroidsample.R;
import com.candroidsample.R.id;
import com.candroidsample.R.layout;
import com.candroidsample.R.menu;

import getdb.DB;
import getdb.PushDB;
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
				dialog.setTitle("�T��"); // �]�wdialog ��title��ܤ��e
				dialog.setIcon(android.R.drawable.ic_dialog_alert);// �]�wdialog
																	// ��ICO
				String mesString = "順便刪除此資料?";
				dialog.setMessage(mesString);
				dialog.setCancelable(false);
				dialog.setNegativeButton("���R��",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog,
									int which)
							{
								// TODO Auto-generated method stub
								
							}

						});
				dialog.setPositiveButton("�R��",
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

		Bundle bundle = intent.getExtras();// ��oBundle

		Id = bundle.getLong("ID");

		Title = bundle.getString("Title");
		Detail = bundle.getString("Message");
		Time = bundle.getString("Time");
		Date = bundle.getString("Date");

		titletext.setText("���D : " + Title);
		detailtext.setText(Detail);
		timetext.setText("�ɶ� : " + Time);
		datetext.setText("��� : " + Date);

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
