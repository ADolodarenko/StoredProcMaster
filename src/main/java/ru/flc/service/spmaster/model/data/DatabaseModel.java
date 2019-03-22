package ru.flc.service.spmaster.model.data;

import org.dav.service.settings.Settings;
import org.dav.service.settings.parameter.Parameter;
import ru.flc.service.spmaster.model.data.dao.AccessObjectFactory;
import ru.flc.service.spmaster.model.data.dao.StoredProcDao;
import ru.flc.service.spmaster.model.data.entity.StoredProc;

import java.util.List;

public class DatabaseModel implements DataModel
{
	private StoredProcDao storedProcDao;

	@Override
	public void connectToDatabase(Settings... settingsArray) throws Exception
	{
		storedProcDao = AccessObjectFactory.getStoredProcObject(settingsArray);
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

	@Override
	public List<String> getStoredProcText(StoredProc storedProc) throws Exception
	{
		if (storedProcDao != null)
			return storedProcDao.getStoredProcText(storedProc);
		else
			return null;
	}

	@Override
	public List<Parameter> getStoredProcParams(StoredProc storedProc) throws Exception
	{
		if (storedProcDao != null)
			return storedProcDao.getStoredProcParams(storedProc);
		else
			return null;
	}
}
