package com.example.caselink.test;

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
		for(Case ccase: cases) {
			System.out.print(ccase.getId() + "|");
			System.out.print(ccase.getAutomation() + "|");
			System.out.print(ccase.getProject() + "|");
			System.out.print(ccase.getType());
			System.out.println("");
		}
	}

	@Test
	public void testAutoCase() throws Exception {
		List<Case> cases = client.listAutoCases();
		for(Case ccase: cases) {
			System.out.print(ccase.getId() + "|");
			System.out.print(ccase.getAutomation() + "|");
			System.out.print(ccase.getProject() + "|");
			System.out.print(ccase.getType());
			System.out.println("");
		}
	}
}
