
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 这个类的功能是用于连接到 openGauss 数据库。 它包含一个 getConnection() 方法，该方法返回连接到指定数据库的
 * Connection 对象。 在方法中，使用 DriverManager 类加载数据库驱动，并通过指定的 URL、用户名和密码连接到数据库。
 * 如果连接成功，将打印 "Connection succeed!"，否则返回 `null`。
 */
public class GetConnection {
    private Connection con = null;

    public Connection getConnection() {
        // 数据库连接URL（url：就是一个jdbc的规范的约定）
        // 数据库位置
        String url = "jdbc:postgresql://192.168.56.108:26000/mydb";
        String user = "test";
        String key = "openGauss@taoa";

        try {
            // 加载数据库驱动
            Class.forName("org.postgresql.Driver");
            // 数据库连接用户名、密码
            con = DriverManager.getConnection(url, user, key);
            System.out.println("Connection succeed!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Connection faild!");
            return null;
        }
        // 返回的con就是一个数据库连接对象，通过它你就可以对这个数据库做添删改查的动作
        return con;
    }
}