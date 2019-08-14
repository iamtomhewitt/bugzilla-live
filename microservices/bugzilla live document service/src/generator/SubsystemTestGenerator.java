package generator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblLayoutType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import bugzilla.common.Folders;
import bugzilla.common.OR.OR;
import bugzilla.exception.GenerateDocumentException;

/**
 * Generates a subsystem test document, and returns the file path of the generated document.
 * 
 * @author Tom Hewitt
 * @since 2.3.0
 */
public class SubsystemTestGenerator extends DocumentGenerator
{
	private final String templateLocation = Folders.TEMPLATES_FOLDER + "CRM\\subsystem test template.docx";

	private XWPFDocument document;
	
	private String documentTitle;
	private String documentNumber;
	private String issue;
	private String issueStatus;
	private String developerName;
	private String classification;
	private String testEnvironment;
	private String fileLocation;
	private String filename;
	private String releaseNumber;
	
	private List<OR> ors = new ArrayList<OR>();
	
	private SubsystemTestGenerator()
	{
		// Used for builder
	}

	public String generateSubsystemTest() throws GenerateDocumentException
	{
		try 
		{
			String outputFile = this.fileLocation + "\\" + this.filename + ".docx";
	
			document = new XWPFDocument(new FileInputStream(templateLocation));
	
			// For each OR create the test section
			for (OR or : ors)
			{
				createParagraphWithRun("OR" + or.getNumber() + " - " + or.getSummary(), "Heading2");			
				createParagraphWithRun("Test Environment", "Heading3");				
				setRun(document.createParagraph().createRun(), "Arial", 12, "000000", testEnvironment, false, true);
				createParagraphWithRun("Test Procedure", "Heading3");			
				document.createParagraph().createRun().addBreak();
				createTable(document, Arrays.asList("Tester", "Date", "Result"), 1, 1);
				document.createParagraph().createRun().addBreak();
				createTable(document, Arrays.asList("Step", "Action", "Expected Result", "�"), 1, 1);			
				document.createParagraph().createRun().addBreak(BreakType.PAGE);
			}
			
			replaceText(document, "Doctitle", documentTitle);
			replaceText(document, "Docclassification", classification);
			replaceText(document, "Docnumber", documentNumber);
			replaceText(document, "Docissue", issue);
			replaceText(document, "Issuestatus", issueStatus);
			replaceText(document, "Issuedate", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			replaceText(document, "Developername", developerName);
			replaceText(document, "Docsubsystem", "CRM");
			replaceText(document, "Releasenumber", releaseNumber);
	
			replaceTextWithORTable(document, "%ORs", this.ors);
	
			FileOutputStream os = new FileOutputStream(outputFile);
			document.write(os);
			document.close();
			os.close();
	
			Thread.sleep(500);
			return outputFile;
		}
		catch (Exception e)
		{
			throw new GenerateDocumentException(e.getMessage());
		}
	}
	
	private void createParagraphWithRun(String runText, String paragraphStyle)
	{
		XWPFParagraph para = document.createParagraph();
		para.setStyle(paragraphStyle);
		
		XWPFRun run = para.createRun();
		run.setText(runText);
	}

	private void createTable(XWPFDocument doc, List<String> columns, int rows, int rowHeight)
	{
		XWPFTable table = doc.createTable();
		XWPFTableRow firstRow = table.getRow(0);
		XWPFParagraph paragraph = firstRow.getCell(0).addParagraph();

		// Create the initial row
		setRun(paragraph.createRun(), "Arial", 11, "000000", columns.get(0), true, false);
		firstRow.getCell(0).getCTTc().addNewTcPr().addNewShd().setFill("b3b3b3");
		paragraph.setAlignment(ParagraphAlignment.CENTER);
		firstRow.getCell(0).removeParagraph(0);
        
		for (int i = 1; i < columns.size(); i++)
		{
			createColumn(firstRow, columns.get(i));
		}

		// Add new columns
		for (int i = 0; i < rows; i++)
		{
			XWPFTableRow r = table.createRow();
			r.setHeight((rowHeight * 1440) / 2);
		}

		CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
		width.setType(STTblWidth.DXA);
		width.setW(BigInteger.valueOf(9072));
		table.getCTTbl().getTblPr().addNewTblLayout().setType(STTblLayoutType.FIXED);
	}
	
	// Creates the column row of a table
	private void createColumn(XWPFTableRow row, String column)
	{
		XWPFTableCell cell = row.addNewTableCell();
		XWPFParagraph para = cell.addParagraph();
		XWPFRun run = para.createRun();
		
		if (column.equals("�"))
			setRun(run, "Wingdings", 11, "000000", column, true, false);
		else
			setRun(run, "Arial", 11, "000000", column, true, false);

		cell.getCTTc().addNewTcPr().addNewShd().setFill("b3b3b3");
		cell.removeParagraph(0);
		para.setAlignment(ParagraphAlignment.CENTER);
	}

	public static class Builder
	{
		private String documentTitle;
		private String documentNumber;
		private String issue;
		private String issueStatus;
		private String developerName;
		private String classification;
		private String testEnvironment;
		private String fileLocation;
		private String filename;
		private String releaseNumber;
		
		private List<OR> ors = new ArrayList<OR>();
		
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

		public Builder withDeveloperName(String developerName)
		{
			this.developerName = developerName;
			return this;
		}

		public Builder withTestEnvironment(String testEnvironment)
		{
			this.testEnvironment = testEnvironment;
			return this;
		}

		public Builder withFileLocation(String fileLocation)
		{
			this.fileLocation = fileLocation;
			return this;
		}

		public Builder withFilename(String filename)
		{
			this.filename = filename;
			return this;
		}
		
		public Builder withClassification(String classification)
		{
			this.classification = classification;
			return this;
		}

		public Builder withReleaseNumber(String releaseNumber)
		{
			this.releaseNumber = releaseNumber;
			return this;
		}
		
		public Builder withORs(List<OR> ors)
		{
			this.ors = ors;
			return this;
		}
		
		public SubsystemTestGenerator build()
		{
			SubsystemTestGenerator generator = new SubsystemTestGenerator();
			generator.classification = this.classification;
			generator.developerName = this.developerName;
			generator.documentNumber = this.documentNumber;
			generator.documentTitle = this.documentTitle;
			generator.fileLocation = this.fileLocation;
			generator.filename = this.filename;
			generator.issue = this.issue;
			generator.issueStatus = this.issueStatus;
			generator.ors = this.ors;
			generator.releaseNumber = this.releaseNumber;
			generator.testEnvironment = this.testEnvironment;
			return generator;
		}		
	}
}
