package br.com.cauezito.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
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
	@NotEmpty(message = "O sobrenome deve ser informado")
	@NotNull(message = "O sobrenome deve ser informado")
	private String surname;
	
	private String gender;
	
	private String[] courses;
	
	@NotEmpty(message = "O login deve ser informado")
	@NotNull(message = "O login deve ser informado")
	private String login;
	
	@NotEmpty(message = "A senha deve ser informada")
	@NotNull(message = "A senha deve ser informada")
	private String password;
	
	private String cep;
	
	private String logradouro;
	
	private String localidade;
	
	private String uf;
	
	@Transient /*Não é criada uma coluna no banco de dados*/
	private State state;
	
	@ManyToOne
	private City city;
	
	@CPF(message = "Digite um CPF válido")
	private String cpf;
	
	private String bairro;
	
	@Temporal(TemporalType.DATE)
	private Date birth = new Date();
	
	@Column(columnDefinition = "text")
	private String photoIconB64;
	
	private String extension;
	
	@OneToMany(mappedBy = "person", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Telephone> phones = new ArrayList<Telephone>();
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] photoIconB64Original;
	
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

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	public State getState() {
		return state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}


	public String getPhotoIconB64() {
		return photoIconB64;
	}

	public void setPhotoIconB64(String photoIconB64) {
		this.photoIconB64 = photoIconB64;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public byte[] getPhotoIconB64Original() {
		return photoIconB64Original;
	}

	public void setPhotoIconB64Original(byte[] photoIconB64Original) {
		this.photoIconB64Original = photoIconB64Original;
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

	@Override
	public String toString() {
		return "Person [id=" + id + ", name=" + name + ", surname=" + surname + ", gender=" + gender + ", courses="
				+ Arrays.toString(courses) + ", login=" + login + ", password=" + password
				+ ", cep=" + cep + ", logradouro=" + logradouro + ", localidade=" + localidade + ", uf=" + uf
				+ ", bairro=" + bairro + ", birth=" + birth + "]";
	}
	
}
