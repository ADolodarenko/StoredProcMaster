package ru.flc.service.spmaster.model.settings;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.Settings;
import org.dav.service.settings.ViewSettings;
import org.dav.service.util.ResourceManager;

import java.awt.*;

public class AppSettingsModel implements SettingsModel
{
	private static final Dimension MAIN_WIN_PREF_SIZE = new Dimension(600, 400);
	private static final Dimension MAIN_WIN_MIN_SIZE = new Dimension(600, 400);

	private Exception lastException;

	private ResourceManager resourceManager;

	private DatabaseSettings dbSettings;
	private ViewSettings viewSettings;
	private OperationalSettings operationalSettings;

	public AppSettingsModel(ResourceManager resourceManager)
	{
		this.resourceManager = resourceManager;

		try
		{
			dbSettings = new DatabaseSettings(this.resourceManager);
			viewSettings = new ViewSettings(this.resourceManager, MAIN_WIN_PREF_SIZE);
			operationalSettings = new OperationalSettings(this.resourceManager);

			loadAllSettings();
		}
		catch (Exception e)
		{
			lastException = e;
		}
	}

	@Override
	public void loadAllSettings()
	{
		loadSpecificSettings(dbSettings);
		loadSpecificSettings(viewSettings);
		loadSpecificSettings(operationalSettings);
	}

	@Override
	public void saveAllSettings()
	{
		saveSpecificSettings(dbSettings);
		saveSpecificSettings(viewSettings);
		saveSpecificSettings(operationalSettings);
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
	public Exception getLastException()
	{
		return lastException;
	}
}
