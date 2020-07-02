package br.com.cauezito.model;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.hibernate.service.spi.InjectService;

import com.google.gson.Gson;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.City;
import br.com.cauezito.entity.Person;
import br.com.cauezito.entity.State;
import br.com.cauezito.repository.PersonDao;
import br.com.cauezito.repository.PersonDaoImpl;
import br.com.cauezito.util.ClearComponents;
import br.com.cauezito.util.JPAUtil;

@ViewScoped
@ManagedBean(name = "personBean")
public class PersonBean implements Crud{

	private Person person = new Person();
	private GenericDao<Person> dao = new GenericDao<Person>();
	private List<Person> people = new ArrayList<Person>();
	private PersonDao pdao = new PersonDaoImpl();
	private List<SelectItem> states;
	private List<SelectItem> cities;
	
	// seleciona o arquivo e cria temporariamente no lado do servidor para obter
	// posteriormente no sistema e depois processar
	private Part photo;
	@Override
	public String save() {
		try {
			byte[] imageByte = this.getByte(photo.getInputStream());
			person.setPhotoIconB64Original(imageByte);

			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imageByte));

			int type = bi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bi.getType();
			int width = 200;
			int height = 200;

			// miniature
			BufferedImage resizedImage = new BufferedImage(width, height, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(bi, 0, 0, width, height, null);
			g.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String extension = photo.getContentType().split("\\/")[1];
			ImageIO.write(resizedImage, extension, baos);

			String miniature = "data:" + photo.getContentType() + ";base64,"
					+ DatatypeConverter.printBase64Binary(baos.toByteArray());
			person.setPhotoIconB64(miniature);
			person.setExtension(extension);
			dao.merge(person);
			this.showMessage("Usuário inserido com sucesso!");
			this.listAll();
		} catch (IOException err) {
			this.showMessage("Não foi possível salvar o usuário");
			err.printStackTrace();
		}

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

	private byte[] getByte(InputStream file) throws IOException {

		byte[] bytes = IOUtils.toByteArray(file);

		return bytes;
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

	public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("fileDownloadId");

		Person person = dao.search(Person.class, id);

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		response.addHeader("Content-Disposition",
				"attachment; filename=foto" + person.getName() + "." + person.getExtension());
		response.setContentType("application/octet-stream");
		response.setContentLengthLong(person.getPhotoIconB64Original().length);
		response.getOutputStream().write(person.getPhotoIconB64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void findCities(AjaxBehaviorEvent e) {
		
		State state = (State) ((HtmlSelectOneMenu) e.getSource()).getValue();
		if (state.getId() != null) {
			person.setState(state);
			List<City> cities = JPAUtil.getEntityManager().createQuery("from City where state.id = " + state.getId())
					.getResultList();
			List<SelectItem> selectItemsCities = new ArrayList<SelectItem>();
			for (City city : cities) {
				selectItemsCities.add(new SelectItem(city, city.getName()));
			}
			setCities(selectItemsCities);
		}
	}

	public void edit() {
		if (person.getCity() != null) {
			State state = person.getCity().getState();
			person.setState(state);
			List<City> cities = JPAUtil.getEntityManager().createQuery("from City where state.id = " + state.getId())
					.getResultList();
			List<SelectItem> selectItemsCities = new ArrayList<SelectItem>();
			for (City city : cities) {
				selectItemsCities.add(new SelectItem(city, city.getName()));
			}
			setCities(selectItemsCities);
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

	public Part getPhoto() {
		return photo;
	}

	public void setPhoto(Part photo) {
		this.photo = photo;
	}

	public String login() {
		Person p = pdao.findUser(person.getLogin(), person.getPassword());

		if (p != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ec = context.getExternalContext();
			ec.getSessionMap().put("personOn", p);
			return "index.xhtml";
		}

		return "login.xhtml";
	}

}
