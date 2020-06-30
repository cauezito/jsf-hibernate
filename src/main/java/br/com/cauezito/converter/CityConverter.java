package br.com.cauezito.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.cauezito.entity.City;
import br.com.cauezito.util.JPAUtil;

@FacesConverter(forClass = City.class)
public class CityConverter implements Converter, Serializable {

	/*
	 * Retorna o objeto inteiro (consultando no banco de dados a partir do id
	 * recebido). É executado quando o objeto vem da tela para o servidor.
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String cityCod) {
		EntityManager em = JPAUtil.getEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();

		City city = (City) em.find(City.class, Long.parseLong(cityCod));
		return city;
	}

	/*
	 * Retorna apenas o código em String. É executado quando o objeto vem do
	 * servidor para a tela
	 */
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object city) {

		return ((City) city).getId().toString();
	}

}
