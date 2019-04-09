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
import org.dav.service.view.table.LogEvent;
import org.dav.service.view.table.LogEventTable;
import org.dav.service.view.table.LogEventTableModel;
import org.dav.service.view.table.LogEventWriter;
import ru.flc.service.spmaster.controller.ActionsManager;
import ru.flc.service.spmaster.controller.Controller;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcParameter;
import ru.flc.service.spmaster.model.settings.ViewConstraints;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.util.AppStatus;
import ru.flc.service.spmaster.view.dialog.AboutDialog;
import ru.flc.service.spmaster.view.dialog.ExecutionDialog;
import ru.flc.service.spmaster.view.table.StoredProcListTable;
import ru.flc.service.spmaster.view.table.StoredProcListTableModel;
import ru.flc.service.spmaster.view.table.listener.StoredProcListSelectionListener;
import ru.flc.service.spmaster.view.thirdparty.TextLineNumber;
import ru.flc.service.spmaster.view.util.ViewComponents;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.logging.Logger;

public class MainFrame extends JFrame implements View, SettingsDialogInvoker
{
	private static final Logger LOGGER = Logger.getLogger(MainFrame.class.getName());


	private Controller controller;
	private ResourceManager resourceManager;
	private ActionsManager actionsManager;
	private TitleAdjuster titleAdjuster;

	private ExecutionDialog executionDialog;
	private SettingsDialog settingsDialog;
	private AboutDialog aboutDialog;

	private StoredProcListTableModel procListTableModel;
	private StoredProcListTable procListTable;
	private JScrollPane procListPane;
	private JPanel procListPanel;

	private JTextArea procTextArea;
	private JScrollPane procTextPane;
	private JPanel procTextPanel;

	private JTable procResultTable;
	private JScrollPane procResultPane;
	private JPanel procResultPanel;

	private LogEventTableModel logTableModel;
	private LogEventTable logTable;
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

		controller.changeAppStatus(AppStatus.DISCONNECTED);
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
	public void clearData()
	{
		procListTableModel.clear();
		procListTableModel.fireTableDataChanged();

		procTextArea.setText("");
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
	public void adjustToAppStatus()
	{
		actionsManager.adjustActionsToAppState();
	}

	@Override
	public void showStoredProcList(List<StoredProc> storedProcList)
	{
		procListTableModel.addAllRows(storedProcList);

		titleAdjuster.resetComponents();
		procListTableModel.fireTableDataChanged();
	}

	@Override
	public void showStoredProcText(List<String> storedProcTextLines)
	{
		procTextArea.setText("");

		if (storedProcTextLines != null && (!storedProcTextLines.isEmpty()))
		{
			for (String line : storedProcTextLines)
				procTextArea.append(line);

			procTextArea.setCaretPosition(0);
		}
	}

	@Override
	public void showStoredProcInfo(StoredProc storedProc, List<StoredProcParameter> storedProcParams)
	{
		if (executionDialog == null)
		{
			try
			{
				executionDialog = new ExecutionDialog(this, this, resourceManager);
				executionDialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			}
			catch (Exception e)
			{
				log(e);
			}
		}

		if (executionDialog != null)
		{
			executionDialog.tune(storedProc, storedProcParams);
			executionDialog.setVisible(true);
		}
	}

	@Override
	public StoredProc getCurrentStoredProc()
	{
		ListSelectionModel selectionModel = procListTable.getSelectionModel();

		if ( !selectionModel.isSelectionEmpty() )
		{
			int selectionIndex = selectionModel.getMinSelectionIndex();

			return procListTable.getStoredProc(selectionIndex);
		}
		else
			return null;
	}

	@Override
	public void log(Exception e)
	{
		LogEventWriter.writeThrowable(e, logTableModel);
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
		procListTableModel = new StoredProcListTableModel(resourceManager, StoredProc.getTitleKeys(), null);
		procListTable = new StoredProcListTable(procListTableModel, resourceManager);
		procListTable.getSelectionModel().addListSelectionListener(new StoredProcListSelectionListener(procListTable, controller));

		procListPane = new JScrollPane(procListTable);

		procListPanel = ViewComponents.getTitledPanelWithBorderLayout(resourceManager, titleAdjuster,
				procListPane, BorderLayout.CENTER, BorderFactory.createEmptyBorder(),
				AppConstants.KEY_PANEL_PROC_LIST, TitledBorder.TOP, TitledBorder.CENTER);
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

		procTextPanel = ViewComponents.getTitledPanelWithBorderLayout(resourceManager, titleAdjuster,
				procTextPane, BorderLayout.CENTER, BorderFactory.createEmptyBorder(),
				AppConstants.KEY_PANEL_PROC_TEXT, TitledBorder.TOP, TitledBorder.CENTER);
	}

	private void initProcResultPanel()
	{
		procResultTable = new JTable();

		procResultPane = new JScrollPane(procResultTable);
		procResultPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		procResultPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		procResultPanel = ViewComponents.getTitledPanelWithBorderLayout(resourceManager, titleAdjuster,
				procResultPane, BorderLayout.CENTER, BorderFactory.createEmptyBorder(),
				AppConstants.KEY_PANEL_PROC_RESULT,	TitledBorder.TOP, TitledBorder.CENTER);
	}

	private void initLogPanel()
	{
		LogEvent.setResourceManager(resourceManager);
		LogEventWriter.setLogger(LOGGER);

		logTableModel = new LogEventTableModel(resourceManager, null);
		logTable = new LogEventTable(logTableModel);

		logPane = new JScrollPane(logTable);
		logPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		logPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

		logPanel = ViewComponents.getTitledPanelWithBorderLayout(resourceManager, titleAdjuster,
				logPane, BorderLayout.CENTER, BorderFactory.createEmptyBorder(),
				AppConstants.KEY_PANEL_LOG, TitledBorder.TOP, TitledBorder.CENTER);
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
