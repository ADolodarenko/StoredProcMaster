package ru.flc.service.spmaster.model.data;

import ru.flc.service.spmaster.model.data.entity.DataPage;
import ru.flc.service.spmaster.model.settings.FileSettings;

import java.util.List;

public interface FileModel
{
	void saveStoredProcResult(FileSettings fileSettings, List<DataPage> dataPages) throws Exception;
}
