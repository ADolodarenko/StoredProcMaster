package ru.flc.service.spmaster.view.table.renderer;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.NumberFormat;

public class NumberCellRenderer extends DefaultTableCellRenderer
{
	private ResourceManager resourceManager;

	public NumberCellRenderer(ResourceManager resourceManager)
	{
		if (resourceManager == null)
			throw new IllegalArgumentException(Constants.EXCPT_RESOURCE_MANAGER_EMPTY);

		this.resourceManager = resourceManager;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value != null && Number.class.isAssignableFrom(value.getClass()))
		{
			NumberFormat format = NumberFormat.getInstance(resourceManager.getCurrentLocale());
			label.setText(format.format(value));
		}

		return label;
	}
}
