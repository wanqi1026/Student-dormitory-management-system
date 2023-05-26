
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * 这个类的作用是从数据库中获取用户列表， 并将每个用户的姓名、密码和类型存储在一个ArrayList中。
 * 其中，getConnection()方法用于获取数据库连接， getUsers()方法用于执行SQL查询并将结果存储在ArrayList中。
 * 该类的实例化对象可以用于获取用户列表。
 *
 */
class CheckUsers {
    GetConnection getConnection = new GetConnection();
    Connection connection = getConnection.getConnection();

    public ArrayList<UsersInformation> getUsers() {
        ArrayList<UsersInformation> list = new ArrayList<>();
        try {

            PreparedStatement state = connection.prepareStatement("select * from Users");
            ResultSet res = state.executeQuery();
            while (res.next()) {
                UsersInformation user = new UsersInformation();
                user.setName(res.getString(1));
                user.setPassword(res.getString(2));
                // 类型
                user.setType(res.getInt(3));
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return list;
    }
}

/**
 * 这个类是一个Java Swing界面的面板，实现了安全退出系统的功能。 当用户点击“安全退出”按钮时，程序会调用System.exit(0)方法退出程序。
 * 界面包含了一个“桂电”的标签和一个“安全退出”按钮。
 * 
 * @author taoa
 *
 */
class Exit extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    JButton exit = new JButton("退出");

    JLabel exitLabel;
    // j1, j2, j3, j4, j5, j6;

    public Exit() {
        setLayout(null);
        exit.setBounds(260, 230, 100, 25);
        exitLabel = new JLabel("桂电B区");
        exitLabel.setFont(new Font("宋体", Font.ITALIC, 100));
        exitLabel.setBounds(180, 0, 400, 300);
        add(exit);
        add(exitLabel);

        exit.addActionListener(new ActionListener() {
            // 为重置按钮添加监听事件
            // 同时清空name、password的数据
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
    }

}

/**
 * 这个类是一个Java Swing GUI应用程序的登录界面， 它包含了一个文本框和密码框用于用户输入用户名和密码，以及一个登录按钮和重置按钮。
 * 当用户点击登录按钮时，程序会检查输入的用户名和密码是否正确，
 * 如果正确，程序会根据用户类型显示相应的操作界面，包括学生界面、宿管界面和超管界面。
 * 每个操作界面都是使用JTabbedPane和CardLayout布局管理器实现的，可以通过选项卡切换不同的操作。
 * 此外，程序还提供了重置按钮用于清空文本框和密码框中的内容。
 *
 */
public class Login extends JFrame implements ActionListener {
    // 增加监听事件
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * label组件数组 创建欢迎界面JLabel
     */
    JLabel welcome = new JLabel("欢迎使用学生宿舍管理系统");

    /**
     * 用户、密码
     */
    JLabel user, password;

    /**
     * 单行文本框 用户名框
     */
    JTextField username;

    /**
     * 密码框
     */
    JPasswordField passwordField;

    /**
     * 登录按钮
     */
    JButton loginButton;
    JButton button;

    /**
     * 定义一个卡片式布局
     */
    CardLayout cardLayout = new CardLayout();

    /**
     * 扑克牌,每次显示最前面的一张
     */
    JPanel card;
    JPanel cardPanel, cardPanel2, cardPanel3, cardPanel4;

    /**
     * 界面上需要放置多个组件时
     */
    JTabbedPane jTabbedPane, jTabbedPane2;
    int type = 1;
    UsersInformation users;
    Font font = new Font("宋体", Font.BOLD | Font.ITALIC, 20);

    public Login() {
        // 在文档加载完后调用函数，不调用不执行
        init();
    }

    /**
     * 初始化界面,该函数为初始化功能
     */
    private void init() {
        // 设计欢迎界面的UI
        welcome.setFont(font);
        setTitle("学生宿舍管理系统");
        getContentPane().setLayout(new BorderLayout());
        user = new JLabel("用户名");
        password = new JLabel("密码");
        button = new JButton("重置");
        card = new JPanel(cardLayout);
        JPanel panel1 = new JPanel(new BorderLayout());
        username = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("登录");
        loginButton.addActionListener(this);
        // 标题面板
        JPanel titlepanel = new JPanel(new FlowLayout());
        // 登录面板
        JPanel loginpanel = new JPanel();
        // 关闭窗口布局管理器，使后面的setBounds生效
        loginpanel.setLayout(null);

        // 设定窗体的偏移量
        welcome.setBounds(300, 100, 400, 25);
        user.setBounds(340, 170, 50, 20);
        password.setBounds(340, 210, 50, 20);
        username.setBounds(390, 170, 120, 20);
        passwordField.setBounds(390, 210, 120, 20);
        loginButton.setBounds(340, 250, 80, 25);
        button.setBounds(430, 250, 80, 25);

        // 把页面各项信息添加到登录login面板上
        loginpanel.add(welcome);
        loginpanel.add(user);
        loginpanel.add(password);
        loginpanel.add(username);
        loginpanel.add(passwordField);
        loginpanel.add(loginButton);
        loginpanel.add(button);
        panel1.add(titlepanel, BorderLayout.NORTH);
        panel1.add(loginpanel, BorderLayout.CENTER);
        card.add(panel1, "login");

        // 初始化一个容器 ，在容器上添加控件
        getContentPane().add(card);
        setBounds(300, 100, 900, 600);

        // 显示JFrame对象
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 禁止页面伸缩
        setResizable(false);
        // pack();// 在setResizable(false)后进行pack()，防止在Windows下系统修改frame的尺寸

        // 为 重置按钮 添加监听事件
        button.addActionListener(new ActionListener() {
            // 同时清空name、password的数据
            @Override
            public void actionPerformed(ActionEvent arg0) {
                username.setText("");
                passwordField.setText("");
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 用来标志用户是否正确
        boolean flag = false;

        if (e.getSource() == loginButton) {
            // 获得所有用户信息
            ArrayList<UsersInformation> list = new CheckUsers().getUsers();
            // 遍历所有用户信息，以此来判断输入的信息是否正确
            for (int i = 0; i < list.size(); i++) {
                users = list.get(i);
                String passwordStr = new String(passwordField.getPassword());

                // 根据输入的用户名和密码决定进入哪个界面
                if (username.getText().equals(users.getName()) && passwordStr.equals(users.getPassword())) {
                    if (users.getType() == 1) {
                        getStuUi();
                    } else if (users.getType() == 2) {
                        getDorMasterUi();
                    } else if (users.getType() == 3) {
                        getSurperDorMasterUi();
                    }
                    flag = true;
                    // 如果信息正确就退出遍历，提高效率
                    break;
                }
            }
            if (!flag) {
                // 信息不正确，重新输入
                JOptionPane.showMessageDialog(null, "请输入正确的用户名或密码", "警告", JOptionPane.WARNING_MESSAGE);
                // 让文本框显示我要的信息
                username.setText("");
                passwordField.setText("");
            }
        }
    }

    /**
     * 学生界面
     */
    private void getStuUi() {
        // 如果是type是 1 ,表示 学生，待会进入学生UI界面
        type = users.getType();
        System.out.println("登录人员类别" + type);
        JOptionPane.showMessageDialog(null, "欢迎" + username.getText() + "(学生)登录", "学生宿舍管理系统",
                JOptionPane.PLAIN_MESSAGE);
        // 当输入的信息正确时，就开始加载选项卡界面，并把选项卡界面加入到卡片布局器中

        // 学生信息
        StudentInfo studentInfo = new StudentInfo(users, type);

        // 晚归信息
        AddLater later = new AddLater(type, users);

        // guanyu guanyu=new guanyu();
        // guanyu.help help=new guanyu.help();

        Exit exit = new Exit();

        cardPanel = new JPanel();
        cardPanel2 = new JPanel();
        cardPanel3 = new JPanel();
        cardPanel4 = new JPanel();

        jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        jTabbedPane.add("学生信息", studentInfo);
        jTabbedPane.add("缺寝记录", later);
        jTabbedPane.add("退出登录", exit);
        cardPanel.add(jTabbedPane);

        jTabbedPane2 = new JTabbedPane(JTabbedPane.TOP);
        jTabbedPane2.add("系统操作页面", cardPanel);
        // jTabbedPane2.add("关于系统",guanyu);
        // jTabbedPane2.add("帮助",help);
        cardPanel2.add(jTabbedPane2);

        card.add(cardPanel2, "info");
        // 输入信息正确就显示操作界面，否则重新输入正确信息
        cardLayout.show(card, "info");

    }

    /**
     * 宿管界面
     */
    private void getDorMasterUi() {
        // 如果是 2 表示 宿管
        type = users.getType();
        System.out.println("登录人员类别" + type);
        JOptionPane.showMessageDialog(null, "欢迎" + username.getText() + "(宿管)登录", "学生宿舍管理系统",
                JOptionPane.PLAIN_MESSAGE);
        // 当输入的信息正确时，就开始加载选项卡界面，并把选项卡界面加入到卡片布局器中

        // 学生信息
        StudentInfo studentInfo = new StudentInfo(users, type);

        // 宿舍信息
        Dormitory dormitoryInfo = new Dormitory(type, users);

        // 晚归信息
        AddLater later = new AddLater(type, users);

        // 修改缺寝信息
        EditLater editLater = new EditLater(type, users);

        // 删除
        DeleteLater deleteLater = new DeleteLater(type, users);
        // 查询
        SeekLater seekLater = new SeekLater(type, users);
        // guanyu guanyu=new guanyu();
        // guanyu.help help=new guanyu.help();
        // 退出系统
        Exit exit = new Exit();
        cardPanel = new JPanel();
        cardPanel2 = new JPanel();
        cardPanel3 = new JPanel();
        cardPanel4 = new JPanel();

        cardPanel = new JPanel();
        jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        jTabbedPane.add("学生信息", studentInfo);
        jTabbedPane.add("宿舍信息", dormitoryInfo);
        jTabbedPane.add("添加缺寝记录", later);
        jTabbedPane.add("查询缺寝记录", seekLater);
        jTabbedPane.add("修改缺寝记录", editLater);
        jTabbedPane.add("删除缺寝记录", deleteLater);
        jTabbedPane.add("退出登录", exit);
        cardPanel.add(jTabbedPane);

        jTabbedPane2 = new JTabbedPane(JTabbedPane.TOP);
        jTabbedPane2.add("系统操作页面", cardPanel);
        // jTabbedPane2.add("关于系统",guanyu);
        // jTabbedPane2.add("帮助",help);
        cardPanel2.add(jTabbedPane2);

        card.add(cardPanel2, "info");
        // 输入信息正确就显示操作界面，否则重新输入正确信息
        cardLayout.show(card, "info");
    }

    /**
     * 超管界面
     */
    private void getSurperDorMasterUi() {
        type = users.getType();
        System.out.println("登录人员类别" + type);
        JOptionPane.showMessageDialog(null, "欢迎" + username.getText() + "(超管)登录", "学生宿舍管理系统",
                JOptionPane.PLAIN_MESSAGE);
        // 当输入的信息正确时，就开始加载选项卡界面，并把选项卡界面加入到卡片布局器中
        // 晚归信息
        AddLater later = new AddLater(type, users);
        // 修改缺寝信息
        EditLater editLater = new EditLater(type, users);
        // 删除
        DeleteLater deleteLater = new DeleteLater(type, users);
        SeekLater seekLater = new SeekLater(type, users);
        Users cooUsers = new Users(type, users);
        Stu cooStu = new Stu(type, users);
        // 宿舍信息
        Dormitory dormitoryInfo = new Dormitory(type, users);
        Building cooBuilding = new Building(type, users);
        // guanyu guanyu=new guanyu();
        // guanyu.help help=new guanyu.help();
        Exit exit = new Exit();

        cardPanel = new JPanel();
        cardPanel2 = new JPanel();
        cardPanel3 = new JPanel();
        cardPanel4 = new JPanel();

        jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        jTabbedPane.add("添加缺寝记录", later);
        jTabbedPane.add("查询缺寝记录", seekLater);
        jTabbedPane.add("修改缺寝记录", editLater);
        jTabbedPane.add("删除缺寝记录", deleteLater);
        jTabbedPane.add("宿管人员管理", cooUsers);
        jTabbedPane.add("学生人员管理", cooStu);
        jTabbedPane.add("宿舍信息管理", dormitoryInfo);
        jTabbedPane.add("宿舍楼信息管理", cooBuilding);
        jTabbedPane.add("退出登录", exit);
        cardPanel.add(jTabbedPane);
        jTabbedPane2 = new JTabbedPane(JTabbedPane.TOP);
        jTabbedPane2.add("系统操作页面", cardPanel);
        // jTabbedPane2.add("关于系统",guanyu);
        // jTabbedPane2.add("帮助",help);
        cardPanel2.add(jTabbedPane2);
        card.add(cardPanel2, "info");
        // 输入信息正确就显示操作界面，否则重新输入正确信息
        cardLayout.show(card, "info");
    }
}
