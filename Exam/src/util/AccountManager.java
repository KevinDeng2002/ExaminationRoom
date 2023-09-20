package util;

import java.util.HashMap;

// 账户的初始化在launcher中
// 实现一个账号管理的类，可以配合序列化实现账号存储

public class AccountManager {
    private final HashMap<String,String> accounts;

    public AccountManager(){
        accounts = new HashMap<>();
    }

    public void put(String username,String password){
        accounts.put(username, password);
    }

    public void remove(String username){
        accounts.remove(username);
    }

    public boolean check(String username,String password){
        for(String user: accounts.keySet()){
            if(user.equals(username)&&password.equals(accounts.get(username))){
                return true;
            }
        }
        return false;
    }
}
