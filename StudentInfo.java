
import java.awt.FlowLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * 这个类是一个 JPanel 类型的组件，用于显示学生的住宿信息。 根据用户类型的不同，可以显示不同范围的学生住宿信息。
 * 如果是学生用户，则只显示该学生自己的住宿信息； 如果是宿管用户，则显示全部学生的住宿信息。 
 * 该组件使用了 JTable来展示学生住宿信息，并且可以对学生信息进行排序。 
 * 在 search() 方法中，根据用户类型的不同，使用 SQL 语句查询学生住宿信息，并将查询结果添加到JTable 中。

 */
public class StudentInfo extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Connection connection = new GetConnection().getConnection();
    /**
     * 当前用户
     */
    UsersInformation users;
    /**
     * 用户类型
     */
    int type;
    JTable table = new JTable();
    String[] col = { "学号", "姓名", "性别", "专业编号", "宿舍号", "寝楼", "楼层" };
    /**
     * 定义一个表的模板
     */
    DefaultTableModel mm = new DefaultTableModel(col, 0);

    public StudentInfo(UsersInformation users, int type) {
        // 从登录界面传回，用户名和用户类型
        this.type = type;
        this.users = users;
        setLayout(new FlowLayout());

        table.setModel(mm);
        // 排序
        table.setRowSorter(new TableRowSorter<>(mm));
        JPanel jPanel = new JPanel(new FlowLayout());
        JScrollPane js = new JScrollPane(table);
        jPanel.add(js);
        add(jPanel);
        search();
    }

    private void search() {
        PreparedStatement state;
        if (type == 1) {
            // 如果是学生，只显示学生自己的信息
            try {
                state = connection
                        .prepareStatement("select * from student where \"Sname\" = '" + users.getName() + "'");

                reShowStuInformation(state);

                JOptionPane.showMessageDialog(this, "显示成功");

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "显示成功");
                e.printStackTrace();
            }
        } else if (type == 2 || type == 3) {
            // 如果是宿管，则显示全部学生的信息
            try {
                state = connection.prepareStatement("select * from student");

                reShowStuInformation(state);

                JOptionPane.showMessageDialog(this, "显示成功");

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "显示失败");
                e.printStackTrace();
            }
        }
    }

    private void reShowStuInformation(PreparedStatement state) throws SQLException {
        ResultSet resultSet;
        resultSet = state.executeQuery();

        while (resultSet.next()) {
            String Sno = resultSet.getString(1);
            String Sname = resultSet.getString(2);
            String Ssex = resultSet.getString(3);
            String Sdept = resultSet.getString(4);
            String Dno = resultSet.getString(5);
            String Bbu = resultSet.getString(6);
            String[] data = { Sno, Sname, Ssex, Sdept, Dno, Bbu };
            mm.addRow(data);
        }
    }
}
