package br.com.cauezito.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cauezito.entity.Entry;
import br.com.cauezito.util.JPAUtil;

public class EntryDaoImpl implements EntryDao {

	@Override
	public List<Entry> list(Long userId) {
		List<Entry> entries = null;
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		entries = em.createQuery(" from Entry where user.id = " + userId).getResultList();
		
		et.commit();
		em.close();
		return entries;
	}

}
