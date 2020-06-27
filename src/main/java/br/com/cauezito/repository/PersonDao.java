package br.com.cauezito.repository;

import br.com.cauezito.entity.Person;

public interface PersonDao {
	Person findUser(String login, String password);
}
