package bugzilla.common.bug;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 * A data class representing the basic info of an Bug. Used to populate the main table in the GUI.
 * 
 * @see <code>BugDetail</code> for attachments and comments.
 * @author Tom Hewitt
 * @since 1.0.0
 */
public class Bug
{
	private String number;
	private String summary;
	private String status;
	private String product;
	private String component;
	private String assignedTo;
	private String severity;
	private String generatedFrom;
	private String internalExternal;
	private String system;
	private String segmentRelease;
	private String lastUpdated;
	
	// Used for Jackson
	public Bug(){}
		
	public String toString()
	{		
		return "Bug: "+number+
				"\nSummary: "+summary+
				"\nStatus: "+status+
				"\nProduct: "+product+
				"\nComponent: "+component+
				"\nAssigned To: "+assignedTo +
				"\nSeverity: "+severity+
				"\nGenerated From: "+generatedFrom+
				"\nInt/Ext: "+internalExternal+
				"\nEnvironment: "+system +
				"\nSegment Release: "+segmentRelease;
	}
	
	public String toExcelFormat()
	{
		return number + "," +
				status + "," +
				assignedTo + "," +
				product + "," +
				component + "," +
				severity + "," +
				summary.replace(",", " ") + "," +
				generatedFrom + "," +
				internalExternal + "," +
				system + "," +
				segmentRelease+ "," +
				lastUpdated;
	}
	
	/**
	 * Checks if <code>str</code> exists in any variables of the Bug.
	 */
	public boolean contains(String str)
	{
		str = str.toLowerCase();
		return  getAssignedTo().toLowerCase().contains(str) ||
				getComponent().toLowerCase().contains(str) ||
				getGeneratedFrom().toLowerCase().contains(str)||
				getInternalExternal().toLowerCase().contains(str)||
				getLastUpdated().toLowerCase().contains(str)||
				getNumber().toLowerCase().contains(str)||
				getProduct().toLowerCase().contains(str)||
				getSegmentRelease().toLowerCase().contains(str)||
				getSeverity().toLowerCase().contains(str)||
				getStatus().toLowerCase().contains(str)||
				getSummary().toLowerCase().contains(str)||
				getSystem().toLowerCase().contains(str);
	}

	public String getNumber()
	{
		return number;
	}

	public void setNumber(String number)
	{
		this.number = number;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		try
		{
			summary = summary.replaceAll("�€�", "'");
			summary = summary.replaceAll("�€“", "-");
			this.summary = new String(summary.getBytes(), "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			this.summary = summary;
		}
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getProduct()
	{
		return product;
	}

	public void setProduct(String product)
	{
		this.product = product;
	}

	public String getComponent()
	{
		return component;
	}

	public void setComponent(String component)
	{
		this.component = component;
	}

	public String getAssignedTo()
	{
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo)
	{
		String[] email = assignedTo.split("@");

		String username = email[0];
		String[] names = username.split("\\.");			
		
		String firstName = names[0].substring(0, 1).toUpperCase() + names[0].substring(1);
		String secondName = "";
		
		if (names.length > 1)
			secondName = names[1].substring(0, 1).toUpperCase() + names[1].substring(1);
		
		String name = firstName + " " + secondName;
		
		if (name.equals("Thomas Hewitt"))
			name = "Tom Hewitt";
		
		this.assignedTo = name;
	}

	public String getSeverity()
	{
		return severity;
	}

	public void setSeverity(String severity)
	{
		String formatted = severity.substring(0, 1).toUpperCase() + severity.substring(1);
		this.severity = formatted;
	}

	public String getGeneratedFrom()
	{
		return generatedFrom;
	}

	public void setGeneratedFrom(String generatedFrom)
	{
		this.generatedFrom = generatedFrom;
	}

	public String getInternalExternal()
	{
		return internalExternal;
	}

	public void setInternalExternal(String internalExternal)
	{
		this.internalExternal = internalExternal;
	}

	public String getSystem()
	{
		return system;
	}

	public void setSystem(String system)
	{
		this.system = system;
	}

	public String getSegmentRelease()
	{
		return segmentRelease;
	}

	public void setSegmentRelease(String segmentRelease)
	{
		this.segmentRelease = segmentRelease;
	}

	public String getLastUpdated()
	{
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated)
	{
		try
		{
			Date d = Date.from(Instant.parse(lastUpdated));
			SimpleDateFormat instantToDateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			this.lastUpdated = instantToDateFormatter.format(d);
		}
		catch (Exception e)
		{
			this.lastUpdated = lastUpdated;
		}
	}	
}