package ru.flc.service.spmaster.view.table.renderer;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.table.renderer.TableCellRendererFactory;

import javax.swing.table.TableCellRenderer;
import java.util.HashMap;
import java.util.Map;

public class ArbitraryCellRendererFactory extends TableCellRendererFactory
{
	private Map<String, TableCellRenderer> rendererMap;

	public ArbitraryCellRendererFactory(ResourceManager resourceManager)
	{
		super(resourceManager);

		if (resourceManager == null)
			throw new IllegalArgumentException(Constants.EXCPT_RESOURCE_MANAGER_EMPTY);

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

		if (Number.class.isAssignableFrom(dataClass))
			renderer = new NumberCellRenderer(resourceManager);
		if (java.sql.Date.class.isAssignableFrom(dataClass))
			renderer = new SqlDateCellRenderer();
		else if (java.sql.Time.class.isAssignableFrom(dataClass))
			renderer = new SqlTimeCellRenderer();
		else if (java.util.Date.class.isAssignableFrom(dataClass))
			renderer = new DateTimeCellRenderer();

		if (renderer != null)
			rendererMap.put(dataClass.getName(), renderer);

		return renderer;
	}
}
