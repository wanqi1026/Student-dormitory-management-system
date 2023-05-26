
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
 * 这个类是一个学生晚归记录查询的功能实现，它继承了JPanel类，并实现了ActionListener接口。 
 * 它包括了一个JTable表格用于显示查询结果，
 * 一个查询面板用于宿管查询某个学生的晚归记录，以及一个search()方法用于查询所有学生的晚归记录。
 * 在查询面板中，用户需要输入学生的学号，程序会根据学号查询数据库中该学生的晚归记录，并将结果显示在面板中。
 * 在查询所有学生的晚归记录时，程序会查询数据库中所有的晚归记录，并将结果显示在JTable表格中。
 * 同时，程序还提供了一个按钮，用于刷新查询结果。
 */
public class SeekLater extends JPanel implements ActionListener {
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

    public SeekLater(int type, UsersInformation user) {
        this.user = user;
        this.type = type;
        // 整个采用流动式布局 ,很好的适应了表格带来的影响
        setLayout(new FlowLayout());

        table.setModel(mm);
        table.setRowSorter(new TableRowSorter<>(mm));
        JScrollPane js = new JScrollPane(table);
        add(js);
        search();
    }

    private void search() {
        //PreparedStatement state;
        //ResultSet resultSet;
        if (type == 2 || type == 3) {
            try {
                seek();
                /*state = connection.prepareStatement("select * from absent");
                resultSet = state.executeQuery();

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
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void seek() {
        // 这就是宿管查找晚归记录的功能
        Sno = new JLabel("请输入要查学生的学号:");
        SnoText = new JTextField(10);
        Sname = new JLabel("姓名:");
        SnameText = new JTextField(10);
        Dno = new JLabel("宿舍号:");
        DnoText = new JTextField(10);
        Atime = new JLabel("缺寝时间:");
        AtimeText = new JTextField(10);
        Areason = new JLabel("缺寝原因:");
        AreasonText = new JTextField(10);
        submit = new JButton("查找");
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
            PreparedStatement state;
            ResultSet resultSet;
            try {
                state = connection.prepareStatement("select * from absent where \"Sno\" = '" + SnoText.getText() + "';");
                resultSet = state.executeQuery();
               /* while (resultSet.next()) {
                    // suse.setText("电话");
                    SnameText.setText(resultSet.getString("Sname"));
                    DnoText.setText(resultSet.getString("Dno"));
                    AtimeText.setText(resultSet.getString("Atime"));
                    AreasonText.setText(resultSet.getString("Areason"));
                }*/
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
                JOptionPane.showMessageDialog(this, "查询成功!");
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(this, "查询失败!");
                e1.printStackTrace();
            }
        }

    }
}