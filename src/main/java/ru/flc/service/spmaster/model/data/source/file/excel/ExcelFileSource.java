package ru.flc.service.spmaster.model.data.source.file.excel;

import org.apache.poi.ss.usermodel.*;
import ru.flc.service.spmaster.model.data.entity.DataElement;
import ru.flc.service.spmaster.model.data.source.file.DefaultFileSource;
import ru.flc.service.spmaster.model.settings.FileSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public abstract class ExcelFileSource extends DefaultFileSource
{
	private File file;
	private String dateTimeFormat;
	private CellStyle dateCellStyle;
	private int lastRowNumber;

	protected Workbook workbook;
	protected Sheet sheet;

	@Override
	protected void resetParameters(FileSettings fileSettings)
	{
		this.file = fileSettings.getFile();
		this.dateTimeFormat = fileSettings.getDateTimeFormat();
	}

	@Override
	public void setDataSheet(String sheetName) throws Exception
	{
		sheet = workbook.createSheet(sheetName);

		lastRowNumber = 0;
	}

	@Override
	public void addDataLine(List<DataElement> line) throws Exception
	{
		Row newRow = sheet.createRow(lastRowNumber++);

		for (int i = 0; i < line.size(); i++)
		{
			Cell cell = newRow.createCell(i);
			DataElement currentElement = line.get(i);

			Class<?> valueClass = currentElement.getType();
			Object value = currentElement.getValue();

			if (value == null)
			{
				cell.setCellType(CellType.BLANK);
			}
			else if (Date.class.isAssignableFrom(valueClass))
			{
				cell.setCellStyle(dateCellStyle);
				cell.setCellValue((Date) currentElement.getValue());
				//sheet.autoSizeColumn(i);
			}
			else if (Number.class.isAssignableFrom(valueClass))
			{
				cell.setCellValue( ((Number) value).doubleValue() );
			}
			else
				cell.setCellValue(value.toString());
		}
	}

	@Override
	public void addBlankLine() throws Exception
	{
		Row newRow = sheet.createRow(lastRowNumber++);
		Cell cell = newRow.createCell(0);
		cell.setCellType(CellType.BLANK);
	}

	@Override
	public void open() throws Exception
	{
		getWorkbook();

		if (workbook != null)
		{
			DataFormat format = workbook.createDataFormat();
			dateCellStyle = workbook.createCellStyle();
			dateCellStyle.setDataFormat(format.getFormat(dateTimeFormat));
		}
	}

	@Override
	public void close() throws Exception
	{
		if (workbook != null)
		{
			FileOutputStream outputStream = new FileOutputStream(file);
			workbook.write(outputStream);
			outputStream.close();
		}

		sheet = null;
		workbook = null;
	}

	protected abstract void getWorkbook() throws IOException;
}
