/* Implement this class. */

import java.util.Iterator;
import java.util.List;

public class MyDispatcher extends Dispatcher {

    private volatile int ID = 0;

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
    }

    @Override
    public void addTask(Task task) {
        /** RR */
        if (algorithm == SchedulingAlgorithm.ROUND_ROBIN) {
            synchronized (hosts) {
                hosts.get(ID).addTask(task);
                ID = (ID + 1) % hosts.size();
            }
        }

        /** SQ */
        if (algorithm == SchedulingAlgorithm.SHORTEST_QUEUE) {
            synchronized (hosts) {
                Host minimQueueHost = hosts.get(0);
                for (Host host : hosts) {
                    if (host.getQueueSize() < minimQueueHost.getQueueSize()) {
                        minimQueueHost = host;
                    }
                }
                minimQueueHost.addTask(task);
            }
        }

        /** SITA */
        if (algorithm == SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT) {
            synchronized (hosts) {
                if (task.getType() == TaskType.SHORT) {
                    hosts.get(0).addTask(task);
                }

                if (task.getType() == TaskType.MEDIUM) {
                    hosts.get(1).addTask(task);
                }

                if (task.getType() == TaskType.LONG) {
                    hosts.get(2).addTask(task);
                }
            }
        }

        /** LWL */
        if (algorithm == SchedulingAlgorithm.LEAST_WORK_LEFT) {
            synchronized (hosts) {
                Host minimWorkLeftHost = hosts.get(0);
                for (Host host : hosts) {
                    if (host.getWorkLeft() < minimWorkLeftHost.getWorkLeft()) {
                        minimWorkLeftHost = host;
                    }
                }
                minimWorkLeftHost.addTask(task);
            }
        }
    }
}
