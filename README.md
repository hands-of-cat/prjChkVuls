# はじめに
 ベンダが公開している Security Advisory 等の脆弱性情報について、新規公開を確認するためのプログラムです。
 公開済みの Security Advisory が更新された場合には、検知できないことがあります。
 Jsoup を利用して、特定の箇所を比較しているため、更新を検知できないことがあるかもしれません。

# チェックしている Security Advisory は以下になります。
未：実績なし、◎：実績あり、☓：バグがありチェックできていない <BR>
未 https://httpd.apache.org/security/vulnerabilities_24.html<BR>
◎ https://www.drupal.org/security<BR>
未 https://wordpress.org/news/category/security/<BR>
未 https://kb.isc.org/docs/aa-00913<BR>
☓ https://tomcat.apache.org/security-8.html<BR>
☓ https://tomcat.apache.org/security-9.html<BR>
未 https://www.oracle.com/security-alerts/<BR>
  ※ 定例外のみチェックしています。 <BR>
◎ https://www.php.net/ChangeLog-7.php<BR>
未 https://www.postgresql.org/support/security/<BR>
未 https://helpx.adobe.com/security.html<BR>
　※ Adobe CodeFusion のみチェック <BR>
◎ https://tools.cisco.com/security/center/publicationListing.x<BR>
◎ https://www.hitachi.co.jp/hirt/security/index.html<BR>
未 https://www.openssl.org/news/vulnerabilities.html<BR>
未 https://www.nlnetlabs.nl/projects/unbound/security-advisories/<BR>
◎ https://cwiki.apache.org/confluence/display/WW/Security+Bulletins<BR>

# gson-2.8.6.jar
 json を処理する際に利用しています。

# jsoup-1.13.1.jar
 html を処理する際に利用しています。
 
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

# chkVuls.jar ファイル
 jar ファイルに gson-2.8.6.jar, jsoup-1.13.1.jar を含めてエクスポートしています。
 java -jar chkVuls.jar

# Proxy 経由の接続について
 chkVuls_Proxy.jar に分けました。
 差異がない場合は、メッセージダイアログが出ないようにコメントアウトしました。
 jar ファイルに gson-2.8.6.jar, jsoup-1.13.1.jar を含めてエクスポートしています。
 java -jar chkVuls.jar
 ChkVuls.ini に、必要な情報を書き込んで試してください。

# Cisco の Security Advisory について
 json を分解して New をもつ identifier を比較してもうまく行かなかったので、
 取得できた json 内の日付を比較して最新バージョンの有無を比較しています。
 20個ぐらい、json に含まれているので、表示されている Security Advisory すべてが Update だと、
 上手に検出できません。※2021/2/4 に一部修正しました。
