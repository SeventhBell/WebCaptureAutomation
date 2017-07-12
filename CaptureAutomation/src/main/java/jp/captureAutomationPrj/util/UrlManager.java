package jp.captureAutomationPrj.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>URL集合クラス</br>
 * インスタンス作成時に指定ポイントのCSVファイルを読み込み、</br>
 * Listとしてメンバ変数に保有する。
 * </p>
 * @author fujita takashi
 *
 */
public class UrlManager {
	private List<URL> urlList;

	/**
	 * <p>コンストラクタ
	 * </p>
	 * @param csvFileAddress
	 * @throws Exception
	 */
	public UrlManager(File csvFileAddress) throws Exception {
		// テスト時は生成時に値を作成する。
		urlList = new ArrayList<URL>();
		loadUrl(csvFileAddress);
	}

	/**
	 * URLリストを取得
	 * @return
	 */
	public List<URL> getUrlList() {
		return urlList;
	}

	/**
	 * <p>CSV読み込み処理</br>
	 * ","と改行毎に1URLとして読み込み
	 * </p>
	 */
	public void loadUrl(File csvFileAddress) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(csvFileAddress));
			String line;
			List<String> splitTemp = new ArrayList<>();
			while ((line = br.readLine()) != null) {
				splitTemp = Arrays.asList(line.split(","));
				for (String url : splitTemp) {
					urlList.add(new URL(url));
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
