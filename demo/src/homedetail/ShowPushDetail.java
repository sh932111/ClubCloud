package homedetail;

import com.candroidsample.R;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;

import getfunction.DBTools;
import getfunction.DialogShow;
import getfunction.ImageFunction;
import getfunction.PageUtil;
import httpfunction.DownloadImageRunnable;
import uifunction.ShowScrollView;
import uifunction.ShowToolbar;
import utils.AlarmUtils;
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
	private Facebook facebook = new Facebook("719363708136980");
	private UiLifecycleHelper uiHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_detail);

		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		initFb();

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

							DBTools.updatePushDBImage(ShowPushDetail.this, id);
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
				show.show(ShowPushDetail.this,
						getString(R.string.dialog_title1),
						getString(R.string.dialogmsg2),
						getString(R.string.dialog_check),
						getString(R.string.dialog_cancel),
						new DialogShow.Callback()
						{
							@Override
							public void work()
							{
								DBTools.deleteDB(ShowPushDetail.this, id, 1);
								finish();
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
						getString(R.string.delete), new DialogShow.Callback()
						{
							@Override
							public void work()
							{

							}

							@Override
							public void cancel()
							{
								// TODO Auto-generated method stub
								DBTools.deleteDB(ShowPushDetail.this, id, 1);
								finish();
							}
						});
			}
		});

		Button fbButton = (Button) findViewById(R.id.fbbutton);
		fbButton.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if (FacebookDialog.canPresentShareDialog(
						getApplicationContext(),
						FacebookDialog.ShareDialogFeature.SHARE_DIALOG))
				{
					// Publish the post using the Share Dialog
					String app_path = getResources().getString(R.string.IP) +getResources().getString(R.string.downloadRequestImage) 
							+ id + ".png";
					
					FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
							ShowPushDetail.this)
							.setLink("https://developers.facebook.com/android")
							.setName(showScrollView.titleView.getText().toString())
							.setCaption(showScrollView.timeView.getText().toString())
							.setDescription(showScrollView.listView.getText().toString())
							.setPicture(app_path)
							.build();
					uiHelper.trackPendingDialogCall(shareDialog.present());

				} else
				{
					// Fallback. For example, publish the post using the Feed
					// Dialog
				}
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

	public void initFb()
	{
		Session.openActiveSession(ShowPushDetail.this, true,
				new Session.StatusCallback()
				{
					// callback when session changes state
					@Override
					public void call(Session session, SessionState state,
							Exception exception)
					{
						if (session.isOpened())
						{
							// make request to the /me API
							Request.newMeRequest(session,
									new Request.GraphUserCallback()
									{
										@Override
										public void onCompleted(GraphUser user,
												Response response)
										{
											if (user != null)
											{

											}
										}
									}).executeAsync();
						}

					}

				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback()
				{
					@Override
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data)
					{
						// Log.e("Activity", String.format("Error: %s",
						// error.toString()));
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data)
					{
						// Log.i("Activity", "Success!");
					}
				});
	}

	public void creatDB()
	{
		Bundle get_bundle = DBTools.getPushDetail(ShowPushDetail.this, id);

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
			Address = get_bundle.getString("Address");
		}

		DBTools.addTravel(ShowPushDetail.this, id, Title, Detail, Date, Time,
				"1", image_check, Address);
		
		AlarmUtils.showTravelAlarm(ShowPushDetail.this, Date+" "+Time+ ":"+"00", String.valueOf(id), Title, Detail);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		uiHelper.onDestroy();
	}
}
