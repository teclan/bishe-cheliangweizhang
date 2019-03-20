package teclan.springboot.model;


public class User {

	private String id;
	private String post;
	private int age;

	public User() {

	}

	public User(String id, String post, int age) {
		this.id = id;
		this.post = post;
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	

}
