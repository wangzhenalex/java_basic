package org.example.cglibProxy.mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author： zhen wang
 * @date： 2024/3/21
 * @description：
 * @version: 1.0
 */
public class Dao implements IDao {
    @Override
    public void insert() {
        System.out.println("insert into table1 values()");
    }

    @Override
    public void delete() {
        System.out.println("delete from table1 where id = 1");
    }
}
