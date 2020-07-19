package br.com.cauezito.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.Company;
import br.com.cauezito.entity.JobOpportunity;
import br.com.cauezito.entity.Person;
import br.com.cauezito.repository.CompanyDao;
import br.com.cauezito.util.ShowMessages;

@Named(value = "companyBean")
@SessionScoped
public class CompanyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Company company;
	
	@Inject
	private GenericDao<Company> dao;
	
	@Inject
	private GenericDao<JobOpportunity> jobDao;
	
	@Inject
	private JobOpportunity job;
	
	private List<JobOpportunity> jobs = new ArrayList<JobOpportunity>();
	
	private List<String> skills = new ArrayList<String>();
	
	@Inject
	private CompanyDao cdao;
	
	public CompanyBean() {
		this.skills();
	}
	
	public String save() {
		if(job != null) {
			jobs.add(job);
			job.setCompany(company);
			company.setJobs(jobs);			
		}
		
		if (dao.merge(company) != null) {
			this.setSession("companyOn", company);
			ShowMessages.showMessageInfo("Vaga cadastrada!");
			job = new JobOpportunity();
		} else {
			ShowMessages.showMessageError("Não foi possível cadastrar a vaga;");
		}	
		return "";
	}
	
	public String login() {
		Company c = cdao.findCompany(company.getEmail(), company.getPassword(), company.getCnpj());

		if (c != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ec = context.getExternalContext();
			ec.getSessionMap().put("companyOn", c);
			this.getSession();

			return "company/company.xhtml?faces-redirect=true";
		} 
			return "login.xhtml";
	}
	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		ec.getSessionMap().remove("companyOn");
		HttpServletRequest req = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
		req.getSession().invalidate();
		ShowMessages.showMessageInfo("Você saiu");
		return "login";
	}

	
	public String recoverInfoCompany() {
		this.getSession();
		return "/company/updateInfoCompany.xhtml?faces-redirect=true";
	}
	
	public String newJob() {
		job = new JobOpportunity();
		return "/company/newJob.xhtml?faces-redirect=true";
	}
	
	private void getSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		company = (Company) ec.getSessionMap().get("companyOn");
		
		if(company.getJobs() != null && !company.getJobs().isEmpty()) {
			jobs.clear();
			for (JobOpportunity job : company.getJobs()) {
				this.jobs.add(job);
			}
		}	
	}

	private void setSession(String key, Company company) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		ec.getSessionMap().remove(key);
		ec.getSessionMap().put(key, company);
	}
	
	private void skills() {
		skills.add("PHP");
		skills.add("Java");
		skills.add("MySQL");
		skills.add("Oracle");
		skills.add("PostgreSQL");
		skills.add("UML");
		skills.add("Go");
		skills.add("Python");
		skills.add("iReport");
		skills.add("CSS");
		skills.add("Javascript");
		skills.add("NodeJs");
	}


	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public JobOpportunity getJob() {
		return job;
	}

	public void setJob(JobOpportunity job) {
		this.job = job;
	}

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}

	public List<JobOpportunity> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobOpportunity> jobs) {
		this.jobs = jobs;
	}

}
