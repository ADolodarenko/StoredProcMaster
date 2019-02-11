package ru.flc.service.spmaster;

import org.dav.service.filesystem.FileSystem;
import org.dav.service.log.LogUtil;
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

		Controller controller = new Controller();
		controller.setDataModel(new DatabaseModel());
		controller.setSettingsModel(new AppSettingsModel(AppResourceManager.getInstance()));

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
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
}
