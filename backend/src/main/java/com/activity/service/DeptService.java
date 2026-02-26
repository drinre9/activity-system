package com.activity.service;

import com.activity.entity.Dept;
import com.activity.mapper.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DeptService {
    
    @Autowired
    private DeptMapper deptMapper;
    
    public List<Dept> getAllDepts() {
        return deptMapper.findAll();
    }
    
    public Dept getDeptById(Long deptId) {
        return deptMapper.findById(deptId);
    }
    
    public int addDept(Dept dept) {
        Dept exist = deptMapper.findByName(dept.getDeptName());
        if (exist != null) {
            throw new RuntimeException("部门名称已存在");
        }
        return deptMapper.insert(dept);
    }
    
    public int deleteDept(Long deptId) {
        return deptMapper.delete(deptId);
    }
}
