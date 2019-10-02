package generator;

import org.apache.poi.xwpf.usermodel.*;

import bugzilla.common.Folders;
import bugzilla.common.bug.Bug;
import bugzilla.exception.GenerateDocumentException;
import log.DocumentLogger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates a release note and returns the file path of the generated document.
 * 
 * @author Tom Hewitt
 * @since 2.3.0
 */
public class ReleaseNoteGenerator extends DocumentGenerator 
{	
	private XWPFDocument document;
	
	private String classification;
	private String documentTitle;
	private String documentNumber;
	private String issue;
	private String issueDate;
	private String issueStatus;
	private String filename;
	
	private List<Bug> bugs = new ArrayList<Bug>();
	
	public ReleaseNoteGenerator() 
	{
		// Used for builder
	}

	/**
	 * Generates a Release Note for CRM and returns the Release Note's file path.
	 */
	public String generateCrmReleaseNote() throws GenerateDocumentException
	{
		try 
		{
			// Remove characters not allowed in filenames
			this.filename = this.filename.replaceAll(":", "");
			this.filename = this.filename.replaceAll("\"", "");
			this.filename = this.filename.replaceAll("<", "");
			this.filename = this.filename.replaceAll(">", "");
			this.filename = this.filename.replaceAll("|", "");
	
			String outputFile = Folders.DOCUMENTS_FOLDER + this.filename + ".docx";
	
			replaceText(document, "%Classification", this.classification);
			replaceText(document, "%Doc_title", this.documentTitle);
			replaceText(document, "%Document_number", this.documentNumber);
			replaceText(document, "%Issue", this.issue);
			replaceText(document, "%Issue_date", this.issueDate);
			replaceText(document, "%Issue_status", this.issueStatus);
	
			replaceTextWithORTable(document, "%External_Bugs", this.bugs.stream().filter(bug -> bug.getInternalExternal().equals("External")).collect(Collectors.toList()));
			replaceTextWithORTable(document, "%Internal_Bugs", this.bugs.stream().filter(bug -> bug.getInternalExternal().equals("Internal")).collect(Collectors.toList()));
	
			FileOutputStream os = new FileOutputStream(outputFile);
			document.write(os);
			document.close();
			os.close();
	
			Thread.sleep(500);
			
			DocumentLogger.getInstance().print("Release note created: " + outputFile);
	
			return outputFile;
		}
		catch (Exception e)
		{
			throw new GenerateDocumentException(e.getMessage());
		}
	}
	
	public static class Builder
	{
		private final String templateLocation = Folders.TEMPLATES_FOLDER + "CRM\\release note template.docx";
				
		private String classification;
		private String documentTitle;
		private String documentNumber;
		private String issue;
		private String issueDate;
		private String issueStatus;
		private String filename;
		
		private List<Bug> bugs = new ArrayList<Bug>();
		
		public Builder withClassification(String classification)
		{
			this.classification = classification;
			return this;
		}
		
		public Builder withDocumentTitle(String documentTitle)
		{
			this.documentTitle = documentTitle;
			return this;
		}
		
		public Builder withDocumentNumber(String documentNumber)
		{
			this.documentNumber = documentNumber;
			return this;
		}
		
		public Builder withFilename(String filename)
		{
			this.filename = filename;
			return this;
		}
		
		public Builder withIssue(String issue)
		{
			this.issue = issue;
			return this;
		}
		
		public Builder withIssueStatus(String issueStatus)
		{
			this.issueStatus = issueStatus;
			return this;
		}
		
		public Builder withIssueDate(String issueDate)
		{
			this.issueDate = issueDate;
			return this;
		}
		
		public Builder withBugs(List<Bug> ors)
		{
			this.bugs = ors;
			return this;
		}
		
		public ReleaseNoteGenerator build() throws GenerateDocumentException
		{
			ReleaseNoteGenerator generator = new ReleaseNoteGenerator();
			generator.classification = this.classification;
			generator.documentNumber = this.documentNumber;
			generator.documentTitle = this.documentTitle;
			generator.filename = this.filename;
			generator.issue = this.issue;
			generator.issueStatus = this.issueStatus;
			generator.bugs = this.bugs;
			generator.issueDate = this.issueDate;
			
			try 
			{
				generator.document = new XWPFDocument(new FileInputStream(templateLocation));
			} 
			catch (IOException e) 
			{
				throw new GenerateDocumentException(e.getMessage());
			}

			return generator;
		}	
	}
}
