package ru.flc.service.spmaster.model.settings;

import org.dav.service.settings.Settings;
import org.dav.service.settings.ViewSettings;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.view.View;

public interface SettingsModel
{
	void loadAllSettings();
	void saveAllSettings();
	void loadSpecificSettings(Settings settings);
	void saveSpecificSettings(Settings settings);
	Exception getLastException();
	ResourceManager getResourceManager();
	void resetCurrentLocale();
	ViewConstraints getViewConstraints();
	ViewSettings getViewSettings();
	void updateViewBounds(View view);
}
