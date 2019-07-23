package ru.flc.service.spmaster.model.data.source.file;

import org.dav.service.settings.Settings;
import org.dav.service.util.Constants;
import ru.flc.service.spmaster.model.settings.FileSettings;
import ru.flc.service.spmaster.util.AppUtils;

public abstract class DefaultFileSource implements FileSource
{
	@Override
	public void tune(Settings... settingsArray) throws Exception
	{
		close();

		if (settingsArray == null || settingsArray.length == 0)
			throw new IllegalArgumentException(Constants.EXCPT_FILE_SETTINGS_EMPTY);

		FileSettings fileSettings = AppUtils.getFirstValueFromArray(FileSettings.class, settingsArray);
		if (fileSettings == null)
			throw new IllegalArgumentException(Constants.EXCPT_FILE_SETTINGS_EMPTY);

		resetParameters(fileSettings);
	}

	protected abstract void resetParameters(FileSettings fileSettings);
}
