package br.com.cauezito.model;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.Company;
import br.com.cauezito.entity.Person;
import br.com.cauezito.entity.Telephone;
import br.com.cauezito.repository.CompanyDao;
import br.com.cauezito.util.ShowMessages;

@Named(value = "companyBean")
@SessionScoped
public class CompanyBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Company company;
	
	@Inject
	private GenericDao<Company> dao;
	
	@Inject
	private CompanyDao cdao;
	
	public String save() {
		if (dao.merge(company) != null) {
			this.setSession("companyOn", company);
			ShowMessages.showMessage("Informações atualizadas!");
		} else {
			ShowMessages.showMessage("Não foi possível atualizar as informações!");
		}
		return "";
	}
	
	public String login() {
		Company c = cdao.findCompany(company.getEmail(), company.getPassword(), company.getCnpj());

		if (c != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext ec = context.getExternalContext();
			ec.getSessionMap().put("companyOn", c);
			this.getSession();

			return "company/company.xhtml?faces-redirect=true";
		} 
			return "login.xhtml";
	}
	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		ec.getSessionMap().remove("companyOn");
		HttpServletRequest req = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
		req.getSession().invalidate();
		ShowMessages.showMessage("Você saiu");
		return "login";
	}

	
	public String recoverInfoCompany() {
		this.getSession();
		return "company/updateInfoCompany.xhtml?faces-redirect=true";
	}
	
	private void getSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		company = (Company) ec.getSessionMap().get("companyOn");
		
		/*if(company.getPhones() != null && !person.getPhones().isEmpty()) {
			phones.clear();
			for (Telephone phone : company.getPhones()) {
				this.phones.add(phone.getNumber());
			}
		}	*/
	}

	private void setSession(String key, Company company) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ec = context.getExternalContext();
		ec.getSessionMap().remove(key);
		ec.getSessionMap().put(key, company);
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
	

}
