/**
 * SonarQube Xanitizer Plugin
 * Copyright 2012-2016 by RIGS IT GmbH, Switzerland, www.rigs-it.ch.
 * mailto: info@rigs-it.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Created on October 2, 2015
 */
package com.rigsit.xanitizer.sqplugin;

import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.plugins.java.Java;

import com.rigsit.xanitizer.sqplugin.metrics.XanitizerMetrics;

/**
 * @author rust
 * 
 */
@Properties({ @Property(key = XanitizerSonarQubePlugin.XAN_XML_REPORT_FILE,

		name = "Xanitizer XML Report File",

		// HTML format.
		description = "The Xanitizer XML report file from which to read the findings, eiter relative to the "
				+ "sonar-project.properties file or as an absolute path.</BR></BR>"
				+ " Generated by Xanitizer, either from the GUI "
				+ "'Reporting > Generate Findings List Report...', and choosing the XML output format,"
				+ " or by running headless, for example using ANT.",

		defaultValue = "Xanitizer-Findings-List.xml",

		// This is a project-level property.
		global = false, project = true

		) })

public class XanitizerSonarQubePlugin implements Plugin {
	
	public static final String SUPPORTED_LANGUAGES[] = {
            Java.KEY, "scala", "xml"
    };

	public static final String XAN_XML_REPORT_FILE = "sonar.xanitizer.xmlReportFile";

	@Override
	public void define(Context context) {
		context.addExtensions(XanitizerRulesDefinition.class,

				XanitizerSensor.class,

				XanitizerMetrics.class);
	}
}
