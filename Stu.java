
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
 * 超管---学生人员管理 这个类是一个面板，用于实现超级管理员对学生信息的管理。 
 * 其中包括了查询、添加、删除、修改学生信息的功能。
 * 在查询功能中，用户可以通过输入学号来查询该学生的姓名、性别、专业、寝室号和所在寝楼等信息。
 * 在添加功能中，用户需要输入学生的学号、姓名、性别、专业、寝室号和所在寝楼等信息，然后点击添加按钮，即可将该学生信息添加到数据库中。
 * 在删除功能中，用户需要输入学生的学号，然后点击删除按钮，即可将该学生信息从数据库中删除。
 * 在修改功能中，用户可以选择修改学生的专业、寝室号或者同时修改两者， 然后点击修改按钮，即可将修改后的信息更新到数据库中。
 */
public class Stu extends JPanel implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Connection connection = new GetConnection().getConnection();
    int type;
    UsersInformation user;
    JTable table = new JTable();
    String[] col = { "学号", "姓名", "性别", "专业", "宿舍号", "寝楼" };
    /**
     * 定义一个表的模板
     */
    DefaultTableModel mm = new DefaultTableModel(col, 0);

    JLabel Sno, Sname, Ssex, Sdept, Dno, Bbu, SP, SP2;
    JTextField SnoText, SnameText, SsexText, SdeptText, DnoText, BbuText;
    JButton seek, add, delete, edit;
    JPanel student;

    public Stu(int type, UsersInformation user) {
        this.user = user;
        this.type = type;
        // 整个采用流动式布局, 很好的适应了表格带来的影响
        setLayout(new FlowLayout());

        table.setModel(mm);
        table.setRowSorter(new TableRowSorter<>(mm));
        JScrollPane js = new JScrollPane(table);
        add(js);
        search();
    }

    private void search() {
        if (type == 2 || type == 3) {
            try {
                coop();
                reShowStuInformation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void coop() {
        // 这是宿管添加学生信息晚归记录的功能
        Sno = new JLabel("填对应进行操作的学号:");
        SnoText = new JTextField(6);
        Sname = new JLabel("名字(不可修改):");
        SnameText = new JTextField(6);
        Ssex = new JLabel("性别(不可修改):");
        SsexText = new JTextField(6);
        Sdept = new JLabel("专业:");
        SdeptText = new JTextField(6);
        Dno = new JLabel("寝室号:");
        DnoText = new JTextField(6);
        Bbu = new JLabel("寝楼(不可修改):");
        BbuText = new JTextField(6);
        SP = new JLabel("                             ");
        SP2 = new JLabel("                             ");
        add = new JButton("添加");
        delete = new JButton("删除");
        edit = new JButton("修改");
        seek = new JButton("查询");
        add.addActionListener(this);
        edit.addActionListener(this);
        seek.addActionListener(this);
        delete.addActionListener(this);
        student = new JPanel(new GridLayout(14, 2));

        student.add(Sno);
        student.add(SnoText);
        student.add(Sname);
        student.add(SnameText);
        student.add(Ssex);
        student.add(SsexText);
        student.add(Sdept);
        student.add(SdeptText);
        student.add(Dno);
        student.add(DnoText);
        student.add(Bbu);
        student.add(BbuText);
        student.add(SP);
        student.add(SP2);
        student.add(add);
        student.add(delete);
        student.add(edit);
        student.add(seek);
        add(student);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add && type == 3) {
            try {
                PreparedStatement statement = connection.prepareStatement("insert into student values(?,?,?,?,?,?)");
                statement.setString(1, SnoText.getText());
                statement.setString(2, SnameText.getText());
                statement.setString(3, SsexText.getText());
                statement.setString(4, SdeptText.getText());
                statement.setString(5, DnoText.getText());
                statement.setString(6, BbuText.getText());

                statement.executeUpdate();

                reShowStuInformation();
                
                JOptionPane.showMessageDialog(this, "添加成功");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "添加失败");
                e1.printStackTrace();
            }

        }

        if (e.getSource() == delete && type == 3) {
            try {
                PreparedStatement statement = connection
                        .prepareStatement("DELETE FROM student WHERE \"Sno\"=" + "'" + SnoText.getText() + "'");
                statement.executeUpdate();

                reShowStuInformation();

                JOptionPane.showMessageDialog(this, "删除成功");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "删除失败");
                e1.printStackTrace();
            }
        }
        
        if (e.getSource() == edit && type == 3) {
            try {
                if (DnoText.getText().length() > 0 && SdeptText.getText().length() == 0) {
                    // 只修改寝室

                    Statement statement = connection.createStatement();
                    String sql = "update student set \"Dno\"=" + "'" + DnoText.getText() + "'" + "where \"Sno\"" + "="
                            + "'" + SnoText.getText() + "'";
                    statement.executeUpdate(sql);

                    reShowStuInformation();

                }
                if (SdeptText.getText().length() > 0 && SsexText.getText().length() == 0) {
                    // 只修改专业

                    Statement statement = connection.createStatement();
                    String sql = "update student set \"Sdept\"=" + "'" + SdeptText.getText() + "'" + "where \"Sno\""
                            + "=" + "'" + SnoText.getText() + "'";
                    statement.executeUpdate(sql);

                    reShowStuInformation();
                }

                if (SdeptText.getText().length() > 0 && DnoText.getText().length() > 0) {
                    // 全部修改
                    Statement statement = connection.createStatement();
                    String sql = "update student set \"Sdept\"=" + "'" + SdeptText.getText() + "'" + ", \"Dno\"=" + "'"
                            + DnoText.getText() + "'" + "where \"Sno\"" + "=" + "'" + SnoText.getText() + "'";
                    statement.executeUpdate(sql);

                    reShowStuInformation();
                }
                JOptionPane.showMessageDialog(this, "修改成功");
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, "修改失败");
                e1.printStackTrace();
            }
        }

        if (e.getSource() == seek && type == 3) {
            PreparedStatement state;
            ResultSet resultSet;
            try {
                state = connection
                        .prepareStatement("select * from student where \"Sno\" =" + "'" + SnoText.getText() + "' or \"Sname\" = '"+SnameText.getText()+"';");
                resultSet = state.executeQuery();
                JOptionPane.showMessageDialog(this, "查询成功");

                while (mm.getRowCount() > 0) {
                    // 把表格进行刷新，下次显示的时候重头开始显示
                    mm.removeRow(mm.getRowCount() - 1);
                }

                while (resultSet.next()) {
                    // 把更新后的数据重新显示到表格中，下同
                    String Sno = resultSet.getString(1);
                    String Sname = resultSet.getString(2);
                    String Ssex = resultSet.getString(3);
                    String Sdept = resultSet.getString(4);
                    String Dno = resultSet.getString(5);
                    String Bbu = resultSet.getString(6);
                    String[] data = { Sno, Sname, Ssex, Sdept, Dno, Bbu };
                    mm.addRow(data);
                }

                JOptionPane.showMessageDialog(this, "信息刷新成功！");

            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "查询失败！");
                e1.printStackTrace();
            }
        }
    }

    private void reShowStuInformation() throws SQLException {
        
        PreparedStatement state;
        ResultSet resultSet;
        state = connection.prepareStatement("select * from student");
        resultSet = state.executeQuery();
        while (mm.getRowCount() > 0) {
            // 把表格进行刷新，下次显示的时候重头开始显示
            mm.removeRow(mm.getRowCount() - 1);
        }
        while (resultSet.next()) {
            // 把更新后的数据重新显示到表格中，下同
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
