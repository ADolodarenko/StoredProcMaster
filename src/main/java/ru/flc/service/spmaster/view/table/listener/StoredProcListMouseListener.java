package ru.flc.service.spmaster.view.table.listener;

import ru.flc.service.spmaster.controller.ActionsManager;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StoredProcListMouseListener extends MouseAdapter
{
	private ActionsManager actionsManager;

	public StoredProcListMouseListener(ActionsManager actionsManager)
	{
		this.actionsManager = actionsManager;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (e.getClickCount() == 2 && actionsManager != null)
			actionsManager.getExecSpAction().actionPerformed(new ActionEvent(e.getSource(),
					ActionEvent.ACTION_PERFORMED, null));
	}
}
