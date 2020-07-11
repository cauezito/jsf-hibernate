package br.com.cauezito.repository;

import java.util.List;

import br.com.cauezito.entity.Curriculum;
import br.com.cauezito.entity.Person;
import javax.faces.model.SelectItem;

public interface PersonDao {
	Person findUser(String login, String password);
	Curriculum getCurriculum(String id);
}
