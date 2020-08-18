package br.com.cauezito.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.weld.context.RequestContext;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.Company;
import br.com.cauezito.entity.FinalistCandidate;
import br.com.cauezito.entity.JobOpportunity;
import br.com.cauezito.entity.Message;
import br.com.cauezito.entity.Person;
import br.com.cauezito.entity.PersonJob;
import br.com.cauezito.entity.RejectedCandidate;
import br.com.cauezito.entity.Telephone;
import br.com.cauezito.repository.JobDao;
import br.com.cauezito.repository.MessageDao;
import br.com.cauezito.util.ShowMessages;

@SessionScoped
@Named(value = "jobBean")
public class JobBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private GenericDao<JobOpportunity> jobGenericDao;

	@Inject
	private GenericDao<Company> companyGenericDao;

	@Inject
	private GenericDao<Person> personGenericDao;

	@Inject
	private GenericDao<FinalistCandidate> finalistGenericDao;

	@Inject
	private GenericDao<PersonJob> personJobGenericDao;

	@Inject
	private GenericDao<RejectedCandidate> rejectedCandidateGenericDao;

	@Inject
	private GenericDao<Message> messageGenericDao;

	@Inject
	private Person selectedPerson;

	@Inject
	private Person person;

	@Inject
	private FinalistCandidate finalist;

	@Inject
	private RejectedCandidate rejected;

	@Inject
	private Company company;

	@Inject
	private Message message;

	@Inject
	private JobOpportunity job;

	@Inject
	private JobDao jobDao;

	@Inject
	private PersonJob personJob;

	private List<String> phones = new ArrayList<String>();
	private List<JobOpportunity> jobs = new ArrayList<JobOpportunity>();
	private List<Person> candidates = new ArrayList<Person>();

	public String unsubscribedJobs() {
		this.getSession();
		this.getUnsubscribedJobs();
		if(jobs.size() == 0) {
			ShowMessages.showMessageError("Não há vagas disponíveis para o seu perfil");
		}
		return "/user/search.xhtml?faces-redirect=true";
	}

	private void getUnsubscribedJobs() {
		jobs = jobDao.getUnsubscribedJobs(person.getId());
	}

	public String subscribedJobs() {
		this.getSession();
		this.getSubscribedJobs();
		if(jobs.size() == 0) {
			ShowMessages.showMessageError("Você ainda não se candidatou a nenhuma vaga!");
		}
		return "/user/candidatures.xhtml?faces-redirect=true";
	}

	private void getSubscribedJobs() {
		jobs = jobDao.getSubscribedJobs(person.getId());
	}

	public String showJob() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("jobId");
		job = jobGenericDao.search(JobOpportunity.class, id);

		return "/user/showJob.xhtml?faces-redirect=true";
	}

	private void findAllJobs() {
		jobs = jobGenericDao.getListEntity(JobOpportunity.class);
	}

	public String allJobs() {
		this.findAllJobs();
		if (jobs.size() == 0) {
			ShowMessages.showMessageError("Esta empresa ainda não publicou vagas!");
		}

		return "/company/jobs.xhtml?faces-redirect=true";

	}

	public void showCandidate() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("userId");
		person = personGenericDao.search(Person.class, id);
	}

	public String applyForJob() {
		personJob.setJob(job);
		personJob.setPerson(person);

		if (this.save()) {
			ShowMessages.showMessageInfo("Você se candidatou!");
			this.getUnsubscribedJobs();
		} else {
			ShowMessages.showMessageError("Não foi possível se candidatar. Tente novamente mais tarde.");
		}

		return "/user/search.xhtml?faces-redirect=true";
	}

	public String manageJobVacancy() {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("jobId");
		job = jobGenericDao.search(JobOpportunity.class, id);
		candidates = jobDao.getCandidates(Long.parseLong(id));
		return "/company/controlPanel.xhtml?faces-redirect=true";
	}

	public String sendMessage() {

		message.setSubject(job.getResponsibility());
		message.setReceiver(selectedPerson);
		company = companyGenericDao.search(Company.class, job.getCompany().getId().toString());
		message.setSender(company);

		if (messageGenericDao.merge(message) != null) {
			ShowMessages.showMessageInfo("A mensagem foi enviada!");
			jobDao.removeCandidate(selectedPerson.getId(), job.getId());
			candidates = jobDao.getCandidates(job.getId());

			finalist.setJob(job);
			finalist.setCandidate(selectedPerson);

			finalistGenericDao.merge(finalist);

			message = new Message();
		}

		return "/company/controlPanel.xhtml?faces-redirect=true";
	}

	public String rejectCandidate() {
		rejected.setCandidate(selectedPerson);
		rejected.setJob(job);
		if (rejectedCandidateGenericDao.merge(rejected) != null) {
			jobDao.removeCandidate(selectedPerson.getId(), job.getId());
			ShowMessages.showMessageInfo("O usuário foi desqualificado.");

		}

		return "/company/controlPanel.xhtml?faces-redirect=true";
	}

	private boolean save() {
		if (personJobGenericDao.merge(personJob) != null) {
			return true;
		}

		return false;

	}
	
	public String updateJob() {
		
		return "/company/newJob.xhtml";
	}

	public String deleteJob() {
		jobGenericDao.removeById(job);
		ShowMessages.showMessageInfo("Vaga excluída");
		return "/company/jobs.xhtml?faces-redirect=true";
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

	public List<Person> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<Person> candidates) {
		this.candidates = candidates;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Person getSelectedPerson() {
		return selectedPerson;
	}

	public void setSelectedPerson(Person selectedPerson) {
		this.selectedPerson = selectedPerson;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

}
