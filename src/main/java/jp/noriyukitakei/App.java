package jp.noriyukitakei;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.bonigarcia.wdm.WebDriverManager;

public class App {
    public static void main(String[] args) throws MalformedURLException
    {
        Logger logger = LoggerFactory.getLogger(App.class);

        try {
            // インストールされているブラウザのバージョンに対応したWebDriverを
            // 自動的にダウンロードします。
            WebDriverManager.chromedriver().setup();

            // オプションを指定します。
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--no-sandbox");

            while(true) {
                WebDriver driver = new ChromeDriver(options);

                try {
                    // ログイン画面にアクセスする。
                    driver.get("https://hoge.example.com/login.php");

                    // ユーザーIDのテキストボックスが表示されるまで10秒待つ。
                    WebDriverWait wait = new WebDriverWait(driver, 10);
                    WebElement element01 = wait.until(ExpectedConditions.
                                    presenceOfElementLocated(By.id("username")));

                    // ユーザー名をテキストボックスにセットする。
                    element01.sendKeys("test001");

                    // パスワードをテキストボックスにセットする。
                    // 本番ではAzure Key Vaultを利用する等セキュアな構成にして下さい。
                    WebElement element02 = driver.findElement(By.id("password"));
                    element02.sendKeys("password");

                    // ログインボタンをクリックする。
                    WebElement element03 = driver.findElement(By.id("login"));
                    element03.click();

                    // ログイン後の画面が表示されるまで5秒待つ。
                    // WebDriverWaitとExpectedConditionsでログイン後の画面にユーザーIDが表示されるまで
                    // 待とうかと思ったのだがうまくいかず、、、普通にSleepしてる。
                    TimeUnit.SECONDS.sleep(5);

                    // ログイン後の画面に表示されたユーザーIDが期待したものかどうかをチェックしています。
                    if (driver.findElement(By.tagName("body")).getText().equals("test001")) {
                        logger.info("Login Succeeded");
                    } else {
                        logger.error("Login Failed");
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(),e);
                    logger.error("Login Failed");
                } finally {
                    // 念の為Cookieを全て消してからブラウザを終了しています。
                    driver.manage().deleteAllCookies();
                    driver.quit();

                    // 次の監視間隔まで30秒待っています。
                    TimeUnit.SECONDS.sleep(30);
                }
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
