/**
 * 
 */
function Controller(model, view) {
	this._model = model;
	this._view = view;
	
	var _this = this;

    this._view.parseKML.attach(function (sender, args) {
    	var formData = new FormData();
    	formData.append('kml', args.file, args.file.name);
    	_this._model.parseKML(formData, function(data) {
    		_this._view.exibeImportKML(data);
    	}, function() {
    		$.toaster({ priority : 'danger', title : 'Erro', message : 'Falha ao carregar arquivo!'});
    	}, function() {
   
    	});
    });
    this._view.parseTXT.attach(function (sender, args) {
    	_this._model.parseTXT(args.file, function(data) {
    		_this._view.exibeImportTxt(data);
    	}, function() {
    		_this._view._elements.modalAutomatic.plusInfo['this'].show();
    		_this._view._elements.modalAutomatic.plusInfo.arrayIncorreto.show();
    		_this._view._elements.modalAutomatic.gifLoad.hide();
    	});
    });
    this._view.salvar.attach(function(sender, args) {
    	_this._model.salvarArea(args.area, function() {
    		_this._view._elements.modalSalvar['this'].modal('hide');
    		$.toaster({ priority : 'success', title : 'Sucesso', message : 'Salvo com sucesso!'});
    	}, function() {
    		$.toaster({ priority : 'danger', title : 'Erro', message : 'Falha ao salvar!'});
    	}, function() {
    		
    	})
    });
}