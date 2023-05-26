
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
 * 这个类是用于修改学生缺寝记录的界面。 
 * 它包括了一个表格，显示所有的学生缺寝记录，并且提供了一个宿管可以使用的表单，用于修改学生的缺寝记录。
 * 在表单中，宿管可以输入学生的学号、缺寝时间和缺寝原因，然后点击“修改”按钮，就可以将相应的学生缺寝记录进行修改。 
 * 修改成功后，界面会显示一个提示消息。
 */
public class EditLater extends JPanel implements ActionListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Connection connection = new GetConnection().getConnection();
    int type;
    UsersInformation user;
    JTable table = new JTable();
    String[] col = { "学号", "姓名", "宿舍号", "缺寝时间", "缺寝原因" };
    /**
     * 定义一个表的模板
     */
    DefaultTableModel mm = new DefaultTableModel(col, 0);

    JLabel Atime, Areason, Dno, Sno, Sname;
    JTextField AtimeText, AreasonText, DnoText, SnoText, SnameText;
    JButton submit;
    JPanel suguan;

    public EditLater(int type, UsersInformation user) {
        this.user = user;
        this.type = type;
        setLayout(new FlowLayout());

        table.setModel(mm);
        table.setRowSorter(new TableRowSorter<>(mm));
        JScrollPane js = new JScrollPane(table);
        add(js);
        JOptionPane.showMessageDialog(this, "即将进入编辑界面，如要修改请务必输入要修改的学生缺寝信息的学号，不需要修改的项不填");
        search();
    }

    private void search() {
        PreparedStatement state;
        ResultSet resultSet;
        if (type == 1) {
            try {
                // 这里不好 因为名字可以一样 以后要改
                state = connection
                        .prepareStatement("select * from absent where Sname" + "=" + "'" + user.getName() + "'");
                resultSet = state.executeQuery();
                while (resultSet.next()) {
                    String Sno = resultSet.getString(1);
                    String Sname = resultSet.getString(2);
                    String Dno = resultSet.getString(3);
                    String Atime = resultSet.getString(4);
                    String Areason = resultSet.getString(5);
                    String[] data = { Sno, Sname, Dno, Atime, Areason };
                    mm.addRow(data);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (type == 2 || type == 3) {
            try {
                xiugai();
                state = connection.prepareStatement("select * from absent");
                resultSet = state.executeQuery();
                while (resultSet.next()) {
                    String Sno = resultSet.getString(1);
                    String Sname = resultSet.getString(2);
                    String Dno = resultSet.getString(3);
                    String Atime = resultSet.getString(4);
                    String Areason = resultSet.getString(5);
                    String[] data = { Sno, Sname, Dno, Atime, Areason };
                    mm.addRow(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void xiugai() {
        // 宿管修改学生的宿舍信息
        Sno = new JLabel("请输入被修改学生的学号:");
        SnoText = new JTextField(9);
        Atime = new JLabel("缺寝时间:");
        AtimeText = new JTextField(9);
        Areason = new JLabel("缺寝原因:");
        AreasonText = new JTextField(9);
        suguan = new JPanel(new GridLayout(4, 2));
        submit = new JButton("修改");
        submit.addActionListener(this);

        suguan.add(Sno);
        suguan.add(SnoText);
        suguan.add(Atime);
        suguan.add(AtimeText);
        suguan.add(Areason);
        suguan.add(AreasonText);
        suguan.add(submit);
        // 这里加一个提示消息
        add(suguan);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            // 如果点击按钮的是宿管
            try {
                if (AtimeText.getText().length() > 0 && AreasonText.getText().length() == 0) {
                    // 只修改缺寝时间

                    Statement statement = connection.createStatement();
                    String sql = "update absent set \"Atime\"=" + "'" + AtimeText.getText() + "'" + "where \"Sno\""
                            + "=" + "'" + SnoText.getText() + "'";
                    statement.executeUpdate(sql);
                    PreparedStatement state;
                    ResultSet resultSet;
                    state = connection.prepareStatement("select * from absent");
                    resultSet = state.executeQuery();
                    while (mm.getRowCount() > 0) {
                        // 把表格进行刷新，下次显示的时候重头开始显示
                        mm.removeRow(mm.getRowCount() - 1);
                    }
                    while (resultSet.next()) {
                        // 把更新后的数据重新显示到表格中，下同
                        String Sno = resultSet.getString(1);
                        String Sname = resultSet.getString(2);
                        String Dno = resultSet.getString(3);
                        String Atime = resultSet.getString(4);
                        String Areason = resultSet.getString(5);
                        String[] data = { Sno, Sname, Dno, Atime, Areason };
                        mm.addRow(data);
                    }

                }
                if (AreasonText.getText().length() > 0 && AtimeText.getText().length() == 0) {
                    // 只修改缺寝原因

                    Statement statement = connection.createStatement();
                    String sql = "update absent set \"Areason\"=" + "'" + AreasonText.getText() + "'" + "where \"Sno\""
                            + "=" + "'" + SnoText.getText() + "'";
                    statement.executeUpdate(sql);
                    PreparedStatement state;
                    ResultSet resultSet;
                    state = connection.prepareStatement("select * from absent");
                    resultSet = state.executeQuery();
                    while (mm.getRowCount() > 0) {
                        // 把表格进行刷新，下次显示的时候重头开始显示
                        mm.removeRow(mm.getRowCount() - 1);
                    }
                    while (resultSet.next()) {
                        // 把更新后的数据重新显示到表格中，下同
                        String Sno = resultSet.getString(1);
                        String Sname = resultSet.getString(2);
                        String Dno = resultSet.getString(3);
                        String Atime = resultSet.getString(4);
                        String Areason = resultSet.getString(5);
                        String[] data = { Sno, Sname, Dno, Atime, Areason };
                        mm.addRow(data);

                    }
                }
                if (AtimeText.getText().length() > 0 && AreasonText.getText().length() > 0) {
                    // 全部修改
                    Statement statement = connection.createStatement();
                    String sql = "update absent set \"Atime\"=" + "'" + AtimeText.getText() + "'" + ", \"Areason\"="
                            + "'" + AreasonText.getText() + "'" + "where \"Sno\"" + "=" + "'" + SnoText.getText() + "'";
                    statement.executeUpdate(sql);
                    PreparedStatement state;
                    ResultSet resultSet;
                    state = connection.prepareStatement("select * from absent");
                    resultSet = state.executeQuery();
                    while (mm.getRowCount() > 0) {
                        // 把表格进行刷新，下次显示的时候重头开始显示
                        mm.removeRow(mm.getRowCount() - 1);
                    }
                    while (resultSet.next()) {
                        // 把更新后的数据重新显示到表格中，下同
                        String Sno = resultSet.getString(1);
                        String Sname = resultSet.getString(2);
                        String Dno = resultSet.getString(3);
                        String Atime = resultSet.getString(4);
                        String Areason = resultSet.getString(5);
                        String[] data = { Sno, Sname, Dno, Atime, Areason };
                        mm.addRow(data);
                    }
                }
                JOptionPane.showMessageDialog(this, "修改成功");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
