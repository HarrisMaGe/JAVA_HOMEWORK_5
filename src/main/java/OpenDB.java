import java.sql.*;

/**
 * Created by MYC on 2016/12/17.
 */
public class OpenDB {
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://127.0.0.1:330/homework_5?useSSL=false&serverTimezone=America/New_York";

    String user = "root";
    String password = "19950228MAGE";
    public Connection conn = null;

    public OpenDB() {
        try {
            // 加载驱动程序
            Class.forName(driver);
            // 连接数据库
            Connection conn = DriverManager.getConnection(this.url, this.user, this.password);
            this.conn = conn;
            if (!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//将解析后的数据存入数据库
    public void insertoInformation(int tid,String tname,int aid,String title,String author,int coin,int favorite){
        try {
            String insql = "insert into information(tid,tname,aid,title,author,coin,favorite)values(?,?,?,?,?,?,?)";
            PreparedStatement pStmt = conn.prepareStatement(insql);
            pStmt.setInt(1,tid);
            pStmt.setString(2,tname);
            pStmt.setInt(3,aid);
            pStmt.setString(4,title);
            pStmt.setString(5,author);
            pStmt.setInt(6,coin);
            pStmt.setInt(7,favorite);
            pStmt.executeUpdate();
            pStmt.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
//从数据库筛选出对应的每个版块的收藏数前三名。使用MySQL的分组排序，存入二维数组，第一维是第几名，第二维存储对应的属性
    public String[][] selectfromInformation(int tid){
            try {
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery("select * from information a where tid = "+tid+" and 3>(select count(*) from information where tid=a.tid and favorite>a.favorite)order by a.tid,a.favorite desc;");

                rs.last();
                int count = rs.getRow();
                rs.beforeFirst();
                String info[][] =new String[count][7];
                for(int i = 0;i<count;i++){
                    if(rs.next()){
                        info[i][0] = String.valueOf(rs.getInt("tid"));
                        info[i][1] = rs.getString("tname");
                        info[i][2] = String.valueOf(rs.getInt("aid"));
                        info[i][3] = rs.getString("title");
                        info[i][4] = rs.getString("author");
                        info[i][5] = String.valueOf(rs.getInt("coin"));
                        info[i][6] = String.valueOf(rs.getInt("favorite"));
                    }
                }
                statement.close();
                rs.close();
                return info;
            }catch(Exception e){
                e.printStackTrace();
                return null;
            }

    }
    public void close(){
        try{
            this.conn.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }
}
