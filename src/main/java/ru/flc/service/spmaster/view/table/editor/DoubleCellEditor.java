package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.util.Constants;
import org.dav.service.view.ViewUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class DoubleCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private JSpinner editor;

	private boolean confirmationRequired;
	private Object oldValue;

	public DoubleCellEditor(boolean confirmationRequired,
							double initialValue, double minimum, double maximum, double stepSize)
	{
		SpinnerNumberModel model = new SpinnerNumberModel(initialValue, minimum, maximum, stepSize);

		editor = new JSpinner(model);

		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (confirmationRequired)
			oldValue = value;

		editor.setValue(0.0);

		if (value != null)
		{
			String valueClassName = value.getClass().getSimpleName();

			if (Constants.CLASS_NAME_DOUBLE.equals(valueClassName))
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
