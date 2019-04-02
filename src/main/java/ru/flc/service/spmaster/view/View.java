package ru.flc.service.spmaster.view;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.settings.ViewSettings;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcParameter;
import ru.flc.service.spmaster.model.settings.ViewConstraints;

import java.util.List;

public interface View
{
	void repaintFrame();
	void showHelpInfo();
	void setPreferredBounds(ViewConstraints settings);
	void setActualBounds(ViewSettings settings);
	void showSettings(TransmissiveSettings[] settingsArray);
	void clearData();
	void showConnectionStatus(DatabaseSettings settings);
	void showException(Exception e);
	void adjustToAppStatus();
	void showStoredProcList(List<StoredProc> storedProcList);
	void showStoredProcText(List<String> storedProcTextLines);
	void showStoredProcInfo(List<StoredProcParameter> storedProcParams);
	StoredProc getCurrentStoredProc();
}
