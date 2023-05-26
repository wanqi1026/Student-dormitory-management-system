
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
 * 这个类是一个Java Swing的面板，用于实现学生宿舍管理系统中的宿舍信息管理模块。
 * 它实现了一个表格来显示宿舍信息，以及添加、删除、修改和查询宿舍信息的功能。 
 * 它还包含了与数据库的连接和查询语句，以及与用户界面的交互逻辑。
 * 这个类是学生宿舍管理系统的重要组成部分之一。

 */
public class Dormitory extends JPanel implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    Connection connection = new GetConnection().getConnection();
    int type;
    UsersInformation user;
    JTable table = new JTable();
    String[] col = { "宿舍号", "位置", "电话", "容纳人数" };
    /**
     * 定义一个表的模板
     */
    DefaultTableModel mm = new DefaultTableModel(col, 0);
    JLabel Dno, Dpo, Dphone, Dcap, SP;
    JTextField DnoText, DpoText, DphoneText, DcapText;
    JButton seek, add, delete, edit;
    JPanel student;

    public Dormitory(int type, UsersInformation user) {
        this.user = user;
        this.type = type;
        // 整个采用流动式布局 , 很好的适应了表格带来的影响
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
                if (type == 3) {
                    coop();
                }

                state = connection.prepareStatement("select * from dorm");
                // 查询的结果返回的是一个结果集，用ResultSet接收
                resultSet = state.executeQuery();

                while (resultSet.next()) {
                    String Dno = resultSet.getString(1);
                    String Dpo = resultSet.getString(2);
                    String Dphone = resultSet.getString(3);
                    String Dcap = resultSet.getString(4);
                    String[] data = { Dno, Dpo, Dphone, Dcap };
                    mm.addRow(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void coop() {
        Dno = new JLabel("填入对应进行操作的宿舍号:");
        DnoText = new JTextField(8);

        Dpo = new JLabel("位置(不可修改):");
        DpoText = new JTextField(8);

        Dphone = new JLabel("电话:");
        DphoneText = new JTextField(8);

        Dcap = new JLabel("容纳人数:");
        DcapText = new JTextField(8);

        SP = new JLabel("         ");

        add = new JButton("添加");
        delete = new JButton("删除");
        edit = new JButton("修改");
        seek = new JButton("查询");

        add.addActionListener(this);
        edit.addActionListener(this);
        seek.addActionListener(this);
        delete.addActionListener(this);

        student = new JPanel(new GridLayout(14, 2));
        // student.add(tip);
        student.add(Dno);
        student.add(DnoText);
        student.add(Dpo);
        student.add(DpoText);
        student.add(Dphone);
        student.add(DphoneText);
        student.add(Dcap);
        student.add(DcapText);
        student.add(SP);
        student.add(add);
        student.add(delete);
        student.add(edit);
        student.add(seek);

        add(student);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add && type == 3) {
            try {
                PreparedStatement statement = connection.prepareStatement("insert into dorm values(?,?,?,?)");
                statement.setString(1, DnoText.getText());
                statement.setString(2, DpoText.getText());
                statement.setString(3, DphoneText.getText());
                statement.setString(4, DcapText.getText());
                statement.executeUpdate();

                reShowDorInformation();

                JOptionPane.showMessageDialog(this, "添加成功!");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "添加失败!");
                e1.printStackTrace();
            }

        }

        if (e.getSource() == delete && type == 3) {
            try {
                PreparedStatement statement = connection
                        .prepareStatement("DELETE FROM dorm WHERE \"Dno\"=" + "'" + DnoText.getText() + "'");
                statement.executeUpdate();

                reShowDorInformation();

                JOptionPane.showMessageDialog(this, "删除成功!");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "删除失败!");
                e1.printStackTrace();
            }

        }

        if (e.getSource() == edit && type == 3) {
            try {
                if (DphoneText.getText().length() > 0 && DcapText.getText().length() == 0) {
                    // 只修改电话
                    Statement statement = connection.createStatement();
                    String sql = "update dorm set \"Dphone\"=" + "'" + DphoneText.getText() + "'" + "where \"Dno\""
                            + "=" + "'" + DnoText.getText() + "'";
                    statement.executeUpdate(sql);

                    reShowDorInformation();
                }
                if (DcapText.getText().length() > 0 && DphoneText.getText().length() == 0) {
                    // 只修改容量

                    Statement statement = connection.createStatement();
                    String sql = "update dorm set \"Dcap\"=" + "'" + DcapText.getText() + "'" + "where \"Dno\"" + "="
                            + "'" + DnoText.getText() + "'";
                    statement.executeUpdate(sql);

                    reShowDorInformation();
                }

                if (DcapText.getText().length() > 0 && DphoneText.getText().length() > 0) {
                    // 全部修改
                    Statement statement = connection.createStatement();
                    String sql = "update dorm set \"Dcap\"=" + "'" + DcapText.getText() + "'" + ", \"Dphone\"=" + "'"
                            + DphoneText.getText() + "'" + "where \"Dno\"" + "=" + "'" + DnoText.getText() + "'";
                    statement.executeUpdate(sql);

                    reShowDorInformation();
                }

                JOptionPane.showMessageDialog(this, "修改成功!");

            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "修改失败!");
                e1.printStackTrace();
            }

        }

        if (e.getSource() == seek && type == 3) {
            try {
                PreparedStatement state;
                ResultSet resultSet;
                state = connection.prepareStatement("select * from dorm where \"Dno\" = '" + DnoText.getText() + "'");
                resultSet = state.executeQuery();
                while (mm.getRowCount() > 0) {
                    // 把表格进行刷新，下次显示的时候重头开始显示
                    mm.removeRow(mm.getRowCount() - 1);
                }
                while (resultSet.next()) {
                    // 把更新后的数据重新显示到表格中，下同
                    String Dno = resultSet.getString(1);
                    String Dpo = resultSet.getString(2);
                    String Dphone = resultSet.getString(3);
                    String Dcap = resultSet.getString(4);
                    String[] data = { Dno, Dpo, Dphone, Dcap };
                    mm.addRow(data);
                }
                JOptionPane.showMessageDialog(this, "刷新成功!");

            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "查询失败!");
                e1.printStackTrace();
            }
        }
    }

    private void reShowDorInformation() throws SQLException {
        PreparedStatement state;
        ResultSet resultSet;
        state = connection.prepareStatement("select * from dorm");
        resultSet = state.executeQuery();
        while (mm.getRowCount() > 0) {
            // 把表格进行刷新，下次显示的时候重头开始显示
            mm.removeRow(mm.getRowCount() - 1);
        }
        while (resultSet.next()) {
            // 把更新后的数据重新显示到表格中，下同
            String Dno = resultSet.getString(1);
            String Dpo = resultSet.getString(2);
            String Dphone = resultSet.getString(3);
            String Dcap = resultSet.getString(4);
            String[] data = { Dno, Dpo, Dphone, Dcap };
            mm.addRow(data);
        }
    }
}