package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.util.ResourceManager;
import org.dav.service.view.ViewUtils;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.view.util.ViewComponents;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;

public class FloatCellEditor extends NumericCellEditor implements TableCellEditor
{
	private JSpinner editor;
	private boolean confirmationRequired;
	private Object oldValue;

	public FloatCellEditor(boolean confirmationRequired,
						  ResourceManager resourceManager,
						  float initialValue, float minimum, float maximum, float stepSize,
						  int precision, short scale)
	{
		super(resourceManager, precision, scale);

		SpinnerNumberModel model = new SpinnerNumberModel(Float.valueOf(initialValue),
				Float.valueOf(minimum), Float.valueOf(maximum), Float.valueOf(stepSize));

		this.editor = new JSpinner(model);
		this.editorField = ViewComponents.getSpinnerNumberTextField(this.editor);
		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (confirmationRequired)
			oldValue = value;

		customize();

		editor.setValue(Float.valueOf(0.0F));

		if (value != null)
		{
			String valueClassName = value.getClass().getSimpleName();

			if (AppConstants.CLASS_NAME_FLOAT.equals(valueClassName))
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
