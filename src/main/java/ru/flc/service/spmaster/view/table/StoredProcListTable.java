package ru.flc.service.spmaster.view.table;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.view.table.renderer.TableCellRendererFactory;
import ru.flc.service.spmaster.view.table.renderer.TableCellRendererType;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.util.Enumeration;

public class StoredProcListTable extends JTable
{
	private ResourceManager resourceManager;

	public StoredProcListTable(TableModel model, ResourceManager resourceManager)
	{
		super(model);

		if (resourceManager == null)
			throw new IllegalArgumentException(Constants.EXCPT_RESOURCE_MANAGER_EMPTY);

		this.resourceManager = resourceManager;

		setHeaderAppearance();
		setColumnAppearance();
		setSelectionStrategy();

		setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);
	}

	private void setHeaderAppearance()
	{
		JTableHeader header = getTableHeader();
		header.setReorderingAllowed(true);
	}

	private void setColumnAppearance()
	{
		Enumeration<TableColumn> columns = getColumnModel().getColumns();

		while (columns.hasMoreElements())
		{
			TableColumn column = columns.nextElement();
			int columnModelIndex = column.getModelIndex();

			TableCellRendererType[] rendererTypes = TableCellRendererType.values();

			if (columnModelIndex < rendererTypes.length)
			{
				TableCellRenderer renderer = TableCellRendererFactory.getRenderer(rendererTypes[columnModelIndex],
						resourceManager.getImageIcon(AppConstants.ICON_NAME_STORED_PROCEDURE), false);

				if (renderer != null)
					column.setCellRenderer(renderer);
			}
		}
	}

	private void setSelectionStrategy()
	{
		setRowSelectionAllowed(true);
		setColumnSelectionAllowed(false);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
}
