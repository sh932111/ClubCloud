package getfunction;

import toolbar.sample.BarView;
import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;

public class ShowToolbar
{
	public static void showToolbar(LinearLayout layout , Context context , int getH , int index)
	{
		LinearLayout lLayour = new LinearLayout(context);
		
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
		layoutParams.weight = 1.0f;
		
		lLayour.setLayoutParams(layoutParams);
		
		final BarView view = new BarView(context,index);

		LinearLayout.LayoutParams ladderParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, getH);
		
		ladderParams.gravity = Gravity.BOTTOM;
		
		view.setLayoutParams(ladderParams);
		
		view.invalidate();
		
		lLayour.addView(view);
		
		layout.addView(lLayour);
	}
}
