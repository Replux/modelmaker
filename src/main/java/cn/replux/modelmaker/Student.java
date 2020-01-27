package cn.replux.modelmaker;


import cn.replux.modelmaker.annotation.AllArgsConstructor;
import cn.replux.modelmaker.annotation.Builder;
import cn.replux.modelmaker.annotation.Data;
import cn.replux.modelmaker.annotation.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String name;
    private int age;
    private boolean male;
}
