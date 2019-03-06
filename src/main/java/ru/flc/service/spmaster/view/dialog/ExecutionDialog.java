package ru.flc.service.spmaster.view.dialog;

import org.dav.service.settings.parameter.Parameter;
import org.dav.service.util.ResourceManager;
import org.dav.service.view.table.SettingsTable;
import org.dav.service.view.table.SettingsTableModel;
import org.dav.service.view.table.editor.TableCellEditorFactory;
import org.dav.service.view.table.renderer.TableCellRendererFactory;

import javax.swing.*;
import java.awt.*;

public class ExecutionDialog extends JDialog
{
	private Frame owner;

	private ResourceManager resourceManager;

	private SettingsTableModel tableModel;
	private SettingsTable table;

	public ExecutionDialog(Frame owner, ResourceManager resourceManager)
	{
		super(owner, "", true);

		this.owner = owner;
		this.resourceManager = resourceManager;

		initComponents();

		setResizable(false);
	}

	private void initComponents()
	{
		add(initSettingsPanel());
	}

	private JPanel initSettingsPanel()
	{
		tableModel = new SettingsTableModel(resourceManager, Parameter.getTitleKeys(), null);

		table = new SettingsTable(tableModel,
				new TableCellEditorFactory(resourceManager),
				new TableCellRendererFactory(resourceManager));

		JScrollPane tablePane = new JScrollPane(table);

		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.add(tablePane, BorderLayout.CENTER);

		return panel;
	}
}
