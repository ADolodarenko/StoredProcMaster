package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.view.ViewUtils;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.scijava.ui.swing.widget.SpinnerBigDecimalModel;

public class BigDecimalCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private JSpinner editor;

	private boolean confirmationRequired;
	private Object oldValue;

	public BigDecimalCellEditor(boolean confirmationRequired,
								BigDecimal initialValue, BigDecimal minimum, BigDecimal maximum, BigDecimal stepSize)
	{
		SpinnerBigDecimalModel model = new SpinnerBigDecimalModel(initialValue, minimum, maximum, stepSize);

		editor = new JSpinner(model);

		JComponent editorFieldComponent = editor.getEditor();

		if (editorFieldComponent.getClass().getSimpleName().equals(AppConstants.CLASS_NAME_JFORMATTEDTEXTFIELD))
		{
			JFormattedTextField editorField = (JFormattedTextField) editorFieldComponent;

			editorField.setFormatterFactory(new JFormattedTextField.AbstractFormatterFactory() {
				@Override
				public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf)
				{
					return new NumberFormatter(new DecimalFormat("####0.##"));
				}
			});
		}

		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (confirmationRequired)
			oldValue = value;

		editor.setValue(new BigDecimal(0.0));

		if (value != null)
		{
			String valueClassName = value.getClass().getSimpleName();

			if (AppConstants.CLASS_NAME_BIGDECIMAL.equals(valueClassName))
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
