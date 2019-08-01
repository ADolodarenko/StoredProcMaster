package ru.flc.service.spmaster.model.data.source.database;

import ru.flc.service.spmaster.controller.executor.Executor;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.source.Source;

import java.util.List;

public interface DataSource extends Source
{
	List<StoredProc> getStoredProcList() throws Exception;
	List<String> getStoredProcText(StoredProc storedProc) throws Exception;
	void attachStoredProcParams(StoredProc storedProc) throws Exception;
	void executeStoredProc(StoredProc storedProc, List<DataTable> resultTables, Executor executor)
		throws Exception;
	void updateStoredProcHeaders(StoredProc storedProc) throws Exception;
}
