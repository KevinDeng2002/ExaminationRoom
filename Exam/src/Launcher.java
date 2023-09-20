import util.AccountManager;

import java.io.File;

//启动
public class Launcher {
    public static AccountManager accountManager = new AccountManager();
    public static QuestionBank questionBank = new QuestionBank();

    public static void main(String[] args) {
        // 初始化两个可以使用的账户
        accountManager.put("user1","12345678");
        accountManager.put("admin","admin");
        questionBank.load(new File("test.txt"));
        // 先初始化账户再加载界面
        GUI.show();
    }
}

