package ru.flc.service.spmaster.view;

import org.dav.service.settings.ViewSettings;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.TitleAdjuster;
import org.dav.service.view.ViewUtils;
import org.dav.service.view.dialog.SettingsDialog;
import org.dav.service.view.dialog.SettingsDialogInvoker;
import ru.flc.service.spmaster.controller.ActionsManager;
import ru.flc.service.spmaster.controller.Controller;
import ru.flc.service.spmaster.model.settings.ViewConstraints;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame implements View, SettingsDialogInvoker
{
	private Controller controller;
	private ResourceManager resourceManager;
	private ActionsManager actionsManager;
	private TitleAdjuster titleAdjuster;

	private SettingsDialog settingsDialog;
	private AboutDialog aboutDialog;

	public MainFrame(Controller controller)
	{
		this.controller = controller;
		this.controller.setView(this);

		this.resourceManager = controller.getResourceManager();

		this.titleAdjuster = new TitleAdjuster();

		initComponents();
		initFrame();
	}

	@Override
	public void repaintFrame()
	{
		titleAdjuster.resetComponents();
		validate();

		ViewUtils.adjustDialogs();
	}

	@Override
	public void showHelpInfo()
	{
		if (aboutDialog == null)
		{
			try
			{
				aboutDialog = new AboutDialog(this, resourceManager);
				aboutDialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			}
			catch (Exception e)
			{
				log(e);
			}
		}

		if (aboutDialog != null)
			aboutDialog.setVisible(true);
	}

	@Override
	public void setPreferredBounds(ViewConstraints settings)
	{
		setMinimumSize(settings.getMinimumSize());
		setPreferredSize(settings.getPreferredSize());
	}

	@Override
	public void setActualBounds(ViewSettings settings)
	{
		if (settings.isMainWindowMaximized())
			setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
		else
			setBounds(settings.getMainWindowPosition().x,
					settings.getMainWindowPosition().y,
					settings.getMainWindowSize().width,
					settings.getMainWindowSize().height);
	}

	@Override
	public void log(Exception e)
	{

	}

	@Override
	public void setFocus()
	{

	}

	@Override
	public void reloadSettings()
	{

	}

	private void initComponents()
	{
		ViewUtils.setDialogOwner(this);
		ViewUtils.adjustDialogs();

		initActions();
		initToolBar();

		titleAdjuster.resetComponents();

		pack();
	}

	private void initActions()
	{
		actionsManager = new ActionsManager(controller, resourceManager);
	}

	private void initToolBar()
	{
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);

		toolBar.addSeparator();
		toolBar.add(actionsManager.getConnectDbAction());
		toolBar.add(actionsManager.getDisconnectDbAction());
		toolBar.addSeparator();
		toolBar.add(actionsManager.getRefreshSpListAction());
		toolBar.addSeparator();
		toolBar.add(actionsManager.getExecSpAction());
		toolBar.addSeparator();
		toolBar.add(actionsManager.getShowSettingsAction());
		toolBar.add(actionsManager.getShowHelpAction());

		add(toolBar, BorderLayout.NORTH);
	}

	private void initFrame()
	{
		setIconImage(resourceManager.getImageIcon(AppConstants.ICON_NAME_MAIN).getImage());

		controller.setViewPreferredBounds();
		controller.setViewActualBounds();

		setClosingPolicy();
	}

	private void setClosingPolicy()
	{
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				//cancelProcesses();
				//updateViewSettings();
				//updateSpecificSettings(operationalSettings);
			}
		});
	}
}
