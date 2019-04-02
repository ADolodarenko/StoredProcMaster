package ru.flc.service.spmaster.view.dialog;

import org.dav.service.settings.parameter.Parameter;
import org.dav.service.util.Constants;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.Title;
import org.dav.service.view.TitleAdjuster;
import org.dav.service.view.dialog.SettingsDialogInvoker;
import org.dav.service.view.table.SettingsTable;
import org.dav.service.view.table.SettingsTableModel;
import org.dav.service.view.table.editor.TableCellEditorFactory;
import org.dav.service.view.table.renderer.TableCellRendererFactory;
import ru.flc.service.spmaster.model.data.entity.StoredProcParameter;
import ru.flc.service.spmaster.util.AppConstants;
import ru.flc.service.spmaster.view.table.StoredProcParamsTable;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ExecutionDialog extends JDialog
{
	private static final Dimension BUTTON_MAX_SIZE = new Dimension(100, 30);

	private Frame owner;
	private SettingsDialogInvoker invoker;

	private ResourceManager resourceManager;
	private TitleAdjuster titleAdjuster;

	private List<Parameter> parameterList;

	private SettingsTableModel tableModel;
	private StoredProcParamsTable table;
	private JPanel settingsPanel;

	private JLabel imageLabel; //102, 70
	private JLabel procedureLabel;

	private JButton executeButton;
	private JButton cancelButton;

	public ExecutionDialog(Frame owner, SettingsDialogInvoker invoker, ResourceManager resourceManager)
	{
		super(owner, "", true);

		this.owner = owner;
		this.invoker = invoker;

		this.resourceManager = resourceManager;
		this.titleAdjuster = new TitleAdjuster();

		initComponents();

		setResizable(false);
	}

	public void setParameterList(List<StoredProcParameter> storedProcParameters)
	{
		this.parameterList = getParameters(storedProcParameters);
	}

	@Override
	public void setVisible(boolean b)
	{
		if (b)
		{
			tableModel.clear();

			if (parameterList != null && (!parameterList.isEmpty()))
			{
				tableModel.addAllRows(parameterList);
				tableModel.fireTableStructureChanged();

				table.setFillsViewportHeight(false);

				settingsPanel.setVisible(true);
			}
			else
				settingsPanel.setVisible(false);

			titleAdjuster.resetComponents();

			pack();
			setLocationRelativeTo(owner);
		}

		super.setVisible(b);
	}

	private void initComponents()
	{
		add(initTitlePanel(), BorderLayout.NORTH);

		settingsPanel = initSettingsPanel();
		add(settingsPanel, BorderLayout.CENTER);

		add(initCommandPanel(), BorderLayout.SOUTH);

		titleAdjuster.registerComponent(this, new Title(resourceManager, AppConstants.KEY_EXECUTION_DIALOG));
	}

	private JPanel initTitlePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.setBorder(BorderFactory.createEtchedBorder());

		imageLabel = new JLabel();
		imageLabel.setIcon(resourceManager.getImageIcon(AppConstants.ICON_NAME_STORED_PROCEDURE));
		panel.add(imageLabel);

		procedureLabel = new JLabel();
		procedureLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 14));
		panel.add(procedureLabel);

		return panel;
	}

	private JPanel initSettingsPanel()
	{
		tableModel = new SettingsTableModel(resourceManager, Parameter.getTitleKeys(), null);

		table = new StoredProcParamsTable(tableModel,
				new TableCellEditorFactory(resourceManager),
				new TableCellRendererFactory(resourceManager));

		JScrollPane tablePane = new JScrollPane(table);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.add(tablePane, BorderLayout.CENTER);

		return panel;
	}

	private JPanel initCommandPanel()
	{
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEtchedBorder());

		initButtons();

		panel.add(executeButton);
		panel.add(cancelButton);

		return panel;
	}

	private void initButtons()
	{
		executeButton = new JButton();
		titleAdjuster.registerComponent(executeButton, new Title(resourceManager, AppConstants.KEY_BUTTON_EXECUTE));
		executeButton.setPreferredSize(BUTTON_MAX_SIZE);
		executeButton.setMaximumSize(BUTTON_MAX_SIZE);
		executeButton.setIcon(resourceManager.getImageIcon(AppConstants.ICON_NAME_EXECUTE));
		executeButton.addActionListener(event -> execute());

		cancelButton = new JButton();
		titleAdjuster.registerComponent(cancelButton, new Title(resourceManager, Constants.KEY_BUTTON_CANCEL));
		cancelButton.setPreferredSize(BUTTON_MAX_SIZE);
		cancelButton.setMaximumSize(BUTTON_MAX_SIZE);
		cancelButton.setIcon(resourceManager.getImageIcon(Constants.ICON_NAME_CANCEL));
		cancelButton.addActionListener(event -> exit());
	}

	private List<Parameter> getParameters(List<StoredProcParameter> storedProcParameters)
	{
		if (storedProcParameters == null)
			return null;

		List<Parameter> parameters = new LinkedList<>();

		for (StoredProcParameter parameter : storedProcParameters)
		{
			String name = parameter.getName();
			Class<?> valueClass = parameter.getValueClass();
			Object value = parameter.getValue();

			parameters.add(new Parameter(new Title(resourceManager, name), value, valueClass));
		}

		return parameters;
	}

	private void execute()
	{
		stopTableEditing();

		setVisible(false);

		if (invoker != null)
			invoker.setFocus();
	}

	private void exit()
	{
		stopTableEditing();

		setVisible(false);

		if (invoker != null)
			invoker.setFocus();
	}

	private void stopTableEditing()
	{
		if (table != null && table.isEditing())
			table.getCellEditor().stopCellEditing();
	}
}
