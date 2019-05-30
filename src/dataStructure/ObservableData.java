package dataStructure;


import java.util.Observable;

public class ObservableData extends Observable {
    public boolean changed = false;

    @Override
    public synchronized boolean hasChanged() {
        return true;
    }

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
        System.out.println("OBSERVERS NOTIFIED: " + countObservers());
    }
}
