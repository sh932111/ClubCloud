package homedetail;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import pagefunction.PageUtil;
import uifunction.ShowToolbar;
import getdb.UserDB;
import getfunction.FolderFunction;
import getfunction.ImageFunction;
import httpfunction.UploadImage;

import com.candroidsample.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PersonalInformation extends CloudActivity
{
	private DisplayMetrics mPhone;
	private ListView listView;
	private ImageView userImage;
	private final static int CAMERA = 66;
	private final static int Album = 67;
	Bitmap resImage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_information);

		mPhone = new DisplayMetrics();

		getWindowManager().getDefaultDisplay().getMetrics(mPhone);

		userImage = (ImageView) findViewById(R.id.user_image);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				getResources().getDisplayMetrics().widthPixels / 3,
				getResources().getDisplayMetrics().heightPixels / 3);

		userImage.setLayoutParams(layoutParams);

		Button button01 = (Button) findViewById(R.id.openQR);

		button01.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						"com.google.zxing.client.android.SCAN"); // 開啟條碼掃描器
				intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // 設定QR Code參數
				startActivityForResult(intent, 1); // 要求回傳1
			}

		});
		Button button02 = (Button) findViewById(R.id.changeImg);

		button02.setOnClickListener(new Button.OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				showDialog();
			}

		});
		 
		setList();
		
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
						PageUtil mSysUtil = new PageUtil(
								PersonalInformation.this);
						mSysUtil.exit(msg + 1);
						finish();
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.personal_information, menu);
		return true;
	}

	public void setList()
	{
		listView = (ListView) findViewById(R.id.listView);

		getData();

	}

	public void getData()
	{
		UserDB userDB = new UserDB(PersonalInformation.this);

		userDB.open();

		 ArrayList<String> array_list = userDB.getAllDate();
		 
		 if (array_list != null)
		{
			 setImage(array_list.get(0));
			 TextView usernameTextView  = (TextView)findViewById(R.id.username);
			 usernameTextView.setText("帳號："+array_list.get(0));
			 TextView userTextView  = (TextView)findViewById(R.id.user);
			 userTextView.setText("名字："+array_list.get(2));
			 TextView cityTextView  = (TextView)findViewById(R.id.city);
			 cityTextView.setText("縣市："+array_list.get(6));
			 TextView areaTextView  = (TextView)findViewById(R.id.area);
			 areaTextView.setText("區域："+array_list.get(7));
		}
		 
		 userDB.close();
	}
	
	public String getUserName()
	{
		UserDB userDB = new UserDB(PersonalInformation.this);

		userDB.open();

		ArrayList<String> array_list = userDB.getAllDate();
	
		userDB.close();
	
		return array_list.get(0);
	}

	public void setImage(String index)
	{
		ImageFunction get_image = new ImageFunction();

		String app_path = this.getExternalFilesDir(null).getAbsolutePath() + "/"+"userphoto"+"/" + index + ".png";
		
		userImage.setImageBitmap(get_image.getBitmapFromSDCard(app_path));
	}
	public void showDialog()
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				PersonalInformation.this);
		dialog.setTitle(getString(R.string.dialog_title1));
		dialog.setIcon(android.R.drawable.ic_dialog_alert);
		String mesString = getString(R.string.check_img_src);
		dialog.setMessage(mesString);
		dialog.setCancelable(false);
		dialog.setPositiveButton(getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,
							int which)
					{

					}
				});
		dialog.setNegativeButton(getString(R.string.dialog_album),
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,
							int which)
					{
						// TODO Auto-generated method stub
						Intent intent = new Intent(
								Intent.ACTION_GET_CONTENT, null);
						intent.setType("image/*");
						startActivityForResult(intent, Album);
					}
				});
		dialog.setNeutralButton(getString(R.string.dialog_camera),
				new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog,
							int which)
					{
						// TODO Auto-generated method stub

						Intent intent = new Intent(
								"android.media.action.IMAGE_CAPTURE");
						startActivityForResult(intent, CAMERA);
					}
				});
		dialog.show();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1)
		{ // startActivityForResult回傳值
			if (resultCode == RESULT_OK)
			{
				String contents = data.getStringExtra("SCAN_RESULT"); // 取得QR
																		// Code內容
				System.out.println(contents);
			}
		}
		if ((CAMERA == requestCode || Album == requestCode) && data != null)
		{
			Uri uri = data.getData();

			if (uri != null)
			{
				ContentResolver cr = this.getContentResolver();

				Cursor cursor = cr.query(uri, null, null, null, null);
				cursor.moveToFirst();

				try
				{
					Bitmap bitmap = BitmapFactory.decodeStream(cr
							.openInputStream(uri));
					
					ImageFunction getFunction = new ImageFunction();

					if (bitmap.getWidth() > bitmap.getHeight())
						resImage = getFunction.ScalePic(bitmap, mPhone.widthPixels);

					else
						resImage = getFunction.ScalePic(bitmap, mPhone.widthPixels);

					userImage.setImageBitmap(resImage);
					
					String user = getUserName();
					
					String app_path = getExternalFilesDir(null).getAbsolutePath() + "/"+"userphoto"+"/" + user + ".png";
					
					FolderFunction setfolder = new FolderFunction();

					boolean check = setfolder.saveImage(resImage,  app_path);
					
					if (check)
					{
						UploadImage uploadImage = new UploadImage(getString(R.string.IP)
								+ getString(R.string.uploadUserImage), app_path,
								String.valueOf(user));

						Thread t2 = new Thread(uploadImage);

						t2.start();
					}
					
				}
				catch (FileNotFoundException e)
				{

				}
			}
		}
	}
}
