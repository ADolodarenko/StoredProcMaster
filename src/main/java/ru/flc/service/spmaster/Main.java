package ru.flc.service.spmaster;

import org.dav.service.filesystem.FileSystem;
import org.dav.service.log.LogUtil;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.ViewUtils;
import ru.flc.service.spmaster.controller.Controller;
import ru.flc.service.spmaster.model.data.DatabaseModel;
import ru.flc.service.spmaster.model.settings.AppSettingsModel;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.util.AppResourceManager;
import ru.flc.service.spmaster.view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class Main
{
	public static void main(String[] args)
	{
		setLogger();

		ResourceManager resourceManager = AppResourceManager.getInstance();
		ViewUtils.resetResourceManager(resourceManager);

		Controller controller = new Controller();
		controller.setSettingsModel(new AppSettingsModel(resourceManager));
		controller.setDataModel(new DatabaseModel());

		EventQueue.invokeLater(() -> runGUI(controller));
	}

	private static void setLogger()
	{
		LogUtil.setLogger(FileSystem.getCurrentDir(Main.class), AppConstants.MESS_LOGGING_PROPERTIES_FILE_NAME);
	}

	private static void setLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	private static void runGUI(Controller controller)
	{
		setLookAndFeel();

		JFrame mainFrame = new MainFrame(controller);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
}
