package br.com.cauezito.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ShowMessages {
	
	public static void showMessageInfo(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", msg));	
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
	}
	
	public static void showMessageError(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null , msg));	
		FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		
	}
	
	
}
