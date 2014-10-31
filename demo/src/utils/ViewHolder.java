package utils;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder
{
	public ImageView mIcon;
	public TextView mTitie;
	public TextView mDetail;
	public TextView mTime;
	public String img_path;
	public Bitmap bitmap; 
	
	public ViewHolder(ImageView icon, TextView title, TextView text,TextView time)
	{
		this.mIcon = icon;
		this.mTitie = title;
		this.mDetail = text;
		this.mTime = time;
	}
}
