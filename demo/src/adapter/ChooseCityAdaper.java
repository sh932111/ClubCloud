package adapter;

import java.util.ArrayList;

import com.candroidsample.R;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChooseCityAdaper extends BaseAdapter
{
	private LayoutInflater myInflater;
	ArrayList<Bundle> mArrayList;
	int Point;
	private int selectItem = 0;

	public ChooseCityAdaper(Context ctxt, ArrayList<Bundle> bundles, int point)
	{
		myInflater = LayoutInflater.from(ctxt);
		this.mArrayList = bundles;
		this.Point = point;
	}

	public void refresh(ArrayList<Bundle> list)
	{
		mArrayList = list;
		notifyDataSetChanged();
	}


	public int getSelectItem()
	{
		return this.selectItem;
	}
	
	public void setSelectItem(int selectItem)
	{
		this.selectItem = selectItem;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub

		return mArrayList.size();
	}

	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewTag viewTag;
		if (convertView == null)
		{
			convertView = myInflater.inflate(R.layout.choose_city_list, null);
			viewTag = new ViewTag(
					(TextView) convertView.findViewById(R.id.cityText));
			convertView.setTag(viewTag);
		} else
		{
			viewTag = (ViewTag) convertView.getTag();
		}

		if (Point == 1)
		{
			viewTag.mTitie.setText(mArrayList.get(position).getString("city"));
		} else if (Point == 2)
		{
			viewTag.mTitie.setText(mArrayList.get(position).getString("area"));
		}

		if (position == selectItem)
		{
			convertView.setBackgroundResource(R.color.blue);
		} else
		{
			convertView.setBackgroundResource(R.color.white);
		}
		return convertView;
	}

	class ViewTag
	{
		TextView mTitie;

		public ViewTag(TextView title)
		{
			this.mTitie = title;
		}
	}
}
