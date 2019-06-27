package ru.flc.service.spmaster.model.data.entity;

import org.dav.service.util.Constants;
import ru.flc.service.spmaster.util.AppConstants;

import java.math.BigDecimal;

public class StoredProcParameter
{
	public static String[] getTitleKeys()
	{
		return new String[] { AppConstants.KEY_COLUMN_SP_PARAM_TYPE,
				AppConstants.KEY_COLUMN_SP_PARAM_NAME,
				AppConstants.KEY_COLUMN_SP_PARAM_VALUETYPE,
				AppConstants.KEY_COLUMN_SP_PARAM_NULL,
				AppConstants.KEY_COLUMN_SP_PARAM_VALUE};
	}

	private StoredProcParamType type;
	private String name;
	private int sqlType;
	private Class<?> valueClass;
	private Object value;
	private boolean nullValue;
	private int precision;
	private short scale;
	private String valueTypeName;
	private int ordinalPosition;

	public StoredProcParameter(StoredProcParamType type,
							   String name, int sqlType, Class<?> valueClass, Object value,
							   boolean nullValue, int precision, short scale,
							   String valueTypeName, int ordinalPosition)
	{
		this.type = type;
		this.name = name;
		this.sqlType = sqlType;
		this.valueClass = valueClass;
		this.value = value;
		this.nullValue = nullValue;
		this.precision = precision;
		this.scale = scale;
		this.valueTypeName = valueTypeName;
		this.ordinalPosition = ordinalPosition;
	}

	public StoredProcParamType getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
	}

	public int getSqlType()
	{
		return sqlType;
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
		if (!nullValue && value == null)
			throw new IllegalArgumentException(Constants.EXCPT_PARAM_VALUE_EMPTY);

		Class<?> thisClass = getValueClass();
		Class<?> thatClass = value.getClass();

		String thisClassName = thisClass.getSimpleName();
		String thatClassName = thatClass.getSimpleName();

		if (thisClass.equals(thatClass))
			this.value = value;
		else if (Constants.CLASS_NAME_STRING.equals(thisClassName))
			this.value = value.toString();
		else if (Constants.CLASS_NAME_STRING.equals(thatClassName))
		{
			String stringValue = (String) value;

			switch (thisClassName)
			{
				case Constants.CLASS_NAME_BOOLEAN:
					this.value = Boolean.valueOf(stringValue);
					break;
				case Constants.CLASS_NAME_INTEGER:
					this.value = Integer.parseInt(stringValue);
					break;
				case AppConstants.CLASS_NAME_LONG:
					this.value = Long.parseLong(stringValue);
					break;
				case AppConstants.CLASS_NAME_FLOAT:
					this.value = Float.parseFloat(stringValue);
					break;
				case Constants.CLASS_NAME_DOUBLE:
					this.value = Double.parseDouble(stringValue);
					break;
				case AppConstants.CLASS_NAME_BIGDECIMAL:
					this.value = new BigDecimal(stringValue);
					break;
				default:
					throw new IllegalArgumentException(String.format(Constants.EXCPT_PARAM_VALUE_WRONG, stringValue));
			}
		}
		else if (thisClass.isAssignableFrom(thatClass))  //Core.amongSuperClasses(thisClass, thatClass)
			this.value = value;
		else
			throw new IllegalArgumentException(String.format(Constants.EXCPT_PARAM_VALUE_WRONG, value.toString()));
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

	public String getValueTypeName()
	{
		return valueTypeName;
	}

	public int getOrdinalPosition()
	{
		return ordinalPosition;
	}
}
