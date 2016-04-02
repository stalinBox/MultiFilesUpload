<%@page contentType="text/html"
	import="java.util.*, javax.sql.DataSource,javax.naming.*, java.sql.*,java.io.OutputStream,java.io.FileOutputStream,java.io.File;"%>
<html>
<head>
<link href="CSS/applicationResource.css" type="text/css" rel="stylesheet">
<link href="CSS/Css2.css" type="text/css" rel="stylesheet">
<script src="funcionesJs/jquery-1.11.3.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var nomFile = new Array();
	var nomReal = new Array();
	var aux = null;	

	$('.cls_download_link_Zip').click(function(){
     	$('#contenido_UploadFiles .cls_download_link').each(function() {
		      var elemento= this;
		      nomFile.push(elemento.innerHTML);
		      aux = elemento.href;
		      nomReal.push(aux.substring(aux.lastIndexOf('\\')+1,aux.lastIndexOf('&') ));
		});

   	$.ajax({
     		  type: 'get',
     		  url: 'http://localhost:8080/Archivos/GenerarZipDownload',
     		  data: { nomFile:nomFile,nomReal:nomReal },
     		  success: function(data){
     		  var rutaZip = data;
     		  var NomZip = (data.substring(data.lastIndexOf('\\')+1))
     		 	document.location.href="http://localhost:8080/Archivos/DownloadFiles?FilePath="+rutaZip+"&FileName="+NomZip;
     		  }
     		}).fail(function(jqXHR, textStatus, errorThrown){
     		  console.log(jqXHR.statusText);
     		  console.log(textStatus);
     		  console.log(errorThrown);
         		});
 		 return false; 

	});
});

</script>
<script type="text/javascript">
function Confirmar() {
	//Mensaje a mostrar
	var mensaje = confirm("Est\u00E1 seguro que desea salir?");
	//Detectamos si el usuario acepto el mensaje
	if (mensaje) {
	javascript:window.close();
	}
	//Detectamos si el usuario denegó el mensaje
	else {
	return false;
	}
}
</script>
<title>Lista Adjuntos</title>
</head>
<body class="bonita-body">
	<div id="footerpusher">
		<div id="main">
			<div id="static_application">
					<div id="bonita_process_label" class="bonita_process_label">
						<div class="gwt-HTML">Documentos</div>
					</div>
					<div id="bonita-banneraltbas"></div>
					<div id="bonita_form">
							<div id="bonita_form_page_label" class="bonita_form_page_label">
								<div class="gwt-HTML">Lista Adjuntos</div>
							</div>
							<div class="bonita_form_container" >
								<form  method="get"  action="http://localhost:8080/Archivos/GenerarZipDownload" >
									<div id="contenido_UploadFiles">
									<button class="cls_download_link_Zip" >DESCAGAR TODO</button>
									<input class="cls_exit_btn" type="button" onclick="Confirmar()" value="SALIR" />
									<%
									String pid=request.getParameter("id");
									String ppiid=request.getParameter("piid");
									String pdname=request.getParameter("dname");
									String pdauthor=request.getParameter("dauthor");
									
										DataSource ds = null;
										Connection conn = null;
										PreparedStatement ps = null;
										InitialContext ic;

										byte[] fileBytes;
										String query;
										String paramDocumento = null;
										
										//Recuperar Archivos de la BD
										try {
											ic = new InitialContext();
											ds = (DataSource) ic.lookup("java:/bonitaDSC");
											conn = ds.getConnection();
											
											String sql = " select a.documentContentFileName, b.content "
													      +" from [bpmfiseidb].[dbo].[document_mapping] a ,[bpmfiseidb].[dbo].[document_content] b "
														  +" where a.id = b.id " 
														  +" and a.contentStorageId = b.documentId "
														  +" and a.documentAuthor = "+pdauthor
														  +" and a.documentName = " +"\'"+pdname +"\'";    
														  //+" and a.processinstanceid = "+ppiid;
											
											ps = conn.prepareStatement(sql);
											ResultSet rs = ps.executeQuery();
											
											File path = new File("C://seeFilesBonita//");
					                        if (!path.exists()) {
					                       	 boolean status = path.mkdirs();
					                        }
					                        
											while (rs.next()) {
												
												long x = (long) (Math.random () * (1000000000000000000L - 1) + 1);
												
												String nomArch = rs.getString(1);
												String extension = nomArch.substring( nomArch.lastIndexOf( "." ) );
												String nombGen = "tpm_"+x+extension;
												fileBytes = rs.getBytes(2);
												
												OutputStream targetFile = new FileOutputStream( path +"\\"+ nombGen);
												
												targetFile.write(fileBytes);
												targetFile.close();
												
												out.println("<div class=\"classLink\">");
												out.println("<a class=\"cls_download_link\" href=\"http://localhost:8080/Archivos/DownloadFiles?FilePath=C:\\seeFilesBonita\\"
														+ nombGen
														+ "&FileName="
														+ rs.getString(1)
														+ "\">" + rs.getString(1) + "</a>");
												out.println("</div>");
												
											}
											
											rs.close();
											ps.close();
											conn.close();
											
										} catch (Exception e) {
											out.println("ERROR:" + e);
										}
									%>
									</div>
								</form>
							</div>
					</div>
			</div>
		</div>
	</div>
	<div class="footer">
		<center><span id="footer">Universidad Técnica de Ambato</span></center>
	</div>
</body>
</html>