package projava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WebClient {
    public static void main(String[] args) throws IOException {
        var domain = "example.com";
        //example.comの80番ポートに接続
        try (var soc = new Socket(domain, 80);
             //PrintWriterクラス、BufferedReaderクラスではそれぞれ1行単位での入出力が可能
             var pw = new PrintWriter(soc.getOutputStream());
             //InputStreamから直接BufferedReaderオブジェクトを生成できないので、InputStreamReaderクラスを経由
             var isr = new InputStreamReader(soc.getInputStream());
             var bur = new BufferedReader(isr))
        {
            /*
            まずはリクエストを送信
             送信しているリクエスト「HTTPメソッド（GET/POST）　リソース（操作するデータ）　HTTP/バージョン」
            */
            pw.println("GET /index.html HTTP/1.1");
            /*
            リクエストの詳細情報を表すリクエストヘッダー（Host・・・アクセスしているドメイン）
            必ず指定が必要（サーバー側からはどのドメインからアクセスされたのか分からないため）
            */
            pw.println("Host: " + domain);
            //空行を送ることでリクエストは終了となり、レスポンス待ちになる
            pw.println();
            /*
            ソケットへの出力はある程度まとまってから一気に送信する仕組み
            →flushメソッドで強制的に送信
            */
            pw.flush();
            //readLineメソッドで1行入力が出来るが、今回はlinesメソッドで文字列のStreamを得ている
            bur.lines()
                    //受け取った文字列の18行分を表示
                    .limit(18)
                    .forEach(System.out::println);

        }

    }
}


//出力結果
//1行目・・・「HTTP/バージョン　ステータスコード　メッセージ」
//　→見るのはステータスコードのみでOK（200＝正常に終了）
//2行目以降・・・レスポンスの詳細情報（レスポンスヘッダーとして送信される）
//　→「Content-Type」＝ドキュメントの種類
//　　「Last-Modified」＝　最終更新日時
//　　「Content-Length」＝データサイズ
//空行でレスポンスヘッダーが終わり、それ以降がドキュメント本体
//今回はHTMLが送られている、これを解釈して画面を構築する
