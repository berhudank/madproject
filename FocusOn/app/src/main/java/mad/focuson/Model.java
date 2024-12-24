package mad.focuson;

import java.util.ArrayList;

import mad.focuson.interfaces.ModelListener;

// This should have only one instance throughout the app
public class Model {
    private static volatile Model INSTANCE = null;
    private static volatile ArrayList<ModelListener> modelListeners;
    private ArrayList<Task> tasks = new ArrayList<>();

    // private constructor to prevent instantiation of the class
    private Model() {

    }

    // public static method to retrieve the singleton instance
    public static Model getInstance() {
        // Check if the instance is already created
        if(INSTANCE == null) {
            // synchronize the block to ensure only one thread can execute at a time
            synchronized (Model.class) {
                // check again if the instance is already created
                if (INSTANCE == null) {
                    // create the singleton instance
                    INSTANCE = new Model();

                }
            }
        }
        // return the singleton instance
        return INSTANCE;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void appendTask(Task task){
        tasks.add(task);
    }
}
