package br.com.cauezito.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cauezito.util.JPAUtil;

public class GenericDao<E> {
	
	public void save(E entity){
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		entityManager.persist(entity);
		
		entityTransaction.commit();
		entityManager.close();
	}
	
	
	public E merge(E entity){
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		E registry = entityManager.merge(entity);
		
		entityTransaction.commit();
		entityManager.close();
		
		return registry;
	}
	
	
 	public void remove(E entity){
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		entityManager.remove(entity);
		
		entityTransaction.commit();
		entityManager.close();
	} 
	
	public void removeById(E entity){
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		Object id = JPAUtil.getPrimaryKey(entity);
		entityManager.createQuery("delete from " + entity.getClass().getCanonicalName() +
				" where id = " + id).executeUpdate();
		
		entityTransaction.commit();
		entityManager.close();
	}

	public List<E> getListEntity(Class<E> entity){
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		List<E> registry = entityManager.createQuery("from " + 
		entity.getName()).getResultList();
		
		entityTransaction.commit();
		entityManager.close();
		
		return registry;
	}
	
	public E search(Class<E> entity, String cod) {
		EntityManager entityManager = JPAUtil.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		
		E object = (E) entityManager.find(entity, Long.parseLong(cod));
		entityTransaction.commit();
		
		return object;
	}
}
