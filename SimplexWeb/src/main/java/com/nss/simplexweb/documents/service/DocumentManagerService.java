package com.nss.simplexweb.documents.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nss.simplexweb.documents.model.DocumentCategory;
import com.nss.simplexweb.enums.DOCUMENT_CATEGORY_TYPE;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.utility.Utility;
import com.nss.simplexweb.utility.document.model.Document;
import com.nss.simplexweb.utility.document.repository.DocumentRepository;
import com.nss.simplexweb.utility.document.service.DocumentService;

@Service("documentUploadService")
public class DocumentManagerService {

	@Autowired
	private DocumentCategoriesService documentCategoriesService;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private DocumentService documentService;

	@Value("${file.po.po.file}")
	private String PO_PO_FILE_UPLOADED_FOLDER_PATH;

	public DocumentManagerService(DocumentCategoriesService documentCategoriesService,
			DocumentRepository documentRepository, DocumentService documentService) {
		// TODO Auto-generated constructor stub

		this.documentCategoriesService = documentCategoriesService;
		this.documentRepository = documentRepository;
		this.documentService = documentService;
	}

	public ArrayList<Document> getActiveDocumentsByDocumentOwnerPartnerAndDocumentCategoryId(User currentUser,
			Long documentCategoryId) {
		// TODO Auto-generated method stub
		ArrayList<Document> documentList = null;
		try {
			String DOCUMENT_PARENT_ENTITY_TYPE = DOCUMENT_CATEGORY_TYPE.ALL_DOCUMENTS.name();
			if (documentCategoryId < 1) {
				DOCUMENT_PARENT_ENTITY_TYPE = DOCUMENT_CATEGORY_TYPE.ALL_DOCUMENTS.name();
			} else {
				DOCUMENT_PARENT_ENTITY_TYPE = documentCategoriesService
						.getDocumentCategoryByDocumentCategoryId(documentCategoryId).getDocumentCategoryAbbr();
			}

			if (DOCUMENT_PARENT_ENTITY_TYPE.equalsIgnoreCase(DOCUMENT_CATEGORY_TYPE.ALL_DOCUMENTS.name())) {
				ArrayList<DocumentCategory> allCategoriesList = documentCategoriesService.getAllCategoriesList();
				for (DocumentCategory documentCategory : allCategoriesList) {
					if (documentList == null) {
						documentList = new ArrayList<>();
					}
					ArrayList<Document> documentListChild = documentRepository
							.findByDocumentOwnerPartnerAndDocumentParentEntityTypeAndIsDeleted(currentUser,
									documentCategory.getDocumentCategoryAbbr(), 0);
					if (documentListChild != null && documentListChild.size() > 0) {
						documentList.addAll(documentListChild);
					}
				}

			} else {
				documentList = documentRepository.findByDocumentOwnerPartnerAndDocumentParentEntityTypeAndIsDeleted(
						currentUser, DOCUMENT_PARENT_ENTITY_TYPE, 0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return documentList;
	}

	public Document uploadDocumentAccordingToCategory(MultipartFile[] files, User documentOwnerPartner,
			Long documentCategoryId, User uploader) {
		// TODO Auto-generated method stub
		String documentCategoryAbbr = documentCategoriesService
				.getDocumentCategoryByDocumentCategoryId(documentCategoryId).getDocumentCategoryAbbr();
		if (documentCategoryAbbr.equalsIgnoreCase(DOCUMENT_CATEGORY_TYPE.ALL_DOCUMENTS.name())) {
			documentCategoryAbbr = DOCUMENT_CATEGORY_TYPE.OTHER_DOCUMENTS.name();
			documentCategoryId = documentCategoriesService
					.getDocumentCategoryByDocumentCategoryAbbr(documentCategoryAbbr).getDocumentCategoryId();
		}
		DocumentCategory documentCategory = documentCategoriesService
				.getDocumentCategoryByDocumentCategoryId(documentCategoryId);
		Document document = null;
		if (files != null) {
			for (MultipartFile file : files) {
				document = Utility.getMultipartFileDetails(file);
				if (document != null) {
					document.setDocumentParentId(documentCategory.getDocumentCategoryId());
					document.setDocumentParentEntityType(documentCategory.getDocumentCategoryAbbr());
					document.setDocumentStorePath(documentCategory.getDocumentFolderBasePath());
					document.setDocumentStoreByName(Utility.generateDocumentNameAccordingToCategory(documentCategory.getDocumentCategoryAbbr(),null) + "." + document.getDocumentExtension());
					document.setDocumentUploader(uploader);
					document.setDocumentOwnerPartner(documentOwnerPartner);
					document.setDocumentDownloadByName(document.getDocumentOriginalNameWithoutExtension());

					// Save
					Utility.singleFileUpload(file, document.getDocumentStorePath(), document.getDocumentStoreByName());
					document = documentService.saveDocumentDetails(document);
				}
			}
		}
		return document;
	}

	public Document uploadDocumentByCategory(MultipartFile file, User documentOwnerPartner, Long documentCategoryId, User uploader) {
		// TODO Auto-generated method stub
		String documentCategoryAbbr = documentCategoriesService.getDocumentCategoryByDocumentCategoryId(documentCategoryId).getDocumentCategoryAbbr();
		if (documentCategoryAbbr.equalsIgnoreCase(DOCUMENT_CATEGORY_TYPE.ALL_DOCUMENTS.name())) {
			documentCategoryAbbr = DOCUMENT_CATEGORY_TYPE.OTHER_DOCUMENTS.name();
			documentCategoryId = documentCategoriesService.getDocumentCategoryByDocumentCategoryAbbr(documentCategoryAbbr).getDocumentCategoryId();
		}
		DocumentCategory documentCategory = documentCategoriesService.getDocumentCategoryByDocumentCategoryId(documentCategoryId);
		Document document = null;
		if (file != null) {
			document = Utility.getMultipartFileDetails(file);
			if (document != null) {
				document.setDocumentParentId(documentCategory.getDocumentCategoryId());
				document.setDocumentParentEntityType(documentCategory.getDocumentCategoryAbbr());
				document.setDocumentStorePath(documentCategory.getDocumentFolderBasePath());
				document.setDocumentStoreByName(Utility.generateDocumentNameAccordingToCategory(documentCategory.getDocumentCategoryAbbr(),null) + "." + document.getDocumentExtension());
				document.setDocumentUploader(uploader);
				document.setDocumentOwnerPartner(documentOwnerPartner);
				document.setDocumentDownloadByName(document.getDocumentOriginalNameWithoutExtension());

				// Save
				Utility.singleFileUpload(file, document.getDocumentStorePath(), document.getDocumentStoreByName());
				document = documentService.saveDocumentDetails(document);
			}
		}
		return document;
	}

	public Document getDocumentDetailsById(Long documentId) {
		return documentRepository.findBydocumentDetailId(documentId);
	}

	@ResponseBody
	public byte[] downloadDocument(Document doc) throws IOException {
		try {
			String filepath = doc.getDocumentStorePath() + File.separator + doc.getDocumentStoreByName();

			if (filepath != null) {
				File file = new File(filepath);
				if (file.exists() && file.isFile()) {
					return Files.readAllBytes(file.toPath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
