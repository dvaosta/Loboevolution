/*
    GNU GENERAL LICENSE
    Copyright (C) 2006 The Lobo Project. Copyright (C) 2014 - 2017 Lobo Evolution

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    verion 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General License for more details.

    You should have received a copy of the GNU General Public
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    

    Contact info: lobochief@users.sourceforge.net; ivan.difrancesco@yahoo.it
 */
package org.lobobrowser.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import org.lobobrowser.main.ExtensionManager;
import org.lobobrowser.ua.NavigatorFrame;

/**
 * The default window class used by the browser.
 *
 * @see WindowFactory
 */
public class DefaultBrowserWindow extends AbstractBrowserWindow {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The frame panel. */
    private final FramePanel framePanel;

    /** The window context. */
    private final NavigatorWindowImpl windowContext;

    /**
     * Instantiates a new default browser window.
     *
     * @param hasMenuBar
     *            the has menu bar
     * @param hasAddressBar
     *            the has address bar
     * @param hasToolBar
     *            the has tool bar
     * @param hasStatusBar
     *            the has status bar
     * @param windowContext
     *            the window context
     * @throws HeadlessException
     *             the headless exception
     */
    public DefaultBrowserWindow(boolean hasMenuBar, boolean hasAddressBar,
            boolean hasToolBar, boolean hasStatusBar,
            NavigatorWindowImpl windowContext) throws HeadlessException {
        // TODO: SECURITY: Security needed in this class to prevent removal of
        // all window components??
        this.windowContext = windowContext;
        this.framePanel = windowContext.getFramePanel();
        ExtensionManager.getInstance().initExtensionsWindow(windowContext);
        Object componentLock = windowContext.getComponentLock();
        if (hasMenuBar) {
            JMenuBar menuBar = new JMenuBar();
            this.setJMenuBar(menuBar);
            synchronized (componentLock) {
                for (JMenu menu : windowContext.getMenus()) {
                    menuBar.add(menu);
                }
                // Collection<JMenuItem> sharedMenuItems =
                // windowContext.getSharedMenuItems();
                // if(sharedMenuItems.size() > 0) {
                // JMenu extensionsMenu = new JMenu("Extensions");
                // menuBar.add(extensionsMenu);
                // for(JMenuItem menuItem : sharedMenuItems) {
                // extensionsMenu.add(menuItem);
                //}
                //}
            }
        }
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        if (hasAddressBar) {
            AddressBarPanel abp = new AddressBarPanel();
            contentPane.add(abp);
            synchronized (componentLock) {
                for (Component c : windowContext.getAddressBarComponents()) {
                    abp.add(c);
                }
            }
        }
        if (hasToolBar) {
            synchronized (componentLock) {
                for (Component c : windowContext.getToolBars()) {
                    contentPane.add(c);
                }
            }
            SharedToolBarPanel stbp = new SharedToolBarPanel();
            contentPane.add(stbp);
            synchronized (componentLock) {
                for (Component c : windowContext.getSharedToolbarComponents()) {
                    stbp.add(c);
                }
            }
        }
        contentPane.add(new FillerComponent(this.framePanel, false));
        if (hasStatusBar) {
            StatusBarPanel statusBar = new StatusBarPanel();
            contentPane.add(statusBar);
            synchronized (componentLock) {
                for (Component c : windowContext.getStatusBarComponents()) {
                    statusBar.add(c);
                }
            }
        }
    }

    /** Gets the top frame.
	 *
	 * @return the top frame
	 */
    public NavigatorFrame getTopFrame() {
        return this.framePanel;
    }

    /*
     * (non-Javadoc)
     * @see org.lobobrowser.gui.AbstractBrowserWindow#getTopFramePanel()
     */
    @Override
    public FramePanel getTopFramePanel() {
        return this.framePanel;
    }

    /*
     * (non-Javadoc)
     * @see org.lobobrowser.gui.AbstractBrowserWindow#getWindowCallback()
     */
    @Override
    public WindowCallback getWindowCallback() {
        return this.windowContext;
    }

    // private Image lastDbImage;
    // private Dimension lastDbSize;
    //
    // private Image getDoubleBufferingImage(Dimension size) {
    // Dimension lastDbSize = this.lastDbSize;
    // Image newImage = this.lastDbImage;
    // if(lastDbSize == null || lastDbSize.width != size.width ||
    // lastDbSize.height != size.height) {
    // newImage = this.createImage(size.width, size.height);
    // this.lastDbImage = newImage;
    // this.lastDbSize = size;
    //}
    // return newImage;
    //}

    /*
     * (non-Javadoc)
     * @see javax.swing.JFrame#update(java.awt.Graphics)
     */
    @Override
    public void update(Graphics g) {
        if (g instanceof Graphics2D) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        super.update(g);
        // Rectangle clipBounds = g.getClipBounds();
        // Dimension size = this.getSize();
        // Image dbi = this.getDoubleBufferingImage(size);
        // this.paint(dbi.getGraphics());
        // g.drawImage(dbi, 0, 0, size.width, size.height, clipBounds.x,
        // clipBounds.y, clipBounds.width, clipBounds.height, this);
    }
}
