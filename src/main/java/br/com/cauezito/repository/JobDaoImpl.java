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
import br.com.cauezito.util.ShowMessages;

@Named
public class JobDaoImpl implements Serializable, JobDao {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	private List<JobOpportunity> jobs;

	@Override
	public List<JobOpportunity> getUsubscribedJobs(Long userId) {
		jobs = new ArrayList<JobOpportunity>();
		
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		try {
		
			TypedQuery<JobOpportunity> query = (TypedQuery<JobOpportunity>) entityManager.createQuery(
					  "FROM JobOpportunity job where not exists " +
			"(from PersonJob pjob where pjob.job.id = job.id and pjob.person.id = :userId)");
			
			query.setParameter("userId", userId);

			jobs = query.getResultList();
			
					
			t.commit();
		} catch (NoResultException e) {
			ShowMessages.showMessage("Dados inválidos");
		}

		
		
		return jobs;
	}

}
