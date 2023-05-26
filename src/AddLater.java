
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
 * 这个类的作用是添加学生晚归信息，包括宿管添加晚归记录和学生查询自己的晚归记录。 
 * 它包含了一个表格用于显示学生晚归信息，以及一个表单用于宿管添加晚归记录。
 * 在添加晚归记录时，它会将记录插入到数据库中，并刷新表格以显示最新的晚归记录。 同时，它还会弹出一个提示框，提示添加成功。
 */
public class AddLater extends JPanel implements ActionListener {
    // 监听器
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

    JLabel Atime, Areason, Dno, Sno, Sname, tip;
    JTextField AtimeText, AreasonText, DnoText, SnoText, SnameText;
    JButton submit;
    JPanel student;

    public AddLater(int type, UsersInformation user) {
        this.user = user;
        this.type = type;
        // 整个采用流动式布局 很好的适应了表格带来的影响
        setLayout(new FlowLayout());

        table.setModel(mm);
        table.setRowSorter(new TableRowSorter<>(mm));
        JScrollPane js = new JScrollPane(table);
        add(js);

        JLabel lblNewLabel = new JLabel("New label");
        js.setColumnHeaderView(lblNewLabel);

        search();
    }

    private void search() {
        PreparedStatement state;

        // 如果是学生登录
        if (type == 1) {
            try {
                state = connection.prepareStatement("select * from absent where \"Sname\" = '" + user.getName() + "';");
                reShowLaterInformation(state);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (type == 2 || type == 3) {
            try {
                record();
                state = connection.prepareStatement("select * from absent");
                reShowLaterInformation(state);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void record() {
        // 这是宿管添加晚归记录的功能
        Sno = new JLabel("要添加学生的学号:");
        SnoText = new JTextField(10);
        Sname = new JLabel("姓名:");
        SnameText = new JTextField(10);
        Dno = new JLabel("宿舍号:");
        DnoText = new JTextField(10);
        Atime = new JLabel("缺寝时间:");
        AtimeText = new JTextField(10);
        Areason = new JLabel("缺寝原因:");
        AreasonText = new JTextField(10);
        submit = new JButton("添加");
        submit.addActionListener(this);

        student = new JPanel(new GridLayout(8, 1));

        student.add(Sno);
        student.add(SnoText);
        student.add(Sname);
        student.add(SnameText);
        student.add(Dno);
        student.add(DnoText);
        student.add(Atime);
        student.add(AtimeText);
        student.add(Areason);
        student.add(AreasonText);
        student.add(submit);
        add(student);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submit) {
            // 这是宿管添加晚归记录的功能
            try {
                PreparedStatement statement = connection.prepareStatement("insert into absent values(?,?,?,?,?)");
                statement.setString(1, SnoText.getText());
                statement.setString(2, SnameText.getText());
                statement.setString(3, DnoText.getText());
                statement.setString(4, AtimeText.getText());
                statement.setString(5, AreasonText.getText());
                statement.executeUpdate();

                PreparedStatement state = connection.prepareStatement("select * from absent");
                
                reShowLaterInformation(state);

                JOptionPane.showMessageDialog(this, "添加成功!");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "添加失败!");
                e1.printStackTrace();
            }
        }

    }

    private void reShowLaterInformation(PreparedStatement state) throws SQLException {
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
    }
}
