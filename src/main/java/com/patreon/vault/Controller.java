import static spark.Spark.*;

public class Controller {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}