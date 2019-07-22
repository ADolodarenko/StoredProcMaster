package ru.flc.service.spmaster.view.table.listener;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StoredProcResultTableMouseListener extends MouseAdapter
{
	private JPopupMenu popupMenu;

	public StoredProcResultTableMouseListener(JPopupMenu popupMenu)
	{
		this.popupMenu = popupMenu;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		showPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		showPopup(e);
	}

	private void showPopup(MouseEvent e)
	{
		if (e.isPopupTrigger() && popupMenu != null)
			popupMenu.show(e.getComponent(), e.getX(), e.getY());
	}
}
