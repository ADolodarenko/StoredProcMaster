package ru.flc.service.spmaster.model.data.source;

import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseTypes
{
	private static Map<Integer, Class<?>> typesMap;

	static
	{
		typesMap = new HashMap<>();

		typesMap.put(Types.CHAR, String.class);
		typesMap.put(Types.VARCHAR, String.class);
		typesMap.put(Types.LONGVARCHAR, String.class);
		typesMap.put(Types.NUMERIC, BigDecimal.class);
		typesMap.put(Types.DECIMAL, BigDecimal.class);
		typesMap.put(Types.BIT, Boolean.class);
		typesMap.put(Types.TINYINT, Integer.class);
		typesMap.put(Types.SMALLINT, Integer.class);
		typesMap.put(Types.INTEGER, Integer.class);
		typesMap.put(Types.BIGINT, Long.class);
		typesMap.put(Types.REAL, Float.class);
		typesMap.put(Types.FLOAT, Double.class);
		typesMap.put(Types.DOUBLE, Double.class);
		typesMap.put(Types.BINARY, byte[].class);
		typesMap.put(Types.VARBINARY, byte[].class);
		typesMap.put(Types.LONGVARBINARY, byte[].class);
		typesMap.put(Types.DATE, Date.class);
		typesMap.put(Types.TIME, Time.class);
		typesMap.put(Types.TIMESTAMP, Timestamp.class);
		typesMap.put(Types.CLOB, Clob.class);
		typesMap.put(Types.BLOB, Blob.class);
		typesMap.put(Types.ARRAY, Array.class);
		typesMap.put(Types.STRUCT, SQLData.class);
		typesMap.put(Types.REF, Ref.class);
	}

	public static Class<?> getJavaClass(Integer databaseTypeId)
	{
		return typesMap.get(databaseTypeId);
	}

	private DatabaseTypes(){}
}
