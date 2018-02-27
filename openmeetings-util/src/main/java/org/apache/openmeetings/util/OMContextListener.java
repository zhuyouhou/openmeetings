/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openmeetings.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class OMContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
		String ctx = pathToName(event);
		System.setProperty("current_openmeetings_context_name", ctx);
		System.setProperty("webapp.contextPath", String.format("/%s", ctx));
		try {
			LoggerContext context = (LoggerContext)LoggerFactory.getILoggerFactory();
			JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(context);
			context.reset();
			configurator.doConfigure("logback-config.xml");
		} catch (JoranException je) {
			// StatusPrinter will handle this
		}
		//System.setProperty("logback.configurationFile", "logback-config.xml");
	}

	private static String pathToName(ServletContextEvent event) {
		String contextName = event.getServletContext().getContextPath().replaceAll("/", "");
		if ("".equals(contextName)) {
			contextName = "root";
		}
		return contextName;
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}
}
