package gui.app.component;

public class BugCounter
{
	public static int countSeverityBugs(String severity)
	{
		return (int) BugTable.getInstance().getTableView().getItems().stream().filter(o -> o.getSeverity().equalsIgnoreCase(severity)).count();
	}

	public static int countStatusBugs(String status)
	{
		return (int) BugTable.getInstance().getTableView().getItems().stream().filter(o -> o.getStatus().equalsIgnoreCase(status)).count();
	}

	public static int countActiveSeverityBugs(String severity)
	{
		return (int) BugTable.getInstance().getTableView().getItems().stream().filter(o -> o.getSeverity().equalsIgnoreCase(severity) && (o.getStatus().equalsIgnoreCase("Investigation") || o.getStatus().equalsIgnoreCase("Diagnosed"))).count();
	}
}
