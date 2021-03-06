package ru.flc.service.spmaster.view;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.settings.ViewSettings;
import org.dav.service.view.Title;
import ru.flc.service.spmaster.model.data.entity.DataPage;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.settings.ViewConstraints;

import java.io.File;
import java.util.List;

public interface View
{
	void repaintFrame();
	void showHelpInfo();
	void setPreferredBounds(ViewConstraints settings);
	void setActualBounds(ViewSettings settings);
	void showSettings(TransmissiveSettings[] settingsArray);
	void clearAllData();
	void showConnectionStatus(DatabaseSettings settings);
	void showException(Exception e);
	void adjustToAppStatus();
	void showStoredProcList(List<StoredProc> storedProcList);
	void showStoredProcText(List<String> storedProcTextLines);
	void showStoredProcInfo(StoredProc storedProc);
	StoredProc getCurrentStoredProc();
	void showStoredProcOutput(List<DataTable> resultTables);
	void addToLog(Object value);
	void clearCurrentData();
	void showStoredProc(StoredProc storedProc);
	void showStoredProcWarning(StoredProc storedProc);
	File getResultFile();
	List<DataPage> getActiveResultPageList();
	List<DataPage> getAllResultPagesList();
	void showProcess(String messageKey);
	void showProcessMessage(String messageKey);
	void addTitle(String titleKey);
	Title getTitle(String titleKey);
}
