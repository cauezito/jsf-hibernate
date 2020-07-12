package br.com.cauezito.repository;

import java.util.List;

import br.com.cauezito.entity.JobOpportunity;

public interface JobDao {
	List<JobOpportunity> getUnsubscribedJobs(Long userId);
	List<JobOpportunity> getSubscribedJobs(Long userId);
}
