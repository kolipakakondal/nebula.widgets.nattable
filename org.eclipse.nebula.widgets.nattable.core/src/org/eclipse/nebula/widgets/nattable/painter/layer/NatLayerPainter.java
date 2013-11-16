/*******************************************************************************
 * Copyright (c) 2012 Original authors and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Original authors and others - initial API and implementation
 ******************************************************************************/
package org.eclipse.nebula.widgets.nattable.painter.layer;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.painter.IOverlayPainter;


public class NatLayerPainter implements ILayerPainter {

	private final NatTable natTable;

	public NatLayerPainter(NatTable natTable) {
		this.natTable = natTable;
	}
	
	@Override
	public void paintLayer(final ILayer natLayer, final GC gc, final int xOffset, final int yOffset,
			final Rectangle rectangle, final IConfigRegistry configRegistry) {
		try {
			paintBackground(natLayer, gc, xOffset, yOffset, rectangle, configRegistry);
			
			gc.setForeground(natTable.getForeground());
			
			ILayerPainter layerPainter = natTable.getLayer().getLayerPainter();
			final Rectangle natTableArea = new Rectangle(xOffset, yOffset, natLayer.getWidth(), natLayer.getHeight());
			if (rectangle.intersects(natTableArea)) {
				layerPainter.paintLayer(natLayer, gc, xOffset, yOffset, rectangle.intersection(natTableArea),
						configRegistry );
			}
			paintOverlays(natLayer, gc, xOffset, yOffset, rectangle, configRegistry);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.err.println("Error while painting table: " + e.getMessage()); //$NON-NLS-1$
		}
	}
	
	protected void paintBackground(ILayer natLayer, GC gc, int xOffset, int yOffset, Rectangle rectangle, IConfigRegistry configRegistry) {
		gc.setBackground(natTable.getBackground());

		// Clean Background
		gc.fillRectangle(rectangle);
	}
	
	protected void paintOverlays(ILayer natLayer, GC gc, int xOffset, int yOffset, Rectangle rectangle, IConfigRegistry configRegistry) {
		for (IOverlayPainter overlayPainter : natTable.getOverlayPainters()) {
			overlayPainter.paintOverlay(gc, natTable);
		}
	}

	@Override
	public Rectangle adjustCellBounds(int columnPosition, int rowPosition, Rectangle cellBounds) {
		ILayerPainter layerPainter = natTable.getLayer().getLayerPainter();
		return layerPainter.adjustCellBounds(columnPosition, rowPosition, cellBounds);
	}

}
