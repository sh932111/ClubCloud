package com.candroidsample;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter
{
	private LayoutInflater myInflater;
	CharSequence[] title_array = null;
	CharSequence[] time1_array = null;
	CharSequence[] time2_array = null;
	CharSequence[] icon_array = null;   
	
	public MyAdapter(Context ctxt, CharSequence[] title ,CharSequence[] time1,CharSequence[] time2,CharSequence[] icon_show )
	{
		myInflater = LayoutInflater.from(ctxt);
		this.title_array = title;
		this.time1_array = time1;
		this.time2_array = time2;
		this.icon_array = icon_show;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
	
		return title_array.length;
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return title_array[position];
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position,View convertView,ViewGroup parent)
	{
		ViewTag viewTag;
		
		if(convertView == null)
		{
			
			convertView = myInflater.inflate(R.layout.mylist, null);
			
			viewTag = new ViewTag(
					(ImageView)convertView.findViewById(R.id.imglogo),
					(TextView) convertView.findViewById(R.id.list_title),
					(TextView) convertView.findViewById(R.id.time_text)
					);
			
			convertView.setTag(viewTag);
		}
		
		else
		{
			viewTag = (ViewTag) convertView.getTag();
		}
		
		viewTag.title.setText(title_array[position]);
		viewTag.text.setText(time1_array[position].toString()+" "+time2_array[position].toString());
		
		int v = Integer.parseInt(icon_array[position].toString());
		
		if (v == 0) 
		{
			viewTag.icon.setBackgroundColor(Color.YELLOW);
		}
		else if (v == 1) 
		{
			viewTag.icon.setBackgroundColor(Color.RED);
		}
		
		return convertView;
	}
	
	
	class ViewTag
	{
		ImageView icon;
		TextView title;
		TextView text;
		
		public ViewTag(ImageView icon, TextView title, TextView text)
		{
			this.icon = icon;
			this.title = title;
			this.text = text;
		}
	}

}
