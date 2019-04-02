package ru.flc.service.spmaster.view.table;

import org.dav.service.view.table.SettingsTable;
import org.dav.service.view.table.editor.TableCellEditorFactory;
import org.dav.service.view.table.renderer.TableCellRendererFactory;

import javax.swing.table.TableModel;
import java.awt.*;

public class StoredProcParamsTable extends SettingsTable
{
	public StoredProcParamsTable(TableModel model, TableCellEditorFactory editorFactory, TableCellRendererFactory rendererFactory)
	{
		super(model, editorFactory, rendererFactory);
	}

	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		return new Dimension(super.getPreferredSize().width, getRowHeight() * getRowCount());
	}
}
