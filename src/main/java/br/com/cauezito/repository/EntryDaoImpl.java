package br.com.cauezito.repository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cauezito.entity.Entry;

@Named
public class EntryDaoImpl implements EntryDao {

	@Inject
	private EntityManager entityManager;

	@Override
	public List<Entry> list(Long userId) {
		List<Entry> entries = null;
		EntityTransaction et = entityManager.getTransaction();
		et.begin();
		entries = entityManager.createQuery(" from Entry where user.id = " + userId).getResultList();

		et.commit();
		return entries;
	}

}
