package br.com.cauezito.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cauezito.util.JPAUtil;

@Named
public class GenericDao<E> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;
	
	@Inject
	private JPAUtil jpa;
	
	public void save(E entity){
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		entityManager.persist(entity);
		
		entityTransaction.commit();
	}
	
	
	public E merge(E entity){
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		E registry = entityManager.merge(entity);
		
		entityTransaction.commit();
		
		return registry;
	}
	
	
 	public void remove(E entity){
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		entityManager.remove(entity);
		
		entityTransaction.commit();
	} 
	
	public void removeById(E entity){
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		Object id = jpa.getPrimaryKey(entity);
		entityManager.createQuery("delete from " + entity.getClass().getCanonicalName() +
				" where id = " + id).executeUpdate();
		
		entityTransaction.commit();
	}

	public List<E> getListEntity(Class<E> entity){
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		List<E> registry = entityManager.createQuery("from " + 
		entity.getName()).getResultList();
		
		entityTransaction.commit();
		
		return registry;
	}
	
	public E search(Class<E> entity, String cod) {
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		E object = (E) entityManager.find(entity, Long.parseLong(cod));
		entityTransaction.commit();
		
		return object;
	}
}
