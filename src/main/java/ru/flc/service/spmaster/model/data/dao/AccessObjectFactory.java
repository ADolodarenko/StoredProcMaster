package ru.flc.service.spmaster.model.data.dao;

import org.dav.service.settings.Settings;
import ru.flc.service.spmaster.model.data.source.AseDataSource;
import ru.flc.service.spmaster.model.data.source.DataSource;

public class AccessObjectFactory
{
	public static StoredProcDao getStoredProcObject(Settings... settingsArray)
	{
		if (settingsArray == null || settingsArray.length == 0)
			return null;

		DataSource source = AseDataSource.getInstance();

		StoredProcDao object = null;

		try
		{
			source.tune(settingsArray);
			object = new StoredProcDao(source);
		}
		catch (Exception e)
		{}

		return object;
	}
}
