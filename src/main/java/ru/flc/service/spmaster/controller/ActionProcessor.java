package ru.flc.service.spmaster.controller;

public interface ActionProcessor
{
	void connectDatabase();
	void disconnectDatabase();
	void execStoredProcedure();
	void showAboutInfo();
}
