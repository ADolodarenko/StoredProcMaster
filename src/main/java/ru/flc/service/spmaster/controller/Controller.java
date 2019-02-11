package ru.flc.service.spmaster.controller;

import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.settings.SettingsModel;
import ru.flc.service.spmaster.view.View;

public class Controller
{
	private DataModel dataModel;
	private SettingsModel settingsModel;
	private View view;

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

	}

	public void disconnectFromDatabase()
	{

	}

	public void execStoredProcedure()
	{

	}

	public void showSettings()
	{

	}

	public void showHelp()
	{
		view.showHelpInfo();
	}
}
