package br.com.cauezito;

import javax.persistence.Persistence;

public class Hibernate {

	public static void main(String args[]) {
		Persistence.createEntityManagerFactory("projetoJSF");
	}
}
