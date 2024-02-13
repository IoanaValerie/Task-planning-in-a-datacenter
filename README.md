# Task-planning-in-a-datacenter
Homework2 -> Parallel and Distributed Algorithms

MyDispatcher:
  In this class I have implemented the addTask method for each type of algorithm,
using synchronized, to make sure that multiple threads don't send
tasks at the same time.

MyHost:
  In this class, I created the task execution logic, using
sleep, and I stored the tasks in a priority queue: priorityQueue,
by priority, start time, and finally by ID. Every time I have
accessed the queue I used synchronized, considering
that 2 commands can be executed on it at the same time. I implemented the methods:
1) shutdown, which has the role of stopping the execution of the node. To do this
I got a member of the private instance shutDown which is initially false, but
when the method is called, it becomes true;
2) getWorkLeft, which calculates the amount of work left, and for the tasks
which are currently running, I used the instance member remainingTimeLeft;
3) getQueueSize, which calculates the number of elements in the queue, and for the tasks
what is currently running, I used the instance member runningTask;
4) addTask, which adds tasks to the priority queue;
5) executeTask, where Thread.sleep is used for a number of seconds and I calculate
the remaining time of each task in case it is interrupted by another, but also to
set the remaining work of each, to know when to stop the execution,
but also for the last set of tests, to know the remaining workload
of each task that is running and is no longer in the priority queue;
6) run, which realizes the task execution logic: in while I removed one node from the queue and
I checked if it is preemptible or not:
    - non-preemptive: I execute it, then move on to the next one;
    - preemptible: I call executeTask which executes it, but if in the queue
appears another task, with a higher priority than it, it stops executing, setting
the remaining work and adding it to the queue, and then all the tasks with
higher priority than him are executed;
