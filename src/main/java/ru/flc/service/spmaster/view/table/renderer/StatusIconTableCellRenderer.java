package ru.flc.service.spmaster.view.table.renderer;

import ru.flc.service.spmaster.model.data.entity.StoredProcStatus;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Map;

public class StatusIconTableCellRenderer extends DefaultTableCellRenderer
{
	private Map<StoredProcStatus, ImageIcon> statusIconMap;
	private boolean keepTheText;

	public StatusIconTableCellRenderer(Map<StoredProcStatus, ImageIcon> statusIconMap, boolean keepTheText)
	{
		this.statusIconMap = statusIconMap;
		this.keepTheText = keepTheText;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

		if (statusIconMap != null && value != null)
		{
			String valueClassName = value.getClass().getSimpleName();

			if (AppConstants.CLASS_NAME_STOREDPROCSTATUS.equals(valueClassName))
			{
				StoredProcStatus status = (StoredProcStatus) value;

				label.setIcon(statusIconMap.get(status));
			}
		}

		if (!keepTheText)
			label.setText("");

		return label;
	}
}
