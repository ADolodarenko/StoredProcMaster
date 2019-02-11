package ru.flc.service.spmaster.view;

import org.dav.service.util.ResourceManager;
import org.dav.service.view.dialog.SettingsDialog;
import org.dav.service.view.dialog.SettingsDialogInvoker;
import ru.flc.service.spmaster.controller.Controller;

import javax.swing.*;

public class MainFrame extends JFrame implements View, SettingsDialogInvoker
{
	private Controller controller;
	private ResourceManager resourceManager;

	private SettingsDialog settingsDialog;
	private AboutDialog aboutDialog;

	public MainFrame(Controller controller)
	{
		this.controller = controller;
		this.controller.setView(this);

	}

	@Override
	public void showHelpInfo()
	{
		if (aboutDialog == null)
		{
			try
			{
				aboutDialog = new AboutDialog(this, resourceManager);
				aboutDialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			}
			catch (Exception e)
			{
				log(e);
			}
		}

		if (aboutDialog != null)
			aboutDialog.setVisible(true);
	}

	@Override
	public void log(Exception e)
	{

	}

	@Override
	public void setFocus()
	{

	}

	@Override
	public void reloadSettings()
	{

	}
}
