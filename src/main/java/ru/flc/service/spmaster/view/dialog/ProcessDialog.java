package ru.flc.service.spmaster.view.dialog;

import org.dav.service.util.ResourceManager;
import org.dav.service.view.TitleAdjuster;
import org.dav.service.view.UsableGBC;
import org.dav.service.view.dialog.SettingsDialogInvoker;

import javax.swing.*;
import java.awt.*;

public class ProcessDialog extends JDialog
{
	private static final Dimension BUTTON_MAX_SIZE = new Dimension(100, 30);
	private static final Dimension IMAGE_SIZE = new Dimension(102, 70);

	private Frame owner;
	private SettingsDialogInvoker invoker;
	private ResourceManager resourceManager;
	private TitleAdjuster titleAdjuster;
	private JLabel imageLabel;
	private JLabel messageLabel;

	public ProcessDialog(Frame owner, SettingsDialogInvoker invoker, ResourceManager resourceManager)
	{
		super(owner, "", true);

		this.owner = owner;
		this.invoker = invoker;

		this.resourceManager = resourceManager;
		this.titleAdjuster = new TitleAdjuster();

		initComponents();

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setResizable(false);
	}

	private void initComponents()
	{
		initTitlePanel();
	}

	private JPanel initTitlePanel()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		UsableGBC constraints;

		constraints = new UsableGBC(0, 0);
		constraints.setInsets(new Insets(0, 10, 0, 5));

		imageLabel = new JLabel();

		imageLabel.setMinimumSize(IMAGE_SIZE);
		imageLabel.setPreferredSize(IMAGE_SIZE);
		imageLabel.setMaximumSize(IMAGE_SIZE);
		imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

		panel.add(imageLabel, constraints);

		constraints = new UsableGBC(1, 0);
		constraints.setInsets(new Insets(0, 5, 0, 10));

		messageLabel = new JLabel();
		messageLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 14));
		panel.add(messageLabel, constraints);

		return panel;
	}
}
