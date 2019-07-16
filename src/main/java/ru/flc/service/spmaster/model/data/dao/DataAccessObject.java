package ru.flc.service.spmaster.model.data.dao;

import org.dav.service.util.Constants;
import ru.flc.service.spmaster.controller.Executor;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.source.database.DataSource;

import java.util.List;

public class DataAccessObject implements AccessObject
{
	private DataSource source;

	public DataAccessObject(DataSource source)
	{
		if (source == null)
			throw new IllegalArgumentException(Constants.EXCPT_DATA_SOURCE_EMPTY);

		this.source = source;
	}

	@Override
	public void open() throws Exception
	{
		source.open();
	}

	@Override
	public void close() throws Exception
	{
		source.close();
	}

	public List<StoredProc> getStoredProcList() throws Exception
	{
		return source.getStoredProcList();
	}

	public List<String> getStoredProcText(StoredProc storedProc) throws Exception
	{
		return source.getStoredProcText(storedProc);
	}

	public void attachStoredProcParams(StoredProc storedProc) throws Exception
	{
		source.attachStoredProcParams(storedProc);
	}

	public void executeStoredProc(StoredProc storedProc, List<DataTable> resultTables, Executor executor) throws Exception
	{
		source.executeStoredProc(storedProc, resultTables, executor);
	}

	public void updateStoredProcHeaders(StoredProc storedProc) throws Exception
	{
		source.updateStoredProcHeaders(storedProc);
	}
}
