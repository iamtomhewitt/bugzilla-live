package generator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import bugzilla.common.Folders;
import bugzilla.common.bug.Bug;
import bugzilla.exception.GenerateDocumentException;

/**
 * Generates an excel document using the supplied list of bugs, and returns the file path of the generated document.
 * 
 * @author Tom Hewitt
 * @since 2.3.0
 */
public class ExcelGenerator
{
	public static String generateExcelDocument(List<Bug> bugs) throws GenerateDocumentException
	{
		try 
		{
			List<String> columns = Arrays.asList("Number", "Status", "Assigned To", "Subsystem", "Component", "Severity", "Summary", "Generated From", "Internal/External", "Environment", "Segment Release", "Last Updated");
		
			String fileLocation = Folders.DOCUMENTS_FOLDER + Calendar.getInstance().getTimeInMillis() + ".csv";
	
			String excel = "";
				
			for (String c : columns)
				excel += c + ",";
	
			excel += "\n";
	
			for (Bug bug : bugs)
				excel += bug.toExcelFormat() + "\n";
	
			Files.write(Paths.get(fileLocation), excel.getBytes());
			
			return fileLocation;
		}
		catch (Exception e)
		{
			throw new GenerateDocumentException(e.getMessage());
		}
	}
}
