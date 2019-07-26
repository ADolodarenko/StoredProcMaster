package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.view.ViewUtils;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.util.AppUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class SqlDateCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private JSpinner editor;

	private boolean confirmationRequired;
	private Object oldValue;

	public SqlDateCellEditor(boolean confirmationRequired)
	{
		editor = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor dateTimeEditor = new JSpinner.DateEditor(editor, AppConstants.DEFAULT_FORMAT_DATE);
		editor.setEditor(dateTimeEditor);

		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (confirmationRequired)
			oldValue = value;

		editor.setValue(AppUtils.getSqlDate(null));

		if (value != null)
		{
			Class<?> valueClass = value.getClass();

			if (java.sql.Date.class.isAssignableFrom(valueClass))
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

		return new java.sql.Date( ((java.util.Date) newValue).getTime() );
	}
}
