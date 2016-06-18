package com.org;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.Assert;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.By;

/*
 * This is the class for Testing HttpBin Http requests and response
 */
public class TestHttpBin {
	public String baseURL = "http://httpbin.org/";
	public String postURL = "http://httpbin.org/forms/post";
	int responseCode = 0;
	WebDriver driver;

	/*
	 * Initializing the browser before the Test
	 */
	@BeforeTest
	public void launchBrowser() {
		
		driver = new HtmlUnitDriver();
	}

	/*
	 * To test the GET request
	 */
	@Test
	public void testHTTPGet() {
	
		driver.get(baseURL);
		String pageTitle = driver.getTitle();
		try {
			responseCode = GetResponse(baseURL);
		} catch (Exception e) {
			e.getMessage();
		}
		Assert.assertEquals(responseCode, 200);
	}
/*
 * To test the httpPOST 
 */
	@Test
	public void testHTTPPost() throws InterruptedException {

		driver.get(baseURL);
		driver.findElement(By.xpath("//body[@id='manpage']/div/ul/li[42]/a/code")).click();
		String url = driver.getCurrentUrl();
		driver.findElement(By.name("custname")).clear();
		driver.findElement(By.name("custname")).sendKeys("xxxxx");
		driver.findElement(By.name("custtel")).clear();
		driver.findElement(By.name("custtel")).sendKeys("126357");
		driver.findElement(By.name("topping")).click();
		driver.findElement(By.xpath("(//input[@name='size'])[3]")).click();
		driver.findElement(By.name("comments")).clear();
		driver.findElement(By.name("comments")).sendKeys("All is well");

		if (url.equals(postURL)) {

			driver.findElement(By.xpath("//button")).click();
			Thread.sleep(5000);

			try {
				responseCode = GetResponse(postURL);
			} catch (Exception e) {
				e.getMessage();
			}
		}
		Assert.assertEquals(responseCode, 200);

	}

	/*
	 * This method is for testing
	 */
	@Test
	public static void testMimeType() throws ClientProtocolException, IOException {

		HttpUriRequest request = new HttpGet("http://httpbin.org/ip");
		HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		Assert.assertEquals("application/json", ContentType.getOrDefault(httpResponse.getEntity()).getMimeType());
	}

	/*
	 * Method to close the browser
	 */
	@AfterTest
	public void closeBrowser() {
		driver.close();
	}

	/*
	 * This is the generic method for retrieving HttpResponse Code
	 */
	public static int GetResponse(String url) throws ClientProtocolException, IOException {
		final String USER_AGENT = "Mozilla/5.0";
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response = client.execute(request);
		int responseCode = response.getStatusLine().getStatusCode();
		return responseCode;
	}

}

