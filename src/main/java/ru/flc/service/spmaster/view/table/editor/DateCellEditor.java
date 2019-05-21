package ru.flc.service.spmaster.view.table.editor;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class DateCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private JSpinner editor;

	private boolean confirmationRequired;
	private Object oldValue;

	/*public DateCellEditor(Date initialValue)
	{
		SpinnerDateModel model = new SpinnerDateModel(initialValue, minimum, maximum, stepSize);

		//editor = new JSpinner(model);

		this.confirmationRequired = confirmationRequired;

	}*/

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		return null;
	}

	@Override
	public Object getCellEditorValue()
	{
		return null;
	}
}
