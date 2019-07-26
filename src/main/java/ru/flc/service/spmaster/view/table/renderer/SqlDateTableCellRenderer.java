package ru.flc.service.spmaster.view.table.renderer;

import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;

public class SqlDateTableCellRenderer extends DefaultTableCellRenderer
{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value != null && java.sql.Date.class.isAssignableFrom(value.getClass()))
		{
			java.sql.Date date = (java.sql.Date) value;
			SimpleDateFormat format = new SimpleDateFormat(AppConstants.DEFAULT_FORMAT_DATE);
			label.setText(format.format(date));
		}

		return label;
	}
}
