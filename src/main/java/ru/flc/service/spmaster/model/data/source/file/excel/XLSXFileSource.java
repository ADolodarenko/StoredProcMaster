package ru.flc.service.spmaster.model.data.source.file.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class XLSXFileSource extends ExcelFileSource
{
	@Override
	protected void getWorkbook() throws IOException
	{
		workbook = new XSSFWorkbook();
	}
}
