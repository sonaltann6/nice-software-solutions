package com.nss.simplexrest.documents.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nss.simplexweb.documents.model.DocumentCategory;
import com.nss.simplexweb.documents.service.DocumentCategoriesService;
import com.nss.simplexweb.documents.service.DocumentManagerService;
import com.nss.simplexweb.enums.DISTRIBUTER;
import com.nss.simplexweb.enums.DOCUMENT;
import com.nss.simplexweb.enums.DOCUMENT_CATEGORY_TYPE;
import com.nss.simplexweb.enums.PROJECT;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.DistributerService;
import com.nss.simplexweb.user.service.UserService;
import com.nss.simplexweb.utility.document.model.Document;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/documents/documentManagerController")
@Api(value = "Documents Resource REST Endpoint", description = "All Documents Related Operations")
public class DocumentManagerRestController {

	@Autowired
	private DocumentManagerService documentManagerService;
	
	@Autowired
	private DistributerService distributerService;
	
	@Autowired
	private DocumentManagerService documentUploadService;
	
	@Autowired
	private DocumentCategoriesService documentCategoriesService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/getDocumentManager")
	@ResponseBody
	public Map<String, List<?>> getDocumentManager() {
		Map<String, List<?>> map = new HashMap<String, List<?>>();
		ArrayList<DocumentCategory> allDocumentCategoriesList = documentCategoriesService.getAllCategoriesList();
		map.put(DISTRIBUTER.DISTRIBUTER_LIST.name(), distributerService.findAllActiveDistributersList());
		map.put(DOCUMENT_CATEGORY_TYPE.ALL_DOCUMENT_CATEGORIES_LIST.name(), allDocumentCategoriesList);
		return map;
	}
	
	@GetMapping(value = "/getDoucmentListByCategory")
	public Map<String, List<?>> getDoucmentListByCategory(@RequestParam("documentOwnerPartnerId") Long documentOwnerPartnerId,
			@RequestParam("documentCategoryId") Long documentCategoryId){
		Map<String, List<?>> map = new HashMap<String, List<?>>();
		User currentUser = distributerService.findDistributerByUserId(documentOwnerPartnerId);
		ArrayList<Document> documentsList = documentManagerService
												.getActiveDocumentsByDocumentOwnerPartnerAndDocumentCategoryId(currentUser, documentCategoryId);
		
		map.put(DOCUMENT.DOCUMENTS_LIST.name(), documentsList);
		return map;
	}
	
	
	@PostMapping(value = "/uploadDocumentInFolder")
	@ResponseBody
	public String uploadDocumentInFolder(@RequestParam("ionicfile") MultipartFile file,
    		@RequestParam("documentOwnerPartner") String base64Json,
    		@RequestParam("documentCategoryId") Long documentCategoryId,
            RedirectAttributes redirectAttributes, @RequestParam ("userId") Long userId) throws JsonParseException, JsonMappingException, IOException {
		
		User uploader = userService.findUserByUserId(userId);
		String json = new String(Base64.getDecoder().decode(base64Json));
		ObjectMapper mapper = new ObjectMapper();
		User documentOwnerPartner = mapper.readValue(json, User.class);
		if (file==null) {
            redirectAttributes.addFlashAttribute(PROJECT.ERROR_MSG.name(), "Please select a file to upload");
        }else {
       	 documentUploadService.uploadDocumentByCategory(file, documentOwnerPartner, documentCategoryId, uploader); 
        }
   	 
   	 return PROJECT.SUCCESS_MSG.name();
	}
	
	/*@PostMapping
	@ResponseBody
	public String uploadDocument(@RequestBody String base64Image, 
			@RequestParam("documentOwnerPartner") User documentOwnerPartner,
    		@RequestParam("documentCategoryId") Long documentCategoryId,
            RedirectAttributes redirectAttributes, @RequestParam ("userId") Long userId) {
		
		User uploader = userService.findUserByUserId(userId);
		if (base64Image == null) {
            redirectAttributes.addFlashAttribute(PROJECT.ERROR_MSG.name(), "Please select a file to upload");
        }else {
        	documentUploadService.uploadDocumentByCategory(base64Image, documentOwnerPartner, documentCategoryId, uploader);
        }
		return PROJECT.SUCCESS_MSG.name();
	}*/
	
	@GetMapping(value = "/downloadDocumentById")
	@ResponseBody
	public byte[] downloadDocumentById(Long documentId, HttpServletResponse response) {
		try {
			Document doc = documentManagerService.getDocumentDetailsById(documentId);
			byte[] fileBytes = documentManagerService.downloadDocument(doc);
			ServletOutputStream outStream = response.getOutputStream();
			response.setContentType(doc.getDocumentMimeType());
			response.setHeader("Content-Disposition", "attachment; filename="+doc.getDocumentDownloadByName() + "." + doc.getDocumentExtension());
			outStream.write(fileBytes);
			outStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
}
