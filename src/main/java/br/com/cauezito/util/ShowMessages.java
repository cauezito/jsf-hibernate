package br.com.cauezito.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ShowMessages {
	
	public static void showMessageInfo(String msg) {
		FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,"", msg));	
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
	}
	
	public static void showMessageError(String msg) {
		FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "" , msg));	
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
	}
	
	
}
