$(document).ready(function() {
	var max_fields = 10; 
	var wr         = $(".cls_label");
	var addBtn     = $(".cls_add_btn"); 
	var x = 1; 
	var y = 2;
	
	//Añadir field inicial
	$(wr).append ('<TABLE id="documento" class="cls_form_entry" >'+
    '<TR>'+
    '<TH COLSPAN="2">'+
		'<div class="classFormHide" style="" aria-hidden="false">'+
			'<form id="forma" action="http://localhost:8080/Archivos/UploadFiles" method="post" enctype="multipart/form-data">'+
				'<input type="file" name="file" id="file" accept="application/pdf" />'+
			'</form>'+
		'</div>'+
    '</TH>'+
    '</TR>'+
    '<TR>'+
    '<TH COLSPAN="2">'+
     '<div class="upload" style="display: none;">Uploading....</div>'+
    '</TH>'+
    '</TR>'+
    '<TR>'+
    '<TH COLSPAN="2">'+
		'<div class="classLink" style="display: none;" aria-hidden="true" >'+
			'<a class="cls_download_link"></a>'+
		'</div>'+
    '</TH>'+
    '</TR>'+
    '<TR>'+
    '<TD>'+
    '<div class="cls_upload_btn" title="cambiar el adjunto" style="display: none;" aria-hidden="true">Cambio</div>'+
    '</TD>'+
    '</TR>'+
    '</TABLE>');

	//Añadir Fields Secundarios
	$(addBtn).click(function(e){
		e.preventDefault();
		if (x>=9){
		alert("Solo puede a\u00F1adir "+max_fields+" documentos");
		return false;
		}
		if(x < max_fields){
			x++;
			$(wr).append ('<TABLE id="documento" class="cls_form_entry">'+
		      '<TR>'+
		      '<TH COLSPAN="2">'+
				'<div class="classFormHide" style="" aria-hidden="false">'+
					'<form id="forma" action="http://localhost:8080/Archivos/UploadFiles" method="post" enctype="multipart/form-data">'+
						'<input type="file" name="file" id="file" accept="application/pdf" />'+
					'</form>'+
				'</div>'+
		      '</TH>'+
		      '</TR>'+
		      '<TR>'+
		      '<TH COLSPAN="2">'+
		       '<div class="upload" style="display: none;">Uploading....</div>'+
		      '</TH>'+
		      '</TR>'+
		      '<TR>'+
		      '<TH COLSPAN="2">'+
				'<div class="classLink" style="display: none;" aria-hidden="true" >'+
					'<a class="cls_download_link"></a>'+
				'</div>'+
		      '</TH>'+
		      '</TR>'+
		      '<TR>'+
		      '<TD >'+
		      '<a href="" class="remove_field">Remover</a>'+
		      '</TD>'+
		      '<TD>'+
		      '<div class="cls_upload_btn" title="cambiar el adjunto" style="display: none;" aria-hidden="true">Cambio</div>'+
		      '</TD>'+
		      '</TR>'+
			'</TABLE>');
			y++;
		}
	});

	//Remover field
	$(wr).on("click",".remove_field", function(e){
		e.preventDefault(); 
		$(this).parent().parent().parent().parent(".cls_form_entry").remove(); 
		x--;
	}); 
	//Validar y enviar al servidor
	$(wr).on("click",".classFormHide", function(){
		var parentTag = null;
		var filesValue = null;
		var extension = null;
		var tamBits = null;
		var bytes = null;
		$(this).children().children('input[type="file"]').on('change', function() {
			try{
				extension = (this.value.substring(this.value.lastIndexOf("."))).toLowerCase();
				tamBits = ((this.files[0].size/1024)/1024).toFixed(3);
				if(extension!='.pdf' || tamBits >= 20){
					alert("ERROR: Su archivo puede contener los siguientes errores:"+
							" \nNo es un archivo PDF o, \nSu tama\u00F1o excede las 20 MB\nNOTA:"+
							" \nSu tama\u00F1o real es: "+ tamBits +" MB \nSu extensi\u00F3n real es: " + extension );
					$(this).parent('#forma').get(0).reset();
				}	
			}
			catch(err){
				tamBits = null;
			}
		});

		$(this).children().children('input[type="file"]').ajaxfileupload({
			'action': 'http://localhost:8080/Archivos/UploadFiles',
			'onComplete': function(response) {
				var ruta = response.substring(0 ,response.lastIndexOf('\\'));
				$(this).parent().parent('.classFormHide').myHide();
				$(this).parent().parent().parent().parent().siblings().siblings().children().children('.classLink').myShow();
				$(this).parent().parent().parent().parent().siblings().siblings().siblings().children().children('.cls_upload_btn').myShow();
				$(this).parent().parent().parent().parent().siblings().children().children('.upload').hide();
				var nombre = (response.substring(response.lastIndexOf('\\')+1));      	 	
				var link = document.createTextNode(nombre); 
				var elemento = $(this).parent().parent().parent().parent().siblings().siblings().children().children().children('.cls_download_link')[0];
				var dirServer = "http://localhost:8080/Archivos/DownloadFiles";
				var FilePath =ruta;
				var FileName =nombre;
				elemento.href=dirServer+"?FilePath="+FilePath+"&FileName="+FileName;
				elemento.appendChild(link);
			},'onStart': function() {
				$(this).parent().parent().parent().parent().siblings().children().children('.upload').show();
			}
		});

		$(this).parent().parent().siblings().siblings().siblings().children().children('.cls_upload_btn').click(function(i){
			$(this).parent().parent().siblings().children().children().children('.cls_download_link')[0].href=null;
			$(this).parent().parent().siblings().children().children().children('.cls_download_link').empty();
			$(this).parent().parent().siblings().siblings().siblings().children().children('.classFormHide').myShow();
			$(this).parent().parent().siblings().children().children('.classLink').myShow();
			$(this).myHide();
			$(this).parent().parent().siblings().siblings().siblings().children().children('.classFormHide').children("#forma").get(0).reset();
		});

		(function ( $ ) {
			$.fn.myShow = function(){
				return this.each(function() {
					$(this).attr('aria-hidden', 'false').show();
				});
			};   
			$.fn.myHide = function(){
				return this.each(function() {
					$(this).attr('aria-hidden', 'true').hide();
				});
			};
		}(jQuery));		

	});
});