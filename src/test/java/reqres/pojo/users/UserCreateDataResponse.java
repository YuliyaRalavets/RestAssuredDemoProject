package reqres.pojo.users;

public class UserCreateDataResponse extends UserCreateData {
    private String createdAt;
    private Integer id;

    public UserCreateDataResponse(){
        super();
    }

    public UserCreateDataResponse(String createdAt, Integer id) {
        this.createdAt = createdAt;
        this.id = id;
    }

    public UserCreateDataResponse(String name, String job, String createdAt, Integer id) {
        super(name, job);
        this.createdAt = createdAt;
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
