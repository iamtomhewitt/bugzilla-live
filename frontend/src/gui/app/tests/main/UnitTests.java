package gui.app.tests.main;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import gui.app.tests.bug.BugTests;
import gui.app.tests.config.ConfigTests;
import gui.app.tests.exception.ExceptionTests;
import gui.app.tests.lists.ListTests;

@RunWith(Suite.class)
@SuiteClasses({ BugTests.class, ListTests.class, ConfigTests.class, ExceptionTests.class })
public class UnitTests
{
	public static void main(String[] args)
	{
		System.out.println("Running Unit Tests");
		System.out.println("==================");

		Result result = JUnitCore.runClasses(UnitTests.class);

		for (Failure f : result.getFailures())
		{
			System.out.println("Failure: " + f.getTrace());
		}

		System.out.println();
		System.out.println("Complete");
		System.out.println("==================");
		System.out.println("Tests run: \t" + result.getRunCount());
		System.out.println("Tests failed: \t" + result.getFailureCount());
		System.out.println("Success: \t" + result.wasSuccessful());
		System.out.println("Total time: \t" + result.getRunTime() + "ms");
	}
}
