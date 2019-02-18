package ru.flc.service.spmaster.view;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.settings.ViewSettings;
import ru.flc.service.spmaster.model.settings.ViewConstraints;

public interface View
{
	void repaintFrame();
	void showHelpInfo();
	void setPreferredBounds(ViewConstraints settings);
	void setActualBounds(ViewSettings settings);
	void showSettings(TransmissiveSettings[] settingsArray);
	void showConnectionStatus(DatabaseSettings settings);
	void showException(Exception e);
	void adjustToAppState();
}
