package ru.flc.service.spmaster.util;

import java.util.Arrays;

public class AppUtils
{
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

	private AppUtils(){}
}
