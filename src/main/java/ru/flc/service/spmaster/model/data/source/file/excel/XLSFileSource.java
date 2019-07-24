package ru.flc.service.spmaster.model.data.source.file.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;

public class XLSFileSource extends ExcelFileSource
{
	@Override
	protected void getWorkbook() throws IOException
	{
		workbook = new HSSFWorkbook();
	}
}
