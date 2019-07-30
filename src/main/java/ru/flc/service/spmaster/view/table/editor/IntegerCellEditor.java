package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.ViewUtils;
import ru.flc.service.spmaster.view.util.ViewComponents;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.text.DefaultFormatter;
import java.awt.*;

public class IntegerCellEditor extends NumericCellEditor implements TableCellEditor
{
	private JSpinner editor;
	private boolean confirmationRequired;
	private Object oldValue;

	public IntegerCellEditor(boolean confirmationRequired,
							 ResourceManager resourceManager,
							 int initialValue, int minimum, int maximum, int stepSize,
							 int precision, short scale)
	{
		super(resourceManager, precision, scale);

		SpinnerNumberModel model = new SpinnerNumberModel(initialValue, minimum, maximum, stepSize);

		this.editor = new JSpinner(model);
		this.editorField = ViewComponents.getSpinnerNumberTextField(this.editor);

		/*DefaultFormatter formatter = ViewComponents.getSpinnerNumberFormatter(editor);
		if (formatter != null)
			formatter.setCommitsOnValidEdit(true);*/

		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (confirmationRequired)
			oldValue = value;

		customize();

		editor.setValue(0);

		if (value != null)
		{
			String valueClassName = value.getClass().getSimpleName();

			if (Constants.CLASS_NAME_INTEGER.equals(valueClassName))
				editor.setValue(value);
		}

		return editor;
	}

	@Override
	public Object getCellEditorValue()
	{
		Object newValue = editor.getValue();

		if (confirmationRequired && !newValue.equals(oldValue))
			newValue = ViewUtils.confirmedValue(oldValue, newValue);

		return newValue;
	}
}
