package ru.flc.service.spmaster.model.data.source.file.plain;

import ru.flc.service.spmaster.model.data.entity.DataElement;
import ru.flc.service.spmaster.model.data.source.file.DefaultFileSource;
import ru.flc.service.spmaster.model.settings.FileSettings;
import ru.flc.service.spmaster.util.AppConstants;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PlainTextFileSource extends DefaultFileSource
{
	private static String buildStringFromElements(List<DataElement> elementList,
												  String elementDelimiter, DateFormat dateFormatter)
	{
		if (elementList == null || elementList.isEmpty())
			return null;

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < elementList.size(); i++)
		{
			if (i > 0)
				builder.append(elementDelimiter);

			DataElement element = elementList.get(i);
			Class<?> valueClass = element.getType();
			Object value = element.getValue();

			if (value != null)
			{
				if (Date.class.isAssignableFrom(valueClass))
					builder.append(dateFormatter.format(value));
				else
					builder.append(value.toString());
			}
		}

		return builder.toString();
	}

	private File file;
	private String fieldDelimiter;
	private PrintWriter writer;
	private DateFormat dateFormatter;

	@Override
	protected void resetParameters(FileSettings fileSettings)
	{
		this.file = fileSettings.getFile();

		String dateTimeFormat = fileSettings.getDateTimeFormat();
		if (dateTimeFormat == null || dateTimeFormat.isEmpty())
			this.dateFormatter = new SimpleDateFormat(AppConstants.DEFAULT_FORMAT_DATETIME);
		else
			this.dateFormatter = new SimpleDateFormat(dateTimeFormat);

		String fieldDelimiter = fileSettings.getFieldDelimiter();
		if (fieldDelimiter == null || fieldDelimiter.isEmpty())
			this.fieldDelimiter = AppConstants.MESS_DEFAULT_FIELD_DELIMITER;
		else
			this.fieldDelimiter = fieldDelimiter;
	}

	@Override
	public void setDataSheet(String sheetName) throws Exception
	{
		List<DataElement> lineList = new LinkedList<>();
		lineList.add(new DataElement(sheetName, String.class));

		addBlankLine();
		addDataLine(lineList);
		addBlankLine();
	}

	@Override
	public void addDataLine(List<DataElement> line) throws Exception
	{
		if (writer != null)
			writer.println(buildStringFromElements(line, fieldDelimiter, dateFormatter));
	}

	@Override
	public void addBlankLine() throws Exception
	{
		if (writer != null)
			writer.println();
	}

	@Override
	public void open() throws Exception
	{
		writer = new PrintWriter(file, StandardCharsets.UTF_8.name());
	}

	@Override
	public void close() throws Exception
	{
		if (writer != null)
		{
			writer.close();
			writer = null;
		}
	}
}
