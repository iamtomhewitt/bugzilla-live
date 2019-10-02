package generator;

import java.math.BigInteger;
import java.util.List;

import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.PositionInParagraph;
import org.apache.poi.xwpf.usermodel.TextSegment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

import bugzilla.common.bug.Bug;
import bugzilla.common.UnitTestStep;
import log.DocumentLogger;

/**
 * Generates a document by replacing placeholders in a .docx template with the specified element, such as a table, or a title.<p>
 * The template will need to have the placeholder present in the document, and must be appropiately named, such as 'DocMyORTable' in order for the placeholder to be found.
 * Spaces do not work for placeholders.<p>
 * 
 * Heading styles can also be applied, if they exist in the template.
 * 
 * @author Tom Hewitt
 * @since 2.3.0
 */
public abstract class DocumentGenerator
{	
	protected XWPFDocument replaceText(XWPFDocument doc, String placeHolder, String replaceText)
	{
		DocumentLogger.getInstance().print("Searching document for: '" + placeHolder + "' to replace with: " + replaceText);
		// REPLACE ALL HEADERS
		for (XWPFHeader header : doc.getHeaderList())
			replaceAllBodyElements(header.getBodyElements(), placeHolder, replaceText);

		// REPLACE BODY
		replaceAllBodyElements(doc.getBodyElements(), placeHolder, replaceText);

		// REPLACE ALL FOOTERS
		for (XWPFFooter footer : doc.getFooterList())
			replaceAllBodyElements(footer.getBodyElements(), placeHolder, replaceText);

		return doc;
	}

	protected void replaceAllBodyElements(List<IBodyElement> bodyElements, String placeHolder, String replaceText)
	{
		for (IBodyElement bodyElement : bodyElements)
		{
			if (bodyElement.getElementType().compareTo(BodyElementType.PARAGRAPH) == 0)
				replaceParagraph((XWPFParagraph) bodyElement, placeHolder, replaceText);

			if (bodyElement.getElementType().compareTo(BodyElementType.TABLE) == 0)
				replaceTable((XWPFTable) bodyElement, placeHolder, replaceText);
		}
	}

	protected void replaceTable(XWPFTable table, String placeHolder, String replaceText)
	{
		for (XWPFTableRow row : table.getRows())
		{
			for (XWPFTableCell cell : row.getTableCells())
			{
				for (IBodyElement bodyElement : cell.getBodyElements())
				{
					if (bodyElement.getElementType().compareTo(BodyElementType.PARAGRAPH) == 0)
						replaceParagraph((XWPFParagraph) bodyElement, placeHolder, replaceText);

					if (bodyElement.getElementType().compareTo(BodyElementType.TABLE) == 0)
						replaceTable((XWPFTable) bodyElement, placeHolder, replaceText);
				}
			}
		}
	}

	protected void replaceParagraph(XWPFParagraph paragraph, String placeHolder, String replaceText)
	{
		for (XWPFRun r : paragraph.getRuns())
		{
			String text = r.getText(0);
			if (text != null && (text.contains(placeHolder) || text.contentEquals(placeHolder) || text.endsWith(placeHolder) || text.equals(placeHolder) || text.equalsIgnoreCase(placeHolder) || text.startsWith(placeHolder)))
			{
				DocumentLogger.getInstance().print("Found: '" + text + "', changing to: '" + replaceText + "'");
				text = text.replace(placeHolder, replaceText);
				r.setText(text, 0);
			}
		}
	}
	
	private XWPFTable findTable(XWPFDocument doc, String placeholder)
	{
		XWPFTable table = null;
		
		for (XWPFParagraph paragraph : doc.getParagraphs())
		{
			java.util.List<XWPFRun> runs = paragraph.getRuns();

			TextSegment found = paragraph.searchText(placeholder, new PositionInParagraph());

			if (found != null)
			{
				DocumentLogger.getInstance().print("Found: '" + placeholder + "', replacing");

				if (found.getBeginRun() == found.getEndRun())
				{
					// whole search string is in one Run
					XmlCursor cursor = paragraph.getCTP().newCursor();
					table = doc.insertNewTbl(cursor);
					XWPFRun run = runs.get(found.getBeginRun());

					// Clear the "%TABLE" from doc
					String runText = run.getText(run.getTextPosition());
					String replaced = runText.replace(placeholder, "");
					run.setText(replaced, 0);
				} 
				else
				{
					// The search string spans over more than one Run
					StringBuilder b = new StringBuilder();
					for (int runPos = found.getBeginRun(); runPos <= found.getEndRun(); runPos++)
					{
						XWPFRun run = runs.get(runPos);
						b.append(run.getText(run.getTextPosition()));
					}

					String connectedRuns = b.toString();
					XmlCursor cursor = paragraph.getCTP().newCursor();
					table = doc.insertNewTbl(cursor);
					String replaced = connectedRuns.replace(placeholder, ""); // Clear search text

					// The first Run receives the replaced String of all connected Runs
					XWPFRun partOne = runs.get(found.getBeginRun());
					partOne.setText(replaced, 0);

					// Removing the text in the other Runs.
					for (int runPos = found.getBeginRun() + 1; runPos <= found.getEndRun(); runPos++)
					{
						XWPFRun partNext = runs.get(runPos);
						partNext.setText("", 0);
					}
				}
			}
		}
		
		return table;
	}

	protected void replaceTextWithORTable(XWPFDocument doc, String placeholder, List<Bug> listOfORs)
	{
		XWPFTable table = findTable(doc, placeholder);		

		// Create the title row, the column headings
		XWPFTableRow titleRow = table.getRow(0);
		titleRow.addNewTableCell();
		XWPFParagraph para = titleRow.getCell(0).addParagraph();
		setRun(para.createRun(), "Arial", 11, "000000", "OR", true, false);
		setRun(titleRow.getCell(1).addParagraph().createRun(), "Arial", 11, "000000", "Description", true, false);
		titleRow.getCell(0).removeParagraph(0);
		titleRow.getCell(1).removeParagraph(0);

		// Set the width of the table
		CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
		width.setType(STTblWidth.DXA);
		width.setW(BigInteger.valueOf(9072));

		// create rows
		for (int i = 0; i < listOfORs.size(); i++)
		{
			XWPFTableRow row = table.createRow();
			setRun(row.getCell(0).addParagraph().createRun(), "Arial", 11, "000000", listOfORs.get(i).getNumber(), false, false);
			setRun(row.getCell(1).addParagraph().createRun(), "Arial", 11, "000000", listOfORs.get(i).getSummary(), false, false);
			row.getCell(0).removeParagraph(0);
			row.getCell(1).removeParagraph(0);
			
			// Add a paragraph below the text to give some spacing
			row.getCell(0).addParagraph();
		}
	}

	

	protected void replaceTextWithUnitTestTable(XWPFDocument doc, String placeholder, List<UnitTestStep> listOfTestSteps)
	{
		XWPFTable table = findTable(doc, placeholder);

		// Create the title row, the column headings
		XWPFTableRow titleRow = table.getRow(0);
		titleRow.addNewTableCell();
		titleRow.addNewTableCell();

		XWPFParagraph para = titleRow.getCell(0).addParagraph();
		setRun(para.createRun(), "Arial", 11, "000000", "Step", true, false);
		setRun(titleRow.getCell(1).addParagraph().createRun(), "Arial", 11, "000000", "Action", true, false);
		setRun(titleRow.getCell(2).addParagraph().createRun(), "Arial", 11, "000000", "Expected Result", true, false);
		titleRow.getCell(0).removeParagraph(0);
		titleRow.getCell(1).removeParagraph(0);
		titleRow.getCell(2).removeParagraph(0);

		// Set the width of the table
		CTTblWidth width = table.getCTTbl().addNewTblPr().addNewTblW();
		width.setType(STTblWidth.DXA);
		width.setW(BigInteger.valueOf(9072));

		// create rows
		for (int i = 0; i < listOfTestSteps.size(); i++)
		{
			XWPFTableRow row = table.createRow();
			setRun(row.getCell(0).addParagraph().createRun(), "Arial", 11, "000000", listOfTestSteps.get(i).getStep(), false, false);
			setRun(row.getCell(1).addParagraph().createRun(), "Arial", 11, "000000", listOfTestSteps.get(i).getAction(), false, false);
			setRun(row.getCell(2).addParagraph().createRun(), "Arial", 11, "000000", listOfTestSteps.get(i).getExpectedResult(), false, false);

			row.getCell(0).removeParagraph(0);
			row.getCell(1).removeParagraph(0);
			row.getCell(2).removeParagraph(0);
			
			// Add a paragraph below the text to give some spacing
			row.getCell(0).addParagraph();
		}
	}

	protected void setRun(XWPFRun run, String fontFamily, int fontSize, String colorRGB, String text, boolean bold, boolean addBreak)
	{
		run.setFontFamily(fontFamily);
		run.setFontSize(fontSize);
		run.setColor(colorRGB);
		run.setText(text);
		run.setBold(bold);
		if (addBreak)
			run.addBreak();
	}
}
