import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MYC on 2016/12/16.
 */
public class Crawler {
    OpenDB openDB = new OpenDB();
    //根据tid号爬取指定页数的数据，返回一个动态数组
    public List<String>  getJson(int tid,int pg) {
        try {
            HttpClient httpClient = HttpClients.createDefault();
            HttpGet req = new HttpGet("http://api.bilibili.com/archive_rank/getarchiverankbypartion?callback=?&type=jsonp&tid="+tid+"&pn="+pg);
            req.addHeader("Accept", "application/json,text/javascript, */*;q=0.01");
            req.addHeader("Accept-Encoding", "gzip, deflate, sdch");
            req.addHeader("Accept-Language", "zh-CN,zh;q=0.8");
            req.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
            req.addHeader("Content-Type", "text/html;charset=UTF-8");
            HttpResponse rep = httpClient.execute(req);
            HttpEntity repEntity = rep.getEntity();
            // JsonObject JO = (JsonObject)repEntity;
            //String str = repEntity.getContent();
            String content = EntityUtils.toString(repEntity);
            //System.out.println(content);
            List<String>jsonstr = new ArrayList<String>();
            int length  = content.length();
            if(length<100){
                return null;
            }else{

                int begin;
                int end;
                //将得到的content分割，得到一系列的json数组，以String格式存入动态数组
                begin = content.indexOf("\"aid\"");
                for(int i = 0;i<20;i++){
                    end = content.indexOf("\"aid\"", begin + 1);
                    if(i<9) {
                        if (end > 0) {
                            String str = content.substring(begin - 1, end - 6);
                            jsonstr.add(str);
                            begin = end;
                        } else {
                            break;
                        }
                    }else {
                        if (end > 0) {
                            String str = content.substring(begin - 1, end - 7);
                            jsonstr.add(str);
                            begin = end;
                        } else {
                            break;
                        }
                    }
                }

                //for(int i = 0;i<jsonstr.size();i++){
                //    System.out.println(jsonstr.get(i));
                // }

                return jsonstr;}

        } catch (IOException E) {
            return null;
        }
    }

    public void setInformation(){
        try {
            //tid从0开始，每个版块爬取前1000页
            for(int i = 0;i<200;i++){
                for(int j = 0;j<1000;j++){
                    List<String>jsonstr_temp = new ArrayList<String>();
                    jsonstr_temp = this.getJson(i,j);
                    Gson gson = new Gson();

                    if(jsonstr_temp==null){break;}//判断如果不足1000页，或者为空,跳出内层循环
                    else{
                        int length = jsonstr_temp.size();
                        List<ContentOfJson> jsonContent = new ArrayList<ContentOfJson>();
                        //Gson自带的映射方法，得到解析json后的数据
                        for(int s = 0;s<length;s++) {
                            ContentOfJson cont = gson.fromJson(jsonstr_temp.get(s), ContentOfJson.class);
                            jsonContent.add(cont);
                        }

                        //将解析后的数据存入数据库
                        for(int k =0;k<jsonContent.size();k++){
                            openDB.insertoInformation(jsonContent.get(k).getTid(),jsonContent.get(k).getTname(),jsonContent.get(k).getAid(),jsonContent.get(k).getTitle(),jsonContent.get(k).getAuthor(),jsonContent.get(k).stat.getCoin(),jsonContent.get(k).stat.getFavorite());
                            //System.out.printf(json_1.get(i).getAid()+"\t"+json_1.get(i).getTitle()+"\t"+json_1.get(i).getAuthor()+"\t"+json_1.get(i).stat.getCoin()+"\t"+json_1.get(i).stat.getFavorite()+"\n");
                        }
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//根据板块的tid号从数据库获取数据，筛选前三名，存入二维数组，第一维存储前三名的av号，第二维存储相应的视频名称
     public String[][] setAvnumber(int tid){

            String info[][] = openDB.selectfromInformation(tid);
            int length = info.length;
            if (length == 0) {
                return null;
            } else {
                String avnum[][]=new String[2][3];
                for (int i = 0; i < length; i++) {
                    avnum[0][i] = "av"+info[i][2];
                    avnum[1][i] = info[i][3];
                }
                return avnum;
            }

    }

    //使用Jsoup解析网页的html，通过下载列表里的id = firstLi,提取出av号对应的href链接
    public String downLoad(String avnumber){
        try {
                String downloadUrl = "http://www.ibilibili.com/video/"+avnumber;
                Document doc = Jsoup.connect(downloadUrl).get();
                Element content = doc.getElementById("firstLi");
                Elements links = content.select("a[href]");
                String url = links.get(2).attr("href");
            if(url.contains("javascript")){//非下载链接
                return null;
            } else{
                return url;
            }

        }catch(IOException E){
            E.printStackTrace();
            return null;
        }
    }

}

