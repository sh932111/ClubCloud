package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtils
{
	public static int getTimeGap(String day) throws ParseException
	{
		//定義時間格式

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		//取的兩個時間

		Date dt2 = sdf.parse(day);

		Date dt1 =  new Date();;

		//取得兩個時間的Unix時間

		Long ut1=dt1.getTime();

		Long ut2=dt2.getTime();

		//相減獲得兩個時間差距的毫秒

		Long timeP=ut2-ut1;//毫秒差

		Long sec=timeP/1000;//秒差
		
		int b = new Integer(sec.toString());
		
		return b;
	}

	public static String getNowTime()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		
		Date dt=new Date();
		
		String dts=sdf.format(dt);
		
		return dts;
	}
}
