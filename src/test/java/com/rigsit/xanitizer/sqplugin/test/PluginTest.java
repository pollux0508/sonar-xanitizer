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
 * Created on 07.07.2016
 *
 */
package com.rigsit.xanitizer.sqplugin.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.sonar.api.SonarPlugin;
import org.sonar.api.config.Settings;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Repository;

import com.rigsit.xanitizer.sqplugin.XanitizerQualityProfile;
import com.rigsit.xanitizer.sqplugin.XanitizerRule;
import com.rigsit.xanitizer.sqplugin.XanitizerRulesDefinition;
import com.rigsit.xanitizer.sqplugin.XanitizerSensor;
import com.rigsit.xanitizer.sqplugin.XanitizerSonarQubePlugin;
import com.rigsit.xanitizer.sqplugin.metrics.XanitizerMetrics;
import com.rigsit.xanitizer.sqplugin.ui.XanitizerWidget;
import com.rigsit.xanitizer.sqplugin.util.SensorUtil;

/**
 * @author nwe
 *
 */
public class PluginTest {

	@Test
	@SuppressWarnings("rawtypes")
	public void testExtensions() {
		final SonarPlugin plugin = new XanitizerSonarQubePlugin();
		final List extensions = plugin.getExtensions();
		assertNotNull(extensions);

		assertTrue(extensions.contains(XanitizerSensor.class));
		assertTrue(extensions.contains(XanitizerWidget.class));
		assertTrue(extensions.contains(XanitizerMetrics.class));
		assertTrue(extensions.contains(XanitizerQualityProfile.class));
		assertTrue(extensions.contains(XanitizerRulesDefinition.class));
	}

	@Test
	public void testRules() {
		final RulesDefinition rulesDefinition = new XanitizerRulesDefinition();
		final RulesDefinition.Context context = new RulesDefinition.Context();
		rulesDefinition.define(context);

		final Repository repository = context
				.repository(XanitizerRulesDefinition.getRepositoryKey());
		assertNotNull(XanitizerRulesDefinition.getRepositoryKey(), repository);
		assertEquals(XanitizerRulesDefinition.getLanguageKey(), repository.language());

		for (XanitizerRule rule : XanitizerRule.values()) {
			assertNotNull(repository.rule(rule.toString()));
		}
	}

	@Test
	public void testSettings() {
		final Settings settings = new Settings();
		assertNull(SensorUtil.geReportFile(settings));

		settings.setProperty(XanitizerSonarQubePlugin.XAN_XML_REPORT_FILE, "");
		assertNull(SensorUtil.geReportFile(settings));

		settings.setProperty(XanitizerSonarQubePlugin.XAN_XML_REPORT_FILE, "doesNotExist.xml");
		assertNull(SensorUtil.geReportFile(settings));

		final String reportFileString = getClass().getResource("/webgoat-Findings-List.xml")
				.getFile();
		settings.setProperty(XanitizerSonarQubePlugin.XAN_XML_REPORT_FILE, reportFileString);
		final File reportFile = SensorUtil.geReportFile(settings);
		assertNotNull(reportFile);
		assertTrue(reportFile.isFile());
		assertEquals(new File(reportFileString), reportFile);
	}

}