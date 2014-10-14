package homedetail;

import com.candroidsample.R;

import getdb.TravelDB;
import getdb.PushDB;
import getfunction.DBTools;
import getfunction.DialogShow;
import getfunction.ImageFunction;
import httpfunction.DownloadImageRunnable;
import pagefunction.PageUtil;
import uifunction.ShowScrollView;
import uifunction.ShowToolbar;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ShowPushDetail extends Activity
{
	Long id;
	ShowScrollView showScrollView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_detail);
				
		Intent intent = this.getIntent();

		Bundle bundle = intent.getExtras();

		id = bundle.getLong("ID");

		Bundle get_bundle = DBTools.getPushDetail(ShowPushDetail.this, id);

		DBTools.updatePushDBLook(ShowPushDetail.this, id);
		
		float h_size = 1920 / getResources().getDisplayMetrics().heightPixels;

		float s_size = 350 / h_size;

		showScrollView = new ShowScrollView();
		showScrollView.showView(
				(LinearLayout) findViewById(R.id.LinearLayout1), this,
				get_bundle, getResources().getDisplayMetrics().heightPixels
						- getResources().getDisplayMetrics().widthPixels
						/ ShowToolbar.getMenuNum(this) - (int) s_size);

		showScrollView.titleView.setEnabled(false);
		showScrollView.listView.setEnabled(false);
		showScrollView.chooseDayBt.setVisibility(View.INVISIBLE);
		showScrollView.chooseImgBt.setVisibility(View.INVISIBLE);
		showScrollView.chooseTimeBt.setVisibility(View.INVISIBLE);

		String image_check = get_bundle.getString("Image");
		
		if (Integer.parseInt(image_check) == 1)
		{
			DownloadImageRunnable dImageRunnable = new DownloadImageRunnable(
					String.valueOf(id), this, "pushphoto", getResources()
							.getString(R.string.downloadRequestImage),
					new DownloadImageRunnable.Callback()
					{
						@Override
						public void service_result()
						{
							// TODO Auto-generated method stub
							ImageFunction get_image = new ImageFunction();

							String app_path = getExternalFilesDir(null)
									.getAbsolutePath()
									+ "/"
									+ "pushphoto"
									+ "/" + id + ".png";

							showScrollView.showImageView
									.setImageBitmap(get_image
											.getBitmapFromSDCard(app_path));
							
							updateDBImage();
						}
					});
			dImageRunnable.downLoadImage();
		}
		else if(Integer.parseInt(image_check) == 2)
		{
			ImageFunction get_image = new ImageFunction();

			String app_path = getExternalFilesDir(null)
					.getAbsolutePath()
					+ "/"
					+ "pushphoto"
					+ "/" + id + ".png";

			showScrollView.showImageView
					.setImageBitmap(get_image
							.getBitmapFromSDCard(app_path));
		}
		ShowToolbar showToolbar = new ShowToolbar();
		showToolbar.showToolbar(
				(LinearLayout) findViewById(R.id.LinearLayout1),
				this,
				getResources().getDisplayMetrics().widthPixels
						/ ShowToolbar.getMenuNum(this), 1,
				new ShowToolbar.Callback()
				{

					@Override
					public void service_result(int msg)
					{
						// TODO Auto-generated method stub
						PageUtil mSysUtil = new PageUtil(ShowPushDetail.this);
						mSysUtil.exit(msg + 1);
						finish();

					}
				});

		Button deleteButton = (Button) findViewById(R.id.cancel);
		deleteButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				DialogShow show = new DialogShow();
				show.show(ShowPushDetail.this, getString(R.string.dialog_title1),
						getString(R.string.dialogmsg2),
						getString(R.string.dialog_check),
						getString(R.string.dialog_cancel),
						new DialogShow.Callback()
						{
							@Override
							public void work()
							{
								deleteDB();
							}

							@Override
							public void cancel()
							{
								// TODO Auto-generated method stub
								System.out.println("cancel");
							}
						});
			}
		});

		Button insertButton = (Button) findViewById(R.id.choosetime);
		insertButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				creatDB();
				
				DialogShow show = new DialogShow();
				show.show(ShowPushDetail.this,
						getString(R.string.dialog_title1),
						getString(R.string.dialogmsg1),
						getString(R.string.nodelete),
						getString(R.string.delete),
						new DialogShow.Callback()
						{
							@Override
							public void work()
							{
								
							}

							@Override
							public void cancel()
							{
								// TODO Auto-generated method stub
								deleteDB();
							}
						});
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_detail, menu);
		return true;
	}


	public void updateDBImage()
	{

		PushDB mDbHelper = new PushDB(ShowPushDetail.this);

		mDbHelper.open();

		mDbHelper.updateImage(id, "2");

		mDbHelper.close();
	}

	public void deleteDB()
	{
		PushDB mDbHelper = new PushDB(ShowPushDetail.this);

		mDbHelper.open();

		mDbHelper.delete(id);

		mDbHelper.close();

		finish();
	}

	public void creatDB()
	{
		PushDB pushDB = new PushDB(this);

		pushDB.open();

		Bundle get_bundle = pushDB.get(id);
		
		TravelDB mDbHelper = new TravelDB(ShowPushDetail.this);

		mDbHelper.open();

		String Title = "";
		String Time = "";
		String Detail = "";
		String Date = "";
		String image_check = "";
		String Address = "";
		
		if (get_bundle != null)
		{
			Title = get_bundle.getString("Title");
			Detail = get_bundle.getString("Message");
			Time = get_bundle.getString("Time");
			Date = get_bundle.getString("Date");
			image_check = get_bundle.getString("Image");
			Address =get_bundle.getString("Address");
		}
		
		mDbHelper.create(id, Title, Detail, Date, Time, "1" ,image_check,Address);

		mDbHelper.close();
		
		pushDB.close();

	}
}
