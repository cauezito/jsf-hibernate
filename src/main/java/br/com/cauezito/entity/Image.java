package br.com.cauezito.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

import br.com.cauezito.dao.GenericDao;

@Entity
public class Image implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Transient
	private UploadedFile photo;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] photoIconB64Original;

	@Column(columnDefinition = "text")
	private String photoIconB64;

	@Column
	private String extension;
	
	@OneToOne(mappedBy = "image")
	private Person person;
	
	public void savePhoto(UploadedFile photo) {
		byte[] imageByte;
		try {
			imageByte = this.getByte(photo.getInputstream());
			this.setPhotoIconB64Original(imageByte);

			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(imageByte));

			int type = bi.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bi.getType();
			int width = 200;
			int height = 200;

			// miniature
			BufferedImage resizedImage = new BufferedImage(width, height, type);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(bi, 0, 0, width, height, null);
			g.dispose();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String extension = photo.getContentType().split("\\/")[1];
			ImageIO.write(resizedImage, extension, baos);

			String miniature = "data:" + photo.getContentType() + ";base64,"
					+ DatatypeConverter.printBase64Binary(baos.toByteArray());
			setPhotoIconB64(miniature);
			setExtension(extension);
			System.out.println(this);
		} catch (IOException e) {
			//this.showMessage("Erro ao salvar imagem");
			e.printStackTrace();
		}
	}
	
	private byte[] getByte(InputStream file) throws IOException {

		byte[] bytes = IOUtils.toByteArray(file);

		return bytes;
	}
	
	/*public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String id = params.get("fileDownloadId");

		Person person = dao.search(Person.class, id);

		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();

		response.addHeader("Content-Disposition",
				"attachment; filename=foto" + person.getName() + "." + getExtension());
		response.setContentType("application/octet-stream");
		response.setContentLengthLong(getPhotoIconB64Original().length);
		response.getOutputStream().write(getPhotoIconB64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}
*/
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public UploadedFile getPhoto() {
		return photo;
	}
	public void setPhoto(UploadedFile photo) {
		this.photo = photo;
	}

	public byte[] getPhotoIconB64Original() {
		return photoIconB64Original;
	}

	public void setPhotoIconB64Original(byte[] photoIconB64Original) {
		this.photoIconB64Original = photoIconB64Original;
	}

	public String getPhotoIconB64() {
		return photoIconB64;
	}

	public void setPhotoIconB64(String photoIconB64) {
		this.photoIconB64 = photoIconB64;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}	
}
