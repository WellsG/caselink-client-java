package com.example.caselink.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.caselink.Case;
import com.example.caselink.CaselinkClient;
import com.example.caselink.CaselinkUtil;

public class CaselinkTest {

	private CaselinkClient client;

	@Before
	public void setup() {
		client = CaselinkUtil.setUp();
	}

	@Test
	public void testManualCase() throws Exception {
		List<Case> cases = client.listManualCases();
	}

	@Test
	public void testAutoCase() throws Exception {
		List<Case> cases = client.listAutoCases();
	}

	@Test
	public void testCreateCase() throws Exception {
		Case manualCase = new Case();
		manualCase.setId("VIRTTP-9376");
		manualCase.setProject("RHEL7");
		manualCase.setCommit("commit");
		manualCase.setType("testcase");
		manualCase.setTitle("Dashboard Test 5");
		manualCase.setAutomation("automated");
		List<String> documents = new ArrayList<String>();
		documents.add("Libvirt Test Plan - Scalability and Longevity");
		manualCase.setDocuments(documents);
		List<String> archs = new ArrayList<String>();
		manualCase.setArchs(archs);
		client.createManualCase(manualCase);
	}

	@Test
	public void testGetCasesById() throws Exception {
		Case caseResult = client.getCaseById("VIRTTP-9376");
		assertEquals(caseResult.getProject(), "RHEL7");
		assertEquals(caseResult.getType(), "testcase");
		assertEquals(caseResult.getTitle(), "Dashboard Test 5");
	}

	@Test
	public void testUpdateCase() throws Exception {
		Case manualCase = new Case();
		manualCase.setId("VIRTTP-9376");
		manualCase.setProject("RHEL7");
		manualCase.setCommit("commit1");
		manualCase.setType("testcase");
		manualCase.setTitle("Dashboard Test 5");
		manualCase.setAutomation("automated");
		List<String> documents = new ArrayList<String>();
		documents.add("Libvirt Test Plan - Scalability and Longevity");
		manualCase.setDocuments(documents);
		List<String> archs = new ArrayList<String>();
		manualCase.setArchs(archs);
		client.updateManualCase(manualCase);
	}
}
