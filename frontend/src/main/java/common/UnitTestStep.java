package common;

/**
 * Data class to hold information about unit test steps.
 *
 * @author Tom Hewitt
 * @since 2.3.0
 */
public class UnitTestStep
{
	private String step;
	private String action;
	private String expectedResult;
	
	public UnitTestStep()
	{
		// Used for Jackson
	}
	
	public UnitTestStep(String step, String action, String expectedResult)
	{
		this.step = step;
		this.action = action;
		this.expectedResult = expectedResult;
	}
	
	public String getStep()
	{
		return step;
	}
	
	public void setStep(String step)
	{
		this.step = step;
	}
	
	public String getAction()
	{
		return action;
	}
	
	public void setAction(String action)
	{
		this.action = action;
	}
	
	public String getExpectedResult()
	{
		return expectedResult;
	}
	
	public void setExpectedResult(String expectedResult)
	{
		this.expectedResult = expectedResult;
	}	
}
