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

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.JobOpportunity;
import br.com.cauezito.entity.Person;
import br.com.cauezito.entity.PersonJob;
import br.com.cauezito.entity.Telephone;
import br.com.cauezito.repository.JobDao;
import br.com.cauezito.util.ShowMessages;

@SessionScoped
@Named(value = "jobBean")
public class JobBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private GenericDao<JobOpportunity> jobGenericDao;
	
	@Inject
	private JobDao jobDao;

	@Inject
	private GenericDao<PersonJob> personJobDao;

	@Inject
	private Person person;

	private List<String> phones = new ArrayList<String>();

	@Inject
	private JobOpportunity job;
	
	@Inject
	private PersonJob personJob;

	private List<JobOpportunity> jobs = new ArrayList<JobOpportunity>();

	public String allJobs() {
		this.getSession();
		jobs = jobDao.getUnsubscribedJobs(person.getId());
		
		return "/user/search.xhtml?faces-redirect=true";
	}
	
	public String showCandidatures() {
		this.getSession();
		jobs = jobDao.getSubscribedJobs(person.getId());
		return "/user/candidatures.xhtml?faces-redirect=true";
	}

	public String showJob() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("jobId");
		job = jobGenericDao.search(JobOpportunity.class, id);
		return "/user/showJob.xhtml?faces-redirect=true";
	}

	public String applyForJob() {
		personJob.setJob(job);
		personJob.setPerson(person);		

		if (this.save()) {
			ShowMessages.showMessage("Você se candidatou!");
		} else {
			ShowMessages.showMessage("Não foi possível se candidatar. Tente novamente mais tarde.");
		}

		return "/user/search.xhtml?faces-redirect=true";
	}

	private boolean save() {
		if (personJobDao.merge(personJob) != null) {
			return true;
		}

		return false;

	}

	private void getSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		person = (Person) ec.getSessionMap().get("personOn");

		if (person.getPhones() != null && !person.getPhones().isEmpty()) {
			phones.clear();
			for (Telephone phone : person.getPhones()) {
				this.phones.add(phone.getNumber());
			}
		}
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
