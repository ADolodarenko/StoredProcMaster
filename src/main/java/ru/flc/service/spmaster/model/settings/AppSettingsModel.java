package ru.flc.service.spmaster.model.settings;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.Settings;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.settings.ViewSettings;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class AppSettingsModel implements SettingsModel
{
	private Exception lastException;

	private ResourceManager resourceManager;

	private DatabaseSettings dbSettings;
	private ViewSettings viewSettings;
	private OperationalSettings operationalSettings;
	private FileSettings fileSettings;

	private List<TransmissiveSettings> originalSettingsList;
	private List<TransmissiveSettings> clonedSettingsList;

	private ViewConstraints viewConstraints;

	public AppSettingsModel(ResourceManager resourceManager)
	{
		this.resourceManager = resourceManager;

		originalSettingsList = new ArrayList<>();
		clonedSettingsList = new ArrayList<>();

		try
		{
			viewConstraints = new ViewConstraints();

			dbSettings = createDatabaseSettings();
			addToSettingsLists(dbSettings, createDatabaseSettings());

			viewSettings = createViewSettings();
			addToSettingsLists(viewSettings, createViewSettings());

			operationalSettings = createOperationalSettings();
			addToSettingsLists(operationalSettings, createOperationalSettings());

			fileSettings = createFileSettings();
			addToSettingsLists(fileSettings, createFileSettings());

			loadAllSettings();
		}
		catch (Exception e)
		{
			lastException = e;
		}
	}

	private DatabaseSettings createDatabaseSettings() throws Exception
	{
		return new DatabaseSettings(this.resourceManager);
	}

	private ViewSettings createViewSettings() throws Exception
	{
		return new ViewSettings(this.resourceManager, viewConstraints.getPreferredSize());
	}

	private OperationalSettings createOperationalSettings() throws Exception
	{
		return new OperationalSettings(this.resourceManager);
	}

	private FileSettings createFileSettings() throws Exception
	{
		return new FileSettings(this.resourceManager);
	}

	private void addToSettingsLists(TransmissiveSettings originalSettings, TransmissiveSettings clonedSettings)
	{
		originalSettingsList.add(originalSettings);
		clonedSettingsList.add(clonedSettings);
	}

	@Override
	public void loadAllSettings()
	{
		for (Settings settings : originalSettingsList)
			loadSpecificSettings(settings);
	}

	@Override
	public void saveAllSettings()
	{
		for (Settings settings : originalSettingsList)
			saveSpecificSettings(settings);
	}

	@Override
	public void loadSpecificSettings(Settings settings)
	{
		if (settings != null)
			try
			{
				settings.load();
			}
			catch (Exception e)
			{
				lastException = e;
			}
	}

	@Override
	public void saveSpecificSettings(Settings settings)
	{
		if (settings != null)
			try
			{
				settings.save();
			}
			catch (Exception e)
			{
				lastException = e;
			}
	}

	@Override
	public ResourceManager getResourceManager()
	{
		return resourceManager;
	}

	@Override
	public ViewConstraints getViewConstraints()
	{
		return viewConstraints;
	}

	@Override
	public ViewSettings getViewSettings()
	{
		return viewSettings;
	}

	@Override
	public DatabaseSettings getDatabaseSettings()
	{
		return dbSettings;
	}

	@Override
	public OperationalSettings getOperationalSettings()
	{
		return operationalSettings;
	}

	@Override
	public FileSettings getFileSettings()
	{
		return fileSettings;
	}

	@Override
	public void updateViewBounds(View view)
	{
		if (view instanceof JFrame)
		{
			JFrame viewFrame = (JFrame) view;

			viewSettings.setMainWindowMaximized(viewFrame.getExtendedState() == JFrame.MAXIMIZED_BOTH);
			viewSettings.setMainWindowPosition(viewFrame.getBounds().getLocation());
			viewSettings.setMainWindowSize(viewFrame.getSize());
		}
	}

	@Override
	public TransmissiveSettings[] getVisibleSettings()
	{
		lastException = null;

		TransmissiveSettings[] clonedSettingsArray = new TransmissiveSettings[clonedSettingsList.size()];

		try
		{
			for (int i = 0; i < clonedSettingsList.size(); i++)
				clonedSettingsList.get(i).init(originalSettingsList.get(i));

			clonedSettingsArray = clonedSettingsList.toArray(clonedSettingsArray);
		}
		catch (Exception e)
		{
			lastException = e;
		}

		return clonedSettingsArray;
	}

	@Override
	public void resetCurrentLocale()
	{
		resourceManager.setCurrentLocale(viewSettings.getAppLocale());
	}

	@Override
	public Exception getLastException()
	{
		return lastException;
	}
}
