package ru.flc.service.spmaster.controller;

import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.view.View;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class StoredProcExecutor extends SwingWorker<Void, String> implements Executor
{
	private boolean interrupted;

	private StoredProc storedProc;
	private DataModel model;
	private View view;

	private List<DataTable> resultTables;

	public StoredProcExecutor(StoredProc storedProc, DataModel model, View view)
	{
		this.interrupted = false;

		this.storedProc = storedProc;
		this.model = model;
		this.view = view;
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		if ( !isInterrupted() )
		{
			resultTables = new ArrayList<>();

			try
			{
				model.executeStoredProc(storedProc, resultTables, this);
			}
			catch (Exception e)
			{
				view.addToLog(e);
			}
		}

		return null;
	}

	@Override
	protected void process(List<String> chunks)
	{
		for (String message : chunks)
			view.addToLog(message);
	}

	@Override
	protected void done()
	{
		if ( !isInterrupted() )
			view.showStoredProcOutput(resultTables);
	}

	@Override
	public void publishMessages(String... messages)
	{
		publish(messages);
	}

	@Override
	public void interrupt()
	{
		interrupted = true;
	}

	@Override
	public boolean isInterrupted()
	{
		return interrupted;
	}
}
