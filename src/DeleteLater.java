
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
 * 这个类是一个继承自JPanel的Java Swing组件，它实现了ActionListener接口。
 * 它的作用是提供一个删除学生晚归记录的功能，包括从数据库中删除记录以及在界面上删除对应的行。
 * 在界面上，它显示了一个表格，列出了所有学生的晚归记录，其中包括学号、姓名、宿舍号、缺寝时间和缺寝原因。
 * 用户可以输入要删除的学生的学号，然后点击“删除”按钮，就可以将该学生的晚归记录从数据库中删除，并在表格中删除对应的行。
 * 在删除完成后，它还会弹出一个对话框提示用户删除成功。
 */
public class DeleteLater extends JPanel implements ActionListener {
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
    JPanel student;

    public DeleteLater(int type, UsersInformation user) {
        this.user = user;
        this.type = type;
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
                delete();
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

    private void delete() {
        // 这就是宿管添加晚归记录的功能
        Sno = new JLabel("请输入要删除学生的学号:");
        SnoText = new JTextField(10);
        submit = new JButton("删除");
        submit.addActionListener(this);
        student = new JPanel(new GridLayout(6, 1));
        student.add(Sno);
        student.add(SnoText);
        student.add(submit);
        add(student);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            try {
                PreparedStatement statement = connection
                        .prepareStatement("DELETE FROM absent WHERE \"Sno\"=" + "'" + SnoText.getText() + "'");
                statement.executeUpdate();

                PreparedStatement state = connection.prepareStatement("select * from absent");
                ResultSet resultSet = state.executeQuery();
                while (mm.getRowCount() > 0) {
                    // 把表格进行刷新，下次显示的时候重头开始显示
                    mm.removeRow(mm.getRowCount() - 1);
                }

                while (resultSet.next()) {
                    String Sno = resultSet.getString(1);
                    String Sname = resultSet.getString(2);
                    String Dno = resultSet.getString(3);
                    String Atime = resultSet.getString(4);
                    String Areason = resultSet.getString(5);
                    String[] data = { Sno, Sname, Dno, Atime, Areason };
                    mm.addRow(data);
                }
                JOptionPane.showMessageDialog(this, "删除成功!");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "删除失败!");
                e1.printStackTrace();
            }
        }

    }
}
