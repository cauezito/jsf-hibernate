package br.com.cauezito.model;

import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

import com.google.gson.Gson;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.City;
import br.com.cauezito.entity.Person;
import br.com.cauezito.entity.State;
import br.com.cauezito.entity.Telephone;
import br.com.cauezito.repository.PersonDao;

@Named(value = "personBean")
@SessionScoped
public class PersonBean implements Crud, Serializable {

	private static final long serialVersionUID = 1L;

	private Person person = new Person();

	@Inject
	private GenericDao<Person> dao;

	@Inject
	private PersonDao pdao;

	@Inject
	private EntityManager entityManager;

	private List<Person> people = new ArrayList<Person>();
	private List<SelectItem> states;
	private List<SelectItem> cities;
	private List<String> skills = new ArrayList<String>();
	private UploadedFile photo;
	private List<String> phones = new ArrayList<String>();
	private Telephone telephone;

	// seleciona o arquivo e cria temporariamente no lado do servidor para obter
	// posteriormente no sistema e depois processar

	public PersonBean() {
		this.skills();
	}

	public String recoverInfoUser() {
		this.getSession();
		return "updateInfoUser.xhtml";
	}

	@Override
	public String save() {
		if (photo.getSize() != 0) {
			savePhoto(photo);
		}

		if (!phones.isEmpty()) {
			for (String phone : phones) {
				List<Telephone> p = new ArrayList<Telephone>();
				telephone = new Telephone();
				telephone.setNumber(phone);
				telephone.setPerson(person);
				p.add(telephone);
				person.setPhones(p);
			}
		}

		if (dao.merge(person) != null) {
			this.setSession("personOn", person);
			this.showMessage("Usuário inserido com sucesso!");
		} else {
			this.listAll();
			this.showMessage("Não foi possível salvar o usuário");
		}
		return "";
	}

	// passo o inputStream direto
	private void savePhoto(UploadedFile photo) {
		byte[] imageByte;
		try {
			imageByte = this.getByte(photo.getInputstream());
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
		} catch (IOException e) {
			this.showMessage("Erro ao salvar imagem");
			e.printStackTrace();
		}
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
		System.out.println("oaksoaksask");
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
			List<City> cities = entityManager.createQuery("from City where state.id = " + state.getId())
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
			List<City> cities = entityManager.createQuery("from City where state.id = " + state.getId())
					.getResultList();
			List<SelectItem> selectItemsCities = new ArrayList<SelectItem>();
			for (City city : cities) {
				selectItemsCities.add(new SelectItem(city, city.getName()));
			}
			setCities(selectItemsCities);
		}
	}

	public String login() {
		Person p = pdao.findUser(person.getLogin(), person.getPassword());

		if (p != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ec = context.getExternalContext();
			ec.getSessionMap().put("personOn", p);
			this.getSession();

			return "home.xhtml?faces-redirect=true";
		}

		return "login.xhtml";
	}

	private void getSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		person = (Person) ec.getSessionMap().get("personOn");

		if (person.getPhones().isEmpty()) {
			for (Telephone phone : person.getPhones()) {
				phones.add(phone.getNumber());
			}
		}
	}

	private void setSession(String key, Person person) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		ec.getSessionMap().remove(key);
		ec.getSessionMap().put(key, person);
	}

	private void skills() {
		skills.add("PHP");
		skills.add("JAVA");
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

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}

	public UploadedFile getPhoto() {
		return photo;
	}

	public void setPhoto(UploadedFile photo) {
		this.photo = photo;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

}
