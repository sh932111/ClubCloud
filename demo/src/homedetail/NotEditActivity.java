package homedetail;

import getfunction.DBTools;
import getfunction.ImageFunction;
import getfunction.PageUtil;
import httpfunction.DownloadImageRunnable;
import uifunction.ShowScrollView;
import uifunction.ShowToolbar;

import com.candroidsample.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class NotEditActivity extends Activity
{
	ShowScrollView showScrollView;

	Long id;

	Bitmap resImage = null;
	ScrollView scrollview;
	
	String image_path;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_not_edit);

		Intent intent = this.getIntent();

		final Bundle bundle = intent.getExtras();

		id = bundle.getLong("ID");

		Bundle get_bundle = DBTools.getTravelDetail(NotEditActivity.this, id);
		scrollview = (ScrollView) findViewById(R.id.backgroundScrollView);
		
		String image_check = get_bundle.getString("Image");

		DBTools.updateTravelLook(NotEditActivity.this, id);

		float h_size = 1920 / getResources().getDisplayMetrics().heightPixels;

		float s_size = 350 / h_size;

		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout2);
		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(
				getResources().getDisplayMetrics().widthPixels - 100,
				getResources().getDisplayMetrics().heightPixels
				- getResources().getDisplayMetrics().widthPixels
				/ ShowToolbar.getMenuNum(this) - (int) s_size);
		layout_params.gravity=Gravity.CENTER;
		layout_params.topMargin = 20;
		layout.setLayoutParams(layout_params);
		
		showScrollView = new ShowScrollView();
		showScrollView.showNotEditView(
				(LinearLayout) findViewById(R.id.LinearLayout2), this,
				get_bundle, getResources().getDisplayMetrics().heightPixels
						- getResources().getDisplayMetrics().widthPixels
						/ ShowToolbar.getMenuNum(this) - (int) s_size);

		showScrollView.chooseDayBt.setVisibility(View.INVISIBLE);
		showScrollView.chooseImgBt.setVisibility(View.INVISIBLE);
		showScrollView.chooseTimeBt.setVisibility(View.INVISIBLE);

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
							DBTools.updateTravelImage(NotEditActivity.this, id);
						}

						@Override
						public void err_service_result()
						{
							// TODO Auto-generated method stub
							
						}
					});
			dImageRunnable.downLoadImage();
		} else if (Integer.parseInt(image_check) == 2)
		{
			ImageFunction get_image = new ImageFunction();

			String app_path = getExternalFilesDir(null).getAbsolutePath() + "/"
					+ "pushphoto" + "/" + id + ".png";

			showScrollView.showImageView.setImageBitmap(get_image
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
						PageUtil mSysUtil = new PageUtil(NotEditActivity.this);
						mSysUtil.exit(msg + 1);
						finish();

					}
				});

		Button cancel = (Button) findViewById(R.id.cancel);

		cancel.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				DBTools.deleteDB(NotEditActivity.this, id, 2);

				finish();
			}
		});

		Button check = (Button) findViewById(R.id.check);

		check.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(NotEditActivity.this,
						ShowTravelDetail.class);

				intent.putExtras(bundle);

				startActivityForResult(intent, 0);
			}
		});

		float s_size2 = 250 / h_size;

		LinearLayout.LayoutParams scrollviewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				getResources().getDisplayMetrics().heightPixels
				- getResources().getDisplayMetrics().widthPixels
				/ ShowToolbar.getMenuNum(this) - (int) s_size2);
		
		scrollview.setLayoutParams(scrollviewParams);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.not_edit, menu);
		return true;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK)
		{
			Bundle bundleResult = data.getExtras();
			
			id = bundleResult.getLong("ID");

			Bundle data_bundle = DBTools.getTravelDetail(NotEditActivity.this, id);

			String Title = "";
			String Time = "";
			String Detail = "";
			String Date = "";
			String Address = "";
			
			if (data_bundle != null)
			{
				Title = data_bundle.getString("Title");
				Detail = data_bundle.getString("Message");
				Time = data_bundle.getString("Time");
				Date = data_bundle.getString("Date");
				Address = data_bundle.getString("Address");
			}

			showScrollView.titleView2.setText(Title);
			showScrollView.listView2.setText(Detail);
			showScrollView.dateView.setText(Date);
			showScrollView.timeView.setText(Time);
			showScrollView.addressView2.setText(Address);
			
			ImageFunction get_image = new ImageFunction();

			String app_path = getExternalFilesDir(null).getAbsolutePath() + "/"
					+ "pushphoto" + "/" + id + ".png";

			showScrollView.showImageView.setImageBitmap(get_image
					.getBitmapFromSDCard(app_path));
		}
	}
}
