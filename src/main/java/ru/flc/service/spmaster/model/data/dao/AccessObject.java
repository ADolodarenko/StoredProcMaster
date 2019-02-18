package ru.flc.service.spmaster.model.data.dao;

import ru.flc.service.spmaster.model.data.entity.StoredProc;

import java.util.List;

public interface AccessObject
{
	void open() throws Exception;
	void close() throws Exception;
	List<StoredProc> getStoredProcList() throws Exception;
	void execStoredProc(StoredProc proc) throws Exception;
}
