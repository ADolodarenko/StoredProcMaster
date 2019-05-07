package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.util.Constants;
import org.dav.service.view.ViewUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class BooleanCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private JCheckBox editor;

	private boolean confirmationRequired;
	private Object oldValue;

	public BooleanCellEditor(boolean confirmationRequired)
	{
		editor = new JCheckBox();

		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (confirmationRequired)
			oldValue = value;

		editor.setSelected(false);

		if (value != null)
		{
			String valueClassName = value.getClass().getSimpleName();

			if (Constants.CLASS_NAME_BOOLEAN.equals(valueClassName))
				editor.setSelected(((Boolean) value).booleanValue());
		}

		return editor;
	}

	@Override
	public Object getCellEditorValue()
	{
		Object newValue = editor.isSelected();

		if (confirmationRequired && !newValue.equals(oldValue))
			newValue = ViewUtils.confirmedValue(oldValue, newValue);

		return newValue;
	}
}
