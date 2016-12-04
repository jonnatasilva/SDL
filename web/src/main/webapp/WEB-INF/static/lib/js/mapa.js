function Mapa() {
	var map;
	var draw;
	var source;
	var selectSingleClick;
	var modify;
	var vector;
	var _this = this;

	this.openFormSave = new Event(this);
	this.enableRemoveFeature = new Event(this);
	this.disableRemoveFeature = new Event(this);

}

Mapa.prototype = {
	addFeature : function(feature) {
		this.source.addFeature(feature);
	},
	createPolygon : function(coords, codigo, descricao) {
		var thing = new ol.geom.Polygon([ coords ]);
		// thing.transform('EPSG:4326', 'EPSG:3857');
		var feature = new ol.Feature({
			id : codigo,
			name : descricao,
			geometry : thing
		});
		return feature;
	},
	getSelectSingleClick : function() {
		return this.selectSingleClick;
	},
	addInteraction : function(interaction) {
		this.map.addInteraction(interaction);
	},
	removeInteractions : function() {
		this.map.removeInteraction(this.draw);
		this.map.removeInteraction(this.modify);
	},
	addDrawInteraction : function() {
		this.removeInteractions();
		this.map.removeInteraction(this.selectSingleClick);
		this.map.addInteraction(this.draw);
	},
	addSelectSingleClickInteraction : function() {
		this.removeInteractions();
		this.map.addInteraction(this.selectSingleClick);
	},
	addModifyInteraction : function() {
		this.removeInteractions();
		this.map.removeInteraction(this.selectSingleClick);
		this.map.addInteraction(this.modify);
	},
	defineStyle : function(backgroundColor, borderColor) {
		source = this.source;
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
			
		})
		return style;
	},
	buildMapa : function(geojsonObject) {
		var _this = this;

		var raster = new ol.layer.Tile({
			source : new ol.source.OSM("Mapa")
		});

		this.source = this.createVector(geojsonObject);

		// Adicionar evento que irá executar, quando
		// terminar de desenha polygon
		this.source.on('addfeature', function(evt) {
			if(_this.isPolygon(evt.feature.getGeometry().getCoordinates())) {
				_this.notifyAddFeature(evt.feature);
			} else {
				_this.removeFeaturesWithOutId();
			}
		});

		this.selectSingleClick = new ol.interaction.Select({
			style : new ol.style.Style({
				stroke : new ol.style.Stroke({
					color : '#FF4500',
					width : 2
				})
			}),
			filter : function(feature, layer) {
				_this.enableRemoveFeature.notify({})
				return true;
			}
		});
		
		this.vector = new ol.layer.Vector({
			source : _this.source,
			style: _this.createPolygonStyleFunction()
		});

		var select = new ol.interaction.Select({
			wrapX : false
		});

		this.modify = new ol.interaction.Modify({
			features : select.getFeatures()
		});
		
		this.modify.on('modifyend', function(e) {
			_this.notifyAddFeature(e.features.getArray()[0]);
		});

		this.map = new ol.Map({
			interactions : ol.interaction.defaults().extend([ select ]),
			layers : [ raster, this.vector ],
			target : 'map',
			view : new ol.View({
				projection : 'EPSG:4326',
				center : [-45.8944950663676, -23.198074855969935],
				zoom : 15
			})
		});

		if(this.source.getFeatures().length > 0 ) {
			 this.map.getView().fit(this.source.getExtent(), this.map.getSize());
		 }

		var value = 'Polygon';
		this.draw = new ol.interaction.Draw({
			source : this.source,
			type : /** @type {ol.geom.GeometryType} */
			(value)
		});

		this.map.addInteraction(this.selectSingleClick);
		this.selectSingleClick.on('select', function(e) {
			if(e.selected != undefined && e.selected != null && e.selected.length > 0) {
				e.selected[0].setStyle(_this.styleSelected(e.selected[0], 16));
			} else {
				_this.disableRemoveFeature.notify({});
			} 
			
			if(e.deselected != undefined && e.deselected != null && e.deselected.length > 0){
				e.deselected[0].setStyle(_this.styleDeselected(e.deselected[0], 12));
			}
		});
		
		_this.map.on('click', function(evt) {
			console.log(evt.coordinate);
		});
	},
	carregaGeoJsonObject : function(data) {
		var features = [];
		for (var i = 0; i < data.length; i++) {
			var aux = [];
			var x = data[i].localeArray;
			features.push({
				'type' : 'Feature',
				'properties' : {
					'id' : data[i]['codigo'],
					'name' : data[i]['codigo'] + ' - ' + data[i]['descricao'],
				},
				'geometry' : {
					'type' : 'Polygon',
					'coordinates' : [ x ]
				},
			});
		}
		var geojsonObject = {
			'type' : 'FeatureCollection',
			'crs' : {
				'type' : 'name',
			},
			'features' : features
		};
		return geojsonObject;
	},
	buscarArea : function(codArea) {
		var source;
		if (codArea != undefined && codArea != '') {
			this.source.forEachFeature(function(feature) {
				if (feature.get('id') === codArea) {
					source = new ol.source.Vector({
						features : [ feature ],
						wrapX : false
					});
				}
			});
		} else {
			source = this.source;
		}
		this.map.getView().fit(source.getExtent(), this.map.getSize());
	},
	notifyAddFeature : function(feature) {
		this.openFormSave.notify({
			coords : feature.getGeometry().getCoordinates(),
			feature : feature
		});
	},
	setSource : function(source) {
		this.source = source;
	}, 
	createVector : function(geoJSON) {
		return  new ol.source.Vector({format: new ol.format.GeoJSON(),
			features : (new ol.format.GeoJSON()).readFeatures(this.carregaGeoJsonObject(geoJSON)), wrapX : false});
	},
	removeFeaturesWithOutId : function() {
		_this = this;
		this.source.forEachFeature(function(feature) {
			if(feature.get('id') === undefined || feature.get('id') === '' || feature.get('id') === 0) {
				_this.removeFeature(feature);
				$.toaster({ priority : 'warning', title : 'Notificação', message : 'Area removida!'});
			}
		});
	},
	removeFeature : function(feature) {
		if(feature != null && feature != undefined) {
			this.source.removeFeature(feature);
		}
		this.source.refresh();
		this.map.render();
		this.selectSingleClick.getFeatures().clear();
	},
	getFeatureById : function(id) {
		featureAux = undefined;
		this.source.forEachFeature(function(feature) {
			if(id === feature.get('id')) 
				featureAux = feature;
				return;
		});
		return featureAux;
	},
	getSelectedFeature : function() {
		return this.selectSingleClick.getFeatures().getArray()[0];
	},
	isPolygon: function(coordinates) {
		polygon = new ol.geom.Polygon(coordinates);
		console.log(polygon.getArea());
		if(polygon != null && polygon.getCoordinates() != null && polygon.getCoordinates()[0].length > 3
				&& polygon.getFirstCoordinate()[0] === polygon.getLastCoordinate()[0] 
				&& polygon.getFirstCoordinate()[1] === polygon.getLastCoordinate()[1]
				&& polygon.getArea() > 0) {
			return true;
		}
		return false;
	},
	getText: function(feature) {
	    var text = feature.get('name');
	    return text;
	},
	createTextStyle: function(feature, fontSize, color) {
		_this = this;
		return new ol.style.Text({
			textAlign: 'center',
		    textBaseline: 'middle',
		    font: fontSize+'px Verdana',
		    text: _this.getText(feature),
		    fill: new ol.style.Fill({color: color}),
		    stroke: new ol.style.Stroke({color: '#000000', width: 0.7})
		});
	},
	createPolygonStyleFunction: function() {
		_this = this;
		return function(feature) {
			styles = [
			          new ol.style.Style({
			        	  stroke: new ol.style.Stroke({
			        		  color: 'blue',
			        		  width: 3
			        	  }),
			        	  fill: new ol.style.Fill({
			        		  color: 'rgba(0, 0, 255, 0.1)'
						  }),
						  text: _this.createTextStyle(feature, 12, '#1C1C1C')
			          }),
			          new ol.style.Style({
						  image: new ol.style.Circle({
							  radius: 5,
						      fill: new ol.style.Fill({
						          color: 'orange'
						       })
					  }),
					  geometry: function(feature) {
						  var coordinates = feature.getGeometry().getCoordinates()[0];
						  return new ol.geom.MultiPoint(coordinates);
					   }
			         })]
			return styles;
		}
	},
	styleSelected: function(feature, fontSize) {
		return new ol.style.Style({
      	  stroke: new ol.style.Stroke({
      		  color: '#008B8B',
      		  width: 1
      	  }),
      	  fill: new ol.style.Fill({
      		  color: 'rgba(0, 0, 255, 0.1)'
			  }),
			  text: _this.createTextStyle(feature, fontSize, '#FF0000')
        })
	},
	styleDeselected: function(feature) {
		styles = [
		          new ol.style.Style({
		        	  stroke: new ol.style.Stroke({
		        		  color: 'blue',
		        		  width: 3
		        	  }),
		        	  fill: new ol.style.Fill({
		        		  color: 'rgba(0, 0, 255, 0.1)'
					  }),
					  text: _this.createTextStyle(feature, 12, '#1C1C1C')
		          }),
		          new ol.style.Style({
					  image: new ol.style.Circle({
						  radius: 5,
					      fill: new ol.style.Fill({
					          color: 'orange'
					       })
				  }),
				  geometry: function(feature) {
					  var coordinates = feature.getGeometry().getCoordinates()[0];
					  return new ol.geom.MultiPoint(coordinates);
				   }
		         })]
		return styles;
	}
}
