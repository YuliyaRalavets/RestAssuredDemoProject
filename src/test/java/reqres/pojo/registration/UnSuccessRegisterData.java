package reqres.pojo.registration;

public class UnSuccessRegisterData {
    private String error;

    public UnSuccessRegisterData() {
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public UnSuccessRegisterData(String error) {
        this.error = error;
    }
}
