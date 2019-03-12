package ru.flc.service.spmaster.model.data;

import org.dav.service.settings.Settings;
import ru.flc.service.spmaster.model.data.entity.StoredProc;

import java.util.List;

public interface DataModel
{
	void connectToDatabase(Settings... settingsArray) throws Exception;
	void disconnectFromDatabase() throws Exception;
	List<StoredProc> getStoredProcList() throws Exception;
	List<String> getStoredProcText(StoredProc storedProc) throws Exception;
}
