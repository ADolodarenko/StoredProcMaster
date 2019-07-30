package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.table.TableCellEditor;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TableCellEditorFactory
{
	private ResourceManager resourceManager;
	private Map<TableCellEditorKey, TableCellEditor> editors;

	public TableCellEditorFactory(ResourceManager resourceManager)
	{
		if (resourceManager == null)
			throw new IllegalArgumentException(Constants.EXCPT_RESOURCE_MANAGER_EMPTY);

		this.resourceManager = resourceManager;
		this.editors = new HashMap<>();
	}

	public TableCellEditor getEditor(Class<?> dataClass, int precision, short scale, boolean confirmationRequired)
	{
		TableCellEditorKey key = new TableCellEditorKey(dataClass, precision, scale, confirmationRequired);

		TableCellEditor editor = editors.get(key);

		if (editor == null)
			editor = createEditor(key);

		return editor;
	}

	private TableCellEditor createEditor(TableCellEditorKey editorKey)
	{
		Class<?> valueClass = editorKey.getValueClass();
		String valueClassName = valueClass.getSimpleName();
		boolean confirmationRequired = editorKey.isConfirmationRequired();
		int valuePrecision = editorKey.getPrecision();
		short valueScale = editorKey.getScale();

		TableCellEditor editor = createEditorForClassName(valueClassName, valuePrecision, valueScale, confirmationRequired);

		if (editor == null)
			editor = createEditorForClass(valueClass, valuePrecision, valueScale, confirmationRequired);

		if (editor != null)
			editors.put(editorKey, editor);

		return editor;
	}

	private TableCellEditor createEditorForClassName(String className, int precision, short scale,
													 boolean confirmationRequired)
	{
		TableCellEditor editor = null;

		switch (className)
		{
			case Constants.CLASS_NAME_BOOLEAN:
				editor = new BooleanCellEditor(confirmationRequired);
				break;
			case Constants.CLASS_NAME_INTEGER:
				editor = new IntegerCellEditor(confirmationRequired,
						resourceManager,
						0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1,
						precision, scale);
				break;
			case AppConstants.CLASS_NAME_LONG:
				editor = new LongCellEditor(confirmationRequired,
						resourceManager,
						0, Long.MIN_VALUE, Long.MAX_VALUE, 1,
						precision, scale);
				break;
			case AppConstants.CLASS_NAME_FLOAT:
				editor = new FloatCellEditor(confirmationRequired,
						resourceManager,
						0.0F, -Float.MAX_VALUE, Float.MAX_VALUE, 0.01F,
						precision, scale);
				break;
			case Constants.CLASS_NAME_DOUBLE:
				editor = new DoubleCellEditor(confirmationRequired,
						resourceManager,
						0.0, -Double.MAX_VALUE, Double.MAX_VALUE, 0.01,
						precision, scale);
				break;
			case AppConstants.CLASS_NAME_BIGDECIMAL:
				BigDecimal currentValue = new BigDecimal(0.0);
				currentValue.setScale(scale);

				editor = new BigDecimalCellEditor(confirmationRequired,
						resourceManager,
						currentValue, null, null, new BigDecimal(1),
						precision, scale);
				break;
			case AppConstants.CLASS_NAME_TIMESTAMP:
				editor = new SqlTimestampCellEditor(confirmationRequired);
				break;
			case Constants.CLASS_NAME_STRING:
				editor = new StringCellEditor(confirmationRequired);
				break;
		}

		return editor;
	}

	private TableCellEditor createEditorForClass(Class<?> cls, int precision, short scale,
													 boolean confirmationRequired)
	{
		TableCellEditor editor = null;

		if (java.sql.Date.class.isAssignableFrom(cls))
			editor = new SqlDateCellEditor(confirmationRequired);
		else if (java.sql.Time.class.isAssignableFrom(cls))
			editor = new SqlTimeCellEditor(confirmationRequired);

		return editor;
	}
}
