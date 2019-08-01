package ru.flc.service.spmaster.model.settings;

import java.awt.*;

public class ViewConstraints
{
	private static final Dimension MAIN_WIN_PREF_SIZE = new Dimension(640, 480);
	private static final Dimension MAIN_WIN_MIN_SIZE = new Dimension(640, 480);

	public Dimension getPreferredSize()
	{
		return MAIN_WIN_PREF_SIZE;
	}

	public Dimension getMinimumSize()
	{
		return MAIN_WIN_MIN_SIZE;
	}
}
