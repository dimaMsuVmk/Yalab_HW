package HW_2.task_StatsAccumulator;

public class StatsAccumulatorImpl implements StatsAccumulator{
    private int count = 0;
    private int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
    private int summ = 0;
    private double avg;

    @Override
    public void add(int value) {
        count++;
        if(value < min) min = value;
        if(value > max) max = value;
        summ = summ + value;
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        return (summ*1.0)/count;
    }
}
