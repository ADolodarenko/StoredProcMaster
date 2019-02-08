package ru.flc.service.spmaster.util;

import org.dav.service.util.ResourceManager;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppResourceManager implements ResourceManager
{
	private static AppResourceManager instance;

	private static final ClassLoader loader = AppResourceManager.class.getClassLoader();

	public static final Locale RUS_LOCALE = new Locale.Builder().setLanguage("ru").setRegion("RU").build();
	public static final Locale ENG_LOCALE = new Locale.Builder().setLanguage("en").setRegion("US").build();

	public static AppResourceManager getInstance()
	{
		if (instance == null)
			instance = new AppResourceManager();

		return instance;
	}

	private Locale currentLocale;
	private ResourceBundle bundle;

	private AppResourceManager()
	{
		setCurrentLocale(ENG_LOCALE);
	}

	@Override
	public List<Locale> getAvailableLocales()
	{
		List<Locale> locales = new LinkedList<>();

		locales.add(ENG_LOCALE);
		locales.add(RUS_LOCALE);

		return locales;
	}

	@Override
	public Locale getCurrentLocale()
	{
		return currentLocale;
	}

	@Override
	public void setCurrentLocale(Locale locale)
	{
		this.currentLocale = locale;

		bundle = ResourceBundle.getBundle(AppConstants.MESS_RESOURCE_BUNDLE_NAME, this.currentLocale);
	}

	@Override
	public ResourceBundle getBundle()
	{
		return bundle;
	}

	@Override
	public File getConfig()
	{
		File result = null;
		String fullJarPath = null;

		try
		{
			fullJarPath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
		}
		catch (URISyntaxException e){}

		if (fullJarPath != null)
		{
			String fullConfPath = Paths.get(fullJarPath).getParent().toAbsolutePath() + File.separator + AppConstants.MESS_CONFIG_FILE_NAME;

			result = new File(fullConfPath);
		}

		return result;
	}

	@Override
	public ImageIcon getImageIcon(String s)
	{
		URL url = loader.getResource(s);

		if (url == null)
			return new ImageIcon();
		else
			return new ImageIcon(url);
	}

	public void switchCurrentLocale()
	{
		if (getCurrentLocale() == ENG_LOCALE)
			setCurrentLocale(RUS_LOCALE);
		else
			setCurrentLocale(ENG_LOCALE);
	}
}
