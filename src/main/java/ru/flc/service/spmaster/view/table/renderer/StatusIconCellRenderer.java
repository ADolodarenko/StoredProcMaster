package ru.flc.service.spmaster.view.table.renderer;

import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcStatus;
import ru.flc.service.spmaster.model.data.entity.User;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.view.table.StoredProcListTable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Map;

public class StatusIconCellRenderer extends DefaultTableCellRenderer
{
	private static void setLabelToolTipText(JTable table, int row, StoredProcStatus procStatus, JLabel cellLabel)
	{
		if (procStatus == StoredProcStatus.OCCUPIED)
		{
			String tableClassName = table.getClass().getSimpleName();

			if (AppConstants.CLASS_NAME_STOREDPROCLISTTABLE.equals(tableClassName))
			{
				StoredProc storedProc = ((StoredProcListTable) table).getStoredProc(row);
				User occupant = storedProc.getOccupant();

				if (occupant != null)
					cellLabel.setToolTipText(occupant.getLogin() + " (" + occupant.getName() + ")");
			}
		}
	}

	private Map<StoredProcStatus, ImageIcon> statusIconMap;
	private boolean keepTheText;

	public StatusIconCellRenderer(Map<StoredProcStatus, ImageIcon> statusIconMap, boolean keepTheText)
	{
		this.statusIconMap = statusIconMap;
		this.keepTheText = keepTheText;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setToolTipText(null);

		if (value != null)
		{
			String valueClassName = value.getClass().getSimpleName();

			if (AppConstants.CLASS_NAME_STOREDPROCSTATUS.equals(valueClassName))
			{
				StoredProcStatus status = (StoredProcStatus) value;

				if (statusIconMap != null)
					label.setIcon(statusIconMap.get(status));

				setLabelToolTipText(table, row, status, label);
			}
		}

		if (!keepTheText)
			label.setText("");

		return label;
	}
}
