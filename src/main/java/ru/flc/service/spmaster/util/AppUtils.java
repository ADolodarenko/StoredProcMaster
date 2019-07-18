package ru.flc.service.spmaster.util;

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

	private AppUtils(){}
}
