package prjChkVuls;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;							// Java ファイル読み書き用
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.google.gson.Gson;			// json 解析用

import prjChkVuls.ChkVuls.cisco_json;

import java.util.Date;					// 実行日付用
import javax.swing.*;					// メッセージ表示用


public class ChkVuls_Proxy {
	// チェック内容保存のための配列
		static int max_row=17;						// 2021/2/9 ★追加(修正) 
		static int max_col=6;						// 2021/1/19 修正
		static String data_file="lastcheckdata_proxy.csv";
		static String data_path=".";
		static String proxy_id="";
		static String proxy_pw="";
		static String proxy_url="";
		static int proxy_port=8080;
		static String config_file="ChkVuls.ini";	// 2021/1/17 追加

		public static void main(String[] args) throws IOException {
			
//			// 本プログラムの実行確認
//			int retval = JOptionPane.showConfirmDialog
//			(
//			null,
//			"実行しますか？",
//			"確認",
//			JOptionPane.OK_CANCEL_OPTION
//			);

//			// retval
//			System.out.println(retval);	

//			// アプリをここで止める。
//			if (retval == JOptionPane.OK_CANCEL_OPTION) {
//				System.out.println("アプリ止める");	
//				System.exit(0);
//				}

			// 2021/1/17 追記 ここから
			// 初期設定ファイルの読込		
			String inifile[] = readFile_Config();
			
			if ((inifile[0] != "") && (inifile[3] != "")) {		
				if (inifile[3].chars().allMatch(Character::isDigit)) {
					proxy_id=inifile[0];
					proxy_pw=inifile[1];
					proxy_url=inifile[2];
					proxy_port=Integer.parseInt(inifile[3]);
				}
			}

//			// アプリをここで止める。
//			if (true) {System.exit(0);}
			
			// 脆弱性有無
			boolean blnChk = false;
			
			// 出力メッセージ
			String strMsg = "";

			// 今回のチェック日付を設定
			Date now = new Date();
	       DateFormat YYYYMMDD = new SimpleDateFormat("yyyy/MM/dd");
			
			// 日付の出力確認
			System.out.println(YYYYMMDD.format(now));	
			//if (true) {System.exit(0);}
			
	        // チェック内容保存のための配列
	        String[][] conf_data = new String[max_row][max_col];
	        String[][] conf_data_now = new String[max_row][max_col];

	        // 配列の初期化
	        for(int i=0; i< max_row; i++) {
	            for (int j=0; j<max_col; j++) {
	                conf_data[i][j]="";
	                conf_data_now[i][j]="";
	            }
	        }

	       // ファイルの読込
	      conf_data = readFile();

	      // Java 8 より 「認証が必要なプロキシ経由でのHTTPSアクセスを禁止」がデフォルト設定で Basic 認証が利用できない。設定変更する。
	      System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");

	      
	       // 個別に情報を確認
	       ChkAdv001(conf_data_now[0]);
	       ChkAdv002(conf_data_now[1]);
	       ChkAdv003(conf_data_now[2]);
	       ChkAdv004(conf_data_now[3]);
	       ChkAdv005(conf_data_now[4]);
			System.out.println(" 5 個チェック完了");	

			ChkAdv006(conf_data_now[5]);
			ChkAdv007(conf_data_now[6]);
	       ChkAdv008(conf_data_now[7]);
	       ChkAdv009(conf_data_now[8]);
	       ChkAdv010(conf_data_now[9]);
	       System.out.println("10 個チェック完了");	

	       ChkAdv011(conf_data_now[10]);		
			ChkAdv012(conf_data_now[11]);
	       ChkAdv013(conf_data_now[12]);
	       ChkAdv014(conf_data_now[13]);
	       ChkAdv015(conf_data_now[14]);
			System.out.println("15 個チェック完了");	

			ChkAdv016(conf_data_now[15]);
			ChkAdv017(conf_data_now[16]);		// 2021/2/9 ★追加(修正)
			System.out.println("全チェック完了");	

	       for(int i=0; i< max_row; i++) {
	            // 初回実行時の設定
	    	   if ((conf_data[i][2] == null) || conf_data[i][2].equals("") ) {
	    		   conf_data[i][4] = YYYYMMDD.format(now); 
	    		   conf_data[i][3] = conf_data_now[i][2];
	    		   conf_data[i][2] = conf_data_now[i][2];
	    		   conf_data[i][1] = conf_data_now[i][1];
	    		   conf_data[i][0] = conf_data_now[i][0];
	            }
	           // 取得した値が NULL の場合
	           else if (conf_data_now[i][2] == null || conf_data_now[i][2].equals("") ) { // 2021/1/19 修正
	               // ここを通ることはないはず。→情報取得失敗したときにここを通る
	        	   blnChk = true;															// 2021/1/19 追加
	        	   strMsg += "◎" + conf_data[i][1] +":" + conf_data_now[i][5] +"\n";	// 2021/1/19 追加
               	conf_data[i][5] = conf_data_now[i][5];							// 2021/1/19 追加
//	                	conf_data[i][4] = YYYYMMDD.format(now); 						// 2021/1/19 コメント
//	                	conf_data[i][3] = conf_data_now[i][3];							// 2021/1/19 コメント
//	                	conf_data[i][2] = conf_data_now[i][2];							// 2021/1/19 コメント
//	                	conf_data[i][1] = conf_data_now[i][1];							// 2021/1/19 コメント
//	                	conf_data[i][0] = conf_data_now[i][0];							// 2021/1/19 コメント
	           }
	           // 製品に新しい脆弱性が見つからなかった場合
	           else if (conf_data_now[i][2].equals(conf_data[i][2])) {
//	            		strMsg += "?" + conf_data[i][1] +":" + conf_data[i][2] + "\n";
	            		conf_data[i][3] = conf_data[i][2];
	            		conf_data[i][2] = conf_data_now[i][2];
	              	conf_data[i][1] = conf_data_now[i][1];
	              	conf_data[i][0] = conf_data_now[i][0];            
	           }
	           // 製品に新しい脆弱性が見つかったとき
	           else if (!conf_data_now[i][2].equals(conf_data[i][2])) {
	        	   blnChk = true;
	        	   strMsg += "◎" + conf_data[i][1] +":" + conf_data[i][2] + "->" + conf_data_now[i][2]+"\n";
	              conf_data[i][4] = YYYYMMDD.format(now);     // 新しい脆弱性が見つかれば最終変化日を更新
	              conf_data[i][3] = conf_data[i][2];
	              conf_data[i][2] = conf_data_now[i][2];
	              conf_data[i][1] = conf_data_now[i][1];
	              conf_data[i][0] = conf_data_now[i][0];
	            }
	        }

	        // 書込データの準備
	        String strTmp;
	        strTmp = "";
	        for(int i = 0; i < max_row; i++) {
	            strTmp += conf_data[i][0];
	            for(int j = 1; j < max_col ; j++) {
	                strTmp += "," + conf_data[i][j];
	            }
	            // 改行を追加
	          strTmp = strTmp + "\n";
	        }

	        // ファイルの書込
	        saveFile(strTmp);

			// 日付の出力確認
			System.out.println(strMsg);	

			// チェック対象が見つかったとき
			if (blnChk) {
				// チェック終了のダイアログを表示
				JOptionPane.showMessageDialog(
						null,strMsg, "一部製品で確認が必要です。",
						JOptionPane.INFORMATION_MESSAGE,
						null
				);					
			}
			// チェック対象がなかったとき
			else {
//				// チェック終了のダイアログを表示
//				JOptionPane.showMessageDialog(
//						null, "チェック対象はありませんでした。", "チェック終了",
//						JOptionPane.INFORMATION_MESSAGE,
//						null
//				);					
			}
		}


	    // ファイル保存
	    private static void saveFile(String strTmp) {
	        File file = new File(data_path, data_file);

	        // トライ&エラー
	        try (FileWriter writer = new FileWriter(file)) {
	            writer.write(strTmp);
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    // ファイル読込
	    private static String[][] readFile() {
	        // ファイルの行数をカウント
	        int line_index=-1;

	        // ファイルの情報を保存
	        String[][] conf_data = new String[max_row][max_col];

	        File file = new File(data_path, data_file);

	        try {
	            // ファイルがある場合
	            if (file.exists()) {
	                // BufferedReaderクラスのreadLineメソッドを使って1行ずつ読み込み表示する
	                FileReader fileReader = new FileReader(file);

	                BufferedReader bufferedReader = new BufferedReader(fileReader);

	                String strLine;

	                while ((strLine = bufferedReader.readLine()) != null) {
	                	line_index++;
//	                	System.out.println("Line:"+ line_index );
	                	if(line_index >= max_row) {
	                		break;
	                	}

	                	// 読み込んだファイルの内容を分割(最大 6項目)			//2021/1/19 修正
	                  String[] strCols = strLine.split(",", 6);		//2021/1/19 修正

//	              	System.out.println("Split");

	                    // 値の保存
	                  for(int i = 0; i < strCols.length ; i++) {
//	                  	System.out.println("Col:" + i);
	                     conf_data[line_index][i]=strCols[i];
	                        // 最大行数を超えた場合はループを抜ける
	                     if(i > max_col) break;
	                    }
	                  conf_data[line_index][5]="";		// 2021/1/19 修正
	                }

	                // 最後にファイルを閉じてリソースを開放する
	                bufferedReader.close();
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return conf_data;
	    }

		public static boolean isDouble(String num) {
		    try {
		        Double.parseDouble(num);
		        return true;
	       } catch (NumberFormatException e) {
		        return false;
		    }
		}
		
	    // 初期設定ファイル読込	// 2021/1/17 追加
	    private static String[] readFile_Config() {
	        // ファイルの情報を保存
	       String strLine;
	       String strTmp[] = new String[2];
	       String[] conf_data = new String[4];

	       // 初期化
	       for(int i = 0; i <= 3 ; i++) {
	    	   conf_data[i]="";
	       }

	        // 初期設定ファイルの読込
	       File file = new File(data_path, config_file);

//	       System.out.println("ファイル名:" + file.getAbsoluteFile());	

	       try {
	    	   // ファイルがある場合
	    	  if (file.exists()) {
	    		  // System.out.println("初期設定ファイルあります！");	

	    		  // BufferedReaderクラスのreadLineメソッドを使って1行ずつ読み込み表示する
	    		  FileReader fileReader = new FileReader(file);
	    		  BufferedReader bufferedReader = new BufferedReader(fileReader);
	    		  
	    		  while ((strLine = bufferedReader.readLine()) != null) {
	    			  // 読み込んだファイルの内容を分割(最大 2項目)
	    			  strTmp = strLine.split("=", 2);
	    			  
	    			  switch (strTmp[0]) {
	    				case "proxy_id":
	    					conf_data[0]=strTmp[1].trim();
	    				case "proxy_pw":
	    					conf_data[1]=strTmp[1].trim();
	    				case "proxy_url":
	    					conf_data[2]=strTmp[1].trim();
	    				case "proxy_port":
	    					conf_data[3]=strTmp[1].trim();
	    			  }  			  
	    		  }
	    		  // 最後にファイルを閉じてリソースを開放する
	    		  bufferedReader.close();
	    	  }

	       } catch (IOException e) {
	            e.printStackTrace();
	        }
	       return conf_data;
	    }
	    
	    // Apache HTTP Server
	  public static void ChkAdv001(String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // Apache HTTP　個別部分
	      conf_data[0]="01";
	      conf_data[1]="Apache HTTP";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://httpd.apache.org/security/vulnerabilities_24.html").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://httpd.apache.org/security/vulnerabilities_24.html").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("h1[id*=2.4]");
	          conf_data[2]=elements.get(0).id();
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // Drupal
	  public static void ChkAdv002( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // Drupal 個別部分
	      conf_data[0]="02";
	      conf_data[1]="Drupal";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://www.drupal.org/security").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://www.drupal.org/security").proxy(proxy_url,proxy_port).get();
	    	  	}
	    	  
	          Elements elements = document.select("a[href*=/sa-core]");
	          conf_data[2]=elements.get(0).attr("href");
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // WordPress
	  public static void ChkAdv003( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // WordPress　個別部分
	      conf_data[0]="03";
	      conf_data[1]="WordPress";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://wordpress.org/news/category/security/").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://wordpress.org/news/category/security/").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("td a[href*=https://wordpress.org/news/]");
	          conf_data[2]=elements.get(0).text();

	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // ISC BIND
	  public static void ChkAdv004( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // ISC BIND 個別部分
	      conf_data[0]="04";
	      conf_data[1]="ISC BIND";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://kb.isc.org/docs/aa-00913").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://kb.isc.org/docs/aa-00913").proxy(proxy_url,proxy_port).get();
	    	  	}
//            Elements elements = document.select("a[href*=http://cve.mitre.org/cgi-bin/cvename.cgi]");
                Elements elements = document.select("script");
    		 	String cvevalues[] = elements.get(5).html().split("\\&quot;",0);

              conf_data[2]="";
  			for(String cvetmp: cvevalues) {
  		        if (cvetmp.contains("cve"))
  		        {
  		            conf_data[2]=cvetmp;
  		            break;
  		        }
  			}
//                conf_data[2]=elements.get(0).text();
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // Tomcat 8
	  public static void ChkAdv005( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // Tomcat 8 個別部分
	      conf_data[0]="05";
	      conf_data[1]="Tomcat 8";

	      try{
	    	  if (proxy_url.equals("")){
//	    		  document = Jsoup.connect("https://tomcat.apache.org/security-8.html").get();
				  document = Jsoup.connect("https://tomcat.apache.org/download-80.cgi").get();
	    	  	} else {
//	      		  document = Jsoup.connect("https://tomcat.apache.org/security-8.html").proxy(proxy_url,proxy_port).get();
				  document = Jsoup.connect("https://tomcat.apache.org/download-80.cgi").proxy(proxy_url,proxy_port).get();
	    	  	}
//	          Elements elements = document.select("h3[id*=Fixed_in_Apache_Tomcat_8]");
 			   Elements elements = document.select("h3[id^=8]");
	          conf_data[2]=elements.get(0).text();
	      
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // Tomcat 9
	  public static void ChkAdv006( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // Tomcat 9 個別部分
	      conf_data[0]="06";
	      conf_data[1]="Tomcat 9";

	      try{
	    	  if (proxy_url.equals("")){
//	    		  document = Jsoup.connect("https://tomcat.apache.org/security-9.html").get();
	    		  document = Jsoup.connect("https://tomcat.apache.org/download-90.cgi").get();
	    	  	} else {
//	      		  document = Jsoup.connect("https://tomcat.apache.org/security-9.html").proxy(proxy_url,proxy_port).get();
	    		  document = Jsoup.connect("https://tomcat.apache.org/download-90.cgi").proxy(proxy_url,proxy_port).get();
	    	  	}
//	          Elements elements = document.select("h3[id*=Fixed_in_Apache_Tomcat_9]");
     		   Elements elements = document.select("h3[id^=9]");
	          conf_data[2]=elements.get(0).text();
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // Oracle Security Alert
	  public static void ChkAdv007( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // Oracle Security Alert　個別部分
	      conf_data[0]="07";
	      conf_data[1]="Oracle Security Alert";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://www.oracle.com/security-alerts/").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://www.oracle.com/security-alerts/").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("td a[href*=/security-alerts/alert]");
	          conf_data[2]=elements.get(0).text();
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // PHP7
	  public static void ChkAdv008( String[] conf_data) {
	      // 宣言
	      Document document;

	      // PHP7　個別部分
	      conf_data[0]="08";
	      conf_data[1]="PHP7";

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://www.php.net/ChangeLog-7.php").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://www.php.net/ChangeLog-7.php").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("section[class*=version]");
	          conf_data[2]=elements.get(0).id();
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // PostgreSQL
	  public static void ChkAdv009( String[] conf_data) {
	      // 宣言
	      Document document;

	      double cvssvalue;
	      Elements cvssTmps;
	      Elements cveTmps;
	      
	      // PostgreSQL 個別部分
	      conf_data[0]="09";
	      conf_data[1]="PostgreSQL";

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://www.postgresql.org/support/security/").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://www.postgresql.org/support/security/").proxy(proxy_url,proxy_port).get();
	    	  	}
      
	    	  	Elements elements = document.select("table[class=table table-striped] tbody tr");
				for(Element elmTmp: elements) {
//					cveTmps = elmTmp.select("a[href*=https://access.redhat.com/security/cve/]");
					cveTmps = elmTmp.select("a[href*=support/security/]");
					cvssTmps = elmTmp.select("a[href*=https://nvd.nist.gov/vuln-metrics/cvss/v3-calculator]");

					if (isDouble(cvssTmps.text())) {
						cvssvalue = Double.parseDouble(cvssTmps.text());
						if (cvssvalue >= 7 ) {
							conf_data[2]=cveTmps.get(0).text();
		  					break;
						}
					}
		        }
	      	      
	      
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // Adobe ColdFusion
	  public static void ChkAdv010( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // Adobe ColdFusion　個別部分
	      conf_data[0]="10";
	      conf_data[1]="Adobe ColdFusion";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://helpx.adobe.com/security.html").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://helpx.adobe.com/security.html").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("a[href*=https://helpx.adobe.com/security/products/coldfusion/]");

	          	if (elements.isEmpty() ) {
	                conf_data[5]="0000/00/00";
	          	}else {
	                conf_data[2]=elements.get(0).select("b").get(0).text();          		
	          	}

	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // Cisco
	  public static void ChkAdv011( String[] conf_data) {

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

		  // 宣言
	      String json;

	      String strTmp;
	      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'.'SZ");
	      Date date_new;
	      Date date_tmp;

	      SimpleDateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");
	      
	      // Gson の宣言
	      Gson gson = new Gson();

	      // Cisco　個別部分
	      conf_data[0]="11";
	      conf_data[1]="Cisco";
	      conf_data[2]="";

	      try{
	    	  if (proxy_url.equals("")){
	    		  json = Jsoup.connect("https://tools.cisco.com/security/center/publicationService.x?criteria=exact&limit=20&offset=0&publicationTypeIDs=1,3&securityImpactRatings=critical,high&sort=-day_sir").ignoreContentType(true).execute().body();
	    	  	} else {
	      		  json = Jsoup.connect("https://tools.cisco.com/security/center/publicationService.x?criteria=exact&limit=20&offset=0&publicationTypeIDs=1,3&securityImpactRatings=critical,high&sort=-day_sir").proxy(proxy_url,proxy_port).ignoreContentType(true).execute().body();
	    	  	}

	          // JSON文字列をJavaオブジェクトに変換する
	          cisco_json[] jsonArray = gson.fromJson(json, cisco_json[].class);

	          // 初期値を設定
	          strTmp = jsonArray[0].firstPublished;
	          date_new = dateFormat.parse(strTmp);
	          
	          for(int i=1; i< jsonArray.length; i++) {      	  
	        	  date_tmp = dateFormat.parse(jsonArray[i].firstPublished);
	        	  
	             if (date_new.before(date_tmp)){
	            	 date_new = date_tmp;
	             }
	           conf_data[2]=YYYYMMDD.format(date_new);
	           
	          }
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      } catch (ParseException e) {
	          conf_data[5]="情報取得失敗";
			e.printStackTrace();
	      }
	  }

	  // Cisco の Json 定義
	  public static class cisco_json {
	      public String identifier="";
	      public String title = "";				// 脆弱性のタイトル
	      public String version = "";				// 1.0とか
	      public String firstPublished = "";
	      public String lastPublished = "";
	      public String workflowStatus = "";		// null 以外もあるか？
	      public String id = "";					// id はあまり意味がなさそう
	      public String name = "";					// Cisco Security Advisory で固定
	      public String url = "";					// 詳細情報が乗っている
	      public String severity = "";			// critical or high で絞っている
	      public String status = "";				// New が対象
	      public String cwe = "";					// CWE-XXX,....
	      public String cve = "";					// CVE-XXXX,....
	      public String ciscoBugId = "";			// CiscoのバグID CXXX,CXXX
	      public String summary = "";				// サマリ
	      public String totalCount = "";			// 表示回数など
	      //		public String relatedResource = ""; // これがオブジェクトなのでエラーを吐く
	  }

	  // VMware
	  public static void ChkAdv012( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // VMware　個別部分
	      conf_data[0]="12";
	      conf_data[1]="VMware";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://www.vmware.com/jp/security/advisories.html").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://www.vmware.com/jp/security/advisories.html").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("a[aria-label*=VMSA]");
	          conf_data[2]=elements.get(0).text();
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // Hitachi
	  public static void ChkAdv013( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // Hitachi　個別部分
	      conf_data[0]="13";
	      conf_data[1]="Hitachi";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://www.hitachi.co.jp/hirt/security/index.html").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://www.hitachi.co.jp/hirt/security/index.html").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("dt");
	          conf_data[2]=elements.get(0).text();
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // OpenSSL
	  public static void ChkAdv014( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // OpenSSL　個別部分
	      conf_data[0]="14";
	      conf_data[1]="OpenSSL";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://www.openssl.org/news/vulnerabilities.html").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://www.openssl.org/news/vulnerabilities.html").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("a[href*=https://cve.mitre.org/cgi-bin/cvename.cgi]");
	          conf_data[2]=elements.get(0).text();
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // unbound
	  public static void ChkAdv015( String[] conf_data) {
	      // 宣言
	      Document document;

	      // unbound　個別部分
	      conf_data[0]="15";
	      conf_data[1]="unbound";

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://www.nlnetlabs.nl/projects/unbound/security-advisories/").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://www.nlnetlabs.nl/projects/unbound/security-advisories/").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("tbody tr td[class=field-body]");
	          conf_data[2]=elements.get(0).text();
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // Apache Struts
	  public static void ChkAdv016( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // Apache Struts　個別部分
	      conf_data[0]="16";
	      conf_data[1]="Apache Struts";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://cwiki.apache.org/confluence/display/WW/Security+Bulletins").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://cwiki.apache.org/confluence/display/WW/Security+Bulletins").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("a[href*=/confluence/display/WW/]");
	          conf_data[2]=elements.last().text();
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";	// 2021/1/19 修正
	      }
	  }

	  // PaloAlto 2021/2/9 ★追加
	  public static void ChkAdv017( String[] conf_data) {
	      // 宣言
	      Document document;

	      // プロキシのIDとパスワードをAuthenticator経由で渡す予定
	      Authenticator.setDefault(new Authenticator() {
	    	  @Override
	         protected PasswordAuthentication getPasswordAuthentication() {
	    		  return new PasswordAuthentication(proxy_id, proxy_pw.toCharArray());
	          }
	      	});

	      // PaloAlto　個別部分
	      conf_data[0]="17";
	      conf_data[1]="PaloAlto";

	      try{
	    	  if (proxy_url.equals("")){
	    		  document = Jsoup.connect("https://security.paloaltonetworks.com/?severity=CRITICAL&severity=HIGH&sort=-date").get();
	    	  	} else {
	      		  document = Jsoup.connect("https://security.paloaltonetworks.com/?severity=CRITICAL&severity=HIGH&sort=-date").proxy(proxy_url,proxy_port).get();
	    	  	}
	          Elements elements = document.select("table tr");
	          Elements cvssTmps = elements.get(1).select("a[href]");
			 	String lastdata[] = cvssTmps.text().split(" ",0);
	          conf_data[2]=lastdata[0];
	      } catch (IOException e) {
	          conf_data[5]="情報取得失敗";
	      }
	  }
	  // 2021/2/9 ★追加
}
