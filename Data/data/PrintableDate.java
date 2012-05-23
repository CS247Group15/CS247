package cs247.group15.data;

import java.util.Date;


public class PrintableDate extends Date implements ListClass {

	public PrintableDate(Date date)
	{
		super(date.getTime());
	}
	public PrintableDate(long milliseconds)
	{
		super(milliseconds);
	}
	
	public boolean sameDayAs(Date date2)
	{
		return
				date2.getDate()==getDate()
				&&date2.getMonth()==getMonth()
				&&date2.getYear()==getYear();
	}
	
	@Override
	public String toString()
	{
		return getDate() + "/" + (getMonth()+1) + "/" + getYear();
	}

}
