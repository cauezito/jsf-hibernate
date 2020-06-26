package br.com.cauezito.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.Person;


@ViewScoped
@ManagedBean(name = "personBean")
public class PersonBean {

	private Person person = new Person();
	private GenericDao<Person> dao = new GenericDao<Person>();
	private List<Person> people = new ArrayList<Person>();

	public String save() {
		person = dao.merge(person);
		this.listAll();
		return "";
	}
	
	public String newPerson() {
		person = new Person();
		return "";
	}
	
	public String remove() {
		dao.deleteById(person);
		person = new Person();
		this.listAll();
		return "";
	}
	
	@PostConstruct
	public void listAll() {
		people = dao.getListEntity(Person.class);
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

	public List<Person> getPeople() {
		return people;
	}

}
