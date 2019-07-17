package ru.flc.service.spmaster.model.file;

import ru.flc.service.spmaster.model.data.entity.DataPage;

import java.util.List;

public interface FileModel
{
	void saveStoredProcResult(String fileName, List<DataPage> dataPages) throws Exception;
}
