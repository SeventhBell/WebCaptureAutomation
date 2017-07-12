package jp.captureAutomationPrj.constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * <p>プロパティ＆コンスタントクラス</br>
 * プロパティ読み込みと定数設定と値保持</br>
 * DesignPattern:Singleton
 * </p>
 * @author fujita takashi
 *
 */
public final class CaptureProperties {
	// プロパティから呼び出す為のキー群
	// csvファイル
	public static final String CSV_ADDRESS = "csvAddress";
	// 保存先
	public static final String SAVE_ADDRESS = "saveAddress";
	// 対象ブラウザ
	public static final String BROESER = "browser";
	// exe
	public static final String EXE_NAME = "exeName";
	// ドライバー
	public static final String DRIVER_NAME = "driverName";

	// プロパティ
	private Properties properties = new Properties();

	// インスタンス
	private static final CaptureProperties instance = new CaptureProperties();

	/**
	 * コンストラクタ
	 */
	private CaptureProperties() {
		try {
			load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  インスタンス取得
	 * @return CaptureProperties
	 */
	public static CaptureProperties getInstance() {
		return CaptureProperties.instance;
	}

	// 設定ファイルのロード
	public void load() throws Exception {
		properties.clear();

		// プロパティファイルの場所
		File file = new File("config/application.properties");

		// ファイルの存在確認
		if (!file.isFile()) {
			// ファイルが存在しない場合は処理終了
			throw new FileNotFoundException(file.getAbsolutePath());
		}

		// ロード処理
		try (InputStreamReader in = new InputStreamReader(new FileInputStream(file), "utf-8")) {
			properties.load(in);
		}
	}

	/**
	 * プロパティ取得
	 * @return Properties
	 */
	public Properties getProperties() {
		return properties;
	}
}
