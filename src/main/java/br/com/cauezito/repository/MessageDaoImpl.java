package br.com.cauezito.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import br.com.cauezito.entity.JobOpportunity;
import br.com.cauezito.entity.Message;
import br.com.cauezito.util.ShowMessages;

@Named
public class MessageDaoImpl implements MessageDao, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager entityManager;
	
	private List<Message> message;

	@SuppressWarnings("unchecked")
	@Override
	public List<Message> allMessages(Long userId) {
		message = new ArrayList<Message>();

		EntityTransaction t = entityManager.getTransaction();
		t.begin();
		try {

			TypedQuery<Message> query = (TypedQuery<Message>) entityManager
					.createQuery("FROM Message m order by m.date desc where m.receiver.id = :userId");

			query.setParameter("userId", userId);

			message = query.getResultList();

			t.commit();
		} catch (NoResultException e) {
			ShowMessages.showMessage("Você ainda não recebeu mensagens!");
		}

		return message;
	}

}
