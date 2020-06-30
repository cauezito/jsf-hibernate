package br.com.cauezito.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.City;
import br.com.cauezito.entity.Person;
import br.com.cauezito.entity.State;
import br.com.cauezito.repository.PersonDao;
import br.com.cauezito.repository.PersonDaoImpl;
import br.com.cauezito.util.JPAUtil;


@ViewScoped
@ManagedBean(name = "personBean")
public class PersonBean implements Crud {

	private Person person = new Person();
	private GenericDao<Person> dao = new GenericDao<Person>();
	private List<Person> people = new ArrayList<Person>();
	private PersonDao pdao = new PersonDaoImpl();
	private List<SelectItem> states;
	private List<SelectItem> cities;
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
		ExternalContext ec = context.getExternalContext();
		ec.getSessionMap().remove("personOn");
		HttpServletRequest req = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
		req.getSession().invalidate();
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
	
	public void searchZipCode(AjaxBehaviorEvent event) {		
		try {
			String url = "http://viacep.com.br/ws/" + person.getCep() + "/json/";
			String json = IOUtils.toString(new URL(url), "UTF-8");
			Person gsonAux = new Gson().fromJson(json.toString(), Person.class);
			person.setCep(gsonAux.getCep());
			person.setLogradouro(gsonAux.getLogradouro());
			person.setUf(gsonAux.getUf());
			person.setBairro(gsonAux.getBairro());
			person.setLocalidade(gsonAux.getLocalidade());
			System.out.println(gsonAux);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void findCities(AjaxBehaviorEvent e) {
		String idState = (String) e.getComponent().getAttributes().get("submittedValue");
		if(idState != null) {
			State state = JPAUtil.getEntityManager()
					.find(State.class, Long.parseLong(idState));
			
			if(state != null) {
				person.setState(state);
				List<City> cities = JPAUtil.getEntityManager()
						.createQuery("from City where state.id = " + idState)
						.getResultList();
				List<SelectItem> selectItemsCities = new ArrayList<SelectItem>();
				for (City city : cities) {
					selectItemsCities.add(new SelectItem(city.getId(), city.getName()));
				}
				setCities(selectItemsCities);
			}
		}
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
	public List<SelectItem> getStates() {
		states = pdao.allStates();
		return states;
	}
	public List<SelectItem> getCities() {
		return cities;
	}
	public void setCities(List<SelectItem> cities) {
		this.cities = cities;
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
