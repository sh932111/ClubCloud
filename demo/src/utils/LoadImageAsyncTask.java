package utils;

import getfunction.ImageFunction;

import android.os.AsyncTask;

public class LoadImageAsyncTask extends AsyncTask<ViewHolder, Void, ViewHolder> {

	@Override
	protected ViewHolder doInBackground(ViewHolder... params) {
		// TODO Auto-generated method stub
		//load image directly
		ViewHolder viewHolder = params[0];
		
		ImageFunction get_image = new ImageFunction();

		String app_path = viewHolder.img_path;
		
		viewHolder.bitmap = (get_image.getBitmapFromSDCard(app_path));
		
		return viewHolder;
	}
	
	@Override
	protected void onPostExecute(ViewHolder result) {
		// TODO Auto-generated method stub
		result.mIcon.setImageBitmap(result.bitmap);
	}
}
