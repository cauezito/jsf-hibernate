package br.com.cauezito.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ShowMessages {
	
	public static void showMessage(String msg) {		
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}
}
