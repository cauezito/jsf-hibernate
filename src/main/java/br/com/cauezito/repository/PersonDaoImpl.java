package br.com.cauezito.repository;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cauezito.entity.Person;
import br.com.cauezito.entity.State;
import br.com.cauezito.util.JPAUtil;

public class PersonDaoImpl implements PersonDao{

	@Override
	public Person findUser(String login, String password) {
		Person person = null;
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction t = em.getTransaction();
		t.begin();
		person = (Person) em.createQuery("select p from Person p where p.login = '" + login + 
				"' and p.password = '" + password + "'").getSingleResult();
		t.commit();
		em.close();
		
		return person;
	}

	@Override
	public List<SelectItem> allStates() {
		List<SelectItem> selectItems = new ArrayList<SelectItem>();
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction t = em.getTransaction();
		t.begin();
		
		List<State> state = em.createQuery("from State").getResultList();
		for (State st : state) {
			selectItems.add(new SelectItem(st.getId(), st.getName()));
		}
		
		em.close();
		return selectItems;
	}

}
