/**
 * 
 */

function Local(id, codigo, descricao, timezone, areas) {
	this._id = id;
	this._codigo = codigo;
	this._descricao = descricao;
	this._timezone = timezone;
	this._areas = areas;
	
}

Local.prototype = {
	getId : function() {
		return this._id;
	},
	getCodigo : function() {
		return this._codigo;
	},
	getDescricao : function() {
		return this._descricao;
	},
	getTimeZone : function() {
		return this._timexone;
	},
	getAreas : function() {
		return this._areas;
	}
	
}

function AreaLocal(id, codigo, descricao) {
	this._id = id;
	this._codigo = codigo;
	this._descricao = descricao;
}

AreaLocal.prototype = {
		getId : function() {
			return this._id;
		},
		getCodigo : function() {
			return this._codigo;
		},
		getDescricao : function() {
			return this._descricao;
		}
}

function Usuario(id, codigo, nome) {
	this._id = id;
	this._codigo = codigo;
	this._nome = nome;
}

Usuario.prototype = {
		getId : function() {
			return this._id;
		},
		getCodigo : function() {
			return this._codigo;
		},
		getNome: function() {
			return this._nome;
		}
}


function Event(sender) {
	this._sender = sender;
	this._listeners = [];
}

Event.prototype = {
	attach : function(listener) {
		this._listeners.push(listener);
	},
	notify : function(args) {
		var index;
		for (index = 0; index < this._listeners.length; index += 1) {
			this._listeners[index](this._sender, args);
		}
	}
};

function View(model, elements, usuario, local) {
	this._model = model;
	this._elements = elements;
	this._mapa = new Mapa();
	this._usuario = usuario;
	this._local = local;
	var _this = this;
	
	this.salvar = new Event(this);
	this.parseKML = new Event(this);
	this.parseTXT = new Event(this);
	
	this._elements.modificar.click(function() {
		_this._mapa.addModifyInteraction();
	});

	this._elements.paintManual.click(function() {
		_this._mapa.addDrawInteraction();
		setInterval(function() {
			_this._mapa.addSelectSingleClickInteraction();
		}, 300000);
	});
	
	this._elements.visualizarDeta.click(function() {
		_this._mapa.addSelectSingleClickInteraction();
	});
	
	this._elements.modalAutomatic['this'].on('show.bs.modal', function(event) {
		var button = $(event.relatedTarget)
		var modal = _this._elements.modalAutomatic;
		containErro = modal.plusInfo;
		_this.hideElementsObjeto(containErro);
		array = [];
		formModal = modal.formModal;
		formModal.find('.formGeometry').empty();
		formModal.find('.formGeometry').html(_this.getVertice(0, {}));
		
		//Evento chamado para adicionar uma nova vértice no modo automatico
		modal.verticeAdd.on('click', function() {
			_this._model.addRow(formModal, function(isAdd) {
				if(isAdd) {
					_this.mensagemValidateForm(_this._elements.modalAutomatic.plusInfo.erroCampos, true);
					formModal.append(_this.getVertice(l, value));
				} else {
					_this.mensagemValidateForm(_this._elements.modalAutomatic.plusInfo.erroCampos, false);
				}
			});
		});
		
		modal.next.on('click', function() {
			_this.next(formModal);
		});
		
		modal.import.jfilestyle({
			buttonText : "<span class='glyphicon glyphicon-folder-open'></span>"
		});
		modal.import.change(function(event) {
			modal.gifLoad.show();
			_this.hideElementsObjeto(_this._elements.modalAutomatic.plusInfo);
			file = event.target.files[0];
			if (file.type === 'text/plain') {
				_this.parseTXT.notify({ index : event.target.selectedIndex, file: file });
			} else if (file.type.indexOf('kml') != -1) {
				_this.parseKML.notify({ index : event.target.selectedIndex, file: file });
			}
		});
	});
	this._mapa.openFormSave.attach(function (sender, args) {
//		_this._mapa.removeInteractions();
		_this.openForm(args.coords, args.feature);
	});
	this._elements.buscarArea.change(function(event){
		_this._mapa.buscarArea($(this).val());
	});

}

View.prototype = {
	show : function () {
		this._elements.usuario.html(this._usuario.getNome());
		this.buildMapa();
	},
	buildMapa : function() {
		var _this = this;
		this._model.listAreas(_this._local.getId(), function(data){
			_this._mapa.buildMapa(data);
		}, function() {
			alert("erro");
		}, function() {
			_this._elements.painelPrerando.hide();
		});
	},
	next: function($form) {
		array = [];
		values = [];
		codigo = '';
		descricao = '';
		
		if (!this._model.validateBrancoOuZero($form, ['codigo', 'descricao'])) {
			this.mensagemValidateForm(this._elements.modalAutomatic.plusInfo.erroCampos, true);
			formArray = $form.serializeArray();
			for(var i = 0; i < formArray.length; i++) {
				field = formArray[i];
				if (field.name.indexOf("lat") === 0) {
					values.push(Number(field.value));
				} else if(field.name.indexOf("long") === 0) {
					values.push(Number(field.value));
					array.push(values);
					values = [];
				} else if(field.name.indexOf("codigo") === 0) {
					codigo = field.value;
				} else if(field.name.indexOf("descricao") === 0) {
					descricao = field.value;
				} 
			}
			var polygon = this._mapa.createPolygon(array, codigo, descricao);
			style = this._mapa.defineStyle(this._elements.modalAutomatic.background.val(), this._elements.modalAutomatic.border.val());			
			polygon.setStyle(style);
			this._mapa.addFeature(polygon);
			this._elements.modalAutomatic['this'].modal('hide');
		} else {
			this.mensagemValidateForm(this._elements.modalAutomatic.plusInfo.erroCampos, false);
		}
	},
	mensagemValidateForm: function (obj, valido) {
		if (!valido) {
			this._elements.modalAutomatic.plusInfo['this'].show();
			$(obj).show();
		} else {
			this._elements.modalAutomatic.plusInfo['this'].hide();
			$(obj).hide();
		}
	},
	getVertice: function(l, val) {
		valLat = val.latitude != undefined ? val.latitude : 0;
		valLon = val.longitude != undefined ? val.longitude : 0;
		linha = '<div class="form-group">';
		linha += '<label for="latitude" class="form-control-label">Latitude:</label>';
		linha += ' <input type="number" name="latitude' + l
				+ '" class="form-control" id="latitude" value="' + valLat
				+ '" placeholder="Latitude" autofocus>';
		linha += '</div> ';
		linha += '<div class="form-group">';
		linha += '<label for="longitude" class="form-control-label">Longitude:</label>';
		linha += ' <input type="number" name="longitude' + l
				+ '" class="form-control" id="longitude"  value="' + valLon
				+ '" placeholder="Longitude">';
		linha += '</div><p />';

		return linha;
	},
	hideElementsObjeto: function(obj) {
		$.each(obj, function(i, field){
			field.hide();
		});
	},
	exibeImportKML: function(obj) {
		var array = obj.locale;
		if (array === null) {
			this._elements.modalAutomatic.plusInfo['this'].show();
			this._elements.modalAutomatic.plusInfo.arrayIncorreto.show();
		} else if (array.length > 0) {
			this._elements.modalAutomatic.formModal.find('.formGeometry').empty();
			for (var i = 0; i < array.length; i++) {
				this._elements.modalAutomatic.formModal.find('.formGeometry').append(this.getVertice(i, {
					'latitude' : array[i].latitude,
					'longitude' : array[i].longitude,
				}));
			}
			this._elements.modalAutomatic.formModal.find('.formHidden #codigo').val(obj.codigo != undefined ? obj.codigo : 0);
			this._elements.modalAutomatic.formModal.find('.formHidden #descricao').val(obj.descricao);
		}
 		this._elements.modalAutomatic.gifLoad.hide();
	},
	exibeImportTxt: function(array) {
		if (array === undefined) {
			this._elements.modalAutomatic.plusInfo['this'].show();
			this._elements.modalAutomatic.plusInfo.arrayIncorreto.show();
		} else if (array.length > 0) {
			this._elements.modalAutomatic.formModal.find('.formGeometry').empty();
			for (var i = 0; i < array.length; i++) {
				this._elements.modalAutomatic.formModal.find('.formGeometry').append(this.getVertice(i, {
					'latitude' : array[i][0],
					'longitude' : array[i][1]
				}));
			}
		}
		this._elements.modalAutomatic.gifLoad.hide();
	},
	openForm : function(coords, feature) {
		coords = this._model.tranformToObj(coords);
		var _this = this;
		this._elements.modalSalvar['this'].on('show.bs.modal', function(event) {
			var button = $(event.relatedTarget);
			var modal = _this._elements.modalSalvar['this'];
			containErro = _this._elements.modalSalvar.plusInfo;
			_this.hideElementsObjeto(containErro);
			$('#formSalvar #formModalSalvar input[name="codigo"]').val(feature.get('id'));
			$('#formSalvar #formModalSalvar input[name="descricao"]').val(feature.get('name'));
			$('#formSalvar #formModalSalvar input[name="coordenadas"]').val(coords);
			$('#formSalvar #formModalSalvar input[name="usuario"]').val(_this._usuario.getId());
			$('#formSalvar #formModalSalvar input[name="local"]').val(_this._local.getId());
			
			_this._elements.modalSalvar.btnSave.on('click', function() {
				values = {};
				var $form = _this._elements.modalSalvar.formModal;
				var erroEnc = false;
				$.each($form.serializeArray(), function(i, field) {
					if (field.name.indexOf("area") === 0) {
						for(var i = 0; i < _this._local._areas.length; i++) {
							if(_this._local._areas[i]._codigo === field.value) {
								values['codigo'] = _this._local._areas[i]._codigo;
								values['descricao'] = _this._local._areas[i]._descricao;
								break;
							}
						}
					} else if(field.name.indexOf("coordenadas") === 0) {
						values['locale'] = field.value;
					} else if(field.name.indexOf("local") === 0) {
						values['local'] = field.value;
					} else if(field.name.indexOf("usuario") === 0) {
						values['usuario'] = field.value;
					}
				});
				if (!erroEnc) {
					_this.salvar.notify({ index : event.target.selectedIndex, area: values, feature : feature });
				}
			});
		});
		$('#formSalvar').modal('show');
	},
	getUrlParameter : function getUrlParameter(sParam) {
		var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL
				.split('&'), sParameterName, i;
	
		for (i = 0; i < sURLVariables.length; i++) {
			sParameterName = sURLVariables[i].split('=');
	
			if (sParameterName[0] === sParam) {
				return sParameterName[1] === undefined ? true : sParameterName[1].replace('+', ' ');
			}
		}
	}
}

