package ru.flc.service.spmaster.view.table.renderer;

import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeTableCellRenderer extends DefaultTableCellRenderer
{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value != null && Date.class.isAssignableFrom(value.getClass()))
		{
			Date dateTime = (Date) value;
			SimpleDateFormat format = new SimpleDateFormat(AppConstants.PATTERN_DATETIME);
			label.setText(format.format(dateTime));
		}

		return label;
	}
}
