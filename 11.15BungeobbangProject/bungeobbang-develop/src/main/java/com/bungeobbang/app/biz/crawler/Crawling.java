package com.bungeobbang.app.biz.crawler;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.bungeobbang.app.biz.product.ProductDTO;


public class Crawling {
    // Naver Shopping - 붕어빵 악세서리 검색
    private static final String URL = "https://search.shopping.naver.com/search/all?query=%EB%B6%95%EC%96%B4%EB%B9%B5%20%EC%95%85%EC%84%B8%EC%84%9C%EB%A6%AC";
    private static final String CSS_ELEMENTS_PRICE = "div > div.product_info_area__xxCTi > div.product_price_area__eTg7I > strong > span.price > span > em";
    private static final String CSS_ELEMENTS_IMAGE = "div > div.product_img_area__cUrko > div > a > img";
    private static final int SCROLL_AMOUNT = 100; // Scroll amount in pixels

    public static ArrayList<ProductDTO> findProductInfo() {
        System.out.println("log : crawling start");
        ArrayList<ProductDTO> datas = new ArrayList<>();
        List<WebElement> priceElements;
        List<WebElement> imageElements;
        
        // WebDriverManager for managing ChromeDriver
//        System.out.println("WebDriverManager setup");
//        WebDriverManager.chromedriver().setup();
        
        WebDriver driver = new ChromeDriver();
        System.out.println("JavascriptExecutor(실행자) create");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Load the webpage
        driver.get(URL);
        
        try {
            // Scroll and find elements
            while (true) {
                priceElements = driver.findElements(By.cssSelector(CSS_ELEMENTS_PRICE));
                imageElements = driver.findElements(By.cssSelector(CSS_ELEMENTS_IMAGE));

                js.executeScript("window.scrollBy(0, " + SCROLL_AMOUNT + ");");
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

                if (imageElements.size() >= 35) {
                    System.out.println("log: Crawling Elements find 35 up");
                    break;
                }
            }

            // Collect crawled information
            for (int index = 0; index < imageElements.size(); index++) {
                ProductDTO data = new ProductDTO();
                WebElement image = imageElements.get(index);
                
                // Populate ProductDTO
                data.setProductName(image.getAttribute("alt")); // Image description (product name)
                data.setProductProfileWay(image.getAttribute("src")); // Image URL
                data.setProductPrice(Integer.parseInt(priceElements.get(index).getText().replaceAll("[^\\d]", ""))); // Price
                datas.add(data);
            }
        } catch (NoSuchElementException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("log: Crawling Exception : " + e.getMessage());
            datas.clear();
        } catch (Exception e) {
            System.err.println("log: Crawling Exception : " + e.getMessage());
            datas.clear();
        } finally {
            // Close the page
            driver.quit();
        }
        return datas;
    }
}
