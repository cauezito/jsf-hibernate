package br.com.cauezito.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.Person;
import br.com.cauezito.repository.PersonDao;
import br.com.cauezito.repository.PersonDaoImpl;


@ViewScoped
@ManagedBean(name = "personBean")
public class PersonBean implements Crud {

	private Person person = new Person();
	private GenericDao<Person> dao = new GenericDao<Person>();
	private List<Person> people = new ArrayList<Person>();
	private PersonDao pdao = new PersonDaoImpl();

	
	@Override
	public String save() {
		person = dao.merge(person);
		this.listAll();
		return "";
	}
	
	@Override
	public String removeById() {
		dao.deleteById(person);
		person = new Person();
		this.listAll();
		return "";
	}

	@Override
	public String remove() {
		return null;
	}

	@Override
	@PostConstruct
	public void listAll() {
		people = dao.getListEntity(Person.class);
	}

	@Override
	public String clear() {
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

	public List<Person> getPeople() {
		return people;
	}
	
	public String login() {
		Person p = pdao.findUser(person.getLogin(), person.getPassword());
		
		if(p != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ec = context.getExternalContext();
			ec.getSessionMap().put("personOn", p);
			return "index.xhtml";
		}

		return "login.xhtml";
	}



}
