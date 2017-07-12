package jp.captureAutomationPrj.util;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * キャプチャユーティリティ
 * 上書き可否設定は、メンバ変数overWriteへ
 * @author fujita takashi
 */
public class CaptureUtil {
	// 上書き 許可/禁止
	private boolean overWrite;
	// 上書き エラーメッセージ
	private final String overWriteMassage = "既に保存先のファイルが存在します。\nファイルの上書きは許可されていません。\n";

	/**
	 * <p>コンストラクタ:デフォルト設定
	 * <ul>
	 * 	<li>上書き:許可</li>
	 * </ul>
	 * 設定にはsetMethodを使用
	 * </p>
	 */
	public CaptureUtil() {
		overWrite = true;
	}

	/**
	 * <p>ドライバーを利用</br>
	 * 表示要素のみをキャプチャ（枠等は入らない）</br>
	 * 保存形式：png
	 * </p>
	 * @param driver
	 * @param saveFile 保存先
	 * @throws IOException
	 */
	public void driverCapture(WebDriver driver, File saveFile) throws IOException {
		if (!overWriteCheck(saveFile)) {
			throw new IOException(overWriteMassage + "保存先 : " + saveFile.getAbsolutePath());
		}
		File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(file, saveFile);
	}

	/**
	 * <p>java標準ライブラリを利用</br>
	 * メインディスプレイを丸ごとキャプチャ
	 * </p>
	 * @param saveFile
	 * @throws IOException
	 * @throws AWTException
	 * @throws HeadlessException
	 */
	private void desktopCapture(File saveFile, String imageType) throws IOException, HeadlessException, AWTException {
		if (!overWriteCheck(saveFile)) {
			throw new IOException(overWriteMassage + "保存先 : " + saveFile.getAbsolutePath());
		}
		BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image, imageType, saveFile);
	}

	/**
	 * <p>java標準ライブラリを利用</br>
	 * メインディスプレイを丸ごとキャプチャ</br>
	 * 保存形式：png
	 * </p>
	 * @param saveFile
	 * @throws IOException
	 * @throws AWTException
	 * @throws HeadlessException
	 */
	public void desktopCaptureToPng(File saveFile) throws IOException, HeadlessException, AWTException {
		desktopCapture(saveFile, "png");
	}

	/**
	 * <p>java標準ライブラリを利用</br>
	 * メインディスプレイを丸ごとキャプチャ</br>
	 * 保存形式：jpeg
	 * </p>
	 * @param saveFile
	 * @throws IOException
	 * @throws AWTException
	 * @throws HeadlessException
	 */
	public void desktopCaptureToJpeg(File saveFile) throws IOException, HeadlessException, AWTException {
		desktopCapture(saveFile, "jpeg");
	}

	/**
	 * <p>ファイルの上書きチェック
	 * </p>
	 * @param saveFile
	 * @return true 保存の続行を許可
	 * @return false 保存の続行を禁止
	 */
	private boolean overWriteCheck(File saveFile) {
		// 上書きが許可されてない && ファイルが既に存在する
		if (!isOverWrite() && saveFile.isFile()) {
			return false;
		}
		return true;
	}

	/**
	 * <p>上書きの許可/禁止設定
	 * </p>
	 * @param overWrite true:許可 false:禁止
	 */
	public void setOverWrite(boolean overWrite) {
		this.overWrite = overWrite;
	}

	/**
	 * <p>上書き許可/禁止の返却
	 * </p>
	 * @return
	 */
	private boolean isOverWrite(){
		return this.overWrite;
	}

}
