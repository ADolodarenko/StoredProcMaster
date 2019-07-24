package ru.flc.service.spmaster.model.data.source.file;

import ru.flc.service.spmaster.model.data.entity.DataElement;
import ru.flc.service.spmaster.model.data.source.Source;

import java.util.List;

public interface FileSource extends Source
{
	void setDataSheet(String sheetName) throws Exception;
	void addDataLine(List<DataElement> line) throws Exception;
	void addBlankLine() throws Exception;
}
