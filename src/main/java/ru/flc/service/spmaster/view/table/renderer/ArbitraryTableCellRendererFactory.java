package ru.flc.service.spmaster.view.table.renderer;

import org.dav.service.util.ResourceManager;
import org.dav.service.view.table.renderer.TableCellRendererFactory;

import javax.swing.table.TableCellRenderer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ArbitraryTableCellRendererFactory extends TableCellRendererFactory
{
	private Map<String, TableCellRenderer> rendererMap;

	public ArbitraryTableCellRendererFactory(ResourceManager resourceManager)
	{
		super(resourceManager);

		rendererMap = new HashMap<>();
	}

	@Override
	public TableCellRenderer getRenderer(Class<?> dataClass)
	{
		TableCellRenderer renderer;

		String className = dataClass.getName();

		if (rendererMap.containsKey(className))
			renderer = rendererMap.get(className);
		else
			renderer = createRenderer(dataClass);

		if (renderer == null)
			renderer = super.getRenderer(dataClass);

		return renderer;
	}

	private TableCellRenderer createRenderer(Class<?> dataClass)
	{
		TableCellRenderer renderer = null;

		if (Date.class.isAssignableFrom(dataClass))
			renderer = new DateTimeTableCellRenderer();

		if (renderer != null)
			rendererMap.put(dataClass.getName(), renderer);

		return renderer;
	}
}
