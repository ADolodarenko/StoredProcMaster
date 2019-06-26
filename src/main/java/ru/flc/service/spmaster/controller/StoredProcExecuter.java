package ru.flc.service.spmaster.controller;

import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class StoredProcExecuter extends SwingWorker<Void, Void>
{
	private StoredProc storedProc;
	private DataModel model;
	private View view;

	private List<DataTable> resultTables;
	private List<String> outputMessages;

	public StoredProcExecuter(StoredProc storedProc, DataModel model, View view)
	{
		this.storedProc = storedProc;
		this.model = model;
		this.view = view;
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		if ( !isCancelled() )
		{
			resultTables = new ArrayList<>();
			outputMessages = new LinkedList<>();

			model.executeStoredProc(storedProc, resultTables, outputMessages);
		}

		return null;
	}

	@Override
	protected void done()
	{
		if ( !isCancelled() )
			view.showStoredProcOutput(resultTables, outputMessages);
	}
}
