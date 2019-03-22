package ru.flc.service.spmaster.model.data.source;

import org.dav.service.settings.parameter.Parameter;
import ru.flc.service.spmaster.model.data.entity.StoredProc;

import java.util.List;

public interface DataSource extends Source
{
	List<StoredProc> getStoredProcList() throws Exception;
	List<String> getStoredProcText(StoredProc storedProc) throws Exception;
	List<Parameter> getStoredProcParams(StoredProc storedProc) throws Exception;
}
