package ru.flc.service.spmaster.model.data.entity;

import ru.flc.service.spmaster.util.AppConstants;

public class StoredProcParameter
{
	public static String[] getTitleKeys()
	{
		return new String[] { AppConstants.KEY_COLUMN_SP_PARAM_TYPE,
				AppConstants.KEY_COLUMN_SP_PARAM_NAME,
				AppConstants.KEY_COLUMN_SP_PARAM_NULL,
				AppConstants.KEY_COLUMN_SP_PARAM_VALUE};
	}

	private StoredProcParamType type;
	private String name;
	private Class<?> valueClass;
	private Object value;
	private boolean nullValue;
	private int precision;
	private short scale;

	public StoredProcParameter(StoredProcParamType type,
							   String name, Class<?> valueClass, Object value,
							   boolean nullValue, int precision, short scale)
	{
		this.type = type;
		this.name = name;
		this.valueClass = valueClass;
		this.value = value;
		this.nullValue = nullValue;
		this.precision = precision;
		this.scale = scale;
	}

	public StoredProcParamType getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public Class<?> getValueClass()
	{
		return valueClass;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value) throws IllegalArgumentException
	{
		//TODO: Code the setting logic here.
	}

	public boolean isNullValue()
	{
		return nullValue;
	}

	public void setNullValue(boolean nullValue)
	{
		this.nullValue = nullValue;
	}

	public int getPrecision()
	{
		return precision;
	}

	public short getScale()
	{
		return scale;
	}
}
