package ru.flc.service.spmaster.model.data;

import org.dav.service.settings.Settings;
import ru.flc.service.spmaster.controller.Executor;
import ru.flc.service.spmaster.model.data.dao.AccessObjectFactory;
import ru.flc.service.spmaster.model.data.dao.DataAccessObject;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.model.data.entity.StoredProc;

import java.util.List;

public class DatabaseModel implements DataModel
{
	private DataAccessObject storedProcDao;

	@Override
	public void connectToDatabase(Settings... settingsArray) throws Exception
	{
		storedProcDao = AccessObjectFactory.getDataAccessObject(settingsArray);
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
	public void attachStoredProcParams(StoredProc storedProc) throws Exception
	{
		if (storedProcDao != null)
			storedProcDao.attachStoredProcParams(storedProc);
		else
			storedProc.setParameters(null);
	}

	@Override
	public void executeStoredProc(StoredProc storedProc, List<DataTable> resultTables, Executor executor)
			throws Exception
	{
		if (storedProcDao != null)
			storedProcDao.executeStoredProc(storedProc, resultTables, executor);
	}

	@Override
	public void updateStoredProcHeaders(StoredProc storedProc) throws Exception
	{
		if (storedProcDao != null)
			storedProcDao.updateStoredProcHeaders(storedProc);
	}
}
