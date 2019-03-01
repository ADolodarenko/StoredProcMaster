package ru.flc.service.spmaster.view.table.listener;

import ru.flc.service.spmaster.controller.Controller;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.view.table.StoredProcListTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class StoredProcListSelectionListener implements ListSelectionListener
{
	private StoredProcListTable table;
	private Controller controller;

	public StoredProcListSelectionListener(StoredProcListTable table, Controller controller)
	{
		if (table == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_SP_LIST_TABLE_EMPTY);

		if (controller == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_CONTROLLER_EMPTY);

		this.table = table;
		this.controller = controller;
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if ( !e.getValueIsAdjusting() )
		{
			ListSelectionModel selectionModel = (ListSelectionModel) e.getSource();

			if ( !selectionModel.isSelectionEmpty() )
			{
				int selectionIndex = selectionModel.getMinSelectionIndex();

				StoredProc storedProc = table.getStoredProc(selectionIndex);

				controller.showStoredProcedureText(storedProc);
			}
		}
	}
}
