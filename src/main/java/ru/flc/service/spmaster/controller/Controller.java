package ru.flc.service.spmaster.controller;

import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.settings.SettingsModel;
import ru.flc.service.spmaster.util.AppState;
import ru.flc.service.spmaster.view.View;

import javax.swing.*;

public class Controller
{
	private DataModel dataModel;
	private SettingsModel settingsModel;
	private View view;
	private AppState appState;

	public Controller()
	{
		appState = AppState.DISCONNECTED;
	}

	public void setDataModel(DataModel dataModel)
	{
		this.dataModel = dataModel;
	}

	public void setSettingsModel(SettingsModel settingsModel)
	{
		this.settingsModel = settingsModel;
	}

	public void setView(View view)
	{
		this.view = view;
	}

	public void connectToDatabase()
	{
		JOptionPane.showMessageDialog(null,
				"Connect to database.", "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	public void disconnectFromDatabase()
	{
		JOptionPane.showMessageDialog(null,
				"Disconnect from database.", "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	public void refreshStoredProcedureList()
	{
		JOptionPane.showMessageDialog(null,
				"Refresh the list of stored procedures.", "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	public void execStoredProcedure()
	{
		JOptionPane.showMessageDialog(null,
				"Execute the procedure.", "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	public void showSettings()
	{
		JOptionPane.showMessageDialog(null,
				"Show application settings.", "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	public void showHelp()
	{
		if (checkView())
			view.showHelpInfo();
	}

	public void resetCurrentLocale()
	{
		if (checkSettingsModel())
		{
			settingsModel.resetCurrentLocale();

			view.repaintFrame();
		}
	}

	public ResourceManager getResourceManager()
	{
		if (checkSettingsModel())
			return settingsModel.getResourceManager();
		else
			return null;
	}

	public void setViewPreferredBounds()
	{
		if (checkSettingsModel())
			view.setPreferredBounds(settingsModel.getViewConstraints());
	}

	public void setViewActualBounds()
	{
		if (checkSettingsModel())
			view.setActualBounds(settingsModel.getViewSettings());
	}

	public void updateViewBounds()
	{
		if (checkView() && checkSettingsModel())
			settingsModel.updateViewBounds(view);
	}

	public void saveAllSettings()
	{
		if (checkSettingsModel())
			settingsModel.saveAllSettings();
	}

	private boolean checkDataModel()
	{
		return dataModel != null;
	}

	private boolean checkSettingsModel()
	{
		return settingsModel != null;
	}

	private boolean checkView()
	{
		return view != null;
	}
}
