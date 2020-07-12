package br.com.cauezito.model;

public interface Crud {
	public String save();
	public String removeById();
	public String remove();
	public void listAll();
	public String clear();
}
