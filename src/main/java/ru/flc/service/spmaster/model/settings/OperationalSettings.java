package ru.flc.service.spmaster.model.settings;

import org.dav.service.settings.SettingsManager;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.settings.parameter.ParameterHeader;
import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.util.AppConstants;

import java.io.File;
import java.nio.charset.Charset;

public class OperationalSettings extends TransmissiveSettings
{
	private static final int PARAM_COUNT = 1;

	private File scriptFilePath;

	public OperationalSettings(ResourceManager resourceManager) throws Exception
	{
		super(resourceManager);

		headers = new ParameterHeader[PARAM_COUNT];
		headers[0] = new ParameterHeader(Constants.KEY_PARAM_CHARSET, Charset.class, Charset.defaultCharset());

		scriptFilePath = new File(Constants.MESS_CURRENT_PATH);

		init();
	}

	@Override
	public void load() throws Exception
	{
		super.load();
	}

	@Override
	public void save() throws Exception
	{
		SettingsManager.setStringValue(headers[0].getKeyString(), getScriptCharset().displayName());
		SettingsManager.setStringValue(AppConstants.KEY_PARAM_SCRIPT_FILE_PATH, getScriptFilePath().getAbsolutePath());

		SettingsManager.saveSettings(resourceManager.getConfig());
	}

	public Charset getScriptCharset()
	{
		return ((Charset) paramMap.get(headers[0].getKeyString()).getValue());
	}

	public File getScriptFilePath()
	{
		return scriptFilePath;
	}

	public void setScriptFilePath(File filePath)
	{
		scriptFilePath = filePath;
	}

	private void loadScriptFilePath()
	{
		if (SettingsManager.hasValue(AppConstants.KEY_PARAM_SCRIPT_FILE_PATH))
			scriptFilePath = new File(SettingsManager.getStringValue(AppConstants.KEY_PARAM_SCRIPT_FILE_PATH));
	}
}
