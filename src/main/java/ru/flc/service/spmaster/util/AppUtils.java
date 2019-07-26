package ru.flc.service.spmaster.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

/**
 * This class consists of static service methods.
 */
public class AppUtils
{
	private static Class<?>[] quotedStringValueClasses = { String.class, Date.class };

	public static boolean arrayContainsElement(int[] array, int element)
	{
		if (array == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_ARRAY_EMPTY);

		Arrays.sort(array);

		return Arrays.binarySearch(array, element) >= -1;
	}

	public static boolean arrayContainsElement(short[] array, short element)
	{
		if (array == null)
			throw new IllegalArgumentException(AppConstants.EXCPT_ARRAY_EMPTY);

		Arrays.sort(array);

		return Arrays.binarySearch(array, element) >= -1;
	}

	public static String getQuotedStringValue(Object value)
	{
		if (value != null)
		{
			Class<?> valueClass = value.getClass();

			for (Class<?> cl : quotedStringValueClasses)
				if (cl.isAssignableFrom(valueClass))
					return "'" + value + "'";

			return value.toString();
		}
		else
			return null;
	}

	public static Timestamp getSqlTimestamp(LocalDateTime localDateTime, ChronoUnit unitUpTo)
	{
		if (localDateTime == null)
			localDateTime = LocalDateTime.now();

		if (unitUpTo != null)
			localDateTime = localDateTime.truncatedTo(unitUpTo);

		return Timestamp.valueOf(localDateTime);
	}

	public static java.sql.Date getSqlDate(LocalDate localDate)
	{
		if (localDate == null)
			localDate = LocalDate.now();

		return java.sql.Date.valueOf(localDate);
	}

	public static java.sql.Time getSqlTime(LocalTime localTime, ChronoUnit unitUpTo)
	{
		if (localTime == null)
			localTime = LocalTime.now();

		if (unitUpTo != null)
			localTime = localTime.truncatedTo(unitUpTo);

		return java.sql.Time.valueOf(localTime);
	}

	@SuppressWarnings("unchecked")
	public static <X, Y> X getFirstValueFromArray(Class<X> valueClass, Y... array)
	{
		for (Y value : array)
			if (valueClass.isAssignableFrom(value.getClass()))
				return (X) value;

		return null;
	}

	private AppUtils(){}
}
