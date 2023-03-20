package HW_2.task_RateLimitedPrinter;

public class RateLimitedPrinter {
    private int interval;
    private long lastTime = 1;
    private long currentTime;

    public RateLimitedPrinter(int interval) {
        this.interval = interval;
    }

    public void print(String message) {
        currentTime = System.currentTimeMillis();
        if ((currentTime - lastTime) > interval) {
            System.out.println(message);
            lastTime = currentTime;
        }
    }

}
