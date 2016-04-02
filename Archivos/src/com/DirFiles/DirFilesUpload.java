package com.DirFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

import javax.activation.MimetypesFileTypeMap;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class DirFilesUpload extends HttpServlet {
	private static DataSource ds = null;
	private static Connection conn = null;
	private static PreparedStatement ps = null;
	private static PreparedStatement ps2 = null;
	private static InitialContext ic = null;
	//private static String sql = null;
	
	public static int id;
	public static String documentId = null;
	public static String documentFileName = null;
	public static String storageId = null;
	public static Byte[] content ;
	
	private static final long serialVersionUID = 1L;
	public DirFilesUpload() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException { 
		String[] nomEta = request.getParameterValues("nomFile[]");
		String[] nomReal = request.getParameterValues("nomReal[]");
		String idBonita = request.getParameter("pid");
		String piidBonita = request.getParameter("ppiid"); 
		String dnameBonita = request.getParameter("pdname");
		String dauthorBonita = request.getParameter("pdauthor");
		
		for(String i:nomEta) {
			System.out.println(i);
		}
		for(String j:nomReal) {
			System.out.println(j);
		}
		System.out.println(idBonita);
		System.out.println(piidBonita);
		System.out.println(dnameBonita);
		System.out.println(dauthorBonita);

		for(int i=0; i<nomReal.length; i++){
				documentFileName = nomEta[i];
				long x = (long) (Math.random () * (1000000000000000000L - 1) + 1); 
				storageId = Long.toString(x*(-1));
				MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
				String f =  "C:\\uploads\\"+nomReal[i];
				String mimeType = mimeTypesMap.getContentType(f);
				System.out.println(mimeType);
				
				Path path = Paths.get("C:\\uploads\\"+nomReal[i]);
				byte[] docs = Files.readAllBytes(path);
			  
			saveToBonitaDM(Integer.parseInt(idBonita),Integer.parseInt(piidBonita),dnameBonita,Integer.parseInt(dauthorBonita),documentFileName,mimeType,storageId);
			saveToBonitaDC(Integer.parseInt(idBonita),docs,storageId);
			}
	}
	
	 public static void saveToBonitaDM(int cidBonita,int cpiidBonita,String cdnameBonita,int cdauthorBonita,String cdocumentFileName,String mimeType,String cstorageId) {
		 try {
				ic = new InitialContext();
				ds = (DataSource)ic.lookup("java:/bonitaDSC");
				conn = ds.getConnection();
				int success=0;
				String sql1="INSERT INTO [bpmfiseidb].[dbo].[document_mapping]("
						+"[tenantid]"
						+",[id]"
						+",[processinstanceid]"
						+",[documentName]"
						+",[documentAuthor]"
						+",[documentCreationDate]"
						+",[documentHasContent]"
						+",[documentContentFileName]"
						+",[documentContentMimeType]"
						+",[contentStorageId]"
						+",[documentURL])"						           
				+"VALUES ("
						+"1,"
						+"?," //id
						+"?," //ProcessInstanceId
						+"?," //documentName
						+"?," //documentAuthor
						+"(select CAST(convert(varchar,getdate(),112) as int)),"
						+"1," //DocumentHashContent
						+"?," //DocumentContentFileName
 						+"?," //documentContentMimeType
						+"?," //contentStorageId
						+"null);";
				
				ps = conn.prepareStatement(sql1);
				ps.setInt(1, cidBonita);
				ps.setInt(2, cpiidBonita);
				ps.setString(3, cdnameBonita);
				ps.setInt(4, cdauthorBonita);
				ps.setString(5, cdocumentFileName);
				ps.setString(6, mimeType);
				ps.setString(7, cstorageId);
				
				success = ps.executeUpdate();
				if(success>=1)  
					System.out.println("Table documment_mapping Stored");
				ps.close();
				conn.close();
			
			}catch(Exception e){
				System.out.println("ERROR: "+e);
			}finally{
	            if (ps != null) {                                            
	                try {                                                         
	                    ps.close();                                                
	                } catch (SQLException sqlex) {                                
	                	sqlex.printStackTrace();
	                }                                                             
	                ps = null;                                            
	            }
			}
	 }
	 
	 public static void saveToBonitaDC(int c_id, byte[] c_docs, String c_storageId) {
		 try {
				ic = new InitialContext();
				ds = (DataSource)ic.lookup("java:/bonitaDSC");
				conn = ds.getConnection();

				int success2=0;
				String sql2 = "INSERT INTO [bpmfiseidb].[dbo].[document_content]("
						+ "[tenantid],"
						+ "[id],"
						+ "[documentId],"
						+ "[content])"
					+ "VALUES("
						+ "1," //tenant
						+ "?," //id
						+ "?," //documentId
						+ "?);"; //content
				ps2 = conn.prepareStatement(sql2);
				ps2.setInt(1, c_id);
				ps2.setString(2, c_storageId);
				ps2.setBytes(3, c_docs);

				success2 = ps2.executeUpdate();
				if (success2>=1)
					System.out.println("Table Document_Content Stored");
				ps2.close();
				//Cerrar la conexion.
				conn.close();
			
			}catch(Exception e){
				System.out.println("Error: "+e);
			}finally{            
	            if (ps2 != null) {                                            
	                try {                                                         
	                    ps2.close();                                                
	                } catch (SQLException sqlex) {                                
	                	sqlex.printStackTrace();
	                }                                                             
	                ps2 = null;                                            
	            }
			}
	 }
}