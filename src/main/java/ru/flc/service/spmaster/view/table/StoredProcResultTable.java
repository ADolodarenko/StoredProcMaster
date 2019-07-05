package ru.flc.service.spmaster.view.table;

import org.dav.service.util.Constants;
import org.dav.service.view.table.renderer.TableCellRendererFactory;
import ru.flc.service.spmaster.model.data.entity.DataElement;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;

public class StoredProcResultTable extends JTable
{
	private TableCellRendererFactory rendererFactory;

	public StoredProcResultTable(TableModel model, TableCellRendererFactory rendererFactory, float rowHeightCoefficient)
	{
		super(model);

		if (rendererFactory == null)
			throw new IllegalArgumentException(Constants.EXCPT_TABLE_RENDERER_FACTORY_EMPTY);

		this.rendererFactory = rendererFactory;

		setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);

		setHeaderAppearance();
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
		DataElement element = getDataElement(row, column);

		if (element != null)
		{
			TableCellRenderer renderer = rendererFactory.getRenderer(element.getType());

			if (renderer != null)
				return renderer;
		}

		return super.getCellRenderer(row, column);
	}

	private void setHeaderAppearance()
	{
		getTableHeader().setReorderingAllowed(false);
	}

	private void setSelectionStrategy()
	{
		setCellSelectionEnabled(true);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	private DataElement getDataElement(int row, int column)
	{
		int modelRowIndex = convertRowIndexToModel(row);
		int modelColumnIndex = convertColumnIndexToModel(column);

		TableModel model = getModel();

		String modelClassName = model.getClass().getSimpleName();

		if (AppConstants.CLASS_NAME_STOREDPROCRESULTTABLEMODEL.equals(modelClassName))
		{
			StoredProcResultTableModel thisModel = (StoredProcResultTableModel) model;

			return thisModel.getDataElementAt(modelRowIndex, modelColumnIndex);
		}

		return null;
	}
}
