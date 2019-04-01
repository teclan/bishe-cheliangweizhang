package teclan.springboot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	private int id;
	private String code;
	private  String name;
	private  String password;

	@JsonProperty("id_card")
	private String idCard;
	private int age;
	private  String phone;
	private String role;
	@JsonProperty("limited_period_from")
	private Date limitedPeriodFrom;
	@JsonProperty("limited_period_to")
	private Date limitedPeriodTo;

	public User() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Date getLimitedPeriodFrom() {
		return limitedPeriodFrom;
	}

	public void setLimitedPeriodFrom(Date limitedPeriodFrom) {
		this.limitedPeriodFrom = limitedPeriodFrom;
	}

	public Date getLimitedPeriodTo() {
		return limitedPeriodTo;
	}

	public void setLimitedPeriodTo(Date limitedPeriodTo) {
		this.limitedPeriodTo = limitedPeriodTo;
	}
}
