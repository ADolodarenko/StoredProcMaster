package ru.flc.service.spmaster.controller.executor;

import javax.swing.*;

public abstract class AbstractExecutor<T, V> extends SwingWorker<T, V> implements Executor<V>
{
	private boolean interrupted;
	private String executionMessageKey;

	public AbstractExecutor(String executionMessageKey)
	{
		this.interrupted = false;
		this.executionMessageKey = executionMessageKey;
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

	@Override
	public void publishObjects(V... objects)
	{
		publish(objects);
	}

	@Override
	public String getExecutionMessageKey()
	{
		return executionMessageKey;
	}
}
