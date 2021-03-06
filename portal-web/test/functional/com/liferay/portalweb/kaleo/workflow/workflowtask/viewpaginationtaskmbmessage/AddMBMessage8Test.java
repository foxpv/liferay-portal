/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portalweb.kaleo.workflow.workflowtask.viewpaginationtaskmbmessage;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class AddMBMessage8Test extends BaseTestCase {
	public void testAddMBMessage8() throws Exception {
		selenium.selectWindow("null");
		selenium.selectFrame("relative=top");
		selenium.open("/web/guest/home/");
		selenium.clickAt("//div[@id='dockbar']",
			RuntimeVariables.replace("Dockbar"));
		selenium.waitForElementPresent(
			"//script[contains(@src,'/aui/aui-editable/aui-editable-min.js')]");
		assertEquals(RuntimeVariables.replace("Go to"),
			selenium.getText("//li[@id='_145_mySites']/a/span"));
		selenium.mouseOver("//li[@id='_145_mySites']/a/span");
		selenium.waitForVisible("link=Control Panel");
		selenium.clickAt("link=Control Panel",
			RuntimeVariables.replace("Control Panel"));
		selenium.waitForPageToLoad("30000");
		selenium.clickAt("link=Message Boards",
			RuntimeVariables.replace("Message Boards"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("MB Category Name"),
			selenium.getText(
				"//tr[contains(.,'MB Category Name')]/td[2]/a/strong"));
		selenium.clickAt("//tr[contains(.,'MB Category Name')]/td[2]/a/strong",
			RuntimeVariables.replace("MB Category Name"));
		selenium.waitForPageToLoad("30000");
		selenium.clickAt("//input[@value='Post New Thread']",
			RuntimeVariables.replace("Post New Thread"));
		selenium.waitForPageToLoad("30000");
		selenium.type("//input[@id='_162_subject']",
			RuntimeVariables.replace("MB Category Thread8 Message Subject"));
		selenium.waitForVisible(
			"//a[contains(@class,'cke_button cke_button__unlink') and contains(@class,' cke_button_disabled')]");
		selenium.waitForVisible("//iframe[contains(@title,'Rich Text Editor')]");
		selenium.typeFrame("//iframe[contains(@title,'Rich Text Editor')]",
			RuntimeVariables.replace("MB Category Thread8 Message Body"));
		selenium.clickAt("//input[@value='Submit for Publication']",
			RuntimeVariables.replace("Submit for Publication"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace(
				"Your request completed successfully."),
			selenium.getText("//div[@class='portlet-msg-success']"));
		assertEquals(RuntimeVariables.replace(
				"MB Category Thread8 Message Subject"),
			selenium.getText("//div[@class='subject']/a/strong"));
		assertEquals(RuntimeVariables.replace("Status: Pending (Review)"),
			selenium.getText("//span[@class='workflow-status']"));
		assertEquals(RuntimeVariables.replace(
				"MB Category Thread8 Message Body"),
			selenium.getText("//div[@class='thread-body']"));
		selenium.clickAt("link=My Submissions",
			RuntimeVariables.replace("My Submissions"));
		selenium.waitForPageToLoad("30000");
		selenium.waitForVisible("link=Pending");
		selenium.clickAt("link=Pending", RuntimeVariables.replace("Pending"));
		selenium.waitForPageToLoad("30000");
		selenium.waitForVisible(
			"//tr[contains(.,'MB Category Thread8 Message Subject')]/td[1]/a");
		assertEquals(RuntimeVariables.replace("Single Approver"),
			selenium.getText(
				"//tr[contains(.,'MB Category Thread8 Message Subject')]/td[1]/a"));
		assertEquals(RuntimeVariables.replace(
				"MB Category Thread8 Message Subject"),
			selenium.getText(
				"//tr[contains(.,'MB Category Thread8 Message Subject')]/td[2]/a"));
		assertEquals(RuntimeVariables.replace("Message Boards Message"),
			selenium.getText(
				"//tr[contains(.,'MB Category Thread8 Message Subject')]/td[3]/a"));
		assertEquals(RuntimeVariables.replace("Review"),
			selenium.getText(
				"//tr[contains(.,'MB Category Thread8 Message Subject')]/td[4]/a"));
		assertTrue(selenium.isVisible(
				"//tr[contains(.,'MB Category Thread8 Message Subject')]/td[5]/a"));
		assertEquals(RuntimeVariables.replace("Never"),
			selenium.getText(
				"//tr[contains(.,'MB Category Thread8 Message Subject')]/td[6]/a"));
	}
}