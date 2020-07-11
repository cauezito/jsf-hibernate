package br.com.cauezito.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.primefaces.model.UploadedFile;

import br.com.cauezito.dao.GenericDao;
import br.com.cauezito.repository.PersonDaoImpl;
import br.com.cauezito.util.ShowMessages;
import br.com.cauezito.util.TypeConverter;

@Entity
public class Curriculum {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private String base64;
	@Column
	private String contentType;
	@OneToOne(mappedBy = "curriculum")
	private Person person;
	@Transient
	private PersonDaoImpl dao = new PersonDaoImpl();

	public void saveCurriculum(UploadedFile curriculum) {
		byte[] curriculumByte;
		try {
			base64 = new Base64().encodeBase64String(TypeConverter.inputStreamToByte(curriculum.getInputstream()));
			this.setBase64(base64);
			this.setContentType(curriculum.getContentType());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void download(ActionEvent e) {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("fileId");
		System.out.println(id);
		Curriculum curriculum = dao.getCurriculum(id);

		if (curriculum.getBase64() != null) {

			HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();

			response.addHeader("Content-Disposition", "attachment; filename=curriculo_" + person.getName() + ".pdf");
			response.setContentType("application/octet-stream");
			//response.setContentLengthLong(getBase64().length());
			byte[] file = new Base64().decodeBase64(getBase64());

			try {
				int read = 0;
				byte[] bytes = new byte[1024];
				OutputStream os;

				os = response.getOutputStream();
				InputStream is = new ByteArrayInputStream(file);

				while ((read = is.read(bytes)) != -1) {
					os.write(bytes, 0, read);
				}

				os.flush();
				os.close();

			} catch (IOException err) {
				ShowMessages.showMessage("Não foi possível fazer download do arquivo");
				err.printStackTrace();
			}
			FacesContext.getCurrentInstance().responseComplete();

		}

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
