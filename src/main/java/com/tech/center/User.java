package com.tech.center;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "svuser")
public class User {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	private String firstName, lastName, username, password, phoneNumber;
	private int gradeSum, gradeCount;
	
	public User() {}
	
	public User(String firstName, String lastName, String username, String password, String phoneNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.phoneNumber = phoneNumber;
	}
	
	public void updateSum(int grade) {
		this.gradeSum += grade;
	}
	
	public void updateCount() {
		this.gradeCount++;
	}
	

	public long getUserId() {
		return userId;
	}
	
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getGradeSum() {
		return gradeSum;
	}

	public void setGradeSum(int gradeSum) {
		this.gradeSum = gradeSum;
	}

	public int getGradeCount() {
		return gradeCount;
	}

	public void setGradeCount(int gradeCount) {
		this.gradeCount = gradeCount;
	}

	@Override
	public String toString() {
		return firstName + " " + lastName;
	}
	
}
