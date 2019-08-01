package ru.flc.service.spmaster.controller.executor;

import org.dav.service.view.Title;

public interface Executor<V>
{
	void interrupt();
	boolean isInterrupted();
	void publishObjects(V... objects);
	String getExecutionMessageKey();
}
