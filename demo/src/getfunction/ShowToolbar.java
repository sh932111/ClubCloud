package getfunction;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ShowToolbar
{
	private Callback mCallback;
	
	public interface Callback
	{
		public abstract void service_result(int msg);
	}
	
	public static int getMenuNum(Context context)
	{
		Resources res = context.getResources();

		String[] array = res.getStringArray(com.candroidsample.R.array.MENU);

		return array.length;
	}


	public void showToolbar(LinearLayout layout, Context context,
			int getH, int index ,Callback callback)
	{
		this.mCallback = callback;
		
		LinearLayout lLayour = new LinearLayout(context);

		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.weight = 1.0f;

		lLayour.setLayoutParams(layoutParams);

		// final BarView view = new BarView(context,index);

		LinearLayout.LayoutParams ladderParams = new LinearLayout.LayoutParams(
				getH, getH);

		ladderParams.gravity = Gravity.BOTTOM;

		Resources res = context.getResources();

		String[] array = res.getStringArray(com.candroidsample.R.array.MENU);

		for (int i = 0; i < array.length; i++)
		{
			Button b1 = new Button(context);

			b1.setText(array[i]);

			setBt(b1,i);
			
			b1.setLayoutParams(ladderParams);

			lLayour.addView(b1);
		}

		layout.addView(lLayour);
	}
	
	public void setBt(Button b1 ,int index)
	{
		final int i = index;
		
		b1.setOnClickListener(new Button.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				mCallback.service_result(i);
			}

		});
	}
	
}
