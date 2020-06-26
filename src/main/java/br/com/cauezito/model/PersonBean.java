package br.com.cauezito.model;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.Person;


@ViewScoped
@ManagedBean(name = "personBean")
public class PersonBean {

	private Person person = new Person();
	private GenericDao<Person> dao = new GenericDao<Person>();

	public String save() {
		person = dao.merge(person);
		return "";
	}
	
	public String newPerson() {
		person = new Person();
		return "";
	}
	
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public GenericDao<Person> getGenericDao() {
		return dao;
	}

	public void setGenericDao(GenericDao<Person> dao) {
		this.dao = dao;
	}

}
