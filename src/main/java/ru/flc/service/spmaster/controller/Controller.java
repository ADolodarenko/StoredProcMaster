package ru.flc.service.spmaster.controller;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.controller.executor.Executor;
import ru.flc.service.spmaster.controller.executor.StoredProcExecutor;
import ru.flc.service.spmaster.controller.executor.StoredProcListLoader;
import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.data.entity.DataPage;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcStatus;
import ru.flc.service.spmaster.model.data.FileModel;
import ru.flc.service.spmaster.model.settings.FileSettings;
import ru.flc.service.spmaster.model.settings.OperationalSettings;
import ru.flc.service.spmaster.model.settings.SettingsModel;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.util.AppStatus;
import ru.flc.service.spmaster.view.View;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.List;

public class Controller
{
	private DataModel dataModel;
	private SettingsModel settingsModel;
	private FileModel fileModel;

	private View view;
	private AppStatus appStatus;

	private StoredProcExecutor spExecutor;
	private StoredProcListLoader spListLoader;

	public void setFileModel(FileModel fileModel)
	{
		this.fileModel = fileModel;
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

			view.clearAllData();
			view.showConnectionStatus(null);

			changeAppStatus(AppStatus.DISCONNECTED);
		}
	}

	public void refreshStoredProcedureList()
	{
		if (checkDataModel() && checkView() && checkAppStatuses(AppStatus.CONNECTED))
		{
			view.clearAllData();

			String messageKey = AppConstants.KEY_EXECUTOR_SP_LOADING_MESS;
			view.addTitle(messageKey);

			spListLoader = new StoredProcListLoader(dataModel, view, messageKey);
			spListLoader.getPropertyChangeSupport().addPropertyChangeListener(
					AppConstants.MESS_WORKER_PROPERTY_NAME_STATE,
					evt -> doForWorkerEvent(spListLoader, evt));
			spListLoader.execute();
		}
	}

	public void updateStoredProcedureHeaders(StoredProc storedProc)
	{
		if (storedProc != null)
			if (checkDataModel() && checkView())
				try
				{
					dataModel.updateStoredProcHeaders(storedProc);

					view.showStoredProc(storedProc);
				}
				catch (Exception e)
				{
					view.showException(e);
				}
	}

	public void showStoredProcedureText(StoredProc storedProc)
	{
		if (storedProc != null && storedProc.getStatus() != StoredProcStatus.DEAD)
			if (checkDataModel() && checkView())
				try
				{
					List<String> storedProcTextLines = dataModel.getStoredProcText(storedProc);

					view.showStoredProcText(storedProcTextLines);
				}
				catch (Exception e)
				{
					view.showException(e);
				}
	}

	public void clearCurrentViewData()
	{
		if (checkView())
			view.clearCurrentData();
	}

	public void showStoredProcedureInfo()
	{
		if (checkDataModel() && checkView())
		{
			try
			{
				StoredProc storedProc = view.getCurrentStoredProc();
				updateStoredProcedureHeaders(storedProc);

				if (storedProc != null)
				{
					if (storedProc.getStatus() == StoredProcStatus.AVAILABLE)
					{
						dataModel.attachStoredProcParams(storedProc);
						view.showStoredProcInfo(storedProc);
					}
					else
						view.showStoredProcWarning(storedProc);
				}
			}
			catch (Exception e)
			{
				view.showException(e);
			}
		}
	}

	public boolean activeStoredProcExists()
	{
		if (checkView())
			return view.getCurrentStoredProc() != null;
		else
			return false;
	}

	public void execStoredProcedure()
	{
		if (checkDataModel() && checkView())
		{
			try
			{
				StoredProc storedProc = view.getCurrentStoredProc();
				updateStoredProcedureHeaders(storedProc);

				if (storedProc != null)
				{
					if (storedProc.getStatus() == StoredProcStatus.AVAILABLE)
					{
						String messageKey = AppConstants.KEY_EXECUTOR_SP_EXECUTION_MESS;
						view.addTitle(messageKey);

						spExecutor = new StoredProcExecutor(storedProc, dataModel, view, messageKey);
						spExecutor.getPropertyChangeSupport().addPropertyChangeListener(
								AppConstants.MESS_WORKER_PROPERTY_NAME_STATE,
								evt -> doForWorkerEvent(spExecutor, evt));
						spExecutor.execute();
					}
					else
						view.showStoredProcWarning(storedProc);
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
				if (spExecutor != null && !spExecutor.isDone() && !spExecutor.isInterrupted())
					spExecutor.interrupt();
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

	public void saveActiveResultPage()
	{
		if (checkView())
		{
			List<DataPage> activePageList = view.getActiveResultPageList();

			saveStoredProcedureResult(activePageList);
		}
	}

	public void saveAllResultPages()
	{
		if (checkView())
		{
			List<DataPage> allPagesList = view.getAllResultPagesList();

			saveStoredProcedureResult(allPagesList);
		}
	}

	private void saveStoredProcedureResult(List<DataPage> dataPages)
	{
		if (dataPages != null && !dataPages.isEmpty())
			if (checkFileModel() && checkSettingsModel() && checkView())
			{
				File file  = view.getResultFile();

				if (file != null)
				{
					FileSettings fileSettings = settingsModel.getFileSettings();
					fileSettings.setFile(file);

					try
					{
						fileModel.saveStoredProcResult(fileSettings, dataPages);
					}
					catch (Exception e)
					{
						view.showException(e);
					}
				}
			}
	}

	public void resetCurrentLocale()
	{
		if (checkSettingsModel())
		{
			settingsModel.resetCurrentLocale();

			if (checkView())
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

	private boolean checkFileModel()
	{
		return fileModel != null;
	}

	private boolean checkSettingsModel()
	{
		return settingsModel != null;
	}

	private boolean checkView()
	{
		return view != null;
	}

	private void doForWorkerEvent(Executor executor, PropertyChangeEvent event)
	{
		if (AppConstants.MESS_WORKER_PROPERTY_NAME_STATE.equals(event.getPropertyName()))
		{
			Object newValue = event.getNewValue();

			if (newValue instanceof SwingWorker.StateValue)
			{
				SwingWorker.StateValue stateValue = (SwingWorker.StateValue) newValue;

				switch (stateValue)
				{
					case STARTED:
						changeAppStatus(AppStatus.RUNNING);
						if (executor == spListLoader)
							showProcess(executor.getExecutionMessageKey());
						break;
					case DONE:
						changeAppStatus(AppStatus.CONNECTED);
						if (executor == spListLoader)
							showProcess(executor.getExecutionMessageKey());
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

	public void showProcess(String messageKey)
	{
		if (checkView())
			view.showProcess(messageKey);
	}

	public boolean checkAppStatuses(AppStatus... desirableStatuses)
	{
		for (AppStatus status : desirableStatuses)
			if (status == appStatus)
				return true;

		return false;
	}
}
