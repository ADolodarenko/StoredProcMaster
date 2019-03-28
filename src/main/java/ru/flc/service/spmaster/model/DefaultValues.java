package ru.flc.service.spmaster.model;

import java.util.HashMap;
import java.util.Map;

public class DefaultValues
{
	private static Map<Class<?>, Object> valuesMap;

	static
	{
		valuesMap = new HashMap<>();

		valuesMap.put(String.class, "");

	}

	public static Object getValue(Class<?> type)
	{
		return valuesMap.get(type);
	}

	private DefaultValues(){}
}
