package br.com.cauezito.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	private String[] skills;
	private String resume;
	private String details;
	private String level;
	private boolean remote;
	private Integer numberCandidates;
	
	@Temporal(TemporalType.DATE)
	private Date publicationDate = new Date();
	
	/*@ManyToMany(mappedBy = "candidatures")
	private List<Person> candidates;*/
	
	@OneToMany(mappedBy = "job")
	private List<PersonJob> personJob = new ArrayList<PersonJob>();
	
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
	public String[] getSkills() {
		return skills;
	}
	public void setSkills(String[] skills) {
		this.skills = skills;
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
	/*public List<Person> getCandidates() {
		return candidates;
	}
	public void setCandidates(List<Person> candidates) {
		this.candidates = candidates;
	}*/
		
}
