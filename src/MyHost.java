/* Implement this class. */

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class MyHost extends Host {


    public PriorityQueue<Task> priorityQueue = new PriorityQueue<>(
            Comparator.comparingInt(Task::getPriority)
                      .reversed()
                      .thenComparingInt(Task::getStart)
                      .thenComparingLong(Task::getId)
    );

    boolean shutDown = false;

    private double remainingTimeLeft = 0;

    private int runningTask = 0;

    @Override
    public void run() {
        if (!shutDown) {
            /** Implementare executie task-uri. */
            while (true) {
                if (shutDown)
                    break;

                /** Scot un task din coada. */
                Task currentTask;
                synchronized (priorityQueue) {
                    if (priorityQueue.isEmpty()) {
                        continue;
                    }
                    currentTask = priorityQueue.poll();
                    if (currentTask == null || currentTask.getLeft() <= 0) {
                        continue;
                    }
                }

                /** Verific daca task-ul este preemptibil sau nu si apelez executeTask in functie de asta. */
                if (!currentTask.isPreemptible() && currentTask.getLeft() > 0) {
                    runningTask++;
                    executeTask(currentTask);
                    continue;
                }
                if (currentTask.isPreemptible()) {
                    runningTask++;
                    executeTask(currentTask);
                    if (!priorityQueue.isEmpty() && currentTask.getLeft() > 0) {
                        while (priorityQueue.peek() != currentTask) {
                            runningTask++;
                            Task nextTask;
                            nextTask = priorityQueue.peek();
                            executeTask(nextTask);
                            synchronized (priorityQueue) {
                                priorityQueue.poll();
                            }
                        }
                    }
                }
            }
        }
    }

    /** Executa un task. */
    private void executeTask(Task task) {
        double remainingTime = task.getLeft();
        remainingTimeLeft = remainingTime;

        while (remainingTime > 0) {
            /** Daca task-ul e preemptibil si afost intrerupt ii
             * salvez volumul de munca ramas si il adaug in coada. */
            if (!priorityQueue.isEmpty() && task.getPriority() < priorityQueue.peek().getPriority() && task.isPreemptible()) {
                synchronized (priorityQueue) {
                    task.setLeft((long) remainingTime);
                    this.addTask(task);
                    remainingTimeLeft = 0;
                    runningTask --;
                }
                break;
            }

            /** Sleep pentru a simula executarea task-ului si
             * calculul volumului de munca ramas la fiecare pas. */
            try {
                double sleepStartTime = System.currentTimeMillis();
                double sleepTime = Math.min(500, remainingTime);
                sleep((long) sleepTime);
                double sleepEndTime = System.currentTimeMillis();

                remainingTime -= (sleepEndTime - sleepStartTime);
                remainingTimeLeft = remainingTime;
            } catch (InterruptedException e) {
                currentThread().interrupt();
                break;
            }
        }
        synchronized (priorityQueue) {
            task.setLeft((long) Math.max(0, remainingTime));
        }

        /** Daca task-ul nu mai are volum de munca
         * va fi apelata metoda finish din clasa Task pentru
         * a determina timpul de finish al task-ului. */
        if (remainingTime <= 0) {
            runningTask--;
            task.finish();
        }
    }

    @Override
    public void addTask(Task task) {
        synchronized (this) {
            priorityQueue.offer(task);
        }
    }

    @Override
    public int getQueueSize() {
        synchronized (this) {
            return priorityQueue.size() + runningTask;
        }
    }

    @Override
    public long getWorkLeft() {
        synchronized (this) {
            long workLeft = 0;

            for (Task task : priorityQueue) {
                workLeft += task.getLeft();
            }

            return (long) (workLeft + remainingTimeLeft);
        }
    }

    @Override
    public void shutdown() {
        shutDown = true;
    }
}
