function Mapa() {
	var map;
	var draw;
	var source;
	var selectSingleClick;
	var modify;
	var _this = this;
	
	this.openFormSave = new Event(this);
	
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
	defineStyle: function(backgroundColor, borderColor) {
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
	buildMapa : function(geojsonObject) {
		var _this = this;
		geojsonObject = this.carregaGeoJsonObject(geojsonObject);
		
		var raster = new ol.layer.Tile({
			// source : new ol.source.MapQuest({
			// layer : 'sat'
			// }),
			source : new ol.source.OSM("Mapa")
		});
		
		this.source = new ol.source.Vector({
			features : (new ol.format.GeoJSON()).readFeatures(geojsonObject),
			wrapX : false
		});
		
		// Adicionar evento que irá executar, quando
		// terminar de desenha polygon
		this.source.on('addfeature', function(evt) {
			var feature = evt.feature;
			var coords = feature.getGeometry().getCoordinates();
			coordsAux = [];
			for(var c = 0; c < coords[0].length; c++) {
				coordsAux.push(ol.proj.transform(coords[0][c], 'EPSG:3857', 'EPSG:4326'));
			}
			
			_this.openFormSave.notify({ coordsAux : coords, feature : feature});
//			openForm(tranformToObj(coords), feature.get('id'), feature.get('name'));
		});
		
		this.selectSingleClick = new ol.interaction.Select({
			style : new ol.style.Style({
				stroke : new ol.style.Stroke({
					color : 'red',
					width : 2
				})
			}),
			filter : function(feature, layer) {
				return true;
			}
		});
		
		var vector = new ol.layer.Vector({
			source : _this.source,
			style : new ol.style.Style({
				fill : new ol.style.Fill({
					color : 'rgba(255, 255, 255, 0.6)'
				}),
				stroke : new ol.style.Stroke({
					color : '#319FD3',
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
					text : this.source.Feature,
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
		});
		
		var select = new ol.interaction.Select({
	        wrapX: false
	    });
		
		this.modify = new ol.interaction.Modify({
	        features: select.getFeatures()
	    });
		this.modify.on('modifyend',function(e){
			coords = e.features.getArray()[0].getGeometry().getCoordinates();
			_this.openFormSave.notify({ coords : coords, feature : e.features.getArray()[0]});
		});

		this.map = new ol.Map({
			interactions: ol.interaction.defaults().extend([select]),
//			new olgm.layer.Google(), 
			layers : [ raster, vector ],
			target : 'map',
			projection: new ol.proj.Projection('EPSG:3857'),
			view : new ol.View({
				center : ol.proj.fromLonLat([-45.8944950663676, -23.198074855969935], 'EPSG:3857'),
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
		var popup = new ol.Overlay.Popup();
		popup.setOffset([ 0, -55 ]);
		
		this.map.addOverlay(popup);
//		this.map.on('singleclick', function(evt) {
//			var feature = _this.map.forEachFeatureAtPixel(evt.pixel, function(feature,
//					layer) {
//			})
//		});
		
		this.map.addInteraction(this.selectSingleClick);
		this.selectSingleClick.on('select', function(e) {
			var lenSele = e.selected.length;
			var lenDese = e.deselected.length;
			if (lenSele == 1) {
				var infoArea = e.selected[0].get('name');
				var content = '<div class="divPopup"><p>' + infoArea + '</p></div>';
				popup.show(e.selected[0].getGeometry().getExtent(), content);
				coords = e.selected[0].getGeometry().getCoordinates();
				_this.openFormSave.notify({ coords : coords, feature : e.selected[0]});
			} else if (lenDese == 1) {
				popup.hide();
			}
		});
		_this.map.on('click', function(evt) {
			 console.log(ol.proj.transform(evt.coordinate, 'EPSG:3857', 'EPSG:4326'));
		 });
//		var olGM = new olgm.OLGoogleMaps({
//			map: _this.map,
//			mapIconOptions: {
//			useCanvas: true
//		}});
//		olGM.activate();
//	
		// $('.btnDelete').on('click', function(){
		// var features = source.getFeatures();
		// for(f in features) {
		// if(features[f].get('name') === feature) {
		// source.removeFeature(features[f]);
		// popup.hide();
		// break;
		// }
		// }
		// });
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
//				'style': this.defineStyle(data[i]['backgroundColor'], data[i]['borderColor'])
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
		return geojsonObject;
	},
	buscarArea : function(codArea) {
		var source;
		if(codArea != undefined && codArea != '') {
			this.source.forEachFeature(function(feature) {
				if(feature.get('id') === codArea) {
					source = new ol.source.Vector({
						features : [feature],
						wrapX : false
					});
				}
			});
		} else {
			source = this.source;
		}
		this.map.getView().fit(source.getExtent(), this.map.getSize());
	}
}
