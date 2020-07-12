package br.com.cauezito.repository;

import java.io.Serializable;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import br.com.cauezito.entity.Curriculum;
import br.com.cauezito.entity.Person;
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
	public Curriculum getCurriculum(String id) {
		Curriculum curriculum = null;
		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		try {
			curriculum = (Curriculum) entityManager.createQuery("select c from Curriculum c where c.id = " + id).getSingleResult();
			t.commit();
		} catch (NoResultException e) {
			ShowMessages.showMessage("Não foi possível obter o currículo");
		}		
		
		return curriculum;
	}

}
