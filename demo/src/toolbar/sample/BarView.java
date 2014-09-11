package toolbar.sample;

import com.candroidsample.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

public class BarView extends View
{
	float xPos = 0;
    float yPos = 0; 
    
    int Index = 0;
	
	public BarView(Context context ,int index)
	{
		super(context);
		
		this.Index = index;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		System.out.println("index:"+Index);
		
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.ballicon);
		
		int W = this.getWidth()/5;
		
		Bitmap resizeBmp = Bitmap.createScaledBitmap(bitmap, W, W, true); 
		
		canvas.save();
        canvas.drawBitmap(resizeBmp, 0, 0, null);
        canvas.translate(xPos, yPos);
        canvas.restore();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{

		final int action = ev.getAction();
		switch (action & MotionEvent.ACTION_MASK)
		{
			case MotionEvent.ACTION_DOWN:
			{
				xPos = ev.getX();
				yPos = ev.getY();

				invalidate();
				System.out.println(xPos);
			}
		}
		return true;
	}
}
