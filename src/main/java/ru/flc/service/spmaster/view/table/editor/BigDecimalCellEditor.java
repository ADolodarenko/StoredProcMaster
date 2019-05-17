package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.view.ViewUtils;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.scijava.ui.swing.widget.SpinnerBigDecimalModel;

public class BigDecimalCellEditor extends AbstractCellEditor implements TableCellEditor
{
	private static String getFormatPattern(int precision, short scale)
	{
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < precision - scale; i++)
			builder.append('#');
		builder.append('.');

		for (short i = 0; i < scale; i++)
			builder.append('#');

		return builder.toString();
	}

	private static BigDecimal getUtmostDecimal(int precision, short scale, boolean negative)
	{
		StringBuilder builder = new StringBuilder();

		if (negative)
			builder.append('-');

		for (int i = 0; i < precision; i++)
			builder.append('9');

		return new BigDecimal(new BigInteger(builder.toString()), scale);
	}

	private JSpinner editor;

	private boolean confirmationRequired;
	private Object oldValue;
	private BigDecimal zeroValue;

	private int precision;
	private short scale;

	public BigDecimalCellEditor(boolean confirmationRequired,
								BigDecimal initialValue, BigDecimal minimum, BigDecimal maximum, BigDecimal stepSize,
								int precision, short scale)
	{
		this.precision = precision;
		this.scale = scale;

		BigDecimal minValue = (minimum != null) ? minimum : getUtmostDecimal(precision, scale, true);
		BigDecimal maxValue = (maximum != null) ? maximum : getUtmostDecimal(precision, scale, false);

		SpinnerBigDecimalModel model = new SpinnerBigDecimalModel(initialValue, minValue, maxValue, stepSize);

		zeroValue = new BigDecimal(new BigInteger("0"), this.scale);

		editor = new JSpinner(model);

		JComponent innerEditorComponent = editor.getEditor();

		if (innerEditorComponent.getClass().getSimpleName().equals(AppConstants.CLASS_NAME_NUMBEREDITOR))
		{
			JSpinner.NumberEditor innerEditor = (JSpinner.NumberEditor) innerEditorComponent;
			JFormattedTextField editorField = innerEditor.getTextField();

			DefaultFormatter formatter = new NumberFormatter(new DecimalFormat(getFormatPattern(precision, scale)));
			formatter.setValueClass(editorField.getValue().getClass());

			DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter, formatter, formatter);
			editorField.setFormatterFactory(factory);
		}

		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if (confirmationRequired)
			oldValue = value;

		editor.setValue(zeroValue);

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

		if (newValue != null)
			newValue = ((BigDecimal) newValue).setScale(this.scale, RoundingMode.HALF_UP);

		if (confirmationRequired && !newValue.equals(oldValue))
			newValue = ViewUtils.confirmedValue(oldValue, newValue);

		return newValue;
	}
}
