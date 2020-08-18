package br.com.cauezito.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;

@Entity
public class JobOpportunity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name="company", nullable = true)
	private Company company;
	
	private String responsibility;
	private String address;
	@ElementCollection
	@Cascade(CascadeType.ALL)
	private List<String> skills = new ArrayList<String>();
	@Lob
	private String resume;
	private String details;
	private String level;
	private boolean remote;
	@Transient
	private String remoteAux;
	private Integer numberCandidates;
	
	@Temporal(TemporalType.DATE)
	private Date publicationDate = new Date();
	
	
	@OneToMany(mappedBy = "job")
	@Cascade(CascadeType.ALL)
	private List<PersonJob> personJob = new ArrayList<PersonJob>();
	
	@OneToMany(mappedBy = "job")
	@Cascade(CascadeType.ALL)
	private List<FinalistCandidate> finalistCandidates = new ArrayList<FinalistCandidate>();
	
	@OneToMany(mappedBy = "job")
	@Cascade(CascadeType.ALL)
	private List<RejectedCandidate> rejectedCandidates = new ArrayList<RejectedCandidate>();
	
	public JobOpportunity() {
		if(this.remote) {
			this.remoteAux = "Sim";
		} else {
			this.remoteAux = "Não";
		}
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public String getResponsibility() {
		return responsibility;
	}
	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getResume() {
		return resume;
	}
	public void setResume(String resume) {
		this.resume = resume;
	}
	public Date getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public boolean getRemote() {
		return remote;
	}
	public void setRemote(boolean remote) {
		this.remote = remote;
	}
	public Integer getNumberCandidates() {
		return numberCandidates;
	}
	public void setNumberCandidates(Integer numberCandidates) {
		this.numberCandidates = numberCandidates;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getRemoteAux() {
		return remoteAux;
	}
	public void setRemoteAux(String remoteAux) {
		this.remoteAux = remoteAux;
	}
	public List<FinalistCandidate> getFinalistCandidates() {
		return finalistCandidates;
	}
	public void setFinalistCandidates(List<FinalistCandidate> finalistCandidates) {
		this.finalistCandidates = finalistCandidates;
	}		

	public List<RejectedCandidate> getRejectedCandidates() {
		return rejectedCandidates;
	}

	public void setRejectedCandidates(List<RejectedCandidate> rejectedCandidates) {
		this.rejectedCandidates = rejectedCandidates;
	}

	public List<String> getSkills() {
		return skills;
	}


	public void setSkills(List<String> skills) {
		this.skills = skills;
	}

	
}
