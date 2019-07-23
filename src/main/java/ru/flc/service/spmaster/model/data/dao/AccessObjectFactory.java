package ru.flc.service.spmaster.model.data.dao;

import org.apache.commons.io.FilenameUtils;
import org.dav.service.settings.Settings;
import org.dav.service.util.Constants;
import ru.flc.service.spmaster.model.data.source.database.AseDataSource;
import ru.flc.service.spmaster.model.data.source.database.DataSource;
import ru.flc.service.spmaster.model.data.source.file.FileSource;
import ru.flc.service.spmaster.model.data.source.file.FileSourceFactory;
import ru.flc.service.spmaster.model.settings.FileSettings;
import ru.flc.service.spmaster.util.AppUtils;

import java.io.File;

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

	public static FileAccessObject getFileAccessObject(Settings... settingsArray) throws Exception
	{
		if (settingsArray == null || settingsArray.length == 0)
			throw new IllegalArgumentException(Constants.EXCPT_FILE_SETTINGS_EMPTY);

		FileSettings fileSettings = AppUtils.getFirstValueFromArray(FileSettings.class, settingsArray);
		if (fileSettings == null)
			throw new IllegalArgumentException(Constants.EXCPT_FILE_SETTINGS_EMPTY);

		File file = fileSettings.getFile();
		String fileNameExtension = FilenameUtils.getExtension(file.getAbsolutePath());

		FileSource source = FileSourceFactory.getSource(fileNameExtension);
		if (source == null)
			throw new IllegalArgumentException(Constants.EXCPT_FILE_SOURCE_EMPTY);

		source.tune(settingsArray);

		return new FileAccessObject(source);
	}
}
