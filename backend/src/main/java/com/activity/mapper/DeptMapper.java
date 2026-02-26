package com.activity.mapper;

import com.activity.entity.Dept;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface DeptMapper {
    
    @Select("SELECT * FROM sys_dept ORDER BY dept_id")
    List<Dept> findAll();
    
    @Select("SELECT * FROM sys_dept WHERE dept_id = #{deptId}")
    Dept findById(@Param("deptId") Long deptId);
    
    @Select("SELECT * FROM sys_dept WHERE dept_name = #{deptName}")
    Dept findByName(@Param("deptName") String deptName);
    
    @Insert("INSERT INTO sys_dept(dept_name) VALUES(#{deptName})")
    @Options(useGeneratedKeys = true, keyProperty = "deptId")
    int insert(Dept dept);
    
    @Delete("DELETE FROM sys_dept WHERE dept_id = #{deptId}")
    int delete(@Param("deptId") Long deptId);
}
