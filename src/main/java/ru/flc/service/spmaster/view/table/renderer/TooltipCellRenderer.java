package ru.flc.service.spmaster.view.table.renderer;

import org.dav.service.util.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class TooltipCellRenderer extends DefaultTableCellRenderer
{
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (value != null)
		{
			String className = value.getClass().getSimpleName();

			if (Constants.CLASS_NAME_STRING.equals(className))
				label.setToolTipText(label.getText());
		}

		return label;
	}
}
