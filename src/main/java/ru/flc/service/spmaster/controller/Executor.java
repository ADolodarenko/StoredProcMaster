package ru.flc.service.spmaster.controller;

public interface Executor
{
	boolean isCancelled();
	void publishMessages(String... messages);
}
