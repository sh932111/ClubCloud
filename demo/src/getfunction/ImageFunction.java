package getfunction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

public class ImageFunction
{
	public Bitmap getBitmapFromSDCard(String file)
	{
		try
		{
			// Bitmap bmImage = BitmapFactory.decodeResource(getResources(),
			// R.drawable.icon);
			String sd = Environment.getExternalStorageDirectory().toString();
			Bitmap bitmap = BitmapFactory.decodeFile(sd
					+ "/ClubCloud/userphoto/" + file);
			return bitmap;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
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
