/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.httpservice.internal;

import com.liferay.httpservice.internal.event.EventUtil;
import com.liferay.httpservice.internal.http.PortalHttpContext;
import com.liferay.httpservice.internal.servlet.BundleServletContext;
import com.liferay.httpservice.internal.servlet.WebExtenderServlet;
import com.liferay.httpservice.servlet.BundleServletConfig;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 * @author Miguel Pastor
 * @author Raymond Augé
 */
public class HttpServiceActivator
	implements BundleActivator,
		ServiceTrackerCustomizer<ServletContext, ServletContext> {

	public ServletContext addingService(
		ServiceReference<ServletContext> serviceReference) {

		BundleContext bundleContext = getBundleContext();

		ServletContext servletContext = bundleContext.getService(
			serviceReference);

		ServletConfig servletConfig = new BundleServletConfig(
			servletContext, "Web Extender Servlet", null,
			new PortalHttpContext(servletContext));

		try {
			_webExtenderServlet = new WebExtenderServlet(bundleContext);

			_webExtenderServlet.init(servletConfig);

			_webPluginDeployer = new WebBundleDeployer(_webExtenderServlet);

			_startedBundleListener = new StartedBundleListener(
				_webPluginDeployer);
			_stoppedBundleListener = new StoppedBundleListener(
				_webPluginDeployer);

			bundleContext.addBundleListener(_startedBundleListener);
			bundleContext.addBundleListener(_stoppedBundleListener);
		}
		catch (Exception e) {
			_log.error(e, e);
		}

		checkStartableBundles();

		return servletContext;
	}

	public BundleContext getBundleContext() {
		return _bundleContext;
	}

	public void modifiedService(
		ServiceReference<ServletContext> serviceReference,
		ServletContext servletContext) {
	}

	public void removedService(
		ServiceReference<ServletContext> serviceReference,
		ServletContext servletContext) {

		_webPluginDeployer.close();
		_webExtenderServlet.destroy();

		BundleContext bundleContext = getBundleContext();

		bundleContext.removeBundleListener(_startedBundleListener);
		bundleContext.removeBundleListener(_stoppedBundleListener);

		_startedBundleListener = null;
		_stoppedBundleListener = null;
		_webExtenderServlet = null;
		_webPluginDeployer = null;
	}

	public void start(BundleContext bundleContext) throws Exception {
		_bundleContext = bundleContext;

		EventUtil.start(_bundleContext);

		Filter filter = bundleContext.createFilter(
			"(&(bean.id=" + ServletContext.class.getName() +
				")(original.bean=*))");

		_servletContextTracker =
			new ServiceTracker<ServletContext, ServletContext>(
				bundleContext, filter, this);

		_servletContextTracker.open();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		_servletContextTracker.close();

		EventUtil.close();

		_bundleContext = null;
		_servletContextTracker = null;
	}

	protected void checkStartableBundles() {
		for (Bundle bundle : getBundleContext().getBundles()) {
			String servletContextName =
				BundleServletContext.getServletContextName(bundle);

			if (Validator.isNotNull(servletContextName)) {
				try {
					_webPluginDeployer.doStart(bundle, servletContextName);
				}
				catch (Exception e) {
					_log.error(e, e);
				}
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(HttpServiceActivator.class);

	private BundleContext _bundleContext;
	private StartedBundleListener _startedBundleListener;
	private StoppedBundleListener _stoppedBundleListener;
	private WebExtenderServlet _webExtenderServlet;
	private ServiceTracker<ServletContext, ServletContext>
		_servletContextTracker;
	private WebBundleDeployer _webPluginDeployer;

}