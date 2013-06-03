package org.vaadin.addon.leaflet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaadin.addon.leaflet.client.vaadin.LeafletMapServerRpc;
import org.vaadin.addon.leaflet.client.vaadin.LeafletMapState;
import org.vaadin.addon.leaflet.shared.BaseLayer;
import org.vaadin.addon.leaflet.shared.Bounds;
import org.vaadin.addon.leaflet.shared.Control;
import org.vaadin.addon.leaflet.shared.Point;

import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;

/**
 * 
 */
public class LMap extends AbstractComponentContainer {

    private List<Component> components = new ArrayList<Component>();

    public LMap() {
        setSizeFull();
        registerRpc(new LeafletMapServerRpc() {
            @Override
            public void onClick(Point p) {
                fireEvent(new LeafletClickEvent(LMap.this, p));
            }

            @Override
            public void onMoveEnd(Bounds bounds, int zoomlevel) {
            	getState(false).zoomLevel = zoomlevel;
            	getState(false).center = bounds.getCenter();
                fireEvent(new LeafletMoveEndEvent(LMap.this, bounds, zoomlevel));
            }
        });

    }

    @Override
    public void beforeClientResponse(boolean initial) {
        super.beforeClientResponse(initial);
    }

    
    @Override
    protected LeafletMapState getState(boolean markAsDirty) {
    	return (LeafletMapState) super.getState(markAsDirty);
    }
    
    @Override
    protected LeafletMapState getState() {
        return (LeafletMapState) super.getState();
    }

    @Override
    public void replaceComponent(Component oldComponent, Component newComponent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void addComponent(Component c) {
        if ((!(c instanceof LeafletLayer)) && (!(c instanceof LLayerGroup))) {
            throw new IllegalArgumentException(
                    "only instances of LeafletLayer or LLayerGroup allowed");
        }
        super.addComponent(c);
        components.add(c);
        markAsDirty(); // ?? is this really needed
    }

    @Override
    public void removeComponent(Component c) {
        super.removeComponent(c);
        components.remove(c);
        markAsDirty(); // ?? is this really needed
    }

    @Override
    public int getComponentCount() {
        return components.size();
    }

    public boolean hasComponent(Component component) {
        return components.contains(component);
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

    public void setCenter(double lat, double lon) {
        Point point = new Point();
        point.setLat(lat);
        point.setLon(lon);
        setCenter(point);
    }

    public void setZoomLevel(int zoomLevel) {
        getState().zoomLevel = zoomLevel;
    }

    public BaseLayer[] getBaseLayers() {
        return getState().getBaseLayers();
    }

    public void setBaseLayers(BaseLayer... baselayer) {
        getState().setBaseLayers(baselayer);
    }

    public void addClickListener(LeafletClickListener listener) {
        addListener(LeafletClickEvent.class, listener,
                LeafletClickListener.METHOD);
    }

    public void removeClickListener(LeafletClickListener listener) {
        removeListener(LeafletClickEvent.class, listener,
                LeafletClickListener.METHOD);
    }

    public void addMoveEndListener(LeafletMoveEndListener moveEndListener) {
        addListener("moveend", LeafletMoveEndEvent.class, moveEndListener,
                LeafletMoveEndListener.METHOD);
    }

    public void removeMoveEndListener(LeafletMoveEndListener moveEndListener) {
        removeListener("moveend", LeafletMoveEndEvent.class, moveEndListener);
    }

    public void setCenter(Bounds bounds) {
        setCenter(bounds.getCenter());
    }

    public void setCenter(Point center) {
        getState().center = center;
    }

    public Integer getZoomLevel() {
        return getState().zoomLevel;
    }

    public void zoomToExtent(Bounds bounds) {
        getState().center = bounds.getCenter();
        getState().zoomToExtent = bounds;
    }

    public void setControls(List<Control> values) {
        if (values == null) {
            values = new ArrayList<Control>();
        }
        getState().controls = values;
    }

	public Point getCenter() {
		return getState(false).center;
	}

}