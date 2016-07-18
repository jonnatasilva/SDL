$(function() {

	function prepare() {
		/* buscar areas já cadastro para o local */
		getAreas();

	}

	/*
	 * Função monta o objeto geo json para montar no mapa as areas para o local
	 */
	function carregaGeoJsonObject(coord) {
		var features = [];
		for (var i = 0; i < coord.length; i++) {
			var aux = [];

			var x = JSON.parse('[' + coord[i]['locale'] + ']');
			features
					.push({
						'type' : 'Feature',
						'properties' : {
							'name' : coord[i]['codigo'] + ' - '
									+ coord[i]['descricao'],
							'amenity' : 'Baseball Stadium',
							'popupContent' : 'This is where the Rockies play!'
						},
						'geometry' : {
							'type' : 'Polygon',
							'coordinates' : [ x ]
						}
					});

		}
		var geojsonObject = {
			'type' : 'FeatureCollection',
			'crs' : {
				'type' : 'name',
				'properties' : {
					'name' : 'EPSG:3857'
				}
			},
			'features' : features
		};
		mapa(geojsonObject);
	}

	function ajaxEnviarGet(url, dados, retorna) {
		$.ajax({
			type : "GET",
			url : url,
			data : {
				idLocal : dados
			},
			// async : !retorna,
			contentType : "application/json; charset=utf-8",
			dataType : "json",

			statusCode : {
				200 : function(data) {
					if (retorna) {
						carregaGeoJsonObject(data);
					}
					setInterval(function() {
						$('#preparando').hide();
					}, 5000);

				},
				409 : function() {
					alert("Erro")
				}

			}

		});
	}

	function getAreas(idLocal) {
		var url = '/backendTG/map/polygon/list';
		var enviar = 1;
		ajaxEnviarGet(url, enviar, true);
	}

	prepare();
});

/*
 * Abre formulário para salvar area e adiciona metódo para escutar ação de
 * submit do form
 */
function openForm(coords) {
	var codigo = getUrlParameter('codigoArea');
	var descricao = getUrlParameter('descricaoArea');
	var area = getUrlParameter('localeArea');

	$('.addForm #formArea #codigo').val(codigo);
	$('.addForm #formArea #descricao').val(descricao);
	$('.addForm #formArea #cordenadas').val(coords);

	$('.addForm').show();

	$('#formArea').submit(function(e) {
		var codigo = $('.addForm #codigo').val();
		var descricao = $('.addForm #descricao').val();
		var coord = $('.addForm #cordenadas').val();

		var dados = {
			"codigo" : codigo,
			"descricao" : descricao,
			"locale" : coord
		};
		var url = "/backendTG/map/polygon/save";

		ajaxEnviarPost(url, dados);
		/*
		 * if() { $('#addForm').clear(); $('.addForm').show(); } else {
		 * alert("Erro ao salvar"); }
		 */

		e.preventDefault();
	});

	$('.cancelar').on('click', function(e) {
		$('.addForm').hide();
		e.preventDefault();
	});

}

function tranformToObj(c) {
	var retorno = '';
	for (var i = 0; i < c[0].length; i++) {
		retorno += '[' + c[0][i].toString() + ']';
		;
		if (i < (c[0].length - 1)) {
			retorno += ',';
		}
	}
	return retorno;
}

var getUrlParameter = function getUrlParameter(sParam) {
	var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL
			.split('&'), sParameterName, i;

	for (i = 0; i < sURLVariables.length; i++) {
		sParameterName = sURLVariables[i].split('=');

		if (sParameterName[0] === sParam) {
			return sParameterName[1] === undefined ? true : sParameterName[1]
					.replace('+', ' ');
		}
	}
};

function ajaxEnviarPost(url, dados, retorna) {
	$.ajax({
		type : "POST",
		url : url,
		data : JSON.stringify(dados),
		contentType : "application/json; charset=utf-8",
		dataType : "json",
		statusCode : {
			201 : function(data) {
				if (retorna) {
					console.log(data);
				}
				alert("Success");
			},
			409 : function() {
				alert("Erro")
			}

		}

	});
}

$(document).ready(function() {
	// $('#sideBar')
	// .mouseenter(function () {
	// $("a > span", this).show();
	// })
	// .mouseleave(function () {
	// $("a > span", this).hide();
	// });

	$('.btnPintar').on('click', function() {
		addInteraction(getDrawInteraction());
		removeInteraction(selectSingleClick);
		setInterval(function() {
			removeInteraction(getDrawInteraction());
			addInteraction(getSelectSingleClick());
		}, 300000);
	});

	$('#btnModal').on('show.bs.modal', function(event) {
		var button = $(event.relatedTarget) // Button that triggered the modal
		// var recipient = button.data('whatever') // Extract info from data-*
		// attributes
		// If necessary, you could initiate an AJAX request here (and then do
		// the updating in a callback).
		// Update the modal's content. We'll use jQuery here, but you could use
		// a data binding library or other methods instead.
		var modal = $(this)
		// modal.find('.modal-title').text('New message to ' + recipient)
		// modal.find('.modal-body input').val(recipient)
		array = [];
		$("#formModal").empty();
		$("#formModal").html(addLinhaVertice(0, {}));
		$('.arrayIncorreto').hide();
		$('.erroCampos').hide();
		$('.arrayInsert').html('');
		$('.plusVt').on('click', function() {
			addRow();
		});
		$("#savePaint").on('click', function() {
			savePaint();
		});
	});
});

function addRow() {
	$form = $('#formModal');
	l = $form.find(':input#latitude').length;
	value = {'latitude': $form.find(':input#latitude')[0].value, 'longitude': $form.find(':input#longitude')[0].value};
	if (validateFormVertice($form)) {
		$('#formModal').append(addLinhaVertice(l, value));
	}

}

function addLinhaVertice(l, val) {
	valLat = val.latitude != undefined ? val.latitude : 0;
	valLon = val.longitude != undefined ? val.longitude : 0;
	linha = '<div class="form-group">';
	linha += '<label for="latitude" class="form-control-label">Latitude:</label>';
	linha += ' <input type="number" name="latitude' + l
			+ '" class="form-control" id="latitude" value="' + valLat
			+ '" placeholder="Latitude">';
	linha += '</div> ';
	linha += '<div class="form-group">';
	linha += '<label for="longitude" class="form-control-label">Longitude:</label>';
	linha += ' <input type="number" name="longitude' + l
			+ '" class="form-control" id="longitude"  value="' + valLon
			+ '" placeholder="Longitude">';
	linha += '</div><p />';

	return linha;
}

function savePaint() {
	array = [];
	values = [];
	$form = $('#formModal');
	if (validateFormVertice($form)) {
		$.each($form.serializeArray(), function(i, field) {
			
			if (field.name.indexOf("lat") === 0) {
				values.push(Number(field.value));
			} else {
				values.push(Number(field.value));
				array.push(values);
				values = [];
			}
		});
		console.log(array);
		var polygon = createPolygon(array);
		console.log(polygon);
		addFeature(polygon);
	}
}

function validateFormVertice(form) {
	$.each($(form).serializeArray(), function(i, field) {
		if (field.value === undefined || field.value === ''
				|| field.value === '0') {
			$('.erroCampos').show();
			return false;
		}
	});
	return true;
}