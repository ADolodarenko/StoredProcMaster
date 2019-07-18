package ru.flc.service.spmaster.model.file;

import ru.flc.service.spmaster.model.data.dao.AccessObjectFactory;
import ru.flc.service.spmaster.model.data.dao.FileAccessObject;
import ru.flc.service.spmaster.model.data.entity.DataPage;

import java.io.File;
import java.util.List;

public class DefaultFileModel implements FileModel
{
	private FileAccessObject fileAccessObject;

	@Override
	public void saveStoredProcResult(File file, List<DataPage> dataPages) throws Exception
	{
		fileAccessObject = AccessObjectFactory.getFileAccessObject(file);

		if (fileAccessObject != null)
		{
			fileAccessObject.open();

			int i = 1;
			for (DataPage dataPage : dataPages)
				if (dataPage != null)
				{
					String pageName = dataPage.getName();

					if (pageName == null || pageName.isEmpty())
					{
						pageName = String.valueOf(i);
						i++;
					}

					fileAccessObject.addDataTable(pageName, dataPage.getDataTable());
				}

			fileAccessObject.close();
		}
	}
}