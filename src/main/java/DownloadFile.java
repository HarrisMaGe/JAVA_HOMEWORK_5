import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by MYC on 2016/12/17.
 */
public class DownloadFile {
    //io流下载文件
    public void saveUrlAs(String Url, String fileName){
        URL url;
        DataOutputStream out = null;
        DataInputStream in = null;
        try {
            url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            in = new DataInputStream(connection.getInputStream());
            out = new DataOutputStream(new FileOutputStream(fileName));
            byte[] buffer = new byte[4096];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            try {
                if(out != null){
                    out.close();
                }
                if(in != null){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
