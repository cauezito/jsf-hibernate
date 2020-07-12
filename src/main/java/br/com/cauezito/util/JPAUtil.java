package br.com.cauezito.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

public class JPAUtil {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("projetoJSF");
	
	@PersistenceContext
	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
	
	public Object getPrimaryKey(Object entity){
		return emf.getPersistenceUnitUtil().getIdentifier(entity);
	}

}
