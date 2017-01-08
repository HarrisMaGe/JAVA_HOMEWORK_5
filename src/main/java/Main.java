import java.util.Scanner;

/**
 * Created by MYC on 2016/12/17.
 */
public class Main {
    public static void main(String[] args) {
        Crawler t = new Crawler();
//        t.setInformation();
        OpenDB openDB = new OpenDB();
        //循环查询数据库中tid号从0到200之间对应的视频信息
        for(int j = 0;j<200;j++){
            String info[][] = openDB.selectfromInformation(j);
            int length = info.length;
            if(length==0){
                continue;
            }else{
                System.out.println();
                System.out.println(j+"  版块对应的前三名为：");
                for(int i = 0;i<length;i++){
                    System.out.printf(info[i][0]+"\t"+info[i][1]+"\t"+info[i][2]+"\t"+info[i][3]+"\t"+info[i][4]+"\t"+info[i][5]+"\t"+info[i][6]+"\n");
                }
            }

        }
        //由用户输入需要下载的版块的tid号码，下载排行榜前三的视频

         while(true){

            System.out.println("每个版块的前三名已经列出，请根据最前面的版块号选择要下载的视频：");
            Scanner sc = new Scanner(System.in);
            String avnum[][] = t.setAvnumber(sc.nextInt());
            if (avnum == null) {
                System.out.println("该板块不存在，请确认输入无误后输入！");
            } else {
                int lengthOfAvnum = avnum[0].length;
                DownloadFile downloadFile = new DownloadFile();
                for (int i = 0; i < lengthOfAvnum; i++) {
                    String url = t.downLoad(avnum[0][i]);
                    if (url != null) {
                        System.out.println("开始下载  " + avnum[1][i]);
                        downloadFile.saveUrlAs(t.downLoad(avnum[0][i]), "F:\\" + avnum[1][i] + ".mp4");
                    } else {
                        System.out.println("开始下载  " + avnum[1][i]);
                        System.out.println("该视频目前无法下载！");
                    }
                }
            }

             System.out.println("是否继续下载？\n1、继续下载\n2、停止下载");
             int yesOrNo = sc.nextInt();
             if(yesOrNo == 1){continue;}
             else{break;}

             }

           t.openDB.close();
            openDB.close();

    }
}
