Tema 2 APD
Nume: Ciobotea Ioana-Valeria
Grupa: 333CC

      MyDispatcher:
  In aceasta clasa am implementat metoda addTask pentru fiecare tip de algoritm,
folosind synchronized, pentru a ma asigura ca mai multe thread-uri nu trimit
task-uri in acelasi timp.

      MyHost:
  In aceasta clasa am realizat logica de executare a task-urilor, utilizand
sleep, iar task-urile le am stocat intr-o coada de prioritati: priorityQueue,
dupa prioritate, timp de start, iar in final dupa ID. De fiecare data cand am 
accesat coada am folosit synchronized, pentru corectitudine, avand in vedere
ca in acelasi timp pot fi executate 2 comenzi asupra ei. Am implementat metodele:
1) shutdown, care are rolul de a opri executia nodului. Pentru a face acest lucru
mi-am luat un membru de instanta privat shutDown care initial este false, iar
cand metoda este apelata se face true;
2) getWorkLeft, care calculeaza cantitatea de munca ramasa, iar pentru task-urile
care ruleaza in prezent am folosit membrul de instanta remainingTimeLeft;
3) getQueueSize, care calculeaza numarul elementelor din coada, iar pentru task-urile
ce ruleaza in prezent am folosit membrul de instanta runningTask;
4) addTask, care adauga task-uri in coada cu prioritati;
5) executeTask, unde se foloseste Thread.sleep cate un nr de secunde si calculez 
timpul ramas al fiecarui task in caz ca este intrerupt de altul, dar si pentru a 
seta munca ramasa a fiecaruia, pentru a sti cand sa ma opresc din executie, 
dar si pentru ultimul set de teste, pentru a sti volumul de munca ramas
al fiecarui task care ruleaza si nu se mai gaseste in coada de prioritati;
6) run, care realizeaza logica de executare a task-urilor: in while am scos cate un nod din coada,
am verificat daca este preemptibil sau nu:
    - nepreemptibil: il execut, dupa care trec la urmatorul;
    - preemptibil: apelez executeTask care il executa, dar daca in coada
apare un task cu o prioritate mai mare decat el, se opreste din executie ii setez 
munca ramasa si il adaug in coada, iar apoi se executa toate task-urile cu
prioritate mai mare decat el;
