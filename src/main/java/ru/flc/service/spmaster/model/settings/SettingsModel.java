package ru.flc.service.spmaster.model.settings;

import org.dav.service.settings.Settings;

public interface SettingsModel
{
	void loadAllSettings();
	void saveAllSettings();
	void loadSpecificSettings(Settings settings);
	void saveSpecificSettings(Settings settings);
	Exception getLastException();
}
