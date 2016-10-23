//var model = new Model();s

//$(function() {
//
//	function prepare() {
//		/* buscar areas já cadastro para o local */
//		model.listAreas(1, function(data){
//			carregaGeoJsonObject(data);
//		}, function() {
//			alert("erro");
//		}, function() {
//			$('#preparando').hide();
//		});
//
//	}
//
//	/*
//	 * Função monta o objeto geo json para montar no mapa as areas para o local
//	 */
//	function carregaGeoJsonObject(data) {
//		var features = [];
//		for (var i = 0; i < data.length; i++) {
//			var aux = [];
//
//			var x = data[i].localeArray;
//			features.push({
//				'type' : 'Feature',
//				'properties' : {
//					'name' : data[i]['codigo'] + ' - ' + data[i]['descricao'],
//				// 'amenity' : 'Baseball Stadium',
//				// 'popupContent' : 'This is where the Rockies play!'
//				},
//				'geometry' : {
//					'type' : 'Polygon',
//					'coordinates' : [ x ]
//				}
//			});
//
//		}
//		var geojsonObject = {
//			'type' : 'FeatureCollection',
//			'crs' : {
//				'type' : 'name',
//				'properties' : {
//					'name' : 'EPSG:3857'
//				}
//			},
//			'features' : features
//		};
//		mapa(geojsonObject);
//	}
//	prepare();
//});

/*
 * Abre formulário para salvar area e adiciona metódo para escutar ação de
 * submit do form
 */
//function openForm(coords, codigo, descricao) {
//	$('#formSalvar')
//			.on(
//					'show.bs.modal',
//					function(event) {
//						var button = $(event.relatedTarget);
//						var modal = $(this);
//						$('.plusInfo').hide();
//						$('.erroCampos').hide();
//						$('.falhaSalvar').hide();
//						$('.arrayIncorreto').hide();
//						$('#formSalvar #formModalSalvar input[name="codigo"]').val(codigo);
//						$('#formSalvar #formModalSalvar input[name="descricao"]').val(descricao);
//						$('#formSalvar #formModalSalvar input[name="coordenadas"]').val(coords);
//						$("#save").on('click', function() {
//							values = {};
//							var $form = $('#formModalSalvar');
//							var erroEnc = false;
//							$.each($form.serializeArray(), function(i, field) {
//								if (field.nameindexOf("codigo") === 0) {
//									if (field.value === undefined || field.value === "") {
//										$('.erroCampos').show();
//										erroEnc = true;
//									}
//									values['codigo'] = field.value;
//								} else if (field.name.indexOf("descricao") === 0) {
//									if (field.value === undefined || field.value === "") {
//										$('.erroCampos').show();
//										erroEnc = true;
//									}
//									values['descricao'] = field.value;
//								} else {
//									values['locale'] = field.value;
//								}
//							});
//							var url = "/backendTG/map/polygon/salvar";
//							if (!erroEnc) {
//								model.salvarArea(values, function() {}, function() {}, function(){});
//								ajaxEnviarPost(url, values, modal);
//							}
//						});
//					});
//					$('#formSalvar').modal('show');
//}
//
//function tranformToObj(c) {
//	var retorno = '';
//	for (var i = 0; i < c[0].length; i++) {
//		retorno += '[' + c[0][i].toString() + ']';
//		;
//		if (i < (c[0].length - 1)) {
//			retorno += ',';
//		}
//	}
//	return retorno;
//}
//
//var getUrlParameter = function getUrlParameter(sParam) {
//	var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL
//			.split('&'), sParameterName, i;
//
//	for (i = 0; i < sURLVariables.length; i++) {
//		sParameterName = sURLVariables[i].split('=');
//
//		if (sParameterName[0] === sParam) {
//			return sParameterName[1] === undefined ? true : sParameterName[1].replace('+', ' ');
//		}
//	}
//};
//
//function ajaxEnviarPost(url, dados, retorna, modal) {
//	$.ajax({
//		type : "POST",
//		url : url,
//		data : JSON.stringify(dados),
//		contentType : "application/json; charset=utf-8",
//		dataType : "json",
//		statusCode : {
//			201 : function(data) {
//				if (retorna) {
//					return data;
//				} else {
//					modal.modal('hide');
//				}
//			},
//			409 : function() {
//				$('.plusInfo').show();
//				$('#formSalvar .falhaSalvar').show();
//			}
//
//		}
//
//	});
//}
//
//$(document)
//		.ready(
//				function() {
//					// $('#sideBar')
//					// .mouseenter(function () {
//					// $("a > span", this).show();
//					// })
//					// .mouseleave(function () {
//					// $("a > span", this).hide();
//					// });
//
//					$('.btnPintar').on('click', function() {
//						addInteraction(getDrawInteraction());
//						removeInteraction(selectSingleClick);
//						setInterval(function() {
//							removeInteraction(getDrawInteraction());
//							addInteraction(getSelectSingleClick());
//						}, 300000);
//					});

//					$('#btnModal')
//							.on(
//									'show.bs.modal',
//									function(event) {
//										var button = $(event.relatedTarget) // Button
//										// that
//										// triggered
//										// the
//										// modal
//										// var recipient =
//										// button.data('whatever') // Extract
//										// info from data-*
//										// attributes
//										// If necessary, you could initiate an
//										// AJAX request here (and then do
//										// the updating in a callback).
//										// Update the modal's content. We'll use
//										// jQuery here, but you could use
//										// a data binding library or other
//										// methods instead.
//										var modal = $(this)
//										// modal.find('.modal-title').text('New
//										// message to ' + recipient)
//										// modal.find('.modal-body
//										// input').val(recipient)
//										array = [];
//										$('.plusInfo').hide();
//										$("#formModal .formGeometry").empty();
//										$("#formModal .formGeometry").html(
//												addLinhaVertice(0, {}));
//										$('.arrayIncorreto').hide();
//										$('.erroCampos').hide();
//										$(
//												'#btnModal .plusInfo .arrayIncorretoImpt')
//												.hide();
//										$('.arrayInsert').html('');
//										$('#btnAddVert').on('click',
//												function() {
//													addRow();
//												});
//										$("#savePaint").on('click', function() {
//											if (savePaint()) {
//												modal.modal('hide');
//											}
//
//										});
//
//										$(":file")
//												.jfilestyle(
//														{
//															buttonText : "<span class='glyphicon glyphicon-folder-open'></span>"
//														});
//										$("input:file")
//												.change(
//														function(event) {
//															$('.loadFile')
//																	.show();
//															f = event.target.files[0];
//															if (f.type === 'text/plain') {
//																readFile(f,
//																		false);
//															} else if (f.type
//																	.indexOf('kml') != -1) {
//																var url = '/backendTG/map/polygon/parseKML';
//																readFile(f,
//																		true,
//																		url);
//															}
//														});
////									});
//				});

//function addRow() {
//	$form = $('#formModal');
//	l = $form.find(':input#latitude').length;
//	value = {
//		'latitude' : $form.find(':input#latitude')[0].value,
//		'longitude' : $form.find(':input#longitude')[0].value
//	};
//	if (!validateBrancoOuZero($form, ['codigo'])) {
//		$('#formModal').append(addLinhaVertice(l, value));
//		mensagemValidateForm('.erroCampos', true);
//	} else {
//		mensagemValidateForm('.erroCampos', false);
//	}
//
//}
//
//function mensagemValidateForm(obj, valido) {
//	if (!valido) {
//		console.log($('.plusInfo').css('display'));
//		$('.plusInfo').css('display', 'inline-block');
//		$(obj).show();
//	} else {
//		$('.plusInfo').hide();
//		$(obj).hide();
//	}
//
//}

//function addLinhaVertice(l, val) {
//	valLat = val.latitude != undefined ? val.latitude : 0;
//	valLon = val.longitude != undefined ? val.longitude : 0;
//	linha = '<div class="form-group">';
//	linha += '<label for="latitude" class="form-control-label">Latitude:</label>';
//	linha += ' <input type="number" name="latitude' + l
//			+ '" class="form-control" id="latitude" value="' + valLat
//			+ '" placeholder="Latitude" autofocus>';
//	linha += '</div> ';
//	linha += '<div class="form-group">';
//	linha += '<label for="longitude" class="form-control-label">Longitude:</label>';
//	linha += ' <input type="number" name="longitude' + l
//			+ '" class="form-control" id="longitude"  value="' + valLon
//			+ '" placeholder="Longitude">';
//	linha += '</div><p />';
//
//	return linha;
//}

function savePaint() {
	array = [];
	values = [];
	$form = $('#formModal');
	var codigo = '';
	var descricao = '';
	var background = $('#backgroundColorPicker').val();
	var border = $('#borderColorPicker').val();
	defineStyle(background, border);
	if (!validateBrancoOuZero($form, ['codigo'])) {
		mensagemValidateForm('.erroCampos', true);
		$.each($form.serializeArray(), function(i, field) {
			if (field.name.indexOf("lat") === 0) {
				values.push(Number(field.value));
			} else if(field.name.indexOf("long")) {
				values.push(Number(field.value) === 0);
				array.push(values);
				values = [];
			} else if(field.name.indexOf("codigo") === 0) {
				codigo = field.value;
			} else if(field.name.indexOf("descricao") === 0) {
				descricao = filed.value;
			}
		});
		var polygon = createPolygon(array, codigo, descricao);
		
		addFeature(polygon);
		return true;

	} else {
		mensagemValidateForm('.erroCampos', false);
		return false;
	}

}

//function validateBrancoOuZero(form, arrayCampos) {
//	var erroEcontrado = false;
//	$.each($(form).serializeArray(), function(i, field) {
//		if (arrayCampos.indexOf(field.name) != 0 && (field.value === undefined || field.value === '' || field.value === '0')) { 
//			erroEcontrado = true;
//			return false;
//
//		}
//	});
//	return erroEcontrado;
//}

//function readFile(f, isKml, url) {
//	var reader = new FileReader();
//	var array = [];
//	reader.onload = (function(theFile) {
//		return function(e) {
//
//			var contents = e.target.result;
//			if (!isKml) {
//				if (contents.substring(0, 2) != '[['
//						&& contents.substring(0, 1) === '[') {
//					array = $.parseJSON('[' + contents + ']');
//				} else if (contents.substring(0, 2) === '[[') {
//					array = $.parseJSON(contents);
//				} else {
//					array = null;
//				}
//
//				exibeImportTxt(array);
//			} else {
//				var formData = new FormData();
//				formData.append('kml', f, f.name);
//				var xhr = new XMLHttpRequest();
//				xhr.open('POST', url, true);
//
//				xhr.onload = function() {
//					if (xhr.status === 200) {
//						var result = $.parseJSON(xhr.response);
//						exibeImportKML(result);
//					} else {
//						alert('An error occurred!');
//					}
//				};
//				xhr.send(formData);
//			}
//		};
//	})(f);
//
//	reader.readAsText(f);
//
//}
//function exibeImportTxt(array) {
//	if (array === null) {
//		$('#btnModal .plusInfo').show();
//		$('#btnModal .plusInfo .arrayIncorretoImpt').show();
//	} else if (array.length > 0) {
//		$("#formModal .formGeometry").empty();
//		for (var i = 0; i < array.length; i++) {
//			$("#formModal .formGeometry").append(addLinhaVertice(i, {
//				'latitude' : array[i][0],
//				'longitude' : array[i][1]
//			}));
//		}
//	}
//	$('.loadFile').hide();
//}

//function exibeImportKML(obj) {
//	var array = obj.locale;
//	if (array === null) {
//		$('#btnModal .plusInfo').show();
//		$('#btnModal .plusInfo .arrayIncorretoImpt').show();
//	} else if (array.length > 0) {
//		$("#formModal .formGeometry").empty();
//		for (var i = 0; i < array.length; i++) {
//			$("#formModal .formGeometry").append(addLinhaVertice(i, {
//				'latitude' : array[i].latitude,
//				'longitude' : array[i].longitude,
//			}));
//		}
//		$('#formModal .formHidden #codigo').val(obj.codigo != undefined ? obj.codigo : 0);
//		$('#formModal .formHidden #descricao').val(obj.descricao);
//	}
//	$('.loadFile').hide();
//}
