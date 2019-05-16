package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.view.ViewUtils;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class LongCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private JSpinner editor;

	private boolean confirmationRequired;
	private Object oldValue;

	public LongCellEditor(boolean confirmationRequired,
						  long initialValue, long minimum, long maximum, long stepSize)
	{
		SpinnerNumberModel model = new SpinnerNumberModel(Long.valueOf(initialValue),
				Long.valueOf(minimum), Long.valueOf(maximum), Long.valueOf(stepSize));

		editor = new JSpinner(model);

		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (confirmationRequired)
			oldValue = value;

		editor.setValue(Long.valueOf(0));

		if (value != null)
		{
			String valueClassName = value.getClass().getSimpleName();

			if (AppConstants.CLASS_NAME_LONG.equals(valueClassName))
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
