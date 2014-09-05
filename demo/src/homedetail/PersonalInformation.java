package homedetail;

import getdb.UserDB;
import getfunction.ImageFunction;

import com.candroidsample.R;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ListView;

public class PersonalInformation extends Activity
{
	private Cursor mCursor;
	private ListView listView;
	private ImageView userImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_information);
		
		userImage = (ImageView) findViewById(R.id.user_image);
		
		setList();
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
		
		int rows_num = mCursor.getCount();
		
		int path_num = mCursor.getColumnCount();
		
		if (rows_num != 0)
		{
			mCursor.moveToFirst();
			
			for (int j = 0; j < path_num; j++)
			{
				setUI(j);
			}
		}
	}
	
	public void getData()
	{
		UserDB userDB = new UserDB(PersonalInformation.this);
		
		userDB.open();
		
		mCursor = userDB.getAllDate();
	}
	
	@Override
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();

		stopManagingCursor(mCursor);

	}
	
	public void setUI(int index)
	{
		if (index == 0)
		{
			ImageFunction get_image = new ImageFunction();
			
			userImage.setImageBitmap(get_image.getBitmapFromSDCard(mCursor.getString(index) + ".PNG"));
			
		}
	}
}
