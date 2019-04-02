package ru.flc.service.spmaster.model.data.entity;

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

		typesMap.put(1, StoredProcParamType.IN);
		typesMap.put(2, StoredProcParamType.IN_OUT);
		typesMap.put(4, StoredProcParamType.OUT);
	}

	public static StoredProcParamType getType(int typeId)
	{
		return typesMap.get(typeId);
	}

	private StoredProcParamTypesMap(){}
}
