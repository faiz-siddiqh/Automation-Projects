package com.goibibo;

import java.io.IOException;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import common.TransformationCommon;

public class AppTest {
	private TransformationCommon common;

	@BeforeSuite
	public void beforeSuite() {
		common = new TransformationCommon();
	}

	@Test
	public void checkFromAndDestination() throws SAXException, IOException {

		// The data should be taken from .xlsx file. To be Implemented Soon
		String departureLocation = "Ben";
		String depature = "Bengaluru, India";
		String destination = "Del";
		String fullDestn = "Delhi, India";

		common.setUp();
		common.clickAndTypeAndWait(common.getElementByXpath(common.getLocator("homepage-from")), departureLocation);
		common.findElementAndClick(
				common.getElementsByTagname(common.getElement(common.getLocator("from-autosuggest"), "id"), "li"),
				depature);

		common.clickAndTypeAndWait(common.getElementByXpath(common.getLocator("homepage-to")), destination);
		common.findElementAndClick(
				common.getElementsByTagname(common.getElement(common.getLocator("from-autosuggest"), "id"), "li"),
				fullDestn);

//		common.afterMethod();

	}

	@Test(dependsOnMethods = { "checkFromAndDestination" })
	public void testDepartureDateAndReturnDate() {

		String departureDate = "12/03/2021";
		String returnDate = "15/04/2021";

		// click on departure date
		common.clickAndWait(common.getElementByXpath(common.getLocator("departureCalendar")));
		common.selectDateFromCalender(departureDate,
				common.getElementByXpath(common.getLocator("calendar-displaytext")));

		// click on return date
		common.clickAndWait(common.getElementByXpath(common.getLocator("returnCalendar")));
		common.selectDateFromCalender(departureDate,
				common.getElementByXpath(common.getLocator("calendar-displaytext")));

	}

}
