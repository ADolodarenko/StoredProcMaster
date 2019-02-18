package ru.flc.service.spmaster.model.data.dao;

import org.dav.service.settings.DatabaseSettings;
import ru.flc.service.spmaster.model.data.source.AseDataSource;
import ru.flc.service.spmaster.model.data.source.DataSource;

public class AccessObjectFactory
{
	public static StoredProcDao getStoredProcObject(DatabaseSettings settings)
	{
		if (settings == null)
			return null;

		DataSource source = AseDataSource.getInstance();

		StoredProcDao object = null;

		try
		{
			source.tune(settings);
			object = new StoredProcDao(source);
		}
		catch (Exception e)
		{}

		return object;
	}
}
