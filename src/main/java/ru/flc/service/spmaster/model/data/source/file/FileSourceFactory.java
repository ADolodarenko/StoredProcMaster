package ru.flc.service.spmaster.model.data.source.file;

import ru.flc.service.spmaster.model.data.source.file.excel.XLSFileSource;
import ru.flc.service.spmaster.model.data.source.file.excel.XLSXFileSource;
import ru.flc.service.spmaster.model.data.source.file.plain.PlainTextFileSource;
import ru.flc.service.spmaster.util.AppConstants;

public class FileSourceFactory
{
	public static FileSource getSource(String fileNameExtension)
	{
		if (fileNameExtension == null)
			return null;

		String extension = fileNameExtension.toLowerCase();

		if (AppConstants.MESS_FILENAME_EXT_VALUE_XLS.equals(extension))
			return new XLSFileSource();

		if (AppConstants.MESS_FILENAME_EXT_VALUE_XLSX.equals(extension))
			return new XLSXFileSource();

		if (AppConstants.MESS_FILENAME_EXT_VALUE_TXT.equals(extension) ||
				AppConstants.MESS_FILENAME_EXT_VALUE_CSV.equals(extension))
			return new PlainTextFileSource();

		return null;
	}
}
