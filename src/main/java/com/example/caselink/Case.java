package com.example.caselink;

import java.util.List;

public class Case {

	private String id;
	private String type;
	private String automation;
	private String title;
	private String project;
	private String commit;
	private List<String> archs;
	private List<String> documents;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAutomation() {
		return automation;
	}
	public void setAutomation(String automation) {
		this.automation = automation;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getCommit() {
		return commit;
	}
	public void setCommit(String commit) {
		this.commit = commit;
	}
	public List<String> getArchs() {
		return archs;
	}
	public void setArchs(List<String> archs) {
		this.archs = archs;
	}
	public List<String> getDocuments() {
		return documents;
	}
	public void setDocuments(List<String> documents) {
		this.documents = documents;
	}

}
