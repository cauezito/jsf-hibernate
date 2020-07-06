package br.com.cauezito.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cauezito.entity.State;
import br.com.cauezito.util.JPAUtil;

@FacesConverter(forClass = State.class, value = "stateConv")
@Named
public class StateConverter implements Converter, Serializable {

	private static final long serialVersionUID = 1L;
	
	private EntityManager entityManager = null;
	
	public StateConverter() {
		JPAUtil jpa = new JPAUtil();
		entityManager = jpa.getEntityManager();
	}
	
	/*
	* Retorna o objeto inteiro (consultando no banco de dados a partir do id recebido).
	*É executado quando o objeto vem da tela para o servidor.
	*/
	@Override 
	public Object getAsObject(FacesContext context, UIComponent component, String stateCod) {
		EntityTransaction et = entityManager.getTransaction();
		et.begin();
		State state = (State) entityManager.find(State.class, Long.parseLong(stateCod));
		return state;
	}

	/*
	* Retorna apenas o código em String.
	*É executado quando o objeto vai do servidor para a tela
	*/
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object state) {
		
		if(state == null) {
			return null;
		}
		
		if(state instanceof State) {
			return ((State) state).getId().toString();
		} else {
			return state.toString();
		}
		
	}
	
}
