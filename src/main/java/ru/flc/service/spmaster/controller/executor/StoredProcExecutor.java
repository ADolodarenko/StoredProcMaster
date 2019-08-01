package ru.flc.service.spmaster.controller.executor;

import ru.flc.service.spmaster.model.data.DataModel;
import ru.flc.service.spmaster.model.data.entity.DataTable;
import ru.flc.service.spmaster.model.data.entity.StoredProc;
import ru.flc.service.spmaster.view.View;

import java.util.ArrayList;
import java.util.List;

public class StoredProcExecutor extends AbstractExecutor<Void, Object>
{
	private StoredProc storedProc;
	private DataModel model;
	private View view;

	private List<DataTable> resultTables;

	public StoredProcExecutor(StoredProc storedProc, DataModel model, View view, String executionMessage)
	{
		super(executionMessage);

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
				publishObjects(e);
			}
		}

		return null;
	}

	@Override
	protected void process(List<Object> chunks)
	{
		for (Object chunk : chunks)
			view.addToLog(chunk);
	}

	@Override
	protected void done()
	{
		if ( !isInterrupted() )
			view.showStoredProcOutput(resultTables);
	}
}
