package com.uploadFiles;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadFiles extends HttpServlet {
	 private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFiles() {
        super();
        // TODO Auto-generated constructor stub
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {

         FileItemFactory factory = new DiskFileItemFactory();

         ServletFileUpload upload = new ServletFileUpload(factory);
         List items = null;
            try {
           	 items = upload.parseRequest(request);
           	 Iterator iterator = items.iterator();
                while (iterator.hasNext()) {
                    FileItem item = (FileItem) iterator.next();
                    if (!item.isFormField()) {
                    	
                    	//Obtengo el nombre
                        String fileName = item.getName();

                        //Obtengo la extension(.ext)
                        String extension = fileName.substring( fileName.lastIndexOf( "." ) );
                        //Genero un nombre para guardar en el servidor
                        long x = (long) (Math.random () * (1000000000000000000L - 1) + 1); 
                        String tpmName = ("tpm_"+ String.valueOf(x)+extension);
                       
                        //Crea la direccion si no existe
                        File path = new File("/uploads");
                        if (!path.exists()) {
                       	 boolean status = path.mkdirs();
                        }
                        //Guarda con el nombre long
                        File uploadedFile = new File(path + "/" + tpmName);
                        item.write(uploadedFile); 
                        
                        response.setContentType("text/html");  
                     	response.setCharacterEncoding("UTF-8"); 
                     	response.getWriter().write(uploadedFile.getAbsolutePath()+"\\"+fileName);
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
}
