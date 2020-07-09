package br.com.cauezito.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.JobOpportunity;

@SessionScoped
@Named(value = "jobBean")
public class JobBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private GenericDao<JobOpportunity> dao;
	
	@Inject
	private JobOpportunity job;
	
	private List<JobOpportunity> jobs = new ArrayList<JobOpportunity>();
	
	public String allJobs() {
		jobs = dao.getListEntity(JobOpportunity.class);
		return "/user/search.xhtml?faces-redirect=true";
	}
	
	public String showJob() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("jobId");
		job = dao.search(JobOpportunity.class, id);
		return "/user/showJob.xhtml?faces-redirect=true";
	}

	public List<JobOpportunity> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobOpportunity> jobs) {
		this.jobs = jobs;
	}

	public JobOpportunity getJob() {
		return job;
	}

	public void setJob(JobOpportunity job) {
		this.job = job;
	}	
}
