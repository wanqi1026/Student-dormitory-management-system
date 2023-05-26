
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * 这个类是一个继承自JPanel的面板，用于管理用户信息。
 * 它包含了一个表格，可以显示所有用户的账号、密码和用户类型，并提供了添加、删除、修改和查询用户的功能。
 * 其中，添加、删除和修改功能只对用户类型为3（宿管）的用户开放。 用户可以通过输入账号来查询对应用户的密码和用户类型。
 */
public class Users extends JPanel implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Connection connection = new GetConnection().getConnection();
    int type;
    UsersInformation user;
    JTable table = new JTable();
    String[] col = { "账号", "密码", "用户类型" };

    /**
     * 定义一个表的模板
     */
    DefaultTableModel mm = new DefaultTableModel(col, 0);

    JLabel Uname, Upassword, Utype, SP;
    JTextField UnameText, UpasswordText, UtypeText;
    JButton seek, add, delete, edit;
    JPanel student;

    public Users(int type, UsersInformation users) {
        this.user = users;
        this.type = type;

        // 整个采用流动式布局 很好的适应了表格带来的影响
        setLayout(new FlowLayout());

        table.setModel(mm);
        table.setRowSorter(new TableRowSorter<>(mm));

        JScrollPane js = new JScrollPane(table);
        add(js);

        search();
    }

    private void search() {
        PreparedStatement state;
        ResultSet resultSet;

        if (type == 2 || type == 3) {
            try {
                coop();
                state = connection.prepareStatement("select * from Users");
                resultSet = state.executeQuery();

                while (resultSet.next()) {
                    String Uname = resultSet.getString(1);
                    String Upassword = resultSet.getString(2);
                    String Utype = resultSet.getString(3);
                    String[] data = { Uname, Upassword, Utype };
                    mm.addRow(data);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void coop() {
        Uname = new JLabel("填入对应进行操作的账号:");
        UnameText = new JTextField(10);

        Upassword = new JLabel("密码:");
        UpasswordText = new JTextField(10);

        Utype = new JLabel("用户类型:");
        UtypeText = new JTextField(10);

        SP = new JLabel("                                   ");

        add = new JButton("添加");
        delete = new JButton("删除");
        edit = new JButton("修改");
        seek = new JButton("查询");

        add.addActionListener(this);
        edit.addActionListener(this);
        seek.addActionListener(this);
        delete.addActionListener(this);

        student = new JPanel(new GridLayout(14, 2));

        student.add(Uname);
        student.add(UnameText);
        student.add(Upassword);
        student.add(UpasswordText);
        student.add(Utype);
        student.add(UtypeText);

        student.add(SP);

        student.add(add);
        student.add(delete);
        student.add(edit);
        student.add(seek);

        add(student);
    }

    public void actionPerformed(ActionEvent e) {

        // 添加管理员信息操作
        if (e.getSource() == add && type == 3) {
            try {
                PreparedStatement statement = connection.prepareStatement("insert into users values(?,?,?)");

                statement.setString(1, UnameText.getText());
                statement.setString(2, UpasswordText.getText());
                statement.setString(3, UtypeText.getText());
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "添加成功");

                showUsersInformation();

            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "添加失败！");
                e1.printStackTrace();
            }

        }

        // 删除管理员操作
        if (e.getSource() == delete && type == 3) {
            try {

                PreparedStatement statement = connection.prepareStatement("DELETE FROM users\r\n" + "WHERE \"Uname\"='" + UnameText.getText() +"';");
                statement.executeUpdate();

                JOptionPane.showMessageDialog(this, "用户信息删除成功！");

                showUsersInformation();

            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "用户信息删除失败！");
               
                e1.printStackTrace();
            }

        }

        // 修改管理员信息
        if (e.getSource() == edit && type == 3) {
            PreparedStatement statement;
            //ResultSet resultSet;
            try {
                // 只修改密码
                if (UpasswordText.getText().length() > 0 && UtypeText.getText().length() == 0) {

                    statement = connection.prepareStatement("update users set \"Upassword\"= '" + UpasswordText.getText() + "'"+ "where \"Uname\" = '" + UnameText.getText() + "'");
                  
                    statement.executeUpdate();

                    showUsersInformation();
                }

                // 只修改用户类型
                if (UpasswordText.getText().length() == 0 && UtypeText.getText().length() > 0) {
                    statement = connection.prepareStatement("update users set \"Utype\" = '" + UtypeText.getText() + "' where \"Uname\" = '" + UnameText.getText() + "'");
                   
                    statement.executeUpdate();

                    showUsersInformation();
                }
                if (UpasswordText.getText().length() > 0) {
                    // 全部修改
                    statement = connection.prepareStatement("update users set \"Upassword\" = '" + UpasswordText.getText() + "' , \"Utype\" = '" + UtypeText.getText() + "' where \"Uname\" = '" + UnameText.getText()+ "';");
                   
                    statement.executeUpdate();
                    showUsersInformation();
                }
                JOptionPane.showMessageDialog(this, "用户信息修改成功！");
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "用户信息修改失败！");
                e1.printStackTrace();
            }

        }

        // 查询管理员信息
        if (e.getSource() == seek && type == 3) {
            PreparedStatement state;
            ResultSet resultSet;
            try {
                state = connection.prepareStatement("select * from users where \"Uname\" =" + "'" + UnameText.getText() + "'");
                resultSet = state.executeQuery();
                JOptionPane.showMessageDialog(this, "查询成功");

                while (mm.getRowCount() > 0) {
                    // 把表格进行刷新，下次显示的时候重头开始显示
                    mm.removeRow(mm.getRowCount() - 1);
                }

                while (resultSet.next()) {
                    String Uname = resultSet.getString(1);
                    String Upassword = resultSet.getString(2);
                    String Utype = resultSet.getString(3);
                    String[] data = { Uname, Upassword, Utype };
                    mm.addRow(data);
                }

                JOptionPane.showMessageDialog(this, "信息刷新成功！");

            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "查询失败！");
                e1.printStackTrace();
            }
        }
    }

    private void showUsersInformation() throws SQLException {
        PreparedStatement state = connection.prepareStatement("select * from users");
        ResultSet resultSet = state.executeQuery();

        while (mm.getRowCount() > 0) {
            // 把表格进行刷新，下次显示的时候重头开始显示
            mm.removeRow(mm.getRowCount() - 1);
        }

        while (resultSet.next()) {
            String Uname = resultSet.getString(1);
            String Upassword = resultSet.getString(2);
            String Utype = resultSet.getString(3);
            String[] data = { Uname, Upassword, Utype };
            mm.addRow(data);
        }
        JOptionPane.showMessageDialog(this, "信息刷新成功！");
    }
}
