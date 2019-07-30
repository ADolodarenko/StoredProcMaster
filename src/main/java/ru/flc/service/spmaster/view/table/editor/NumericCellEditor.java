package ru.flc.service.spmaster.view.table.editor;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class NumericCellEditor extends AbstractCellEditor implements CustomizableEditor
{
	private ResourceManager resourceManager;
	protected int precision;
	protected short scale;
	protected JFormattedTextField editorField;

	public NumericCellEditor(ResourceManager resourceManager, int precision, short scale)
	{
		if (resourceManager == null)
			throw new IllegalArgumentException(Constants.EXCPT_RESOURCE_MANAGER_EMPTY);

		this.resourceManager = resourceManager;
		this.precision = precision;
		this.scale = scale;
	}

	@Override
	public void customize()
	{
		if (editorField != null && resourceManager != null)
		{
			NumberFormat format = DecimalFormat.getInstance(resourceManager.getCurrentLocale());
			format.setMinimumIntegerDigits(1);
			format.setMinimumFractionDigits(0);

			if (scale > 0)
			{
				format.setMaximumIntegerDigits(precision - scale);
				format.setMaximumFractionDigits(scale);
			}

			DefaultFormatter formatter = new NumberFormatter(format);

			formatter.setValueClass(editorField.getValue().getClass());
			formatter.setCommitsOnValidEdit(true);

			DefaultFormatterFactory factory = new DefaultFormatterFactory(formatter, formatter, formatter);
			editorField.setFormatterFactory(factory);
		}
	}
}
