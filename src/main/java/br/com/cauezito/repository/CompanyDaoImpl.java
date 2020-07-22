package br.com.cauezito.repository;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import br.com.cauezito.entity.Company;
import br.com.cauezito.util.ShowMessages;

@Named
public class CompanyDaoImpl implements CompanyDao, Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager entityManager;

	@Override
	public Company findCompany(String email, String password, String cnpj) {
		Company company = null;
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		try {
			company = (Company) entityManager.createQuery(
					"select c from Company c where c.email = '" + email +
					"' and c.password = '" + password + "' and c.cnpj = '" + cnpj +"'")
					.getSingleResult();
			t.commit();
		} catch (NoResultException e) {
			ShowMessages.showMessageError("Opa! Os dados não foram encontrados");
		}

		return company;
	}

}
