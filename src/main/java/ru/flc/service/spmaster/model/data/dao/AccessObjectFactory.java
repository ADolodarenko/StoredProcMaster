package ru.flc.service.spmaster.model.data.dao;

import org.dav.service.settings.Settings;
import ru.flc.service.spmaster.model.data.source.database.AseDataSource;
import ru.flc.service.spmaster.model.data.source.database.DataSource;

public class AccessObjectFactory
{
	public static DataAccessObject getDataAccessObject(Settings... settingsArray)
	{
		if (settingsArray == null || settingsArray.length == 0)
			return null;

		DataSource source = AseDataSource.getInstance();

		DataAccessObject object = null;

		try
		{
			source.tune(settingsArray);
			object = new DataAccessObject(source);
		}
		catch (Exception e)
		{}

		return object;
	}

	public static FileAccessObject getFileAccessObject(String fileName)
	{
		return null;
	}
}
