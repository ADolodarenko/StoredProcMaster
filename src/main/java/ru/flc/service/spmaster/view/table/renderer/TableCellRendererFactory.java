package ru.flc.service.spmaster.view.table.renderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.util.HashMap;
import java.util.Map;

public class TableCellRendererFactory
{
	private static Map<TableCellRendererType, TableCellRenderer> renderers = new HashMap<>();

	public static TableCellRenderer getRenderer(TableCellRendererType rendererType, ImageIcon icon, boolean keepTheText)
	{
		TableCellRenderer renderer;

		if (renderers.containsKey(rendererType))
			renderer = renderers.get(rendererType);
		else
			renderer = createRenderer(rendererType, icon, keepTheText);

		return renderer;
	}

	private static TableCellRenderer createRenderer(TableCellRendererType rendererType, ImageIcon icon, boolean keepTheText)
	{
		TableCellRenderer renderer = null;

		switch (rendererType)
		{
			case ICON:
				renderer = new IconTableCellRenderer(icon, keepTheText);
				break;
			case FIGURED:
				renderer = new FiguredTableCellRenderer();
		}

		if (renderer != null)
			renderers.put(rendererType, renderer);

		return renderer;
	}
}
