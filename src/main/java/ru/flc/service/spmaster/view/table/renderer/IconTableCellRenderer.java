package ru.flc.service.spmaster.view.table.renderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class IconTableCellRenderer extends DefaultTableCellRenderer
{
	private ImageIcon icon;
	private boolean keepTheText;

	public IconTableCellRenderer(ImageIcon icon, boolean keepTheText)
	{
		this.icon = icon;
		this.keepTheText = keepTheText;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		label.setIcon(icon);

		if (!keepTheText)
			label.setText("");

		return label;
	}
}
