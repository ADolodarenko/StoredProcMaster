package ru.flc.service.spmaster.view.table.editor;

import java.util.Objects;

public class TableCellEditorKey
{
	private Class<?> valueClass;
	private int precision;
	private short scale;
	private boolean confirmationRequired;

	public TableCellEditorKey(Class<?> valueClass, int precision, short scale, boolean confirmationRequired)
	{
		this.valueClass = valueClass;
		this.precision = precision;
		this.scale = scale;
		this.confirmationRequired = confirmationRequired;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TableCellEditorKey that = (TableCellEditorKey) o;
		return precision == that.precision &&
				scale == that.scale &&
				confirmationRequired == that.confirmationRequired &&
				valueClass.equals(that.valueClass);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(valueClass, precision, scale, confirmationRequired);
	}

	public Class<?> getValueClass()
	{
		return valueClass;
	}

	public int getPrecision()
	{
		return precision;
	}

	public short getScale()
	{
		return scale;
	}

	public boolean isConfirmationRequired()
	{
		return confirmationRequired;
	}
}
