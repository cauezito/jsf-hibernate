package br.com.cauezito.model;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.Entry;
import br.com.cauezito.entity.Person;

@ViewScoped
@ManagedBean(name = "entryBean")
public class EntryBean implements Crud{
	private Entry entry = new Entry();
	private GenericDao<Entry> dao = new GenericDao<Entry>();
	private List<Entry> entries = new ArrayList<Entry>();
	
	
	@Override
	public String save() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext ex = context.getExternalContext();
		Person p = (Person) ex.getSessionMap().get("personOn");
		entry.setUser(p);
		dao.save(entry);
		return "";
	}
	@Override
	public String removeById() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String remove() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void listAll() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String clear() {
		// TODO Auto-generated method stub
		return null;
	}
	public Entry getEntry() {
		return entry;
	}
	public void setEntry(Entry entry) {
		this.entry = entry;
	}
	public GenericDao<Entry> getDao() {
		return dao;
	}
	public void setDao(GenericDao<Entry> dao) {
		this.dao = dao;
	}
	public List<Entry> getEntries() {
		return entries;
	}
	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
	
}
