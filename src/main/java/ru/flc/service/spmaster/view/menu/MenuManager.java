package ru.flc.service.spmaster.view.menu;

import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;

public class MenuManager
{
	private JPopupMenu popupMenu;

	public MenuManager(AbstractAction... menuActions)
	{
		if (menuActions == null || menuActions.length == 0)
			throw new IllegalArgumentException(AppConstants.EXCPT_ACTION_LIST_EMPTY);

		initMenu(menuActions);
	}

	private void initMenu(AbstractAction... menuActions)
	{
		popupMenu = new JPopupMenu();

		for (AbstractAction action : menuActions)
			popupMenu.add(new JMenuItem(action));
	}

	public JPopupMenu getPopupMenu()
	{
		return popupMenu;
	}
}
