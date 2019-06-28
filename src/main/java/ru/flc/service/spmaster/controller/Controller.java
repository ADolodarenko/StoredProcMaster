package ru.flc.service.spmaster.controller;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcStatus;
import ru.flc.service.spmaster.model.settings.OperationalSettings;
import ru.flc.service.spmaster.model.settings.SettingsModel;
import ru.flc.service.spmaster.util.AppStatus;
import ru.flc.service.spmaster.view.View;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.List;

public class Controller
{
	private static final String WORKER_PROPERTY_NAME_STATE = "state";

	private DataModel dataModel;
	private SettingsModel settingsModel;
	private View view;
	private AppStatus appStatus;

	private StoredProcExecuter spExecuter;

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
			DatabaseSettings databaseSettings = settingsModel.getDatabaseSettings();
			OperationalSettings operationalSettings = settingsModel.getOperationalSettings();

			try
			{
				dataModel.connectToDatabase(databaseSettings, operationalSettings);

				view.showConnectionStatus(databaseSettings);

				changeAppStatus(AppStatus.CONNECTED);
			}
			catch (Exception e)
			{
				view.showException(e);
				view.showConnectionStatus(null);

				changeAppStatus(AppStatus.DISCONNECTED);
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

			changeAppStatus(AppStatus.DISCONNECTED);
		}
	}

	public void refreshStoredProcedureList()
	{
		if (checkDataModel() && checkView() && checkAppStatuses(AppStatus.CONNECTED))
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

				if (storedProc != null && storedProc.getStatus() != StoredProcStatus.DEAD)
					storedProcTextLines = dataModel.getStoredProcText(storedProc);

				view.showStoredProcText(storedProcTextLines);
			}
			catch (Exception e)
			{
				view.showException(e);
			}
	}

	public void showStoredProcedureInfo()
	{
		if (checkDataModel() && checkView())
		{
			try
			{
				StoredProc storedProc = view.getCurrentStoredProc();

				if (storedProc != null && storedProc.getStatus() != StoredProcStatus.DEAD)
				{
					dataModel.attachStoredProcParams(storedProc);
					view.showStoredProcInfo(storedProc);
				}
			}
			catch (Exception e)
			{
				view.showException(e);
			}
		}
	}

	public void execStoredProcedure()
	{
		if (checkDataModel() && checkView())
		{
			try
			{
				StoredProc storedProc = view.getCurrentStoredProc();

				if (storedProc != null)
				{
					spExecuter = new StoredProcExecuter(storedProc, dataModel, view);
					spExecuter.getPropertyChangeSupport().addPropertyChangeListener(WORKER_PROPERTY_NAME_STATE,
							evt -> doForWorkerEvent(spExecuter, evt));
					spExecuter.execute();
				}
			}
			catch (Exception e)
			{
				view.showException(e);
			}
		}
	}

	public void cancelStoredProcedure()
	{
		if (checkDataModel() && checkView())
		{
			try
			{
				if (spExecuter != null && !spExecuter.isDone() && !spExecuter.isCancelled())
					spExecuter.cancel(false);
			}
			catch (Exception e)
			{
				view.showException(e);
			}
		}
	}

	public void cancelProcesses()
	{
		cancelStoredProcedure();

		disconnectFromDatabase();
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

	private void doForWorkerEvent(SwingWorker worker, PropertyChangeEvent event)
	{
		if (WORKER_PROPERTY_NAME_STATE.equals(event.getPropertyName()))
		{
			Object newValue = event.getNewValue();

			if (newValue instanceof SwingWorker.StateValue)
			{
				SwingWorker.StateValue stateValue = (SwingWorker.StateValue) newValue;

				switch (stateValue)
				{
					case STARTED:
						changeAppStatus(AppStatus.RUNNING);
						break;
					case DONE:
						changeAppStatus(AppStatus.CONNECTED);
				}
			}
		}
	}

	public void changeAppStatus(AppStatus newAppStatus)
	{
		appStatus = newAppStatus;

		if (checkView())
			view.adjustToAppStatus();
	}

	public boolean checkAppStatuses(AppStatus... desirableStatuses)
	{
		for (AppStatus status : desirableStatuses)
			if (status == appStatus)
				return true;

		return false;
	}
}
