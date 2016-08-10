package com.example.caselink;

import java.util.List;

public interface CaselinkClient {

	public List<Case> listManualCases() throws Exception;

	public List<Case> listAutoCases() throws Exception;

	public void createManualCase(Case manualCase) throws Exception;

	public Case getCaseById(String id) throws Exception;

	public void updateManualCase(Case manualCase) throws Exception;
}
