package ru.flc.service.spmaster.view.table.listener;

import ru.flc.service.spmaster.controller.ActionsManager;
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
	private ActionsManager actionsManager;
	private Controller controller;

	public StoredProcListSelectionListener(StoredProcListTable table,
										   ActionsManager actionsManager,
										   Controller controller)
	{
		if (table == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_SP_LIST_TABLE_EMPTY);

		if (actionsManager == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_ACTIONS_MANAGER_EMPTY);

		if (controller == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_CONTROLLER_EMPTY);

		this.table = table;
		this.actionsManager = actionsManager;
		this.controller = controller;
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if ( !e.getValueIsAdjusting() )
		{
			controller.clearCurrentViewData();
			actionsManager.adjustToAppStatus(actionsManager.getShowSpInfoAction());

			ListSelectionModel selectionModel = (ListSelectionModel) e.getSource();

			if ( !selectionModel.isSelectionEmpty() )
			{
				int selectionIndex = selectionModel.getMinSelectionIndex();

				StoredProc storedProc = table.getStoredProc(selectionIndex);

				if (storedProc != null)
				{
					controller.updateStoredProcedureHeaders(storedProc);
					controller.showStoredProcedureText(storedProc);
				}
			}
		}
	}
}
