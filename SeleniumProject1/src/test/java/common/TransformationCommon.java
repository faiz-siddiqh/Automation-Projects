package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Faiz-Siddiqh
 *
 */
public class TransformationCommon {

	private Properties projectDetails = new Properties();
	private WebDriver driver;
	private String driverLocation;
	private Document doc;
	private XPath xpath;
	private XPathExpression expr;
	private static XSSFWorkbook ExcelWBook;
	private static XSSFSheet ExcelWSheet;
	public static XSSFCell Cell;
	public static XSSFRow Row;

	/**
	 * Compulsory call this method from testclass to setUp the driver and locators
	 * file.
	 */
	public void setUp() throws SAXException, IOException {
		setUpDriver();
		setUpLocatorsFile();
	}

	public void setUpDriver() {
		// Load the properties file using this method which contains baseURL and
		// WebDriverType
		try {
			projectDetails.load(new FileReader(System.getProperty("user.dir") + "//Project//project.properties"));
			String driverName = projectDetails.getProperty("driver");
			String baseURL = projectDetails.getProperty("baseURL");

			if (driverName.equalsIgnoreCase("ChromeDriver")) {
				// Set System Property to instantiate ChromeDriver with the path of
				// chromedriver.
				driverLocation = projectDetails.getProperty("chromedriver");
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + driverLocation);
				// Set Options using for chrome using the below commented line

				// ChromeOptions options = new ChromeOptions();
				driver = new ChromeDriver();
			} else if (driverName.equalsIgnoreCase("FireFox")) {
				// Set System Property to instantiate ChromeDriver with the path of
				// firefoxdriver.
				driverLocation = projectDetails.getProperty("chromedriver");
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + driverLocation);

				// Set Options using for Firefox
				FirefoxProfile profile = new FirefoxProfile();
				FirefoxOptions options = new FirefoxOptions();
				options.setProfile(profile);

				driver = new FirefoxDriver();
			}
			driver.get(baseURL);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		} catch (IOException e) {
			System.out.println("Properties file not found");
			e.printStackTrace();
		}

	}

	public void setUpLocatorsFile() throws SAXException, IOException {
		String locatorsFileLocation = projectDetails.getProperty("locatorsfile");
		File file = new File(System.getProperty("user.dir") + locatorsFileLocation);

		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = documentFactory.newDocumentBuilder();
			doc = builder.parse(file);
			XPathFactory xpathFactory = XPathFactory.newInstance();
			xpath = xpathFactory.newXPath();
		} catch (ParserConfigurationException e) {
			System.out.println("Xml file parsing failed");
			e.printStackTrace();
		}

	}

	/**
	 * To return WebElement of a field using locator
	 * 
	 * @param locator
	 * @param type    -
	 * @return the WebElement found by described type.
	 * 
	 */
	public WebElement getElement(String locator, String type) {

		type = type.toLowerCase();

		if (type.equals("id")) {
			return this.driver.findElement(By.id(locator));
		} else if (type.equals("xpath")) {
			return this.driver.findElement(By.xpath(locator));
		} else if (type.equals("cssselector")) {
			return this.driver.findElement(By.cssSelector(locator));
		} else if (type.equals("name")) {
			return this.driver.findElement(By.name(locator));
		} else if (type.equals("classname")) {
			return this.driver.findElement(By.className(locator));
		} else {
			System.out.println("Locator not supported");
			return null;

		}
	}

	public WebElement getElementByXpath(String locator) {

		return driver.findElement(By.xpath(locator));
	}

	public List<WebElement> getElements(String locator, String type) {

		type = type.toLowerCase();

		if (type.equals("id")) {
			return driver.findElements(By.id(locator));
		} else if (type.equals("classname")) {
			return driver.findElements(By.className(locator));
		} else if (type.equals("xpath")) {
			return this.driver.findElements(By.xpath(locator));
		} else if (type.equals("cssselector")) {
			return this.driver.findElements(By.cssSelector(locator));
		} else if (type.equals("name")) {
			return this.driver.findElements(By.name(locator));
		} else {
			System.out.println("Locator not supported ,Check the type");
			return null;
		}

	}

	public List<WebElement> getElementsByTagname(WebElement element, String tagname) {

		return element.findElements(By.tagName(tagname));

	}

	public void findElementAndClick(List<WebElement> list, String requiredText) {

		for (WebElement eachElement : list) {
			if (eachElement.getText().contains(requiredText)) {
				clickAndWait(eachElement);
				break;
			}
		}

	}

	public void clickAndWait(WebElement element) {
		element.click();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	public void clickAndTypeAndWait(WebElement element, String keysToSend) {

		clickAndWait(element);
		element.sendKeys(keysToSend);
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
	}

	/*
	 * This getLocator(String locatorname) method can be used when the xml locator
	 * file is in the format <element name="locatorname" >xpath</element> this
	 * method is for locators1.xml file in LocatorsMapping folder
	 */

	/**
	 * @author Faiz-Siddiqh
	 * @param locatorname
	 * @return locator
	 * 
	 *         The xml tag should hav unique "name"attribute.else the below method
	 *         fails
	 * 
	 *         if null pointer exception check the param value
	 */
//	public String getLocator(String locatorname) {
//
//		try {
//			expr = xpath.compile("//element[@name='" + locatorname + "']");
//
//			NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//			return nodeList.item(0).getTextContent();
//
//			/*
//			 * Or
//			 * 
//			 */
//			// return expr.evaluate(doc, XPathConstants.STRING).toString();
//
//		} catch (XPathExpressionException e) {
//			System.out.println("Check the locatorname input value");
//			e.printStackTrace();
//		}
//
//		return null;
//	}

	public void selectDateFromCalender(String date, WebElement element) {

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date expectedDate = dateFormat.parse(date);

			String day = new SimpleDateFormat("dd").format(expectedDate);
			String month = new SimpleDateFormat("MMMM").format(expectedDate);
			String year = new SimpleDateFormat("yyyy").format(expectedDate);
			String idMonth = new SimpleDateFormat("MM").format(expectedDate);

			String expectedMonthYear = month + " " + year;
			String locatorForDate = year + idMonth + day;
			while (true) {

				String displayDate = element.getText();

				if (expectedMonthYear.equals(displayDate)) {
					clickAndWait(getElement("fare_" + locatorForDate, "id"));
					break;
				} else if (expectedDate.compareTo(currentDate) > 0) {
					clickAndWait(getElement("//span[contains(@class,'DayPicker-NavButton--next')]", "xpath"));
				} else {
					clickAndWait(getElement("//span[contains(@class,'DayPicker-NavButton--prev')]", "xpath"));
				}
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/*
	 * This getLocator(String locatorname) method can be used when the xml locator
	 * file is in the format <element name="locatorname" locator="xpath"/> that is
	 * in locators2.xml in LocatorMapping folder
	 */

	public void dragAndDrop(WebElement fromElement, WebElement toElement) {

		Actions action = new Actions(driver);
		// 1)
		action.dragAndDrop(fromElement, toElement).build().perform();

		/*
		 * 2)
		 * action.clickAndHold(fromElement).moveToElement(toElement).build().perform();
		 */
	}

	public void slider(WebElement sliderElement, int xOffset, int yOffset) {

		Actions action = new Actions(driver);

		action.dragAndDropBy(sliderElement, xOffset, yOffset).perform();
	}

	public String getLocator(String locatorname) {

		try {
			expr = xpath.compile("//element[@name='" + locatorname + "']/@*");
			NodeList result = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

			Attr attr = (Attr) result.item(0);
			return attr.getNodeValue();
			// return attr.getTextContent();

		} catch (XPathExpressionException e) {
			System.out.println("check the locatorname input value");
			e.printStackTrace();
		}

		return null;
	}

	public void selectFromDropdown(WebElement element, String textToBeSelected) {

		Select select = new Select(element);
		select.selectByVisibleText(textToBeSelected);

	}

	public static void setExcelFile(String path, String sheetName) throws Exception {
		try {
			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(path);

			// Access the excel data sheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(sheetName);
		} catch (Exception e) {
			throw (e);
		}
	}

	public static String[][] getTestData(String tableName) {
		String[][] testData = null;

		try {
			// Handle numbers and strings
			DataFormatter formatter = new DataFormatter();
			// BoundaryCells are the first and the last column
			// We need to find first and last column, so that we know which rows to read for
			// the data
			XSSFCell[] boundaryCells = findCells(tableName);
			// First cell to start with
			XSSFCell startCell = boundaryCells[0];
			// Last cell where data reading should stop
			XSSFCell endCell = boundaryCells[1];

			// Find the start row based on the start cell
			int startRow = startCell.getRowIndex() + 1;
			// Find the end row based on end cell
			int endRow = endCell.getRowIndex() - 1;
			// Find the start column based on the start cell
			int startCol = startCell.getColumnIndex() + 1;
			// Find the end column based on end cell
			int endCol = endCell.getColumnIndex() - 1;

			// Declare multi-dimensional array to capture the data from the table
			testData = new String[endRow - startRow + 1][endCol - startCol + 1];

			for (int i = startRow; i < endRow + 1; i++) {
				for (int j = startCol; j < endCol + 1; j++) {
					// testData[i-startRow][j-startCol] =
					// ExcelWSheet.getRow(i).getCell(j).getStringCellValue();
					// For every column in every row, fetch the value of the cell
					Cell cell = ExcelWSheet.getRow(i).getCell(j);
					// Capture the value of the cell in the multi-dimensional array
					testData[i - startRow][j - startCol] = formatter.formatCellValue(cell);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Return the multi-dimensional array
		return testData;
	}

	public static XSSFCell[] findCells(String tableName) {
		DataFormatter formatter = new DataFormatter();
		// Declare begin position
		String pos = "begin";
		XSSFCell[] cells = new XSSFCell[2];

		for (Row row : ExcelWSheet) {
			for (Cell cell : row) {
				// if (tableName.equals(cell.getStringCellValue())) {
				if (tableName.equals(formatter.formatCellValue(cell))) {
					if (pos.equalsIgnoreCase("begin")) {
						// Find the begin cell, this is used for boundary cells
						cells[0] = (XSSFCell) cell;
						pos = "end";
					} else {
						// Find the end cell, this is used for boundary cells
						cells[1] = (XSSFCell) cell;
					}
				}
			}
		}
		// Return the cells array
		return cells;
	}

	/**
	 * @author Faiz-Siddiqh Read the testData from an Excel File
	 * @params -RowNum and ColNum
	 */

	public static String getCellData(int RowNum, int ColNum) throws Exception {

		try {
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			String cellData = Cell.getStringCellValue();
			return cellData;
		} catch (Exception e) {
			// throw e;
			// return "The Cell is EMPTY";
			return " ";
		}

	}

	/*
	 * Read the test Data of DateType from the excel file
	 * 
	 * @params --- RowNum and ColNum
	 */

	public static String getDateCellData(int RowNum, int ColNum) throws Exception {

		try {
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			DateFormat df = new SimpleDateFormat("dd/mm/yyyy");

			Date dateValue = Cell.getDateCellValue();
			String dateStringFormat = df.format(dateValue);
			return dateStringFormat;

		} catch (Exception e) {
			return " ";
		}

	}

	/*
	 * Write to the Excel cell
	 * 
	 * @params-Row num and Col Num
	 * 
	 */

	public static void setCellData(int RowNum, int ColNum, String dataToBeWritten, String filePath, String filename)
			throws Exception {
		try {
			Row = ExcelWSheet.getRow(RowNum);
			Cell = Row.getCell(ColNum);
			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(dataToBeWritten);
			} else {
				Cell.setCellValue(dataToBeWritten);
			}

			// Open a file to write the results or data

			FileOutputStream fileOut = new FileOutputStream(filePath + filename);
			ExcelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();

		} catch (Exception e) {
			throw e;
		}

	}

	/*
	 * OverLoading the above method for if the data is of double dataType
	 * 
	 * @params -- double Data,int RowNum,int ColNum
	 * 
	 */

	public static void setCellData(int RowNum, int ColNum, double Data, String filePath, String filename)
			throws Exception {
		try {
			Row = ExcelWSheet.getRow(RowNum);
			Cell = Row.getCell(ColNum);

			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(Data);
			} else {
				Cell.setCellValue(Data);
			}

			// Open a file to write the results or data

			FileOutputStream fileOut = new FileOutputStream(filePath + filename);
			ExcelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();

		} catch (Exception e) {
			throw e;
		}

	}

	public static String takeScreenshot(WebDriver driver, String fileName) throws Exception {

		fileName = fileName + ".png";
		String directory = System.getProperty("user.dir") + "//TestResults//";

		File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(sourceFile, new File(directory + fileName));
		driver.quit();
		String destination = directory + fileName;
		return destination;
	}

	public static String getRandomString(int length) {
		StringBuilder sb = new StringBuilder();
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

		for (int i = 0; i < str.length(); i++) {
			int index = (int) (Math.random() * str.length());
			sb.append(str.charAt(index));
		}

		return sb.toString();
	}

	public void afterMethod() {
		driver.quit();

	}

}
