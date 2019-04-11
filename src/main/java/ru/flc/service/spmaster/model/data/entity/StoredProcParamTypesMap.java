package ru.flc.service.spmaster.model.data.entity;

import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

/**
 * A map for stored procedure parameter types identified by their IDs.
 */
public class StoredProcParamTypesMap
{
	private static Map<Integer, StoredProcParamType> typesMap;

	static
	{
		typesMap = new HashMap<>();

		typesMap.put(DatabaseMetaData.procedureColumnIn, StoredProcParamType.IN);  //1
		typesMap.put(DatabaseMetaData.procedureColumnInOut, StoredProcParamType.IN_OUT);  //2
		typesMap.put(DatabaseMetaData.procedureColumnOut, StoredProcParamType.OUT);  //4
	}

	public static StoredProcParamType getType(int typeId)
	{
		return typesMap.get(typeId);
	}

	private StoredProcParamTypesMap(){}
}
