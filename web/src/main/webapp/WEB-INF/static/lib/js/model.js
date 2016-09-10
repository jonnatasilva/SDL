/**
 * 
 */

function Model() {
	this.base = '/web/map/';
}

Model.prototype = {
	listAreas : function(idLocal, callbackSucesso, callbackErro, callbackAlways) {
		$.getJSON(this.base + 'listJSON', {
			idLocal : 1
		}).done(function(data) {
			callbackSucesso(data);
		}).fail(function() {
			callbackErro();
		}).always(function() {
			callbackAlways();
		});
	},
	salvarArea: function(area, callbackSucesso, callbackErro, callbackAlways) {
		$.post(this.base + 'salvar', {area: JSON.stringify(area)}).done(function() {
			callbackSucesso();
		}).fail(function() {
			callbackErro();
		}).always(function() {
			callbackAlways();
		});
	},
	parseKML: function(formData, callbackSucesso, callbackErro, callbackAlways) {
		jQuery.ajax({
		    url: this.base +  'parseKML',
		    data: formData,
		    cache: false,
		    contentType: false,
		    processData: false,
		    type: 'POST'
		}).done(function(data) {
			callbackSucesso(data);
		}).fail(function() {
		}).always(function(){
			callbackAlways();
		});
	},
	addRow: function($form, callbackErro) {
		l = $form.find(':input#latitude').length;
		value = {
			'latitude' : $form.find(':input#latitude')[0].value,
			'longitude' : $form.find(':input#longitude')[0].value
		};
		if (!this.validateBrancoOuZero($form, ['codigo', 'descricao'])) {
			callbackErro(true);
		} else {
			callbackErro(false);
		}
	},
	validateBrancoOuZero: function(form, arrayCampos) {
		var erroEcontrado = false;
		$.each($(form).serializeArray(), function(i, field) {
			if (arrayCampos.indexOf(field.name) === -1 && (field.value === undefined || field.value === '' || field.value === '0')) { 
				erroEcontrado = true;
				return false;

			}
		});
		return erroEcontrado;
	},
	parseTXT: function(f, callbackSucesso, callbackErro) {
		var reader = new FileReader();
		var array = [];
		reader.onload = (function(theFile) {
			return function(e) {
				var contents = e.target.result;
				if (contents.substring(0, 2) != '[[' && contents.substring(0, 1) === '[') {
					array = $.parseJSON('[' + contents + ']');
					callbackSucesso(array);
				} else if (contents.substring(0, 2) === '[[') {
					array = $.parseJSON(contents);
					callbackSucesso(array);
				} else {
					callbackErro();
				}
			};
		})(f);
		reader.readAsText(f);
	},
	tranformToObj : function(c) {
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
}