package com.nss.simplexweb.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.nss.simplexweb.enums.PROJECT;
import com.nss.simplexweb.enums.UTILITY_CONSTANT;
import com.nss.simplexweb.user.model.EndPoint;
import com.nss.simplexweb.utility.document.model.Document;


@Component("utility")
public class Utility {

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	////Ref: https://stackoverflow.com/questions/9766800/how-to-show-all-controllers-and-mappings-in-a-view
	public ArrayList<EndPoint> getEndPoints() {
		ArrayList<EndPoint> endPointsList = null;
		Set<RequestMappingInfo> mappingsInfo = requestMappingHandlerMapping.getHandlerMethods().keySet();

		for(RequestMappingInfo mapInfo : mappingsInfo) {
			EndPoint endPoint = new EndPoint();
			endPoint.setPatternsCondition(endPoint.getPatternsCondition());
			endPoint.setMethodsCondition(mapInfo.getMethodsCondition());
			endPoint.setConsumesCondition(endPoint.getConsumesCondition());
			endPoint.setProducesCondition(endPoint.getProducesCondition());
			endPoint.setParamsCondition(endPoint.getParamsCondition());
			endPoint.setHeadersCondition(endPoint.getHeadersCondition());
			endPoint.setCustomCondition(endPoint.getCustomCondition());

			if(endPointsList == null) {
				endPointsList = new ArrayList<>();
			}
			System.out.println("endPoint : " + endPoint);
			endPointsList.add(endPoint);
		}
		System.out.println("endPointsList : " + endPointsList);
		return endPointsList;
	}

	public static String generateRandomPassword(int length) {
		String generatedString = RandomStringUtils.randomAlphanumeric(length);
		return generatedString;
	}

	public static String generateRandomEnquiryNumber(int length) {
		final String prefix = UTILITY_CONSTANT.ENQUIRY_PREFIX;
		String generatedString = prefix + RandomStringUtils.randomNumeric(8);
		return generatedString;
	}


	public static Properties propertiesFileReader(final String FILE_NAME) {
		Properties props = null;
		try {
			Resource resource = new ClassPathResource(File.separator + FILE_NAME + ".properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			props.list(System.out);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}

	public static String generateDocumentNameAccordingToCategory(String categoryAbbr, String documentNumber) {
		if(documentNumber == null) {
			documentNumber = generateRandomPassword(5);
		}
		return categoryAbbr+ "_" + documentNumber + "_" + generateRandomPassword(5);
	}

	/**
	 * This method makes a "deep clone" of any Java object it is given.
	 */
	public static Object deepClone(Object object) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			Object clone = ois.readObject();
			//System.out.println("deepClone : " + ois.readObject().toString());
			return clone;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Utility method to upload single file
	 * 
	 * */
	public static String singleFileUpload(MultipartFile file, String UPLOADED_FOLDER_PATH, String UPLOAD_FILE_NAME) {
		if (file.isEmpty()) {
			return PROJECT.ERROR_MSG.name();
		}

		try {

			// Get the file and save it somewhere
			byte[] bytes = file.getBytes();
			//Path path = Paths.get(UPLOADED_FOLDER_PATH + file.getOriginalFilename());
			Path path = Paths.get(UPLOADED_FOLDER_PATH + File.separator +  UPLOAD_FILE_NAME);
			Files.write(path, bytes);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return PROJECT.SUCCESS_MSG.name();
	}
	

	/**
	 * Utility method to upload po file
	 * 
	 * */
	public static String poFileUpload(File file, String UPLOADED_FOLDER_PATH, String UPLOAD_FILE_NAME) {
		if (!file.exists()) {
			return PROJECT.ERROR_MSG.name();
		}
		
		try {

			// Get the file and save it somewhere
			byte[] bytes = Files.readAllBytes(file.toPath());
			//Path path = Paths.get(UPLOADED_FOLDER_PATH + file.getOriginalFilename());
			Path path = Paths.get(UPLOADED_FOLDER_PATH + File.separator +  UPLOAD_FILE_NAME);
			Files.write(path, bytes);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return PROJECT.SUCCESS_MSG.name();
	}
	
	/*public static String processBase64Image(String base64Image, User user) {
		
		String  UPLOAD_FILE_NAME = null;

		//process base64 image string and convert to byte array
		String delims = "[,]";
		String parts[] = base64Image.split(delims);
		String imageString = parts[1];
		byte[] imageByteArray = Base64.getDecoder().decode(imageString);
		InputStream is = new ByteArrayInputStream(imageByteArray);
		
		//find image type
		String mimeType = null;
		String fileExtension = null;
		try {
			mimeType = URLConnection.guessContentTypeFromStream(is);
			String delimiter = "[/]";
			String tokens[] = mimeType.split(delimiter);
			fileExtension = tokens[1];
			
			
		//set values to file attr
		UPLOAD_FILE_NAME = user.getFullName()
					.replaceAll("\\s", "_").concat("_")
					.concat(Utility.generateRandomPassword(10))
					.concat('.' + fileExtension);
		
		//Set value to user bean
		user.setProfilePicFolderpath(PROFILE_PICTURE_UPLOADED_FOLDER_PATH);
		user.setProfilePicFilename(UPLOAD_FILE_NAME);
		
		//save
		File file = new File(PROFILE_PICTURE_UPLOADED_FOLDER_PATH);
		Path path = Paths.get(PROFILE_PICTURE_UPLOADED_FOLDER_PATH + File.separator + UPLOAD_FILE_NAME);
		try {
			Files.write(path, imageByteArray);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	
	return PROJECT.SUCCESS_MSG.name();
}	*/
	
	
	public static String imageFileUpload(File file, String UPLOADED_FOLDER_PATH, String UPLOAD_FILE_NAME) {
		if (!file.exists()) {
			return PROJECT.ERROR_MSG.name();
		}
		try {
			// Get the file and save it somewhere
			byte[] bytes = Files.readAllBytes(file.toPath());
			Path path = Paths.get(UPLOADED_FOLDER_PATH + File.separator + UPLOAD_FILE_NAME);
			Files.write(path, bytes);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return PROJECT.SUCCESS_MSG.name();
	}
	
	public static Document getMultipartFileDetails(MultipartFile file) {
		Document document = null;
		if(file.isEmpty()) {
			return document;
		}
		
		document = new Document();
		document.setDocumentOriginalNameWithExtension(file.getOriginalFilename());//.substring(0, file.getOriginalFilename().length()-1));
		document.setDocumentExtension(file.getOriginalFilename().split("\\.")[1]);
		document.setDocumentOriginalNameWithoutExtension(file.getOriginalFilename().split("\\.")[0]);
		document.setDocumentMimeType(file.getContentType());
		document.setDocumentSizeInBytes(file.getSize());
		document.setDocumentSizeSmart(format(file.getSize(), 2));
		
		return document;
	}
	
	 /**
     * Method to format bytes in human readable format
     * 
     * @param bytes
     *            - the value in bytes
     * @param digits
     *            - number of decimals to be displayed
     * @return human readable format string
     */
    private static String format(double bytes, int digits) {
        String[] dictionary = { "bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
        int index = 0;
        for (index = 0; index < dictionary.length; index++) {
            if (bytes < 1024) {
                break;
            }
            bytes = bytes / 1024;
        }
        return String.format("%." + digits + "f", bytes) + " " + dictionary[index];
    }

	public static String generatePODocumentName(String poNumber) {
		return UTILITY_CONSTANT.PO_DOCUMENT_PREFIX + poNumber + "_" + generateRandomPassword(5);
	}
	
	
	public static Long getDateDifference(LocalDate dateBefore, LocalDate dateAfter) {
		return ChronoUnit.DAYS.between(dateBefore, dateAfter);
	}
}
