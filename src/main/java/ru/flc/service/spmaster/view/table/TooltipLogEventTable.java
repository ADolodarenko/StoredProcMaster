package ru.flc.service.spmaster.view.table;

import org.dav.service.util.Constants;
import org.dav.service.view.table.LogEventTable;
import ru.flc.service.spmaster.view.table.renderer.TooltipCellRenderer;

import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

public class TooltipLogEventTable extends LogEventTable
{
	public TooltipLogEventTable(TableModel model)
	{
		super(model);
	}

	@Override
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass)
	{
		String className = columnClass.getSimpleName();

		if (Constants.CLASS_NAME_STRING.equals(className))
			return new TooltipCellRenderer();
		else
			return super.getDefaultRenderer(columnClass);
	}
}
