
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
 * 这个类的作用是实现学生缺寝记录的管理。 它是一个继承自JPanel的面板，可以在图形用户界面上显示学生缺寝记录的相关信息。
 * 该类包含一个JTable对象，用于显示学生缺寝记录的列表，以及一些文本框、标签和按钮，用于添加、删除、修改和查询学生缺寝记录。
 * 其中，getConnection()方法用于获取数据库连接， search()方法用于执行SQL查询并将结果存储在JTable中，
 * coop()方法用于创建添加、删除、修改和查询学生缺寝记录的文本框、标签和按钮，并将它们添加到面板中。
 * 该类还实现了ActionListener接口，用于处理按钮的点击事件，例如添加、删除、修改和查询学生缺寝记录。
 * 该类的实例化对象可以用于显示学生缺寝记录的列表，并进行相关操作。
 * 
 * @author taoa
 *
 */
public class Building extends JPanel implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Connection connection = new GetConnection().getConnection();
    int type;
    UsersInformation user;
    JTable table = new JTable();

    // JButton button = new JButton("");

    String[] col = { "楼号", "层数", "管理人", "容纳人数" };
    /**
     * 定义一个表的模板
     */
    DefaultTableModel mm = new DefaultTableModel(col, 0);

    JLabel Bname, Bfloor, Bmager, Bcap, SP;
    JTextField BnameText, BfloorText, BmagerText, BcapText;
    JButton seek, add, delete, edit;
    JPanel student;

    public Building(int type, UsersInformation user) {
        this.user = user;
        this.type = type;
        // 整个采用流动式布局,很好的适应了表格带来的影响
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
                state = connection.prepareStatement("select * from building");
                resultSet = state.executeQuery();
                while (resultSet.next()) {
                    String Bname = resultSet.getString(1);
                    String Bfloor = resultSet.getString(2);
                    String Bmager = resultSet.getString(3);
                    String Bcap = resultSet.getString(4);
                    String[] data = { Bname, Bfloor, Bmager, Bcap };
                    mm.addRow(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void coop() {
        Bname = new JLabel("填入对应进行操作的楼名:");
        BnameText = new JTextField(8);

        Bfloor = new JLabel("层数(不可修改):");
        BfloorText = new JTextField(8);

        Bmager = new JLabel("管理人:");
        BmagerText = new JTextField(8);

        Bcap = new JLabel("容纳人数:");
        BcapText = new JTextField(8);

        SP = new JLabel("                                        ");

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
        student.add(Bname);
        student.add(BnameText);
        student.add(Bfloor);
        student.add(BfloorText);
        student.add(Bmager);
        student.add(BmagerText);
        student.add(Bcap);
        student.add(BcapText);
        student.add(SP);
        student.add(add);
        student.add(delete);
        student.add(edit);
        student.add(seek);

        add(student);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add && type == 3) {
            try {
                PreparedStatement statement = connection.prepareStatement("insert into building values(?,?,?,?)");
                statement.setString(1, BnameText.getText());
                statement.setString(2, BfloorText.getText());
                statement.setString(3, BmagerText.getText());
                statement.setString(4, BcapText.getText());
                statement.executeUpdate();

                reShowBuildingInformation();

            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            JOptionPane.showMessageDialog(this, "添加成功");
        }

        if (e.getSource() == delete && type == 3) {
            try {
                PreparedStatement statement = connection
                        .prepareStatement("DELETE FROM building WHERE \"Bname\"=" + "'" + BnameText.getText() + "'");
                statement.executeUpdate();

                reShowBuildingInformation();

                JOptionPane.showMessageDialog(this, "删除成功!");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "删除失败!");
                e1.printStackTrace();
            }

        }

        if (e.getSource() == edit && type == 3) {
            try {
                if (BmagerText.getText().length() > 0 && BcapText.getText().length() == 0) {
                    // 只修改管理人
                    Statement statement = connection.createStatement();
                    String sql = "update building set \"Bmager\"=" + "'" + BmagerText.getText() + "'"
                            + "where \"Bname\"" + "=" + "'" + BnameText.getText() + "'";
                    statement.executeUpdate(sql);

                    reShowBuildingInformation();
                }
                if (BcapText.getText().length() > 0 && BmagerText.getText().length() == 0) {
                    // 只修改容量
                    Statement statement = connection.createStatement();
                    String sql = "update building set \"Bcap\"=" + "'" + BcapText.getText() + "'" + "where \"Bname\""
                            + "=" + "'" + BnameText.getText() + "'";
                    statement.executeUpdate(sql);

                    reShowBuildingInformation();
                }

                if (BcapText.getText().length() > 0 && BmagerText.getText().length() > 0) {
                    // 全部修改
                    Statement statement = connection.createStatement();
                    String sql = "update building set \"Bcap\"=" + "'" + BcapText.getText() + "'" + ", \"Bmager\"="
                            + "'" + BmagerText.getText() + "'" + "where \"Bname\"" + "=" + "'" + BnameText.getText()
                            + "'";
                    statement.executeUpdate(sql);

                    reShowBuildingInformation();
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
                state = connection
                        .prepareStatement("select * from building where \"Bname\"='" + BnameText.getText() + "';");
                resultSet = state.executeQuery();
                JOptionPane.showMessageDialog(this, "查询成功!");

                while (mm.getRowCount() > 0) {
                    // 把表格进行刷新，下次显示的时候重头开始显示
                    mm.removeRow(mm.getRowCount() - 1);
                }
                while (resultSet.next()) {
                    // 把更新后的数据重新显示到表格中，下同
                    String Bname = resultSet.getString(1);
                    String Bfloor = resultSet.getString(2);
                    String Bmager = resultSet.getString(3);
                    String Bcap = resultSet.getString(4);
                    String[] data = { Bname, Bfloor, Bmager, Bcap };
                    mm.addRow(data);
                }
                JOptionPane.showMessageDialog(this, "刷新成功!");

            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "查询失败!");
                e1.printStackTrace();
            }

        }
    }

    private void reShowBuildingInformation() throws SQLException {
        PreparedStatement state;
        ResultSet resultSet;
        state = connection.prepareStatement("select * from building");
        resultSet = state.executeQuery();
        while (mm.getRowCount() > 0) {
            // 把表格进行刷新，下次显示的时候重头开始显示
            mm.removeRow(mm.getRowCount() - 1);
        }
        while (resultSet.next()) {
            // 把更新后的数据重新显示到表格中，下同
            String Bname = resultSet.getString(1);
            String Bfloor = resultSet.getString(2);
            String Bmager = resultSet.getString(3);
            String Bcap = resultSet.getString(4);
            String[] data = { Bname, Bfloor, Bmager, Bcap };
            mm.addRow(data);
        }
        JOptionPane.showMessageDialog(this, "刷新成功!");
    }
}
