package ru.flc.service.spmaster.model.settings;

import org.dav.service.settings.SettingsManager;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.settings.parameter.ParameterHeader;
import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.util.AppConstants;

import java.nio.charset.Charset;

public class OperationalSettings extends TransmissiveSettings
{
	private static final int PARAM_COUNT = 2;

	public OperationalSettings(ResourceManager resourceManager) throws Exception
	{
		super(resourceManager);

		headers = new ParameterHeader[PARAM_COUNT];
		headers[0] = new ParameterHeader(Constants.KEY_PARAM_CHARSET, Charset.class, Charset.defaultCharset());
		headers[1] = new ParameterHeader(AppConstants.KEY_PARAM_DB_SERVICE_CATALOG, String.class, "");

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
		SettingsManager.setStringValue(headers[1].getKeyString(), getServiceCatalog());

		SettingsManager.saveSettings(resourceManager.getConfig());
	}

	public Charset getScriptCharset()
	{
		return ((Charset) paramMap.get(headers[0].getKeyString()).getValue());
	}

	public String getServiceCatalog()
	{
		return ((String) paramMap.get(headers[1].getKeyString()).getValue());
	}
}
