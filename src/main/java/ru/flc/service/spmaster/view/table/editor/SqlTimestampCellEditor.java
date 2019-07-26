package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.view.ViewUtils;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.util.AppUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class SqlTimestampCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private JSpinner editor;

	private boolean confirmationRequired;
	private Object oldValue;

	public SqlTimestampCellEditor(boolean confirmationRequired)
	{
		editor = new JSpinner( new SpinnerDateModel() );
		JSpinner.DateEditor dateTimeEditor = new JSpinner.DateEditor(editor, AppConstants.DEFAULT_FORMAT_DATETIME);
		editor.setEditor(dateTimeEditor);

		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (confirmationRequired)
			oldValue = value;

		editor.setValue(AppUtils.getSqlTimestamp(null, ChronoUnit.SECONDS));

		if (value != null)
		{
			String valueClassName = value.getClass().getSimpleName();

			if (AppConstants.CLASS_NAME_TIMESTAMP.equals(valueClassName))
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

		return new Timestamp( ((Date) newValue).getTime() );
	}
}
