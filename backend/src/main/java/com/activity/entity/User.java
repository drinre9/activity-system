package com.activity.entity;

import java.util.Date;

public class User {
    private Long userId;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private String dept;
    private String position;
    private String role;
    private String status;
    private Date createTime;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
