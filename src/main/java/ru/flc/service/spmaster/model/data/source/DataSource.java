package ru.flc.service.spmaster.model.data.source;

import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcParameter;

import java.util.List;

public interface DataSource extends Source
{
	List<StoredProc> getStoredProcList() throws Exception;
	List<String> getStoredProcText(StoredProc storedProc) throws Exception;
	void attachStoredProcParams(StoredProc storedProc) throws Exception;
}
