package ru.flc.service.spmaster.model.data;

import org.dav.service.settings.Settings;
import ru.flc.service.spmaster.controller.Executor;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.model.data.entity.StoredProc;

import java.util.List;

public interface DataModel
{
	void connectToDatabase(Settings... settingsArray) throws Exception;
	void disconnectFromDatabase() throws Exception;
	List<StoredProc> getStoredProcList() throws Exception;
	List<String> getStoredProcText(StoredProc storedProc) throws Exception;
	void attachStoredProcParams(StoredProc storedProc) throws Exception;
	void executeStoredProc(StoredProc storedProc, List<DataTable> resultTables, Executor executor)
			throws Exception;
	void updateStoredProcHeaders(StoredProc storedProc) throws Exception;
}
