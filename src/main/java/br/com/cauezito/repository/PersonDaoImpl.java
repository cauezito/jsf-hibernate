package br.com.cauezito.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import br.com.cauezito.entity.Person;
import br.com.cauezito.entity.State;
import br.com.cauezito.util.JPAUtil;
import br.com.cauezito.util.ShowMessages;

@Named
public class PersonDaoImpl implements PersonDao, Serializable{

	private static final long serialVersionUID = 1L;
	@Inject
	private EntityManager entityManager;
	
	@Override
	public Person findUser(String login, String password) {
		Person person = null;
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		try {
			person = (Person) entityManager.createQuery("select p from Person p where p.login = '" + login + 
					"' and p.password = '" + password + "'").getSingleResult();
			t.commit();
		} catch (NoResultException e) {
			ShowMessages.showMessage("Dados inválidos");
		}		
		
		return person;
	}

	@Override
	public List<SelectItem> allStates() {
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		
		List<State> state = entityManager.createQuery("from State").getResultList();
		for (State st : state) {
			selectItems.add(new SelectItem(st, st.getName()));
		}
		
		return selectItems;
	}

}
