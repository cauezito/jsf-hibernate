package br.com.cauezito.model;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.commons.io.IOUtils;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.entity.Address;

@ViewScoped
@ManagedBean(name = "addressBean")
public class AddressBean implements Crud {
	
	private Address address = new Address();
	private GenericDao<Address> dao = new GenericDao<Address>();
	private List<Address> addresses = new ArrayList<>(); 

	

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public GenericDao<Address> getDao() {
		return dao;
	}

	public void setDao(GenericDao<Address> dao) {
		this.dao = dao;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	@Override
	public String save() {
		// TODO Auto-generated method stub
		return null;
	}

}
