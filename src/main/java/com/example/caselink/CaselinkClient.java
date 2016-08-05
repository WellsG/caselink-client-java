package com.example.caselink;

import java.util.List;

public interface CaselinkClient {

	public List<Case> listManualCases() throws Exception;

	public List<Case> listAutoCases() throws Exception;
}
