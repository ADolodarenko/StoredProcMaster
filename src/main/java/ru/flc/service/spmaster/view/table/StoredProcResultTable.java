package ru.flc.service.spmaster.view.table;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;

public class StoredProcResultTable extends JTable
{
	public StoredProcResultTable(TableModel model, float rowHeightCoefficient)
	{
		super(model);

		setSelectionStrategy();
		setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);

		setRowHeight((int) (getRowHeight() * rowHeightCoefficient));
	}

	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return new Dimension(super.getPreferredSize().width, getRowHeight() * getRowCount());
	}

	private void setSelectionStrategy()
	{
		setCellSelectionEnabled(true);
		getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
}
