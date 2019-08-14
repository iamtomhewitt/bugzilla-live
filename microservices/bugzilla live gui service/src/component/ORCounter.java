package component;

public class ORCounter
{
	public static int countSubsystemORs(String subsystem)
	{
		return (int) ORTable.getInstance().getTableView().getItems().stream().filter(o -> o.getProduct().equalsIgnoreCase(subsystem)).count();
	}

	public static int countSeverityORs(String severity)
	{
		return (int) ORTable.getInstance().getTableView().getItems().stream().filter(o -> o.getSeverity().equalsIgnoreCase(severity)).count();
	}

	public static int countStatusORs(String status)
	{
		return (int) ORTable.getInstance().getTableView().getItems().stream().filter(o -> o.getStatus().equalsIgnoreCase(status)).count();
	}

	public static int countActiveSeverityORs(String severity)
	{
		return (int) ORTable.getInstance().getTableView().getItems().stream().filter(o -> o.getSeverity().equalsIgnoreCase(severity) && (o.getStatus().equalsIgnoreCase("Investigation") || o.getStatus().equalsIgnoreCase("Diagnosed"))).count();
	}
}
