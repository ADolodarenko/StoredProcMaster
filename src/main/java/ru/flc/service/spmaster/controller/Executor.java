package ru.flc.service.spmaster.controller;

public interface Executor
{
	void interrupt();
	boolean isInterrupted();
	void publishMessages(String... messages);
}
