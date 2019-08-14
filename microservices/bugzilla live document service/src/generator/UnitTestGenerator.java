package generator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import bugzilla.common.Folders;
import bugzilla.common.UnitTestStep;
import bugzilla.exception.GenerateDocumentException;
import log.DocumentLogger;

/**
 * Generates a unit test using the template, and returns the file path of the generated file.
 * 
 * @author Tom Hewitt
 * @since 2.3.0
 */
public class UnitTestGenerator extends DocumentGenerator
{
	private XWPFDocument document;

	private String classification;
	private String documentTitle;
	private String developerUsername;
	private String fileLocation;
	private String filename;
	private String aim;
	private String testEnvironment;
	
	List<UnitTestStep> testSteps = new ArrayList<UnitTestStep>();
	
	public UnitTestGenerator() 
	{
		// Used by builder
	}

	public String generateUnitTest() throws GenerateDocumentException
	{
		try
		{
			String outputFile = this.fileLocation + "\\" + this.filename + ".docx";
	
			replaceText(document, "%Classification", this.classification);
			replaceText(document, "%Doc_title", this.documentTitle);
			replaceText(document, "%Developer_name", this.developerUsername);
			replaceText(document, "%Aim", this.aim);
			replaceText(document, "%Test_environment", this.testEnvironment);
			replaceText(document, "%Issue_date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			replaceTextWithUnitTestTable(document, "%Test_steps", testSteps);
	
			FileOutputStream os = new FileOutputStream(outputFile);
			document.write(os);
			document.close();
			os.close();
	
			Thread.sleep(500);
			
			DocumentLogger.getInstance().print("Unit test created: " + outputFile);
	
			return outputFile;
		}
		catch (Exception e)
		{
			throw new GenerateDocumentException(e.getMessage());
		}
	}
	
	public static class Builder
	{
		private final String templateLocation = Folders.TEMPLATES_FOLDER + "unit test template.docx";

		private String classification;
		private String documentTitle;
		private String developerUsername;
		private String fileLocation;
		private String filename;
		private String aim;
		private String testEnvironment;
		
		List<UnitTestStep> testSteps = new ArrayList<UnitTestStep>();
		
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
		
		public Builder withDeveloperUsername(String developerUsername)
		{
			this.developerUsername = developerUsername;
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
		
		public Builder withAim(String aim)
		{
			this.aim = aim;
			return this;
		}
		
		public Builder withTestEnvironment(String testEnvironment)
		{
			this.testEnvironment = testEnvironment;
			return this;
		}
		
		public Builder withUnitTestSteps(List<UnitTestStep> unitTestSteps)
		{
			this.testSteps = unitTestSteps;
			return this;
		}
		
		public UnitTestGenerator build() throws GenerateDocumentException
		{
			UnitTestGenerator generator = new UnitTestGenerator();
			generator.classification = this.classification;
			generator.documentTitle = this.documentTitle;
			generator.developerUsername = this.developerUsername;
			generator.fileLocation = this.fileLocation;
			generator.filename = this.filename;
			generator.testSteps = this.testSteps;
			generator.aim = this.aim;
			generator.testEnvironment = this.testEnvironment;
			
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
