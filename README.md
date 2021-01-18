# はじめに
 ベンダが公開している Security Advisory 等の脆弱性情報について、新規公開を確認するためのプログラムです。
 公開済みの Security Advisory が更新された場合には、検知できないことがあります。
 Jsoup を利用して、特定の箇所を比較しているため、更新を検知できないことがあるかもしれません。

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

# ファイルの管理について
 git 使いこなせてないので、ファイル単位でアップロードしています。
