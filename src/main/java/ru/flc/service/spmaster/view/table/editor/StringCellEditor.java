package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.util.Constants;
import org.dav.service.view.ViewUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class StringCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private JTextField editor;

	private boolean confirmationRequired;
	private Object oldValue;

	public StringCellEditor(boolean confirmationRequired)
	{
		editor = new JTextField();
		editor.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e)
			{
				editor.selectAll();
			}
		});

		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (confirmationRequired)
			oldValue = value;

		editor.setText("");

		if (value != null)
		{
			String valueClassName = value.getClass().getSimpleName();

			if (Constants.CLASS_NAME_STRING.equals(valueClassName))
				editor.setText(value.toString());
		}

		return editor;
	}

	@Override
	public Object getCellEditorValue()
	{
		Object newValue = editor.getText();

		if (confirmationRequired && !newValue.equals(oldValue))
			newValue = ViewUtils.confirmedValue(oldValue, newValue);

		return newValue;
	}
}
