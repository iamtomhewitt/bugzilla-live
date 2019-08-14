package component.dialog.unittest;

import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.DefaultStringConverter;

/**
 * A custom class that extends <code>TextFieldTableCell</code> to allow <code>TableView</code> row cells to both resize and be editable.
 * @author Tom Hewitt
 */
public class WrappingTextFieldTableCell<UnitTestStep> extends TextFieldTableCell<UnitTestStep, String>
{
	private final Text cellText;

	public WrappingTextFieldTableCell()
	{
		super(new DefaultStringConverter());
		this.cellText = createText();
	}

	@Override
	public void cancelEdit()
	{
		super.cancelEdit();
		setGraphic(cellText);
	}

	@Override
	public void updateItem(String item, boolean empty)
	{
		super.updateItem(item, empty);
		if (!isEmpty() && !isEditing())
		{
			setGraphic(cellText);
		}
	}

	private Text createText()
	{
		Text text = new Text();
		text.wrappingWidthProperty().bind(widthProperty());
		text.textProperty().bind(itemProperty());
		return text;
	}
}