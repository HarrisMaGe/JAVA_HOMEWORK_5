/**
 * Created by MYC on 2016/12/17.
 */

public class ContentOfJson {
    private int aid;
    private String author;
    private int tid;
    private String title;
    private String tname;
    public Stat stat;

    public  static class Stat{
        private int coin;
        private int favorite;

        public int getCoin() {
            return coin;
        }

        public int getFavorite() {
            return favorite;
        }

        public void setCoin(int coin) {
            this.coin = coin;
        }

        public void setFavorite(int favorite) {
            this.favorite = favorite;
        }
    }

    public void setAid(int aid){
        this.aid = aid;
    }
    public int getAid(){
        return this.aid;
    }

    public void setAuthor(String author){
        this.author = author;
    }
    public String getAuthor(){
        return this.author;
    }

    public void setTid(int tid){
        this.tid = tid;
    }
    public int getTid(){
        return this.tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    // 重写toString方法
             @Override
     public String toString() {
                 return "ContentOfJson[aid = " + aid + ", author = " + author + ", tid = " + tid + "]";
             }

}
