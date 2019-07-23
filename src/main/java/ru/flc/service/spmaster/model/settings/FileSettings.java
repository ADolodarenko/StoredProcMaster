package ru.flc.service.spmaster.model.settings;

import org.dav.service.settings.SettingsManager;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.settings.parameter.ParameterHeader;
import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.util.AppConstants;

import java.io.File;

public class FileSettings extends TransmissiveSettings
{
	private static final int PARAM_COUNT = 1;

	private File file;

	public FileSettings(ResourceManager resourceManager) throws Exception
	{
		super(resourceManager);

		headers = new ParameterHeader[PARAM_COUNT];
		headers[0] = new ParameterHeader(Constants.KEY_PARAM_FIELD_DELIMITER, String.class,
				AppConstants.MESS_DEFAULT_FIELD_DELIMITER);

		init();
	}

	@Override
	public void save() throws Exception
	{
		SettingsManager.setStringValue(headers[0].getKeyString(), getFieldDelimiter());

		SettingsManager.saveSettings(resourceManager.getConfig());
	}

	public String getFieldDelimiter()
	{
		return (String) paramMap.get(headers[0].getKeyString()).getValue();
	}

	public File getFile()
	{
		return file;
	}

	public void setFile(File file)
	{
		this.file = file;
	}
}
