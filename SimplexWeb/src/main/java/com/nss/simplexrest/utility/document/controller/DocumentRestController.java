package com.nss.simplexrest.utility.document.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexweb.utility.document.model.Document;
import com.nss.simplexweb.utility.document.service.DocumentService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/document")
@Api(value = "Document Resource REST Endpoint", description = "All Document Related Operations")
public class DocumentRestController {

	@Autowired
	private DocumentService documentService;
	
	@GetMapping(value = "/downloadDocumentByDocumentDetailId")
	public void downloadDocumentByDocumentDetailId(@RequestParam("documentDetailId") Long documentDetailId, HttpServletResponse response) {
		
		//If user is not authorized - he should be thrown out from here itself
        
        //Authorized user will download the file
		Document document = documentService.findDocumentByDocumentDetailId(documentDetailId);
        String filepath = document.getDocumentStorePath() + File.separator + document.getDocumentStoreByName();
        File file = new File(filepath);
        if (file.exists() && file.isFile())
        {
            response.setContentType(document.getDocumentMimeType());
            response.addHeader("Content-Disposition", "attachment; filename="+document.getDocumentDownloadByName()+'.'+document.getDocumentExtension());
            try
            {
                Files.copy(file.toPath(), response.getOutputStream());
                response.getOutputStream().flush();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}
}
