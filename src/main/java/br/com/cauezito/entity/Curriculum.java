package br.com.cauezito.entity;

import java.io.IOException;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.apache.commons.codec.binary.Base64;
import org.primefaces.model.UploadedFile;

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
	
	public void saveCurriculum(UploadedFile curriculum) {
		byte[] curriculumByte;
		try {
			base64 = new Base64().
					encodeBase64String(TypeConverter.inputStreamToByte(curriculum.getInputstream()));
			this.setBase64(base64);
			this.setContentType(curriculum.getContentType());

		} catch (IOException e) {
			e.printStackTrace();
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
