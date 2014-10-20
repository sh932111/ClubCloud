package uifunction;

import getfunction.ChooseCityAdaper;
import getfunction.DBTools;

import java.util.ArrayList;

import com.candroidsample.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class ChooseCityDailog extends DialogFragment
{
	ListView cityList;
	ListView areaList;

	String cityId;
	String city;
	String areaId;
	String area;
	
	ChooseCityAdaper areasAdaper;
	ChooseCityAdaper citysAdaper;
	
	ArrayList<Bundle> citys;
	ArrayList<Bundle> areas;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.choose_city_dailog, null);

		cityList = ( ListView ) view.findViewById(R.id.cityList); 
		areaList = ( ListView ) view.findViewById(R.id.areaList); 
		
		citys = DBTools.getCity(getActivity());
		
		cityId = citys.get(0).getString("city_id");
		city = citys.get(0).getString("city");
		
		areas = DBTools.getArea(getActivity(), city);
		
		citysAdaper = new ChooseCityAdaper(getActivity(), citys,1);
		areasAdaper = new ChooseCityAdaper(getActivity(), areas,2);
		
		cityList.setAdapter(citysAdaper);
		cityList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				areasAdaper.setSelectItem(0);
				citysAdaper.setSelectItem(position);
				citysAdaper.notifyDataSetInvalidated(); 
				
				cityId = citys.get(position).getString("city_id");
				city = citys.get(position).getString("city");
				
				areas = DBTools.getArea(getActivity(), city);
				areasAdaper.refresh(areas);
			}
		});

		areaList.setAdapter(areasAdaper);
		areaList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				areasAdaper.setSelectItem(position);
				areasAdaper.notifyDataSetInvalidated();
			}
		});
		
		builder.setView(view)
		.setPositiveButton("Check",
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int id)
							{
								Bundle bundle = new Bundle();
								
								bundle.putString("city", citys.get(citysAdaper.getSelectItem()).getString("city"));
								bundle.putString("city_id", citys.get(citysAdaper.getSelectItem()).getString("city_id"));
								bundle.putString("area", areas.get(areasAdaper.getSelectItem()).getString("area"));
								bundle.putString("area_id", areas.get(areasAdaper.getSelectItem()).getString("area_id"));
								
								chooseCityListener listener = (chooseCityListener) getActivity();
								listener.onChooseCityComplete(bundle);
							}
						})
		.setNegativeButton("Close", null);
		
		return builder.create();
	}
	public interface chooseCityListener
	{
		void onChooseCityComplete(Bundle bundle);
	}
}
