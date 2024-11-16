public class App {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("server")) {
            QuizServer server = new QuizServer();
            server.start();
        } else {
            QuizClient client = new QuizClient();
            client.start();
        }
    }
}
