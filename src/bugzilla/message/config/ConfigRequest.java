package bugzilla.message.config;

import java.util.Map;

import bugzilla.message.Message;

/**
 * A base message request class for all config requests.
 * 
 * @author Tom Hewitt
 * @since 2.3.1
 */
public abstract class ConfigRequest extends Message
{
	private String propertyType;
	private Map<String, String> properties;

	public String getPropertyType()
	{
		return propertyType;
	}

	public void setPropertyType(String propertyType)
	{
		this.propertyType = propertyType;
	}

	public Map<String, String> getProperties()
	{
		return properties;
	}

	public void setProperties(Map<String, String> properties)
	{
		this.properties = properties;
	}
}