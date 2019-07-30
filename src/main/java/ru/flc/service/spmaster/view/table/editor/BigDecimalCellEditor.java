package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.ViewUtils;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.scijava.ui.swing.widget.SpinnerBigDecimalModel;
import ru.flc.service.spmaster.view.util.ViewComponents;

public class BigDecimalCellEditor extends NumericCellEditor implements TableCellEditor
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

	public BigDecimalCellEditor(boolean confirmationRequired,
								ResourceManager resourceManager,
								BigDecimal initialValue, BigDecimal minimum, BigDecimal maximum, BigDecimal stepSize,
								int precision, short scale)
	{
		super(resourceManager, precision, scale);

		BigDecimal minValue = (minimum != null) ? minimum : getUtmostDecimal(precision, scale, true);
		BigDecimal maxValue = (maximum != null) ? maximum : getUtmostDecimal(precision, scale, false);

		SpinnerBigDecimalModel model = new SpinnerBigDecimalModel(initialValue, minValue, maxValue, stepSize);

		this.zeroValue = new BigDecimal(new BigInteger("0"), this.scale);

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
