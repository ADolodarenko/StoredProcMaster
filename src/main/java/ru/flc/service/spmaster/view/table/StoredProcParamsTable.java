package ru.flc.service.spmaster.view.table;

import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.table.renderer.TableCellRendererFactory;
import org.dav.service.view.table.renderer.TableHeaderRenderer;
import ru.flc.service.spmaster.model.data.entity.StoredProcParameter;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.view.table.editor.TableCellEditorFactory;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Enumeration;

public class StoredProcParamsTable extends JTable
{
	private static void setColumnWidth(TableColumn column, int width)
	{
		column.setMinWidth(width);
		column.setPreferredWidth(width);
		column.setMaxWidth(width);
	}

	private TableCellEditorFactory editorFactory;
	private TableCellRendererFactory rendererFactory;

	private TableCellRenderer baseHeaderRenderer;

	public StoredProcParamsTable(TableModel model,
								 TableCellEditorFactory editorFactory,
								 TableCellRendererFactory rendererFactory,
								 float rowHeightCoefficient)
	{
		super(model);

		if (editorFactory == null)
			throw new IllegalArgumentException(Constants.EXCPT_TABLE_EDITOR_FACTORY_EMPTY);

		if (rendererFactory == null)
			throw new IllegalArgumentException(Constants.EXCPT_TABLE_RENDERER_FACTORY_EMPTY);

		this.editorFactory = editorFactory;
		this.rendererFactory = rendererFactory;

		setAutoResizeMode(AUTO_RESIZE_OFF);

		setHeaderAppearance();
		setColumnAppearance();
		setSelectionStrategy();

		setRowHeight((int) (getRowHeight() * rowHeightCoefficient));
	}

	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return new Dimension(super.getPreferredSize().width, getRowHeight() * getRowCount());
	}

	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		int modelColumnIndex = convertColumnIndexToModel(column);

		if (modelColumnIndex == 4)
		{
			StoredProcParameter rowData = getParameter(row);

			if (rowData != null)
			{
				TableCellRenderer renderer = rendererFactory.getRenderer(rowData.getValueClass());

				if (renderer != null)
					return renderer;
			}

			return super.getCellRenderer(row, column);
		}
		else
			return super.getCellRenderer(row, column);
	}

	@Override
	public TableCellEditor getCellEditor(int row, int column)
	{
		int modelColumnIndex = convertColumnIndexToModel(column);

		if (modelColumnIndex == 4)
		{
			StoredProcParameter rowData = getParameter(row);

			if (rowData != null)
			{
				TableCellEditor editor = editorFactory.getEditor(rowData.getValueClass(),
						rowData.getPrecision(), rowData.getScale(), false);

				if (editor != null)
					return editor;
			}

			return super.getCellEditor(row, column);
		}
		else
			return super.getCellEditor(row, column);
	}

	@Override
	public Class<?> getColumnClass(int column)
	{
		int modelColumnIndex = convertColumnIndexToModel(column);

		if (modelColumnIndex == 3)
			return Boolean.class;

		return super.getColumnClass(column);
	}

	public void resizeColumns()
	{
		TableColumnModel dataColumnModel = getColumnModel();
		TableColumnModel headerColumnModel = getTableHeader().getColumnModel();

		for (int colNum = 0; colNum < getColumnCount(); colNum++)
		{
			int width = headerColumnModel.getColumn(colNum).getWidth() + 1;

			for (int rowNum = 0; rowNum < getRowCount(); rowNum++)
			{
				TableCellRenderer renderer = getCellRenderer(rowNum, colNum);
				Component comp = prepareRenderer(renderer, rowNum, colNum);
				width = Math.max(comp.getPreferredSize().width + 1 , width);
			}

			dataColumnModel.getColumn(colNum).setPreferredWidth(width);
		}
	}

	private void setHeaderAppearance()
	{
		JTableHeader header = getTableHeader();
		baseHeaderRenderer = header.getDefaultRenderer();
		header.setDefaultRenderer(new TableHeaderRenderer());
		header.setReorderingAllowed(false);
	}

	private void setColumnAppearance()
	{
		Enumeration<TableColumn> columns = getColumnModel().getColumns();

		while (columns.hasMoreElements())
		{
			TableColumn column = columns.nextElement();

			//column.setResizable(false);  !!!!

			switch (column.getModelIndex())
			{
				case 0:
					//setColumnWidth(column, COLUMN_TYPE_WIDTH);
					column.setCellRenderer(baseHeaderRenderer);
					break;
				case 1:
					column.setCellRenderer(baseHeaderRenderer);
					break;
				case 2:
					//setColumnWidth(column, COLUMN_VALUETYPE_WIDTH);
					column.setCellRenderer(baseHeaderRenderer);
					break;
				case 3:
					//setColumnWidth(column, COLUMN_NULL_WIDTH);
					break;
			}
		}
	}

	private void setSelectionStrategy()
	{
		setCellSelectionEnabled(true);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	private StoredProcParameter getParameter(int row)
	{
		int modelRowIndex = convertRowIndexToModel(row);
		TableModel model = getModel();

		String modelClassName = model.getClass().getSimpleName();

		if (AppConstants.CLASS_NAME_STOREDPROCPARAMSTABLEMODEL.equals(modelClassName))
		{
			StoredProcParamsTableModel thisModel = (StoredProcParamsTableModel) model;

			if (thisModel.getRowCount() > modelRowIndex)
				return thisModel.getRow(modelRowIndex);
		}

		return null;
	}

	public void resizeLastColumn(int viewWidth)
	{
		TableColumn lastColumn = getColumnModel().getColumn(getColumnCount() - 1);
		int tableWidth = getWidth() - lastColumn.getWidth();
		int diffWidth = viewWidth - tableWidth;

		if (diffWidth > 0)
			lastColumn.setPreferredWidth(diffWidth);
	}
}
