package teclan.springboot.exception;

/**
 * @ClassName: UnImplementException
 * @Description: TODO
 * @Author: Teclan
 * @Date: 2019/1/4 10:57
 **/
public class UnImplementException extends Exception {

    public UnImplementException() {
        super("接口未实现");
    }

    public UnImplementException(String message) {
        super(message);
    }

    public UnImplementException(String message, Throwable cause) {
        super(message, cause);
    }
}
