package com.example.caselink.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.example.caselink.Case;
import com.example.caselink.CaselinkClient;
import com.example.caselink.CaselinkUtil;
import com.example.caselink.Linkage;

public class CaselinkTest {

	private CaselinkClient client;

	@Before
	public void setup() {
		client = CaselinkUtil.setUp();
	}

	@Test
	public void testManualCase() throws Exception {
		List<Case> cases = client.listManualCases();
		assertNotNull(cases);
	}

	@Test
	public void testAutoCase() throws Exception {
		List<Case> cases = client.listAutoCases();
		assertNotNull(cases);
	}

	@Test
	public void testCreateCase() throws Exception {
		Case manualCase = new Case();
		manualCase.setId("VIRTTP-9377");
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
		Case result = client.createManualCase(manualCase);
		assertEquals(result.getId(), "VIRTTP-9377");
		assertEquals(result.getProject(), "RHEL7");
		assertEquals(result.getType(), "testcase");
		assertEquals(result.getTitle(), "Dashboard Test 5");
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

	@Test
	public void testCreateLink() throws Exception {
		Linkage link = new Linkage();
		link.setAutocase_pattern("conf_file.libvirtd_conf.unix_sock2");
		link.setTitle("Test linkage");
		link.setWorkitem("RHEL7-19094");
		Linkage linkage = client.createLinkage(link);
		assertEquals(linkage.getAutocase_pattern(),link.getAutocase_pattern());
		
	}
}
