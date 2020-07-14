package br.com.cauezito.repository;

import java.util.List;

import br.com.cauezito.entity.JobOpportunity;
import br.com.cauezito.entity.Person;

public interface JobDao {
	List<JobOpportunity> getUnsubscribedJobs(Long userId);
	List<JobOpportunity> getSubscribedJobs(Long userId);
	List<Person> getCandidates(Long jobId);
}