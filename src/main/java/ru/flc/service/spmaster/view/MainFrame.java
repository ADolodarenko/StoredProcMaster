package ru.flc.service.spmaster.view;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.settings.ViewSettings;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import org.dav.service.view.TitleAdjuster;
import org.dav.service.view.ViewUtils;
import org.dav.service.view.dialog.SettingsDialog;
import org.dav.service.view.dialog.SettingsDialogInvoker;
import ru.flc.service.spmaster.controller.ActionsManager;
import ru.flc.service.spmaster.controller.Controller;
import ru.flc.service.spmaster.model.settings.ViewConstraints;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.util.AppState;
import ru.flc.service.spmaster.view.thirdparty.TextLineNumber;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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

	private JList<String> procList;
	private JScrollPane procListPane;
	private JPanel procListPanel;

	private JTextArea procTextArea;
	private JScrollPane procTextPane;
	private JPanel procTextPanel;

	private JTable procResultTable;
	private JScrollPane procResultPane;
	private JPanel procResultPanel;

	private JTable logTable;
	private JScrollPane logPane;
	private JPanel logPanel;

	private JSplitPane oneProcPane;
	private JSplitPane operationalPane;
	private JSplitPane generalPane;

	private JLabel statusLabel;

	public MainFrame(Controller controller)
	{
		this.controller = controller;
		this.controller.setView(this);

		this.resourceManager = controller.getResourceManager();

		this.titleAdjuster = new TitleAdjuster();

		initComponents();
		initFrame();

		controller.changeAppState(AppState.DISCONNECTED);
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

		resetPanelsMinimumSizes();
		resetSplitDividers();
	}

	@Override
	public void showSettings(TransmissiveSettings[] settingsArray)
	{
		if (settingsDialog == null)
		{
			try
			{
				settingsDialog = new SettingsDialog(this, this, resourceManager, settingsArray);
				settingsDialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			}
			catch (Exception e)
			{
				log(e);
			}
		}

		if (settingsDialog != null)
			settingsDialog.setVisible(true);
	}

	@Override
	public void showConnectionStatus(DatabaseSettings settings)
	{
		if (settings != null)
		{
			titleAdjuster.changeComponentTitle(statusLabel, new Title(resourceManager,
					AppConstants.KEY_PANEL_STATUS_CONNECTED,
					settings.getHost(), settings.getUserName(), settings.getCatalog()));
		}
		else
		{
			titleAdjuster.changeComponentTitle(statusLabel, new Title(resourceManager,
					AppConstants.KEY_PANEL_STATUS_DISCONNECTED));
		}
	}

	@Override
	public void showException(Exception e)
	{
		log(e);
	}

	@Override
	public void adjustToAppState()
	{
		actionsManager.adjustActionsToAppState();
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
		controller.resetCurrentLocale();
	}

	private void initComponents()
	{
		ViewUtils.setDialogOwner(this);
		ViewUtils.adjustDialogs();

		initActions();
		initToolBar();

		initProcListPanel();
		initProcTextPanel();
		initProcResultPanel();
		initLogPanel();

		initSplitPanels();

		initStatusPanel();

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

	private void initProcListPanel()
	{
		procList = new JList<>();
		procList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		procListPane = new JScrollPane(procList);

		procListPanel = getPanelWithBorderLayout(procListPane, BorderLayout.CENTER, AppConstants.KEY_PANEL_PROC_LIST,
				TitledBorder.TOP, TitledBorder.CENTER);
	}

	private void initProcTextPanel()
	{
		procTextArea = new JTextArea();
		procTextArea.setEditable(false);

		Font font = new Font(Font.MONOSPACED, Font.PLAIN, 14);
		procTextArea.setFont(font);
		procTextArea.setLineWrap(true);
		procTextArea.setWrapStyleWord(true);

		TextLineNumber lineNumber = new TextLineNumber(procTextArea);

		procTextPane = new JScrollPane(procTextArea);
		procTextPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		procTextPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		procTextPane.setRowHeaderView(lineNumber);

		procTextPanel = getPanelWithBorderLayout(procTextPane, BorderLayout.CENTER,
				AppConstants.KEY_PANEL_PROC_TEXT, TitledBorder.TOP, TitledBorder.CENTER);
	}

	private void initProcResultPanel()
	{
		procResultTable = new JTable();

		procResultPane = new JScrollPane(procResultTable);
		procResultPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		procResultPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		procResultPanel = getPanelWithBorderLayout(procResultPane, BorderLayout.CENTER,
				AppConstants.KEY_PANEL_PROC_RESULT,	TitledBorder.TOP, TitledBorder.CENTER);
	}

	private void initLogPanel()
	{
		logTable = new JTable();

		logPane = new JScrollPane(logTable);
		logPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		logPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		logPanel = getPanelWithBorderLayout(logPane, BorderLayout.CENTER, AppConstants.KEY_PANEL_LOG,
				TitledBorder.TOP, TitledBorder.CENTER);
	}

	private void initSplitPanels()
	{
		oneProcPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, procTextPanel, procResultPanel);
		operationalPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, procListPanel, oneProcPane);
		generalPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, operationalPane, logPanel);

		add(generalPane, BorderLayout.CENTER);
	}

	private void initStatusPanel()
	{
		JPanel statusPanel = new JPanel();
		statusPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		statusPanel.setPreferredSize(new Dimension(getWidth(), 20));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));

		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);

		titleAdjuster.registerComponent(statusLabel, new Title(resourceManager, AppConstants.KEY_PANEL_STATUS_DISCONNECTED));

		add(statusPanel, BorderLayout.SOUTH);

		showConnectionStatus(null);
	}

	private JPanel getPanelWithBorderLayout(Component inhabitant, Object constraints,
											String titleKey, int titleJustification, int titlePosition)
	{
		JPanel panel = new JPanel(new BorderLayout());

		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(),
				"", titleJustification, titlePosition));
		titleAdjuster.registerComponent(panel, new Title(resourceManager, titleKey));
		panel.add(inhabitant, constraints);

		return panel;
	}

	private void resetPanelsMinimumSizes()
	{
		resetPanelMinimumSize(procListPanel);
		resetPanelMinimumSize(procTextPanel);
		resetPanelMinimumSize(procResultPanel);
		resetPanelMinimumSize(logPanel);
	}

	private void resetPanelMinimumSize(JPanel panel)
	{
		if (panel != null)
		{
			Dimension size = getPanelMinimumSize(panel);
			panel.setMinimumSize(size);

			panel.repaint();
		}
	}

	private Dimension getPanelMinimumSize(JPanel panel)
	{
		if (panel == procListPanel)
			return new Dimension(getWidth()/4, getHeight()/4);
		else if (panel == procTextPanel)
			return new Dimension(getWidth()/4, getHeight()/8);
		else if (panel == procResultPanel)
			return new Dimension(getWidth()/4, getHeight()/8);
		else if (panel == logPanel)
			return new Dimension(getWidth(), getHeight()/4);

		return null;
	}

	private void resetSplitDividers()
	{
		generalPane.setResizeWeight(1);
		operationalPane.setResizeWeight(0);
		oneProcPane.setResizeWeight(0.5);
	}

	private void initFrame()
	{
		setIconImage(resourceManager.getImageIcon(AppConstants.ICON_NAME_MAIN).getImage());

		setResizingPolicy();
		setClosingPolicy();

		controller.setViewPreferredBounds();
		controller.setViewActualBounds();
	}

	private void setResizingPolicy()
	{
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e)
			{
				resetPanelsMinimumSizes();
				resetSplitDividers();
			}
		});
	}

	private void setClosingPolicy()
	{
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				//cancelProcesses();
				controller.updateViewBounds();
				controller.saveAllSettings();
			}
		});
	}
}
