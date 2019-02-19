package ru.flc.service.spmaster.model.data;

import org.dav.service.settings.DatabaseSettings;
import ru.flc.service.spmaster.model.data.entity.StoredProc;

import java.util.List;

public interface DataModel
{
	void connectToDatabase(DatabaseSettings databaseSettings) throws Exception;
	void disconnectFromDatabase() throws Exception;
	List<StoredProc> getStoredProcList() throws Exception;
}
