/**
 * 
 */
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

function View(model, elements) {
	this._model = model;
	this._elements = elements;
	var _this = this;
	
	this.salvar = new Event(this);
	this.parseKML = new Event(this);
	this.parseTXT = new Event(this);
	
	this._elements.paintManual.click(function() {
		addInteraction(getDrawInteraction());
		removeInteraction(selectSingleClick);
		setInterval(function() {
			removeInteraction(getDrawInteraction());
			addInteraction(getSelectSingleClick());
		}, 300000);
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
}

View.prototype = {
	next: function($form) {
		array = [];
		values = [];
		codigo = '';
		descricao = '';
		this.defineStyle(this._elements.modalAutomatic.background.val(), this._elements.modalAutomatic.border.val());
		if (!this._model.validateBrancoOuZero($form, ['codigo', 'descricao'])) {
			this.mensagemValidateForm(this._elements.modalAutomatic.plusInfo.erroCampos, true);
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
			this.mensagemValidateForm(this._elements.modalAutomatic.plusInfo.erroCampos, false);
			return false;
		}
	},
	defineStyle: function(backgroundColor, borderColor) {
		style = new ol.style.Style({
			fill : new ol.style.Fill({
				color : backgroundColor
			}),
			stroke : new ol.style.Stroke({
				color : borderColor,
				width : 1
			}),
			image : new ol.style.Circle({
				radius : 7,
				fill : new ol.style.Fill({
					color : '#ffcc33'
				})
			}),
			text : new ol.style.Text({
				font : '12px helvetica,sans-serif',
				text : source.Feature,
				rotation : 360 * Math.PI / 180,
				fill : new ol.style.Fill({
					color : '#000'
				}),
				stroke : new ol.style.Stroke({
					color : '#fff',
					width : 1
				})
			})
		})
		return style;
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
	}
}

$(function() {
	var model = new Model();
	view = new View(model, {
		'paintManual' : $('.btnPintar'),
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
		}
	});
	var controller = new Controller(model, view);
});