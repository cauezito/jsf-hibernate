package br.com.cauezito.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.cauezito.entity.JobOpportunity;
import br.com.cauezito.entity.Person;
import br.com.cauezito.util.ShowMessages;

@Named
public class JobDaoImpl implements Serializable, JobDao {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	private List<JobOpportunity> jobs;
	private List<Person> candidates;

	@SuppressWarnings("unchecked")
	@Override
	public List<JobOpportunity> getUnsubscribedJobs(Long userId) {
		jobs = new ArrayList<JobOpportunity>();

		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		try {

			TypedQuery<JobOpportunity> query = (TypedQuery<JobOpportunity>) entityManager
					.createQuery("FROM JobOpportunity job where not exists "
							+ "(from PersonJob pjob where pjob.job.id = job.id and pjob.person.id = :userId)");

			query.setParameter("userId", userId);

			jobs = query.getResultList();

			t.commit();
		} catch (NoResultException e) {
			ShowMessages.showMessage("Não há vagas disponíveis");
		}

		return jobs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<JobOpportunity> getSubscribedJobs(Long userId) {
		jobs = new ArrayList<JobOpportunity>();

		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		try {

			TypedQuery<JobOpportunity> query = (TypedQuery<JobOpportunity>) entityManager
					.createQuery("FROM JobOpportunity job where exists "
							+ "(from PersonJob pjob where pjob.job.id = job.id and pjob.person.id = :userId)");

			query.setParameter("userId", userId);

			jobs = query.getResultList();

			t.commit();
		} catch (NoResultException e) {
			ShowMessages.showMessage("Você ainda não se candidatou a uma vaga!");
		}

		return jobs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Person> getCandidates(Long jobId) {
		candidates = new ArrayList<Person>();
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		try {

			TypedQuery<Person> query = (TypedQuery<Person>) entityManager
					.createQuery("FROM Person p where exists "
							+ "(from PersonJob pjob where pjob.job.id = :jobId "
							+ "and pjob.person.id = p.id)");

			query.setParameter("jobId", jobId);

			candidates = query.getResultList();

			t.commit();
		} catch (NoResultException e) {
			ShowMessages.showMessage("Não há candidatos para esta vaga");
		}

		return candidates;
	}

}
