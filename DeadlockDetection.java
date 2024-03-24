import java.util.Arrays;

public class DeadlockDetection {

    //arrays for the available resources, allocated resources and also the resource requests
    public int[] available;
    public int[][] allocation;
    public int[][] request;


    //used a constructor for the different datasets
    public DeadlockDetection(int[] available, int[][] allocation, int[][] request) {
        this.available = available;
        this.allocation = allocation;
        this.request = request;
    }


    //main
    public static void main(String[] args){


        //datasets
        //dataset 1
        //every dataset calls on the detector
        new DeadlockDetection(
                new int[]{0, 0, 0, 0, 0},
                new int[][]{
                        {0, 1, 0, 0, 0},
                        {1, 0, 0, 0, 0},
                        {0, 0, 0, 1, 0},
                        {0, 0, 0, 0, 1},
                        {0, 0, 1, 0, 0}
                },
                new int[][]{
                        {1, 0, 0, 0, 0},
                        {0, 0, 1, 1, 1},
                        {0, 0, 0, 0, 1},
                        {0, 1, 0, 0, 0},
                        {0, 0, 0, 0, 0}
                }
        ).deadlockDetectionLogic();

        //dataset 2
        new DeadlockDetection(
                new int[]{0, 0, 0, 1},
                new int[][]{
                        {0, 0, 1, 0},
                        {1, 0, 0, 0},
                        {0, 1, 0, 0}
                },
                new int[][]{
                        {1, 0, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 0, 1}
                }
        ).deadlockDetectionLogic();

        //dataset 3
        new DeadlockDetection(
                new int[]{0, 0, 0},
                new int[][]{
                        {0, 1, 0},
                        {2, 0, 0},
                        {3, 0, 3},
                        {2, 1, 1},
                        {0, 0, 2}
                },
                new int[][]{
                        {0, 0, 0},
                        {2, 0, 2},
                        {0, 0, 1},
                        {1, 0, 0},
                        {0, 0, 2}
                }
        ).deadlockDetectionLogic();

        //dataset 4
        new DeadlockDetection(
                new int[]{0, 0, 0},
                new int[][]{
                        {0, 1, 0},
                        {2, 0, 0},
                        {3, 0, 3},
                        {2, 1, 1},
                        {0, 0, 2}
                },
                new int[][]{
                        {0, 0, 0},
                        {2, 0, 2},
                        {0, 0, 0},
                        {1, 0, 0},
                        {0, 0, 2}
                }
        ).deadlockDetectionLogic();
    }


    //logic behind the detection
    public void deadlockDetectionLogic(){

        //determine the number of resources and processes by implementing .length
        int numberOfResources = available.length;
        int numberOfProcesses = allocation. length;


        //copying the available resources into the work array
        int[] work = Arrays.copyOf(available, numberOfResources);
        //finish array will tell us if a process can continue or not
        boolean[] finish = new boolean[numberOfProcesses];

        //loop for the finish array
        for (int i = 0; i < numberOfProcesses; i++){
            //use stream to convert array to a stream of integers and then compare the sum of the stream to zero
            finish[i] = Arrays.stream(allocation[i]).sum() == 0;
        }

        //keep looping until to determine if the processes can continue or not and whether there are deadlocks or not
        while (true){
            //set it false first
            boolean foundDeadlock = false;

            //loop through all the processes to check if the requested resources can be allocated
            for (int i = 0; i < numberOfProcesses; i++){

                //this checks if, first the process hasn't finished yet, and then checks whether the requested resources can be allocated by the current work by checking the work array
                if (!finish[i] && lessThanOrEqual(request[i], work)){
                    //update the work array if it applies
                    for (int j = 0; j < numberOfResources; j++) {

                        work[j] = work[j] + allocation[i][j];
                    }

                    //put the process as finished and that a deadlock was found
                    finish[i] = true;
                    foundDeadlock = true;
                    break;
                 }
            }

            //break if there was no progess made and thus all the remaining processes are in a deadlock state
            if (!foundDeadlock){
                break;
            }
        }

        //now we check for deadlock by looping through the finish array
        boolean deadlockFound = false;

        for (int i = 0; i < finish.length; i++){
            if (!finish[i]){
                deadlockFound = true;
                break;
            }
        }

        //output the results
        outputResult();

        //correct output format
        if (deadlockFound){
            System.out.println("\nSystem is deadlocked, the deadlocked processes are:");

            for (int i = 0; i < numberOfProcesses; i++){
                if (!finish[i]){
                    System.out.println("P" + i);
                }
            }
        } else {
            System.out.println("\nSystem is not in deadlock, all processes can complete");
        }



    }

    //simple logic to check whether the work array's values are less than or equal to the request array
    public boolean lessThanOrEqual(int[] request, int[] work){

        for (int i = 0; i < request.length; i++){

            if (request[i] > work[i]){
                return false;
            }
        }

        return true;
    }

    //correct output
    public void outputResult() {

        System.out.println("\nInitial System State:");

        System.out.println("\nAllocated:");

        for (int i = 0; i < allocation.length; i++){

            System.out.print("P" + i + ": ");

            System.out.println(Arrays.toString(allocation[i]));
        }

        System.out.println("\nAvailable:");

        for (int i = 0; i < available.length; i++){

            System.out.print("R" + i + " = " + available[i] + " ");;
        }

        System.out.println("\n\nRequested:");

        for (int i = 0; i < request.length; i++){

            System.out.print("P" + i + ": ");

            System.out.println(Arrays.toString(request[i]));
        }
    }


    }



