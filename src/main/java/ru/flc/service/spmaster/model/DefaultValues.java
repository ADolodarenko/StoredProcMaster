package ru.flc.service.spmaster.model;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class DefaultValues
{
	private static Map<Class<?>, Object> valuesMap;

	static
	{
		valuesMap = new HashMap<>();

		valuesMap.put(String.class, "");
		valuesMap.put(BigDecimal.class, new BigDecimal(0));
		valuesMap.put(Boolean.class, new Boolean(false));
		valuesMap.put(Integer.class, new Integer(0));
		valuesMap.put(Long.class, new Long(0));
		valuesMap.put(Float.class, new Float(0));
		valuesMap.put(Double.class, new Double(0));
		valuesMap.put(Date.class, new Date(Calendar.getInstance().getTimeInMillis()));
		valuesMap.put(Time.class, new Time(Calendar.getInstance().getTimeInMillis()));
		valuesMap.put(Timestamp.class, new Timestamp(Calendar.getInstance().getTimeInMillis()));
	}

	public static Object getValue(Class<?> type)
	{
		return valuesMap.get(type);
	}

	private DefaultValues(){}
}
