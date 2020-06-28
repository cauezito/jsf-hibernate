package br.com.cauezito.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

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
		this.showMessage("Usuário inserido com sucesso!");
		this.listAll();
		return "";
	}
	
	private void showMessage(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}
	
	@Override
	public String removeById() {
		dao.removeById(person);
		person = new Person();
		this.listAll();
		this.showMessage("Usuário removido com sucesso!");
		return "";
	}

	@Override
	public String remove() {
		return null;
	}
	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getExternalContext().getRequest();		
		httpServletRequest.getSession().invalidate();
		this.showMessage("Você saiu");
		return "login";
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
