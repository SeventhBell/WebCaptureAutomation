package jp.captureAutomationPrj.core;

import java.io.File;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.*;

import jp.captureAutomationPrj.constant.CaptureProperties;
import jp.captureAutomationPrj.util.CaptureUtil;
import jp.captureAutomationPrj.util.UrlManager;

/**
 * <p>テストキャプチャの簡略化プログラム</br>
 * キャプチャ撮るだけなので、Java通常アプリとして作成。</br>
 * (普通に機能テストするならユニットテストを作成すること)
 * </p>
 * @author fujita takashi
 *
 */
public class App {
	public static void main(String[] args) {
		try {
			// プロパティ取得
			CaptureProperties cp = CaptureProperties.getInstance();
			Properties p = cp.getProperties();
			// キャプチャユーティリティ
			CaptureUtil capUtil = new CaptureUtil();

			// exePath設定
			// ChromeとIEは設定しないとエラーになる
			if (p.getProperty(CaptureProperties.BROESER).equals("ie") || p.getProperty(CaptureProperties.BROESER).equals("chrome")) {
				File file = new File("resource/" + p.getProperty(CaptureProperties.EXE_NAME) + ".exe");
				System.setProperty("webdriver." + p.getProperty(CaptureProperties.BROESER) + ".driver", file.getAbsolutePath());
			}

			// 対象ブラウザのドライバーを設定
			Class driverClass = Class.forName(p.getProperty(CaptureProperties.DRIVER_NAME));
			WebDriver driver = (WebDriver) driverClass.newInstance();
			// Wait設定
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			// csvファイル
			File csvFile = new File(p.getProperty(CaptureProperties.CSV_ADDRESS));

			// キャプチャ保存先
			File saveAddress = new File(p.getProperty(CaptureProperties.SAVE_ADDRESS));
			if (!saveAddress.isDirectory()) {
				throw new Exception("保存先が存在しません。\n設定ファイルを再確認してください。");
			}

			// URLリスト
			UrlManager urls = new UrlManager(csvFile);
			// https通信時のインターセプター機能対策(初回のみ許可すれば後は通ると仮定する)
			//antiHttpsInterceptor(driver, urls.getUrlList().get(0).toString());

			for (URL url : urls.getUrlList()) {
				// 指定のURLへ接続する
				driver.get(url.toString());
				// 指定の属性を探す（見つからない場合、wait設定値だけ待つ）
				try {
					driver.findElement(By.className("footer"));
				} catch (Exception e) {
					e.printStackTrace();
				}

				// ファイル名
				String[] str = url.getFile().split("/");
				String fileName = str[str.length - 2] + ".png";

				// キャプチャ
				capUtil.driverCapture(driver, new File(saveAddress, "/Dpng/" + fileName));
				capUtil.desktopCaptureToPng(new File(saveAddress, "/png/" + fileName));
				// capUtil.desktopCaptureToJpeg(new File(saveAddress, "/jpg/" + fileName.replace("png", "jpeg")));

				// cookie削除
				driver.manage().deleteAllCookies();
			}
			// 終了
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>https通信& IE& サーバ証明書未設定の場合、別画面へインターセプトされる事への対策</br>
	 * 単純にインターセプター画面のリンクをクリック
	 * </p>
	 * @param driver
	 * @return
	 */
	public static boolean antiHttpsInterceptor(WebDriver driver, String url) {
		try {
			driver.get(url);
			driver.findElement(By.id("overridelink")).click();
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
