package br.com.cauezito.model;

import java.awt.event.ActionEvent;

public interface Crud {
	public String save();
	public String removeById();
	public String remove();
	public void listAll();
	public String clear();
}
