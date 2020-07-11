package br.com.cauezito.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

@Entity
public class Person implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(unique = true, nullable = false)
	private Long id;

	@NotEmpty
	@Size(min = 3, max = 30, message = "O nome deve ter entre 3 e 30 letras")
	private String name;

	@Size(min = 5, max = 30, message = "O sobrenome deve ter entre 3 e 30 letras")
	private String surname;

	private String gender;

	private String[] courses;

	@Size(max = 300, message = "O resumo não pode ter mais de 300 caracteres")
	private String bio;

	@NotEmpty(message = "O login deve ser informado")
	@NotNull(message = "O login deve ser informado")
	private String login;

	@NotEmpty(message = "A senha deve ser informada")
	@NotNull(message = "A senha deve ser informada")
	private String password;

	@CPF(message = "Digite um CPF válido")
	private String cpf;

	private String relationshipStatus;

	@Temporal(TemporalType.DATE)
	private Date birth;

	@Column
	private Boolean deficient;

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Telephone> phones;

	@OneToOne(cascade = CascadeType.ALL)
	private Image image;

	@OneToOne(cascade = CascadeType.ALL)
	private Curriculum curriculum;

	@ManyToMany
	@JoinTable(name = "person_job", joinColumns = {
			@javax.persistence.JoinColumn(name = "person_id") }, inverseJoinColumns = {
					@javax.persistence.JoinColumn(name = "job_id") })
	private List<JobOpportunity> candidatures;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String[] getCourses() {
		return courses;
	}

	public void setCourses(String[] courses) {
		this.courses = courses;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public List<Telephone> getPhones() {
		return phones;
	}

	public void setPhones(List<Telephone> phones) {
		this.phones = phones;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Curriculum getCurriculum() {
		return curriculum;
	}

	public void setCurriculum(Curriculum curriculum) {
		this.curriculum = curriculum;
	}

	public String getRelationshipStatus() {
		return relationshipStatus;
	}

	public void setRelationshipStatus(String relationshipStatus) {
		this.relationshipStatus = relationshipStatus;
	}

	public Boolean getDeficient() {
		return deficient;
	}

	public void setDeficient(Boolean deficient) {
		this.deficient = deficient;
	}

	public List<JobOpportunity> getCandidatures() {
		return candidatures;
	}

	public void setCandidatures(List<JobOpportunity> candidatures) {
		this.candidatures = candidatures;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
