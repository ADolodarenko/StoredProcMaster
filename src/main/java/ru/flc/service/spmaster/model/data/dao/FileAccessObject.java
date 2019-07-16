package ru.flc.service.spmaster.model.data.dao;

import org.dav.service.util.Constants;
import ru.flc.service.spmaster.model.data.entity.DataElement;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.model.data.source.file.FileSource;

import java.util.List;

public class FileAccessObject implements AccessObject
{
	private FileSource source;

	public FileAccessObject(FileSource source)
	{
		if (source == null)
			throw new IllegalArgumentException(Constants.EXCPT_FILE_SOURCE_EMPTY);

		this.source = source;
	}

	@Override
	public void open() throws Exception
	{
		source.open();
	}

	@Override
	public void close() throws Exception
	{
		source.close();
	}

	public void addDataTable(String sheetName, DataTable dataTable) throws Exception
	{
		source.setDataSheet(sheetName);

		source.addDataLine(dataTable.getHeaders());

		for (List<DataElement> row : dataTable.getRows())
			source.addDataLine(row);
	}
}
