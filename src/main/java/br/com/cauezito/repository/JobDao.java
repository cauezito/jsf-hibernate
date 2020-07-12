package br.com.cauezito.repository;

import java.util.List;

import br.com.cauezito.entity.JobOpportunity;

public interface JobDao {
	List<JobOpportunity> getUsubscribedJobs(Long userId);
}
