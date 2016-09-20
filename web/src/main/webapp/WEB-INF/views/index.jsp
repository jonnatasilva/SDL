<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<script type="text/javascript" src='<c:url value="/static/lib/min/jquery.min.js" /> '></script>
	<script type="text/javascript" src='<c:url value="/static/lib/jquery.toaster.js" /> '></script>
	
	<script type="text/javascript" src='<c:url value="/static/lib/bootstrap-3.3.6/js/bootstrap.min.js" />' ></script>
	<script type="text/javascript" src='<c:url value="/static/lib/ol/v3.18.2-dist/ol.js" />'></script>
	<script type="text/javascript" src='<c:url value="/static/lib/ol/ol-popup.js"/>'></script>
	<script type="text/javascript" src='<c:url value="/static/lib/filestyle/jquery-filestyle.js"/>'></script>
	
<!-- 	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3"></script> -->
<%-- 	<script type="text/javascript" src='<c:url value="/static/lib/ol3-google-maps-v0.11.0/ol3gm.js"/>'></script>	 --%>
	<script type="text/javascript" src='<c:url value="/static/lib/js/mapa.js" />'></script>
	<script type="text/javascript" src='<c:url value="/static/lib/js/model.js" />'></script>
	<script type="text/javascript" src='<c:url value="/static/lib/js/view.js"/>'></script>
	<script type="text/javascript" src='<c:url value="/static/lib/js/controller.js"/>'></script>
	
<%-- 	<script type="text/javascript" src='<c:url value="/static/lib/js/script.js"/>'></script> --%>
	<script type="text/javascript" src='<c:url value="/static/lib/js/menu.js"/>'></script>

	<!-- importação de css -->
	<link type="text/css" rel="stylesheet" href='<c:url value="/static/lib/bootstrap-3.3.6/css/bootstrap.min.css" />'>
	<link type="text/css" rel="stylesheet" href='<c:url value="/static/lib/ol/v3.18.2-dist/ol.css"/>'>
	<link type="text/css" rel="stylesheet" href='<c:url value="/static/lib/filestyle/jquery-filestyle.css"/>'>
	<link type="text/css" rel="stylesheet" href='<c:url value="/static/lib/css/style.css"/>'>
	<link type="text/css" rel="stylesheet" href='<c:url value="/static/lib/css/menu.css"/>'>
<%-- 	<link type="text/css" rel="stylesheet" href='<c:url value="/static/lib/ol3-google-maps-v0.11.0/ol3gm.css"/>'/> --%>
	
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
</head>
<body>	
	<%@include  file="/WEB-INF/static/menu.html" %>
	
	<div id="preparando">
		<div class="conteudo">
			<img alt="" src='<c:url value="/static/lib/img/simova_loading_white.gif"/>' />
			<p>Aguarde...</p>
		</div>
	</div>
	
	<div id="map" class="map">
	</div>
	<div class="sdlControls ol-unselectable ol-control">
		<button>
			<span class="glyphicon glyphicon-pencil btnPintar" aria-hidden="true"></span>
		</button>
		<button>
			<span class="glyphicon glyphicon-hand-up btnVisuDetal" aria-hidden="true"></span>
		</button>
	</div>

	<%@include  file="/WEB-INF/static/modalSalvar.html" %>
	<%@include  file="/WEB-INF/static/modalAutomatic.html" %>
	
	<script type="text/javascript">
		$(function() {
			$('.dropdown-toggle').dropdown();
			//Set Objeto local, (Obra, Filial, ETC)
			var idLocal = <c:out value="${local['id']}"></c:out>;
			var codLocal = '<c:out value="${local['codigo']}"></c:out>';
			var descLocal = '<c:out value="${local['descricao']}"></c:out>';
			var tizoLocal = '<c:out value="${local['timezone']}"></c:out>';
			var areas = [];
			<c:forEach items="${local['areasLocal'] }" var="area">
				codAreaLocal = '<c:out value="${area.codigo }"></c:out>';
				descAreaLocal = '<c:out value="${area.descricao }"></c:out>';
				areas.push(new AreaLocal(null, codAreaLocal, descAreaLocal));
			</c:forEach>
		
			var local = new Local(idLocal, codLocal, descLocal, null, areas);
			
			//Set ObjetoUsuario
			var id = <c:out value="${usuario['id']}"></c:out>;
			var cod = '<c:out value="${usuario['codigo']}"></c:out>';
			var nome = '<c:out value="${usuario['nome']}"></c:out>';
			var usuario = new Usuario(id, cod, nome);
			
			var model = new Model();
			view = new View(model, {
				'modificar': $('.btnModify'),
				'paintManual' : $('.btnPintar'),
				'painelPrerando' : $('#preparando'),
				'usuario' : $('#usuario'),
				'buscarArea' : $('#buscaArea'),
				'visualizarDeta' : $('.btnVisuDetal'),
 				'modalAutomatic' : {
					'this' : $('#btnModal'),
					'verticeAdd': $('#btnModal #btnAddVert'),
					'next' : $('#savePaint'),
					'import': $('#btnModal input:file'),
					'formModal': $('#btnModal #formModal'),
					'gifLoad' : $('#btnModal .loadFile'),
					'plusInfo' : {
						'this' : $('#btnModal .plusInfo'),
						'erroCampos' : $('#btnModal .plusInfo .erroCampos'),
						'arrayIncorreto' : $('#btnModal .plusInfo .arrayIncorreto'),
						'arrayIncorretoImpt' : $('#btnModal .plusInfo .arrayIncorretoImpt'),
						'arrayInc' : $('#btnModal .plusInfo .arrayInc')
					},
					'background': $('#backgroundColorPicker'),
					'border': $('#borderColorPicker')
				},
				'modalSalvar' : {
					'this' : $('#formSalvar'),
					'formModal': $('#formSalvar #formModalSalvar'),
					'plusInfo' : {
						'this' : $('#formSalvar .plusInfo'),
						'erroCampos ': $('#formSalvar .plusInfo .erroCampos'),
						'falhaSalvar' : $('#formSalvar .plusInfo .falha'),
					},
					'btnSave' : $('#formSalvar #save')
				}
			}, usuario, local);
			var controller = new Controller(model, view);
			view.show();
		});
	</script>
	
</body>
</html>