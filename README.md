# はじめに
 ベンダが公開している Security Advisory 等の脆弱性情報について、新規公開を確認するためのプログラムです。<BR>
 公開済みの Security Advisory が更新された場合には、検知できないことがあります。<BR>
 Jsoup を利用して、特定の箇所を比較しているため、更新を検知できないことがあるかもしれません。<BR>
 海外の製品をメインとしているので、１日１回実行すれば確認は十分です。<BR>

# チェックしている Security Advisory は以下になります。
未：実績なし、◎：実績あり、☓：バグがありチェックできていない <BR>
未 https://httpd.apache.org/security/vulnerabilities_24.html<BR>
◎ https://www.drupal.org/security<BR>
未 https://wordpress.org/news/category/security/<BR>
◎ https://kb.isc.org/docs/aa-00913<BR> [2021/4/13 修正]
◎ https://tomcat.apache.org/download-80.cgi<BR>
◎ https://tomcat.apache.org/download-90.cgi<BR>
未 https://www.oracle.com/security-alerts/<BR>
　※ 定例外のみチェックしています。 <BR>
◎ https://www.php.net/ChangeLog-7.php<BR>
未 https://www.postgresql.org/support/security/ [2021/4/13 修正]<BR>
　※ CVSS >= 7 のみチェックしています。 [2021/2/12 更新]
未 https://helpx.adobe.com/security.html [2021/4/13 修正]<BR>
　※ Adobe ColdFusion のみチェック、サイトの変更に対応 [2021/2/28]<BR>
◎ https://tools.cisco.com/security/center/publicationListing.x<BR>
◎ https://www.vmware.com/jp/security/advisories.html<BR>
◎ https://www.hitachi.co.jp/hirt/security/index.html<BR>
× https://www.openssl.org/news/vulnerabilities.html<BR>
 ※25-Mar-2021 のアップデート、チェックできず<BR>
未 https://www.nlnetlabs.nl/projects/unbound/security-advisories/<BR>
◎ https://cwiki.apache.org/confluence/display/WW/Security+Bulletins<BR>
未 https://security.paloaltonetworks.com/?severity=CRITICAL&severity=HIGH&sort=-date [2021/2/9 追加]<BR>

# gson-2.8.6.jar
 json を処理する際に利用しています。

# jsoup-1.13.1.jar
 html を処理する際に利用しています。<BR>
 https://jsoup.org/apidocs/org/jsoup/select/Selector.html<BR>
 
# 使い方
 初回実行時は、各ベンダのセキュリティアドバイザリを確認し、lastcheckdata.csv に初期状態を保存します。
 ２回目以降は、lastcheckdata.csv と各ベンダのセキュリティアドバイザリの差異を確認します。

# 確認する製品を増やしたい
 static int max_row=16; の数を増やす。
 public static void ChkAdv###(String[] conf_data) を追加する。

# lastcheckdata.csv 
 1. ID (特に使っていない)
 2. 製品名等 (メッセージダイアログ出すときにも使用しています）
 3. 実行時に取得した値
 4. 前回実行時に取得した値
 5. 行を更新した日
 6. 情報取得失敗時に利用

# chkVuls.jar ファイル
 jar ファイルに gson-2.8.6.jar, jsoup-1.13.1.jar を含めてエクスポートしています。
 java -jar chkVuls.jar

# Proxy 経由の接続について
 chkVuls_Proxy.jar に分けました。（テストは別環境でやるので、アップロード後すぐに動くかは運です。）<BR>
 差異がない場合は、メッセージダイアログが出ないようにしました。<BR>
 差異があるとダイアログが出るので、バッチ処理には向きません。<BR>
 jar ファイルに gson-2.8.6.jar, jsoup-1.13.1.jar を含めてエクスポートしています。<BR>
 java -jar chkVuls.jar<BR>
 ChkVuls.ini に、必要な情報を書き込んで試してください。<BR>

# Cisco の Security Advisory について
 json を分解して New をもつ identifier を比較してもうまく行かなかったので、
 取得できた json 内の日付を比較して最新バージョンの有無を比較しています。
 20個ぐらい、json に含まれているので、表示されている Security Advisory すべてが Update だと、
 上手に検出できません。※2021/2/4 に一部修正しました。
