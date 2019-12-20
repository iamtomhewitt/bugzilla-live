package ui.component;

import java.util.List;

import common.bug.Bug;

public class BugCounter
{
	public static int countSeverityBugs(List<Bug> bugs, String severity)
	{
		return (int) bugs.stream().filter(o -> o.getSeverity().equalsIgnoreCase(severity)).count();
	}

	public static int countStatusBugs(List<Bug> bugs, String status)
	{
		return (int) bugs.stream().filter(o -> o.getStatus().equalsIgnoreCase(status)).count();
	}

	public static int countActiveSeverityBugs(List<Bug> bugs, String severity)
	{
		return (int) bugs.stream().filter(o -> o.getSeverity().equalsIgnoreCase(severity) && !o.getStatus().equalsIgnoreCase("RESOLVED") && !o.getStatus().equalsIgnoreCase("VERIFIED")).count();
	}
}