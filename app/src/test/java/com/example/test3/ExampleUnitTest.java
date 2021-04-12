package com.example.test3;

import com.example.test3.ui.home.HomeFragment;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(Parameterized.class)
public class ExampleUnitTest {

    private String name;
    private String mail;
    private String password;

    public ExampleUnitTest(String name, String mail, String password) {
        this.name = name;
        this.mail = mail;
        this.password = password;
    }

    @BeforeClass
    public static void setUp() throws Exception
    {
        HomeFragment homeFragment = new HomeFragment();
        //Bmob.initialize(homeFragment.getActivity(),"6f26b60e82795408fabfdf3ffa7d2cc6");

    }

    @Parameterized.Parameters
    @SuppressWarnings("unchecked")
    public static Collection prepareData()
    {
        Object [][] objects =
                {{"zzz", "123456789@qq.com", "123456789"},
                {"znx", "123456789@qq.com", "123456789"},
                {"咕咕秋", "1030926732@qq.com", "123456789"}};
        return Arrays.asList(objects);
    }

    @Test
    public void test()
    {
        String sql = "select * from RegisterMessage";
        List<RegisterMessage> list1 = new ArrayList<>();
        new BmobQuery<RegisterMessage>().doSQLQuery(sql, new SQLQueryListener<RegisterMessage>() {
            @Override
            public void done(BmobQueryResult<RegisterMessage> bmobQueryResult, BmobException e) {
                if (e == null)
                {
                    List<RegisterMessage> list = (List<RegisterMessage>)  bmobQueryResult.getResults();
                    System.out.println("XX");

                }

            }
        });

    }


}