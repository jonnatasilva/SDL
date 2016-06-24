$(function() {
	var map;
	var draw; // global so we can remove it later
	var source;
	var selectSingleClick;
});

function addInteraction() {
	var source;
	var value = 'Polygon';
	draw = new ol.interaction.Draw({
		source : source,
		type : /** @type {ol.geom.GeometryType} */
		(value)
	});
	map.removeInteraction(selectSingleClick);
	map.addInteraction(draw);
}

function removeInteraction() {
	map.removeInteraction(draw);
	map.addInteraction(selectSingleClick);
}

function mapa(geojsonObject) {

	var raster = new ol.layer.Tile({
		source : new ol.source.MapQuest({
			layer : 'sat'
		}),
	});

	source = new ol.source.Vector({
		features : (new ol.format.GeoJSON()).readFeatures(geojsonObject),
		wrapX : false

	});

	// Adicionar evento que irá executar, quando
	// terminar de desenha polygon
	source.on('addfeature', function(evt) {
		var feature = evt.feature;
		var coords = feature.getGeometry().getCoordinates();
		console.log(tranformToObj(coords));
		openForm(tranformToObj(coords));
	});

	selectSingleClick = new ol.interaction.Select({
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
		source : source,
		style : new ol.style.Style({
			fill : new ol.style.Fill({
				color : 'rgba(255, 255, 255, 0.2)'
			}),
			stroke : new ol.style.Stroke({
				color : '#ffcc33',
				width : 2
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
					width : 2
				})
			})
		})

	});

	map = new ol.Map({
		layers : [ raster, vector ],
		target : 'map',
		view : new ol.View({
			center : [ -11000000, 4600000 ],
			zoom : 4
		})
	});

	var popup = new ol.Overlay.Popup;
	popup.setOffset([ 0, -55 ]);
	map.addOverlay(popup);

	/*
	 * map.on('singleclick', function(evt) { var feature =
	 * map.forEachFeatureAtPixel(evt.pixel, function(feature, layer) { }) });
	 */

	map.addInteraction(selectSingleClick);
	selectSingleClick.on('select', function(e) {
		var lenSele = e.selected.length;
		var lenDese = e.deselected.length;
		
		if (lenSele == 1) {
			var infoArea = e.selected[0].get('name');
			
			console.log();
			var content = '<div class="divPopup"><p>' + infoArea + '</p></div>';
			popup.show(e.selected[0].getGeometry().getExtent(),content);
			deleteFeature(infoArea);
			
		} else if (lenDese == 1) {
			popup.hide();
		}

	});
	
	$('.btnDelete').on('click', function(){
		var features = source.getFeatures();
		for(f in features) {
			if(features[f].get('name') === feature) {
				source.removeFeature(features[f]);
				popup.hide();
	            break;
			}
		}
	});
	
	function getInfoArea() {
		
	}
}
