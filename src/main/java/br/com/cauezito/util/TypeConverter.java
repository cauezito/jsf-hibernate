package br.com.cauezito.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public class TypeConverter {

	public static byte[] inputStreamToByte(InputStream file) throws IOException {

		byte[] bytes = IOUtils.toByteArray(file);

		return bytes;
	}
	
}
