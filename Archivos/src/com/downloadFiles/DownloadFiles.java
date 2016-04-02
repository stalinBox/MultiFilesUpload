package com.downloadFiles;

//import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
//import java.io.InputStream;


//import javax.servlet.ServletContext;
import javax.servlet.ServletException;
//import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadFiles extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private String filePath;
	public DownloadFiles() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String fileName = request.getParameter("FileName");
			String filePath = request.getParameter("FilePath");
			response.setContentType("APPLICATION/OCTET-STREAM"); 
			response.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\""); 
			OutputStream out = response.getOutputStream();
			FileInputStream in = new FileInputStream(filePath);
			byte[] buffer = new byte[4096];
			int length;
			while ((length = in.read(buffer)) > 0){
			    out.write(buffer, 0, length);
			}
			in.close();
			out.flush();
		}catch(Exception e){
			System.out.println(e);
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

}
