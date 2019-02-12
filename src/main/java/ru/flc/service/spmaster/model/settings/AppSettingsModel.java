package ru.flc.service.spmaster.model.settings;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.Settings;
import org.dav.service.settings.ViewSettings;
import org.dav.service.util.ResourceManager;


public class AppSettingsModel implements SettingsModel
{
	private Exception lastException;

	private ResourceManager resourceManager;

	private DatabaseSettings dbSettings;
	private ViewSettings viewSettings;
	private OperationalSettings operationalSettings;

	private ViewConstraints viewConstraints;

	public AppSettingsModel(ResourceManager resourceManager)
	{
		this.resourceManager = resourceManager;

		try
		{
			viewConstraints = new ViewConstraints();
			dbSettings = new DatabaseSettings(this.resourceManager);
			viewSettings = new ViewSettings(this.resourceManager, viewConstraints.getPreferredSize());
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