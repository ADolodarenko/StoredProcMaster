package ru.flc.service.spmaster.view.dialog;

import org.dav.service.util.ResourceManager;
import ru.flc.service.spmaster.util.AppConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AboutDialog extends JDialog
{
	private JLabel imageLabel;
	private JLabel titleLabel;
	private JLabel copyrightTitle;

	private Frame owner;
	private ResourceManager resourceManager;

	public AboutDialog(Frame owner, ResourceManager resourceManager)
	{
		super(owner, "", true);

		setUndecorated(true);

		this.owner = owner;
		this.resourceManager = resourceManager;

		initComponents();

		pack();

		addMouseListener(new AboutDialogMouseListener());
	}

	@Override
	public void setVisible(boolean b)
	{
		if (b)
			setLocationRelativeTo(owner);

		super.setVisible(b);
	}

	private void initComponents()
	{
		setLayout(new BorderLayout());

		titleLabel = new JLabel(" Stored Procedure Master 1.0.0");
		titleLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD + Font.ITALIC, 18));
		titleLabel.setOpaque(true);
		titleLabel.setForeground(Color.YELLOW);
		titleLabel.setBackground(Color.BLACK);
		add(titleLabel, BorderLayout.NORTH);

		imageLabel = new JLabel(resourceManager.getImageIcon(AppConstants.ICON_NAME_ABOUT));
		add(imageLabel, BorderLayout.CENTER);

		copyrightTitle = new JLabel(" (c) Family Leasure Club, 2019");
		copyrightTitle.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
		copyrightTitle.setOpaque(true);
		copyrightTitle.setForeground(Color.YELLOW);
		copyrightTitle.setBackground(Color.BLACK);
		add(copyrightTitle, BorderLayout.SOUTH);
	}

	private class AboutDialogMouseListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent e)
		{
			AboutDialog.this.setVisible(false);
		}
	}
}
