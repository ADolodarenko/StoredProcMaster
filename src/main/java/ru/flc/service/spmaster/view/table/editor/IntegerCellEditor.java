package ru.flc.service.spmaster.view.table.editor;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class IntegerCellEditor extends AbstractCellEditor implements TableCellEditor
{
	public IntegerCellEditor(boolean confirmationRequired,
							 int initialValue, int minimum, int maximum, int stepSize)
	{
		;
	}

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
