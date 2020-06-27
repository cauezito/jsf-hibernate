package br.com.cauezito.repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cauezito.entity.Person;
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

}
