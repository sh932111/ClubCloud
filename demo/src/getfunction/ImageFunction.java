package getfunction;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;

public class ImageFunction
{
	public void folderBuild(String folder_name)
	{
		
		File folder = new File(Environment.getExternalStorageDirectory() + "/" + folder_name);
		boolean success = true;
		
	    if (!folder.exists()) 
	    {
	        //Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
	        success = folder.mkdir();
	    }
	    
	    if (success) 
	    {
	        //Toast.makeText(StrokeShowPage.this, "Directory Created", Toast.LENGTH_SHORT).show();
	    }
	    else 
	    {
	      //  Toast.makeText(StrokeShowPage.this, "Failed - Error", Toast.LENGTH_SHORT).show();
	    }
	}
	public Bitmap ScalePic(Bitmap bitmap, int phone)
	{
		// TODO Auto-generated method stub
		float mScale = 1;

		if (bitmap.getWidth() > phone)
		{
			mScale = (float) phone / (float) bitmap.getWidth();

			Matrix mMat = new Matrix();
			mMat.setScale(mScale, mScale);

			Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), mMat, false);
			return mScaleBitmap;

		}
		else
			return bitmap;
	}
	
}
