
/**
 * 这个类定义了一个用户对象，包含了用户的名称、密码和类型。它提供了以下功能： 
 * 1. 设置和获取用户的名称 2. 设置和获取用户的密码 3.设置和获取用户的类型 
 * 通常，在一个应用程序中，我们会创建多个用户对象，用于进行身份验证和授权操作。
 * 这个类可以作为用户对象的基础类，可以被其他类继承和扩展。
 * 
 * @author taoa
 *
 */
public class UsersInformation {
    private String name;
    private String password;
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
