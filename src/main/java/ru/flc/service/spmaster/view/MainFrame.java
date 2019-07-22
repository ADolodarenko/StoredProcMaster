package ru.flc.service.spmaster.view;

import org.dav.service.settings.DatabaseSettings;
import org.dav.service.settings.TransmissiveSettings;
import org.dav.service.settings.ViewSettings;
import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.ExtensionInfoType;
import org.dav.service.view.Title;
import org.dav.service.view.TitleAdjuster;
import org.dav.service.view.ViewUtils;
import org.dav.service.view.dialog.SettingsDialog;
import org.dav.service.view.dialog.SettingsDialogInvoker;
import org.dav.service.view.table.LogEvent;
import org.dav.service.view.table.LogEventTable;
import org.dav.service.view.table.LogEventTableModel;
import org.dav.service.view.table.LogEventWriter;
import org.dav.service.view.textfield.IconHintTextField;
import ru.flc.service.spmaster.controller.ActionsManager;
import ru.flc.service.spmaster.controller.Controller;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.model.data.entity.DataTableType;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.model.data.entity.StoredProcStatus;
import ru.flc.service.spmaster.model.settings.ViewConstraints;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.util.AppStatus;
import ru.flc.service.spmaster.view.dialog.AboutDialog;
import ru.flc.service.spmaster.view.dialog.ExecutionDialog;
import ru.flc.service.spmaster.model.data.entity.DataPage;
import ru.flc.service.spmaster.view.menu.MenuManager;
import ru.flc.service.spmaster.view.table.*;
import ru.flc.service.spmaster.view.table.filter.TableFilterListener;
import ru.flc.service.spmaster.view.table.listener.StoredProcResultTableMouseListener;
import ru.flc.service.spmaster.view.table.listener.StoredProcListMouseListener;
import ru.flc.service.spmaster.view.table.listener.StoredProcListSelectionListener;
import ru.flc.service.spmaster.view.table.renderer.ArbitraryTableCellRendererFactory;
import ru.flc.service.spmaster.view.thirdparty.TextLineNumber;
import ru.flc.service.spmaster.view.util.ViewComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * This class represents the View part of the application (according to MVC pattern) and its main window.
 */
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

	private MenuManager menuManager;

	private StoredProcListTableModel procListTableModel;
	private StoredProcListTable procListTable;
	private Map<StoredProc, Integer> procListMap;
	private JScrollPane procListPane;
	private JPanel procListPanel;
	private JTextField procSearchField;

	private JTextArea procTextArea;
	private JScrollPane procTextPane;
	private JPanel procTextPanel;

	private JTabbedPane procResultsTabs;
	private JPanel procResultsPanel;
	private Icon resultTabIcon;
	private Map<Integer, DataPage> dataPageMap;
	private StoredProcResultTableMouseListener resultTableMouseListener;

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
	public void clearAllData()
	{
		procListMap.clear();
		procListTableModel.clear();
		procListTableModel.fireTableDataChanged();

		clearCurrentData();
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
		actionsManager.adjustToAppStatus();

		if (executionDialog != null && executionDialog.isVisible())
			executionDialog.adjustToAppStatus(controller.checkAppStatuses(AppStatus.RUNNING));
	}

	@Override
	public void showStoredProcList(List<StoredProc> storedProcList)
	{
		procListTableModel.addAllRows(storedProcList);
		procListTableModel.fireTableDataChanged();

		updateProcListMap();

		titleAdjuster.resetComponents();
		procListTableModel.fireTableDataChanged();
	}

	@Override
	public void showStoredProcText(List<String> storedProcTextLines)
	{
		if (storedProcTextLines != null && (!storedProcTextLines.isEmpty()))
		{
			for (String line : storedProcTextLines)
				procTextArea.append(line);

			procTextArea.setCaretPosition(0);
		}
	}

	@Override
	public void showStoredProcInfo(StoredProc storedProc)
	{
		if (executionDialog == null)
		{
			try
			{
				executionDialog = new ExecutionDialog(this, this,
						resourceManager, actionsManager.getExecSpAction(), actionsManager.getCancelSpAction());
			}
			catch (Exception e)
			{
				log(e);
			}
		}

		if (executionDialog != null)
		{
			executionDialog.tune(storedProc);
			executionDialog.setVisible(true);
		}
	}

	@Override
	public StoredProc getCurrentStoredProc()
	{
		StoredProc storedProc = null;

		if (executionDialog != null && executionDialog.isActive())
			storedProc = executionDialog.getStoredProc();

		if (storedProc == null)
		{
			ListSelectionModel selectionModel = procListTable.getSelectionModel();

			if (!selectionModel.isSelectionEmpty())
			{
				int selectionIndex = selectionModel.getMinSelectionIndex();

				storedProc = procListTable.getStoredProc(selectionIndex);
			}
		}

		return storedProc;
	}

	@Override
	public void showStoredProcOutput(List<DataTable> resultTables)
	{
		clearDataPages();

		int index = 1;

		for (DataTable dataTable : resultTables)
		{
			StoredProcResultTableModel model = new StoredProcResultTableModel(dataTable);
			StoredProcResultTable table = new StoredProcResultTable(model,
					new ArbitraryTableCellRendererFactory(resourceManager), 1.3f);

			table.addMouseListener(resultTableMouseListener);

			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

			procResultsTabs.addTab(null, resultTabIcon, scrollPane);
			int tabIndex = procResultsTabs.getTabCount() - 1;

			if (dataTable.getType() == DataTableType.OUTPUT_PARAMS)
				dataPageMap.put(tabIndex, new DataPage(-100, null, dataTable));
			else
				dataPageMap.put(tabIndex, new DataPage(index++, null, dataTable));
		}

		resetProcResultTabs();
	}

	@Override
	public void addToLog(Object value)
	{
		if (value != null)
		{
			Class<?> valueClass = value.getClass();
			String valueClassName = valueClass.getSimpleName();

			if (Constants.CLASS_NAME_STRING.equals(valueClassName))
				LogEventWriter.writeMessage((String) value, logTableModel);
			else if (Exception.class.isAssignableFrom(valueClass))
				log((Exception) value);
		}
	}

	@Override
	public void clearCurrentData()
	{
		procTextArea.setText("");
		clearDataPages();
		clearLog();
	}

	@Override
	public void showStoredProc(StoredProc storedProc)
	{
		if (procListTableModel != null && procListMap != null)
		{
			Integer storedProcIndex = procListMap.get(storedProc);

			if (storedProcIndex != null && procListTableModel.getRowCount() > storedProcIndex)
				procListTableModel.fireTableRowsUpdated(storedProcIndex, storedProcIndex);
		}
	}

	@Override
	public void showStoredProcWarning(StoredProc storedProc)
	{
		//TODO: make a correct messaging logic here.

		String pattern = "The stored procedure %s is %s.";
		String name = storedProc.getName();
		String status = "in unknown status";

		if (storedProc.getStatus() == StoredProcStatus.DEAD)
			status = "dead";
		else if (storedProc.getStatus() == StoredProcStatus.OCCUPIED)
			status = "occupied";

		JOptionPane.showMessageDialog(null, String.format(pattern, name, status), "Verevkin", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public File getResultFile()
	{
		JFileChooser fileChooser = ViewUtils.getFileChooser(new File(Constants.MESS_CURRENT_PATH));

		fileChooser.resetChoosableFileFilters();
		for (FileNameExtensionFilter filter : ViewComponents.getFileNameExtensionFilters())
			fileChooser.addChoosableFileFilter(filter);

		if (fileChooser.showSaveDialog(ViewUtils.getDialogOwner()) == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFile();
		else
			return null;
	}

	@Override
	public List<DataPage> getActiveResultPageList()
	{
		int pageIndex = procResultsTabs.getSelectedIndex();

		if (pageIndex > -1 && !dataPageMap.isEmpty())
		{
			DataPage activeDataPage = dataPageMap.get(pageIndex);

			List<DataPage> pageList = new LinkedList<>();
			pageList.add(activeDataPage);

			return pageList;
		}
		else
			return null;
	}

	@Override
	public List<DataPage> getAllResultPagesList()
	{
		if (!dataPageMap.isEmpty())
			return new LinkedList<>(dataPageMap.values());
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
		procSearchField.requestFocus();
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
		initMenus();
		initToolBar();

		initProcListPanel();
		initProcTextPanel();
		initProcResultPanel();
		initLogPanel();

		initSplitPanels();

		initStatusPanel();

		titleAdjuster.registerComponent(this,
				new Title(resourceManager,
						Constants.KEY_MAIN_FRAME,
						ViewUtils.getAssemblyInformationString(this,
								" - ",
								new ExtensionInfoType[]{ExtensionInfoType.IMPLEMENTATION_TITLE,
														ExtensionInfoType.IMPLEMENTATION_VERSION})));

		titleAdjuster.resetComponents();

		pack();
	}

	private void initActions()
	{
		actionsManager = new ActionsManager(controller, resourceManager);
	}

	private void initMenus()
	{
		menuManager = new MenuManager(actionsManager.getSaveActiveResultPageAction(),
				actionsManager.getSaveAllResultPagesAction());

		resultTableMouseListener = new StoredProcResultTableMouseListener(menuManager.getPopupMenu());
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
		toolBar.add(actionsManager.getShowSpInfoAction());
		toolBar.addSeparator();
		toolBar.add(actionsManager.getShowSettingsAction());
		toolBar.add(actionsManager.getShowHelpAction());

		add(toolBar, BorderLayout.NORTH);
	}

	private void initProcListPanel()
	{
		procListMap = new HashMap<>();
		procListTableModel = new StoredProcListTableModel(resourceManager, StoredProc.getTitleKeys(), null);
		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(procListTableModel);

		procListTable = new StoredProcListTable(procListTableModel, resourceManager);
		procListTable.getSelectionModel().addListSelectionListener(new StoredProcListSelectionListener(
				procListTable, actionsManager, controller));
		procListTable.addMouseListener(new StoredProcListMouseListener(actionsManager));
		procListTable.setRowSorter(rowSorter);

		procListPane = new JScrollPane(procListTable);

		procListPanel = ViewComponents.getTitledPanelWithBorderLayout(resourceManager, titleAdjuster,
				procListPane, BorderLayout.CENTER, BorderFactory.createEmptyBorder(),
				AppConstants.KEY_PANEL_PROC_LIST, TitledBorder.TOP, TitledBorder.CENTER);

		procSearchField = new IconHintTextField(resourceManager.getImageIcon(AppConstants.ICON_NAME_SEARCH));  //RoundedTextField
		procSearchField.getDocument().addDocumentListener(new TableFilterListener(procSearchField, rowSorter));

		JPanel procSearchPanel = new JPanel(new BorderLayout());
		procSearchPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
		procSearchPanel.add(procSearchField, BorderLayout.CENTER);
		procListPanel.add(procSearchPanel, BorderLayout.NORTH);
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
		dataPageMap = new HashMap<>();
		procResultsTabs = new JTabbedPane();

		resultTabIcon = resourceManager.getImageIcon(AppConstants.ICON_NAME_DATA_TABLE);

		/*procResultsScroll = new JScrollPane(procResultsTabs);
		procResultsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		procResultsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);*/

		procResultsPanel = ViewComponents.getTitledPanelWithBorderLayout(resourceManager, titleAdjuster,
				procResultsTabs, BorderLayout.CENTER, BorderFactory.createEmptyBorder(),
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
		oneProcPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, procTextPanel, procResultsPanel);
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
		resetPanelMinimumSize(procResultsPanel);
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
		else if (panel == procResultsPanel)
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

	private void resetProcResultTabs()
	{
		for (int i = 0; i < procResultsTabs.getTabCount(); i++)
		{
			DataPage dataPage = dataPageMap.get(i);
			String pageName;

			if (dataPage.getDataTable().getType() == DataTableType.OUTPUT_PARAMS)
				pageName = new Title(resourceManager,
						AppConstants.KEY_TAB_RESULT_OUTPUT_PARAMS).getText();
			else
				pageName = new Title(resourceManager,
						AppConstants.KEY_TAB_RESULT_REGULAR, dataPage.getIndex()).getText();

			dataPage.setName(pageName);
			procResultsTabs.setTitleAt(i, pageName);
		}
	}

	private void clearDataPages()
	{
		dataPageMap.clear();

		if (procResultsTabs.getTabCount() > 0)
			procResultsTabs.removeAll();
	}

	private void clearLog()
	{
		logTableModel.clear();
		logTableModel.fireTableDataChanged();
	}

	private void initFrame()
	{
		setIconImage(resourceManager.getImageIcon(AppConstants.ICON_NAME_MAIN).getImage());

		setResizingPolicy();
		setClosingPolicy();

		controller.setViewPreferredBounds();
		controller.setViewActualBounds();
	}

	private void updateProcListMap()
	{
		if (procListMap != null && procListTableModel != null)
		{
			procListMap.clear();

			for (int i = 0; i < procListTableModel.getRowCount(); i++)
				procListMap.put(procListTableModel.getRow(i), i);
		}
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
				controller.cancelProcesses();
				controller.updateViewBounds();
				controller.saveAllSettings();
			}
		});
	}
}
