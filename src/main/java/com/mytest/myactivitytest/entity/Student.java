package com.mytest.myactivitytest.entity;

//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/26 11:05
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@TableName("student")
public class Student {
    //@TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;
    //@TableField("name")
    private String name;
    //@TableField("age")
    private String age;
}
