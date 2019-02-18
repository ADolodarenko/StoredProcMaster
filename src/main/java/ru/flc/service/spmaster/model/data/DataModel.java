package ru.flc.service.spmaster.model.data;

import org.dav.service.settings.DatabaseSettings;

public interface DataModel
{
	void connectToDatabase(DatabaseSettings databaseSettings) throws Exception;
	void disconnectFromDatabase() throws Exception;
}
