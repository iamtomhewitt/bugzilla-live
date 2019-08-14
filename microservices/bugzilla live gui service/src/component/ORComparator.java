package component;

import java.beans.PropertyDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import bugzilla.common.Errors;
import bugzilla.common.MessageBox;
import bugzilla.common.OR.OR;

public class ORComparator implements Comparator<OR>
{
	private final List<String> fieldSortOrder;
	
	public ORComparator(String... fieldSortOrder)
	{
		this.fieldSortOrder = new ArrayList<String>(Arrays.asList(fieldSortOrder));
	}

	@Override
	public int compare(OR o1, OR o2)
	{
		try
		{
			if (fieldSortOrder.contains("severity"))
			{
				List<String> definedOrder = Arrays.asList("Unknown", "Low", "Medium", "High", "Critical");
				return Integer.valueOf(definedOrder.indexOf(o1.getSeverity())).compareTo(Integer.valueOf(definedOrder.indexOf(o2.getSeverity())));				 
			}
			else if (fieldSortOrder.contains("status"))
			{
				List<String> definedOrder = Arrays.asList("Investigation", "Diagnosed","Coded", "Built", "Released", "Fixed",  "Addressed", "CLOSED", "No Fault");
				return Integer.valueOf(definedOrder.indexOf(o1.getStatus())).compareTo(Integer.valueOf(definedOrder.indexOf(o2.getStatus())));
			}
			else if (fieldSortOrder.contains("lastUpdated"))
			{
				try
				{
					SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy hh:mm");

					Date d1 = f.parse(o1.getLastUpdated());
					Date d2 = f.parse(o2.getLastUpdated());

					return d1.compareTo(d2);
				}
				catch (ParseException e)
				{
					MessageBox.showExceptionDialog(Errors.GENERAL, e);
				}
				return 0;
			}
			else
			{
				return cmp(o1, o2, fieldSortOrder);
			}
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	private int cmp(OR a, OR b, final List<String> fields) throws Exception
	{
		if (fields.isEmpty())
			return 0;
		
		PropertyDescriptor pd = new PropertyDescriptor(fields.get(0), OR.class);
		
		String firstString = (String) pd.getReadMethod().invoke(a);
		String secondString = (String) pd.getReadMethod().invoke(b);
		
		if (firstString.compareTo(secondString) == 0)
			return cmp(a, b, fields.subList(1, fields.size()));
		else
			return firstString.compareTo(secondString);
	}
}
