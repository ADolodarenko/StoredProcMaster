package ru.flc.service.spmaster.model.data;

import org.dav.service.settings.DatabaseSettings;
import ru.flc.service.spmaster.model.data.dao.AccessObjectFactory;
import ru.flc.service.spmaster.model.data.dao.StoredProcDao;
import ru.flc.service.spmaster.model.data.entity.StoredProc;

import java.util.List;

public class DatabaseModel implements DataModel
{
	private StoredProcDao storedProcDao;

	@Override
	public void connectToDatabase(DatabaseSettings databaseSettings) throws Exception
	{
		storedProcDao = AccessObjectFactory.getStoredProcObject(databaseSettings);
		storedProcDao.open();
	}

	@Override
	public void disconnectFromDatabase() throws Exception
	{
		if (storedProcDao != null)
			storedProcDao.close();
	}

	@Override
	public List<StoredProc> getStoredProcList() throws Exception
	{
		if (storedProcDao != null)
			return storedProcDao.getStoredProcList();
		else
			return null;
	}
}
