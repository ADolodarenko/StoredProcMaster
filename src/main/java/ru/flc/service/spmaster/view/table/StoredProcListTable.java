package ru.flc.service.spmaster.view.table;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcStatus;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.view.table.renderer.TableCellRendererFactory;
import ru.flc.service.spmaster.view.table.renderer.TableCellRendererType;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class StoredProcListTable extends JTable
{
	private static final int COLUMN_STATUS_WIDTH = 20;

	private ResourceManager resourceManager;
	private Map<StoredProcStatus, ImageIcon> statusIcons;

	public StoredProcListTable(TableModel model, ResourceManager resourceManager)
	{
		super(model);

		if (resourceManager == null)
			throw new IllegalArgumentException(Constants.EXCPT_RESOURCE_MANAGER_EMPTY);

		this.resourceManager = resourceManager;

		initiateStatusIcons();

		setHeaderAppearance();
		setColumnAppearance();
		setSelectionStrategy();

		setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);

		setRowHeight((int) (getRowHeight() * 1.3));
	}

	public StoredProc getStoredProc(int row)
	{
		int modelRowIndex = convertRowIndexToModel(row);
		TableModel model = getModel();

		String modelClassName = model.getClass().getSimpleName();

		if (AppConstants.CLASS_NAME_STOREDPROCLISTTABLEMODEL.equals(modelClassName))
		{
			StoredProcListTableModel thisModel = (StoredProcListTableModel) model;

			if (thisModel.getRowCount() > modelRowIndex)
				return thisModel.getRow(modelRowIndex);
		}

		return null;
	}

	private void initiateStatusIcons()
	{
		this.statusIcons = new HashMap<>();
		this.statusIcons.put(StoredProcStatus.DEAD, resourceManager.getImageIcon(AppConstants.ICON_NAME_PROC_DEAD));
		this.statusIcons.put(StoredProcStatus.AVAILABLE, resourceManager.getImageIcon(AppConstants.ICON_NAME_PROC_AVAILABLE));
		this.statusIcons.put(StoredProcStatus.OCCUPIED, resourceManager.getImageIcon(AppConstants.ICON_NAME_PROC_OCCUPIED));
	}

	private void setHeaderAppearance()
	{
		JTableHeader header = getTableHeader();
		header.setReorderingAllowed(true);
	}

	private void setColumnAppearance()
	{
		TableCellRendererFactory.setStatusIcons(statusIcons);

		Enumeration<TableColumn> columns = getColumnModel().getColumns();

		while (columns.hasMoreElements())
		{
			TableColumn column = columns.nextElement();
			int columnModelIndex = column.getModelIndex();

			TableCellRendererType[] rendererTypes = TableCellRendererType.values();

			if (columnModelIndex < rendererTypes.length)
			{
				TableCellRenderer renderer = TableCellRendererFactory.getRenderer(rendererTypes[columnModelIndex],
						false);

				if (renderer != null)
					column.setCellRenderer(renderer);
			}

			if (columnModelIndex == 0)
			{
				column.setMinWidth(COLUMN_STATUS_WIDTH);
				column.setWidth(COLUMN_STATUS_WIDTH);
				column.setMaxWidth(COLUMN_STATUS_WIDTH);
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
