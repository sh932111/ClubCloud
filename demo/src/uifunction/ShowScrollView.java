package uifunction;


import android.R;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class ShowScrollView
{
	public static EditText titleView;
	public static EditText listView;
	public static TextView titleView2;
	public static TextView listView2;
	public static TextView dateView;
	public static TextView timeView;
	public static TextView addressView2;
	public static Button chooseTimeBt;
	public static Button chooseDayBt;
	public static Button chooseImgBt;
	public static EditText addressView;
	public static TextView addressTitleView;

	public static ImageView showImageView;
	
	public void showView(LinearLayout layout, Context context,Bundle data_bundle,int height)
	{
		String Title = "";
		String Time = "";
		String Detail = "";
		String Date = "";
		String Address = "";
		
		if (data_bundle != null)
		{
			Title = data_bundle.getString("Title");
			Detail = data_bundle.getString("Message");
			Time = data_bundle.getString("Time");
			Date = data_bundle.getString("Date");
			Address = data_bundle.getString("Address");
		}
		
		ScrollView scrollView = new ScrollView(context);
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				height);

		scrollView.setLayoutParams(layoutParams);
		scrollView.setPadding(40,40,40,40);
		
		LinearLayout lLayour = new LinearLayout(context);
		
		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		lLayour.setOrientation(LinearLayout.VERTICAL);
		
		lLayour.setLayoutParams(layout_params);
		
		for (int i = 0; i < 5; i++)
		{
			LinearLayout textlayout = new LinearLayout(context);
			
			LinearLayout.LayoutParams text_layout_params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			
			text_layout_params.weight = 1.0f;

			textlayout.setOrientation(LinearLayout.HORIZONTAL);

			textlayout.setLayoutParams(text_layout_params);

			TextView title_view = new TextView(context);

			title_view.setTextSize(20);
			
			if (i == 0)
			{
				title_view.setText(com.candroidsample.R.string.input_title);
				
				titleView  = new EditText(context);
				
				titleView.setText(Title);
				
				titleView.setTextSize(20);
				
				LinearLayout.LayoutParams title_layout_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				
				titleView.setLayoutParams(title_layout_params);
				
				textlayout.addView(title_view);
				textlayout.addView(titleView);
			}
			else if (i == 1)
			{
				title_view.setText(com.candroidsample.R.string.input_list);
				
				listView  = new EditText(context);
				
				listView.setText(Detail);
				
				listView.setTextSize(20);
				
				LinearLayout.LayoutParams title_layout_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				
				listView.setLayoutParams(title_layout_params);
				textlayout.addView(title_view);
				textlayout.addView(listView);
			}
			else if (i == 2)
			{
				title_view.setText(com.candroidsample.R.string.text_date);

				dateView  = new TextView(context);
				
				dateView.setText(Date);
				
				dateView.setTextSize(20);
				
				chooseDayBt = new Button(context);
				
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,100);  

				lp.gravity = Gravity.RIGHT;  
				
				chooseDayBt.setLayoutParams(lp);
				
				chooseDayBt.setBackgroundResource(com.candroidsample.R.drawable.button_date);
				
				textlayout.addView(title_view);
				textlayout.addView(dateView);

				LinearLayout linear = new LinearLayout(context);  
				LinearLayout.LayoutParams title_layout_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				
				//注意，對於LinearLayout布局來說，設置橫向還是縱向是必須的！否則就看不到效果了。  
				linear.setOrientation(LinearLayout.VERTICAL);  
				linear.setLayoutParams(title_layout_params);
				linear.addView(chooseDayBt);  
			
				textlayout.addView(linear);
			}
			else if (i == 3)
			{
				title_view.setText(com.candroidsample.R.string.text_time);

				timeView  = new TextView(context);
				
				timeView.setText(Time);
				
				timeView.setTextSize(20);
				
				chooseTimeBt = new Button(context);
				
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,100);  

				lp.gravity = Gravity.RIGHT;  
				
				chooseTimeBt.setLayoutParams(lp);
				
				chooseTimeBt.setBackgroundResource(com.candroidsample.R.drawable.button_time);
				
				textlayout.addView(title_view);
				textlayout.addView(timeView);
				//此處相當於布局文件中的Android:layout_gravity屬性  
				
				LinearLayout linear = new LinearLayout(context);  
				LinearLayout.LayoutParams title_layout_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				
				//注意，對於LinearLayout布局來說，設置橫向還是縱向是必須的！否則就看不到效果了。  
				linear.setOrientation(LinearLayout.VERTICAL);  
				linear.setLayoutParams(title_layout_params);
				linear.addView(chooseTimeBt);  
			
				textlayout.addView(linear);
			}
			else if (i == 4)
			{
				addressTitleView = new TextView(context);

				addressTitleView.setTextSize(20);
				
				addressTitleView.setText(com.candroidsample.R.string.input_address);

				addressView  = new EditText(context);
				
				addressView.setText(Address);
				
				addressView.setTextSize(20);
				
				textlayout.addView(addressTitleView);
				textlayout.addView(addressView);
				//此處相當於布局文件中的Android:layout_gravity屬性  
				
				LinearLayout linear = new LinearLayout(context);  
				LinearLayout.LayoutParams title_layout_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				addressView.setLayoutParams(title_layout_params);
				
				//注意，對於LinearLayout布局來說，設置橫向還是縱向是必須的！否則就看不到效果了。  
				linear.setOrientation(LinearLayout.VERTICAL);  
				linear.setLayoutParams(title_layout_params);
			
				textlayout.addView(linear);
			}
			lLayour.addView(textlayout);
		}
		
		showImageView = new ImageView(context);
		
		LinearLayout.LayoutParams img_layout_params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		
		showImageView.setLayoutParams(img_layout_params);

		chooseImgBt = new Button(context);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(300,100);  

		lp.gravity = Gravity.RIGHT;  
		
		chooseImgBt.setLayoutParams(lp);
		
		chooseImgBt.setBackgroundResource(com.candroidsample.R.drawable.button_photo);

		lLayour.addView(chooseImgBt);

		lLayour.addView(showImageView);
		
		scrollView.addView(lLayour);
		
		layout.addView(scrollView);
	}
	
	public void showNotEditView(LinearLayout layout, Context context,Bundle data_bundle,int height)
	{
		String Title = "";
		String Time = "";
		String Detail = "";
		String Date = "";
		String Address = "";
		
		if (data_bundle != null)
		{
			Title = data_bundle.getString("Title");
			Detail = data_bundle.getString("Message");
			Time = data_bundle.getString("Time");
			Date = data_bundle.getString("Date");
			Address = data_bundle.getString("Address");
		}
		
		ScrollView scrollView = new ScrollView(context);
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				height);

		scrollView.setLayoutParams(layoutParams);
		scrollView.setPadding(40,40,40,40);
		
		LinearLayout lLayour = new LinearLayout(context);
		
		LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		lLayour.setOrientation(LinearLayout.VERTICAL);
		
		lLayour.setLayoutParams(layout_params);
		
		for (int i = 0; i < 5; i++)
		{
			LinearLayout textlayout = new LinearLayout(context);
			
			LinearLayout.LayoutParams text_layout_params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			
			text_layout_params.weight = 1.0f;

			textlayout.setOrientation(LinearLayout.HORIZONTAL);

			textlayout.setLayoutParams(text_layout_params);

			TextView title_view = new TextView(context);

			title_view.setTextSize(20);
			
			if (i == 0)
			{
				title_view.setText(com.candroidsample.R.string.input_title);
				
				titleView2  = new TextView(context);
				
				titleView2.setText(Title);
				
				titleView2.setTextSize(20);
				
				LinearLayout.LayoutParams title_layout_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				
				titleView2.setLayoutParams(title_layout_params);
				
				textlayout.addView(title_view);
				textlayout.addView(titleView2);
			}
			else if (i == 1)
			{
				title_view.setText(com.candroidsample.R.string.input_list);
				
				listView2  = new TextView(context);
				
				listView2.setText(Detail);
				
				listView2.setTextSize(20);
				
				LinearLayout.LayoutParams title_layout_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				
				listView2.setLayoutParams(title_layout_params);
				textlayout.addView(title_view);
				textlayout.addView(listView2);
			}
			else if (i == 2)
			{
				title_view.setText(com.candroidsample.R.string.text_date);

				dateView  = new TextView(context);
				
				dateView.setText(Date);
				
				dateView.setTextSize(20);
				
				chooseDayBt = new Button(context);
				
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  

				lp.gravity = Gravity.RIGHT;  
				
				chooseDayBt.setLayoutParams(lp);
				
				chooseDayBt.setText("選擇日期");

				textlayout.addView(title_view);
				textlayout.addView(dateView);

				LinearLayout linear = new LinearLayout(context);  
				LinearLayout.LayoutParams title_layout_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				
				//注意，對於LinearLayout布局來說，設置橫向還是縱向是必須的！否則就看不到效果了。  
				linear.setOrientation(LinearLayout.VERTICAL);  
				linear.setLayoutParams(title_layout_params);
				linear.addView(chooseDayBt);  
			
				textlayout.addView(linear);
			}
			else if (i == 3)
			{
				title_view.setText(com.candroidsample.R.string.text_time);

				timeView  = new TextView(context);
				
				timeView.setText(Time);
				
				timeView.setTextSize(20);
				
				chooseTimeBt = new Button(context);
				
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  

				lp.gravity = Gravity.RIGHT;  
				
				chooseTimeBt.setLayoutParams(lp);
				
				chooseTimeBt.setText("選擇時間");
				
				textlayout.addView(title_view);
				textlayout.addView(timeView);
				//此處相當於布局文件中的Android:layout_gravity屬性  
				
				LinearLayout linear = new LinearLayout(context);  
				LinearLayout.LayoutParams title_layout_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				
				//注意，對於LinearLayout布局來說，設置橫向還是縱向是必須的！否則就看不到效果了。  
				linear.setOrientation(LinearLayout.VERTICAL);  
				linear.setLayoutParams(title_layout_params);
				linear.addView(chooseTimeBt);  
			
				textlayout.addView(linear);
			}
			else if (i == 4)
			{
				addressTitleView = new TextView(context);

				addressTitleView.setTextSize(20);
				
				addressTitleView.setText(com.candroidsample.R.string.input_address);

				addressView2  = new TextView(context);
				
				addressView2.setText(Address);
				
				addressView2.setTextSize(20);
				
				textlayout.addView(addressTitleView);
				textlayout.addView(addressView2);
				//此處相當於布局文件中的Android:layout_gravity屬性  
				
				LinearLayout linear = new LinearLayout(context);  
				LinearLayout.LayoutParams title_layout_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);

				addressView2.setLayoutParams(title_layout_params);
				
				//注意，對於LinearLayout布局來說，設置橫向還是縱向是必須的！否則就看不到效果了。  
				linear.setOrientation(LinearLayout.VERTICAL);  
				linear.setLayoutParams(title_layout_params);
			
				textlayout.addView(linear);
			}
			lLayour.addView(textlayout);
		}
		
		showImageView = new ImageView(context);
		
		LinearLayout.LayoutParams img_layout_params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		
		showImageView.setLayoutParams(img_layout_params);

		chooseImgBt = new Button(context);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);  
		
		chooseImgBt.setLayoutParams(lp);

		chooseImgBt.setText("選擇圖片");
		
		lLayour.addView(chooseImgBt);

		lLayour.addView(showImageView);
		
		scrollView.addView(lLayour);
		
		layout.addView(scrollView);
	}
}
