$(document).ready(function() {
	var misVariablesGet = getVarsUrl();
	var verif = new Array();
	var nomFile = new Array();
	var nomReal = new Array();
	var vali = new Array();
	var aux = null;	

	$('.cls_save_btn').click(function(){
		vali = [];
		$('#contenido_UploadFiles #documento').each(function() {
			var table = this;
			var componente = (table.rows[0].cells[0].childNodes[0].children[0].children[0]);
			if(componente.files.length == 0)
				vali.push(0);
			else
				vali.push(1);
		});
			if(!!~vali.indexOf(0)){
				alert("No se ha seleccionado alg\u00FAn");
			}else{
				$('#contenido_UploadFiles .cls_download_link').each(function() {
					      var elemento= this;
					      nomFile.push(elemento.innerHTML);
					      aux = elemento.href;
					      nomReal.push(aux.substring(aux.lastIndexOf('\\')+1,aux.lastIndexOf('&') ));
					});
							
					$.post( "http://localhost:8080/Archivos/DirFilesUpload", { nomFile:nomFile,nomReal:nomReal,pid:misVariablesGet.id,ppiid:misVariablesGet.piid,pdname:misVariablesGet.dname,pdauthor:misVariablesGet.dauthor})
					.done(function() {
						document.location.href="http://localhost:8080/Archivos/mensaje.html";
						});
					return false;
			}
	});
	
	function getVarsUrl(){
	    var url= location.search.replace("?", "");
	    var arrUrl = url.split("&");
	    var urlObj={};   
	    for(var i=0; i<arrUrl.length; i++){
	        var x= arrUrl[i].split("=");
	        urlObj[x[0]]=x[1]
	    }
	    return urlObj;
	}
	

});