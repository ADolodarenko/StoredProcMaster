package ru.flc.service.spmaster.model.data.source.file.plain;

import ru.flc.service.spmaster.model.data.entity.DataElement;
import ru.flc.service.spmaster.model.data.source.file.DefaultFileSource;
import ru.flc.service.spmaster.model.settings.FileSettings;

import java.util.List;

public class PlainTextFileSource extends DefaultFileSource
{

	@Override
	protected void resetParameters(FileSettings fileSettings)
	{

	}

	@Override
	public void setDataSheet(String sheetName) throws Exception
	{

	}

	@Override
	public void addDataLine(List<DataElement> line) throws Exception
	{

	}

	@Override
	public void open() throws Exception
	{

	}

	@Override
	public void close() throws Exception
	{

	}
}
