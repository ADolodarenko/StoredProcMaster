package ru.flc.service.spmaster.model.file;

import ru.flc.service.spmaster.model.data.entity.DataPage;

import java.io.File;
import java.util.List;

public interface FileModel
{
	void saveStoredProcResult(File file, List<DataPage> dataPages) throws Exception;
}
