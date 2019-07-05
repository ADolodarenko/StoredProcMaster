package ru.flc.service.spmaster.view.table.renderer;

import ru.flc.service.spmaster.model.data.entity.StoredProcStatus;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.util.HashMap;
import java.util.Map;

public class SpListTableCellRendererFactory
{
	private static Map<SpListTableCellRendererType, TableCellRenderer> renderers = new HashMap<>();
	private static Map<StoredProcStatus, ImageIcon> statusIcons;

	public static void setStatusIcons(Map<StoredProcStatus, ImageIcon> pStatusIcons)
	{
		statusIcons = pStatusIcons;
	}

	public static TableCellRenderer getRenderer(SpListTableCellRendererType rendererType, boolean keepTheText)
	{
		TableCellRenderer renderer;

		if (renderers.containsKey(rendererType))
			renderer = renderers.get(rendererType);
		else
			renderer = createRenderer(rendererType, keepTheText);

		return renderer;
	}

	private static TableCellRenderer createRenderer(SpListTableCellRendererType rendererType, boolean keepTheText)
	{
		TableCellRenderer renderer = null;

		switch (rendererType)
		{
			case STATUS_ICON:
				renderer = new StatusIconTableCellRenderer(statusIcons, keepTheText);
				break;
			case FIGURED:
				renderer = new FiguredTableCellRenderer();
		}

		if (renderer != null)
			renderers.put(rendererType, renderer);

		return renderer;
	}
}
