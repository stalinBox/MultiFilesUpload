package com.GenerarZip;

import java.io.File;
import java.io.IOException;

import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class GenerarZipDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GenerarZipDownload() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, NullPointerException {
		String [] fileName  = request.getParameterValues("nomFile[]");
		String [] realName  = request.getParameterValues("nomReal[]");
	
		//Generar Nombre Para Zip
			long x = (long) (Math.random () * (1000000000000000000L - 1) + 1); 
			String nomZip = ("Adjuntos"+x+".zip");
			String filePath = ("C:\\seeFilesBonita\\"+nomZip);

		//Copiar con otro nombre 
		try{
			Integer n = 0; 
					n=fileName.length;
			for( int i=0; i<n;i++){
				Path FROM = Paths.get("C:\\seeFilesBonita\\"+realName[i]);
		        Path TO = Paths.get("C:\\seeFilesBonita\\"+fileName[i]);
		        CopyOption[] options = new CopyOption[]{
		          StandardCopyOption.REPLACE_EXISTING,
		          StandardCopyOption.COPY_ATTRIBUTES
		        }; 
		        Files.copy(FROM, TO, options);	
			}
		}catch(Exception e){
			System.out.println(e);
		}

		//Generar Zip 
				try {
						ZipFile zipFile = new ZipFile(filePath);
						ArrayList filesToAdd = new ArrayList();

							for(int i=0; i < realName.length; i++){
								filesToAdd.add(new File("C:\\seeFilesBonita\\"+fileName[i]));
							}

						ZipParameters parameters = new ZipParameters();
						parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
						parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL); 
						zipFile.addFiles(filesToAdd, parameters);
				  	} catch (ZipException e) {
				  		e.printStackTrace();
				  	}

		//Borrar Archivos cambiados el nombre
				try{ 
					for (int j=0; j<fileName.length; j++){
					File deleteFile = new File("C:\\seeFilesBonita\\"+fileName[j]) ;
					if( deleteFile.exists() )
						deleteFile.delete() ;
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}

				response.setContentType("text/html");  
             	response.setCharacterEncoding("UTF-8");
             	response.getWriter().write(filePath);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
