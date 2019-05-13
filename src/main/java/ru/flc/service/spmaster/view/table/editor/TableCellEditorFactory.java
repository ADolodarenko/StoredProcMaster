package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.util.Constants;

import javax.swing.table.TableCellEditor;
import java.util.HashMap;
import java.util.Map;

public class TableCellEditorFactory
{
	private Map<TableCellEditorKey, TableCellEditor> editors;

	public TableCellEditorFactory()
	{
		editors = new HashMap<>();
	}

	public TableCellEditor getEditor(Class<?> dataClass, int precision, short scale,  boolean confirmationRequired)
	{
		TableCellEditorKey key = new TableCellEditorKey(dataClass, precision, scale, confirmationRequired);

		TableCellEditor editor = editors.get(key);

		if (editor == null)
			editor = createEditor(key);

		return editor;
	}

	private TableCellEditor createEditor(TableCellEditorKey editorKey)
	{
		TableCellEditor editor = null;

		String forClassName = editorKey.getValueClass().getSimpleName();
		boolean confirmationRequired = editorKey.isConfirmationRequired();

		switch (forClassName)
		{
			case Constants.CLASS_NAME_BOOLEAN:
				editor = new BooleanCellEditor(confirmationRequired);
				break;
			case Constants.CLASS_NAME_INTEGER:
				editor = new IntegerCellEditor(confirmationRequired,
						0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
				break;
			case Constants.CLASS_NAME_DOUBLE:
				editor = new DoubleCellEditor(confirmationRequired,
						0.0, Double.MIN_VALUE, Double.MAX_VALUE, 0.01);
				break;
			case Constants.CLASS_NAME_STRING:
				editor = new StringCellEditor(confirmationRequired);
				break;
		}

		if (editor != null)
			editors.put(editorKey, editor);

		return editor;
	}

}
