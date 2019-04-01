package com.nss.simplexweb.documents.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nss.simplexweb.SessionUtility;
import com.nss.simplexweb.documents.model.DocumentCategory;
import com.nss.simplexweb.documents.service.DocumentCategoriesService;
import com.nss.simplexweb.documents.service.DocumentManagerService;
import com.nss.simplexweb.enums.DISTRIBUTER;
import com.nss.simplexweb.enums.DOCUMENT;
import com.nss.simplexweb.enums.DOCUMENT_CATEGORY_TYPE;
import com.nss.simplexweb.enums.PROJECT;
import com.nss.simplexweb.notifications.service.NotificationService;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.DistributerService;
import com.nss.simplexweb.utility.document.model.Document;

@Controller
@RequestMapping(value={"/documents/documentManagerController"})
public class DocumentManagerController {

	@Autowired
	private DocumentManagerService documentManagerService;
	
	@Autowired
	private DistributerService distributerService;
	
	@Autowired
	private DocumentManagerService documentUploadService;
	
	@Autowired
	private DocumentCategoriesService documentCategoriesService;
	
	@Autowired
	private NotificationService notificationService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getDocumentManagerPage(/*@RequestParam("documentCategoryId") Long documentCategoryId,
												@RequestParam("documentCategoryAbbr") String documentCategoryAbbr,*/
												@RequestParam(value="DOCUNENT_CATEGORY_ABBR", required=false) String DOCUNENT_CATEGORY_ABBR,
												@RequestParam(value="PARTNER_ID", required=false) Long partnerId,
												ModelAndView mav,
												HttpServletRequest request) {
		if(DOCUNENT_CATEGORY_ABBR != null && documentCategoriesService.checkIfDocumentCategoryAbbrExists(DOCUNENT_CATEGORY_ABBR) && partnerId != null) {
			//DO NOTHING, HANDLE EVERYTHING IN AJAX
		}else {
			mav =  new ModelAndView("redirect:/documents/documentManagerController");
			mav.addObject("DOCUNENT_CATEGORY_ABBR" , DOCUMENT_CATEGORY_TYPE.ALL_DOCUMENTS.name());
			mav.addObject("PARTNER_ID", -1);
			return mav;
		}
		
		ArrayList<DocumentCategory> allDocumentCategoriesList = documentCategoriesService.getAllCategoriesList();
		
		mav
			.addObject(DISTRIBUTER.DISTRIBUTER_LIST.name(), distributerService.findAllActiveDistributersList())
			.addObject(DOCUMENT_CATEGORY_TYPE.ALL_DOCUMENT_CATEGORIES_LIST.name(), allDocumentCategoriesList)
			.setViewName("documents/documentManager");
		
		return mav;
	}
	
	@RequestMapping(path="/getMyDocuments", method=RequestMethod.GET)
	public ModelAndView getMyDocuments(/*@RequestParam("documentCategoryId") Long documentCategoryId,
												@RequestParam("documentCategoryAbbr") String documentCategoryAbbr,*/
												@RequestParam(value="DOCUNENT_CATEGORY_ABBR", required=false) String DOCUNENT_CATEGORY_ABBR,
												ModelAndView mav,
												HttpServletRequest request) {
		
		Long partnerId = SessionUtility.getUserFromSession(request).getUserId();
		if(DOCUNENT_CATEGORY_ABBR != null && documentCategoriesService.checkIfDocumentCategoryAbbrExists(DOCUNENT_CATEGORY_ABBR) && partnerId != null) {
			//DO NOTHING, HANDLE EVERYTHING IN AJAX
		}else {
			mav =  new ModelAndView("redirect:/documents/documentManagerController/getMyDocuments");
			mav.addObject("DOCUNENT_CATEGORY_ABBR" , DOCUMENT_CATEGORY_TYPE.ALL_DOCUMENTS.name());
			return mav;
		}
		
		ArrayList<DocumentCategory> allDocumentCategoriesList = documentCategoriesService.getAllCategoriesList();
		
		mav
			.addObject(DISTRIBUTER.DISTRIBUTER_LIST.name(), distributerService.findAllActiveDistributersList())
			.addObject(DOCUMENT_CATEGORY_TYPE.ALL_DOCUMENT_CATEGORIES_LIST.name(), allDocumentCategoriesList)
			.setViewName("documents/myDocuments");
		
		return mav;
	}
	
	@RequestMapping(path="/getDocumentsListAjaxPageForDocumentManager", method=RequestMethod.POST)
	public ModelAndView getDocumentsListForDocumentManager(
			@RequestParam("documentOwnerPartnerId") Long documentOwnerPartnerId,
			@RequestParam("documentCategoryId") Long documentCategoryId,
			ModelAndView mav) {
		User currentUser = distributerService.findDistributerByUserId(documentOwnerPartnerId);
		ArrayList<Document> documentsList = documentManagerService
												.getActiveDocumentsByDocumentOwnerPartnerAndDocumentCategoryId(currentUser, documentCategoryId);
		
		mav
			.addObject(DOCUMENT.DOCUMENTS_LIST.name(), documentsList)
			.setViewName("documents/documentsListAjaxPage");
		
		return mav;
	}
	
	@RequestMapping(value={"/uploadDocumentInFolder"}, method = RequestMethod.POST)
    public @ResponseBody String uploadDocumentInFolder(
    		@RequestParam("file") MultipartFile[] files,
    		@RequestParam("documentOwnerPartner") User documentOwnerPartner,
    		@RequestParam("documentCategoryId") Long documentCategoryId,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request) {
		
		User uploader = SessionUtility.getUserFromSession(request);
		Document document = null;
    	 if (files==null || files.length<1) {
             redirectAttributes.addFlashAttribute(PROJECT.ERROR_MSG.name(), "Please select a file to upload");
         }else {
        	 document = documentUploadService.uploadDocumentAccordingToCategory(files, documentOwnerPartner, documentCategoryId, uploader); 
        	 notificationService.saveNewDocumentNotification(document, 4);
         }
    	 
    	 return PROJECT.SUCCESS_MSG.name();
    }
	
	@RequestMapping(value={"/downloadDocumentById"}, method = RequestMethod.GET)
    public @ResponseBody byte[] downloadDocumentById(Long documentId, HttpServletResponse response) {
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
