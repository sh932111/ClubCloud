package getfunction;


import android.graphics.Bitmap;
import android.graphics.Matrix;

public class ImageFunction
{
	
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
