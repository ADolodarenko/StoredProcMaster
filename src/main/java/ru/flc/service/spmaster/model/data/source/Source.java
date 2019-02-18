package ru.flc.service.spmaster.model.data.source;

import org.dav.service.settings.Settings;

public interface Source
{
	void open() throws Exception;
	void close() throws Exception;
	void tune(Settings settings) throws Exception;
}
