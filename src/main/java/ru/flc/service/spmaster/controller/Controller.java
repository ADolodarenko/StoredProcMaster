package ru.flc.service.spmaster.controller;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.settings.SettingsModel;
import ru.flc.service.spmaster.util.AppState;
import ru.flc.service.spmaster.view.View;

import javax.swing.*;
import java.util.List;

public class Controller
{
	private DataModel dataModel;
	private SettingsModel settingsModel;
	private View view;
	private AppState appState;

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
		if (checkSettingsModel() && checkDataModel())
		{
			DatabaseSettings settings = settingsModel.getDatabaseSettings();

			try
			{
				dataModel.connectToDatabase(settings);
				view.showConnectionStatus(settings);

				changeAppState(AppState.CONNECTED);
			}
			catch (Exception e)
			{
				view.showException(e);
				view.showConnectionStatus(null);

				changeAppState(AppState.DISCONNECTED);
			}
		}
	}

	public void disconnectFromDatabase()
	{
		if (checkDataModel() && checkView())
		{
			try
			{
				dataModel.disconnectFromDatabase();
			}
			catch (Exception e)
			{
				view.showException(e);
			}

			view.clearData();
			view.showConnectionStatus(null);

			changeAppState(AppState.DISCONNECTED);
		}
	}

	public void refreshStoredProcedureList()
	{
		if (checkDataModel() && checkView())
			try
			{
				view.clearData();

				List<StoredProc> storedProcList = dataModel.getStoredProcList();
				view.showStoredProcList(storedProcList);
			}
			catch (Exception e)
			{
				view.showException(e);
			}
	}

	public void showStoredProcedureText(StoredProc storedProc)
	{
		if (checkDataModel() && checkView())
			try
			{
				List<String> storedProcTextLines = null;

				if (storedProc != null)
					storedProcTextLines = dataModel.getStoredProcText(storedProc);

				view.showStoredProcText(storedProcTextLines);
			}
			catch (Exception e)
			{
				view.showException(e);
			}
	}

	public void execStoredProcedure()
	{
		JOptionPane.showMessageDialog(null,
				"Execute the procedure.", "Message", JOptionPane.INFORMATION_MESSAGE);
	}

	public void showSettings()
	{
		if (checkSettingsModel() && checkView())
		{
			TransmissiveSettings[] settingsArray = settingsModel.getVisibleSettings();
			view.showSettings(settingsArray);
		}
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

	public void changeAppState(AppState newAppState)
	{
		appState = newAppState;

		if (checkView())
			view.adjustToAppState();
	}

	public boolean checkAppStates(AppState... desirableStates)
	{
		for (AppState state : desirableStates)
			if (state == appState)
				return true;

		return false;
	}
}
