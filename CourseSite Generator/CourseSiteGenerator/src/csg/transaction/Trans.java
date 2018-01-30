/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

import csg.TAManagerApp;
import java.util.ArrayList;

/**
 *
 * @author hy
 */
public class Trans {
    private ArrayList<jTPS_Transaction> transactions = new ArrayList();
    private int mostRecentTransaction = -1;
    TAManagerApp app;
    
    public Trans(TAManagerApp initApp) {}
    
    public void addTransaction(jTPS_Transaction transaction) {
        // IS THIS THE FIRST TRANSACTION?
        if (mostRecentTransaction < 0) {
            // DO WE HAVE TO CHOP THE LIST?
            if (transactions.size() > 0) {
                transactions = new ArrayList();
            }
            transactions.add(transaction);
        }
        // ARE WE ERASING ALL THE REDO TRANSACTIONS?
        else if (mostRecentTransaction < (transactions.size()-1)) {
            transactions.set(mostRecentTransaction+1, transaction);
            transactions = new ArrayList(transactions.subList(0, mostRecentTransaction+2));
        }
        // IS IT JUST A TRANSACTION TO APPEND TO THE END?
        else {
            transactions.add(transaction);
        }
        mostRecentTransaction++;
    }
    
    public void doTransaction() {
        if (mostRecentTransaction < (transactions.size()-1)) {
            jTPS_Transaction transaction = transactions.get(mostRecentTransaction+1);
            transaction.doTransaction();
            mostRecentTransaction++;
        }
    }
    
    public void undoTransaction() {
        if (mostRecentTransaction >= 0) {
            jTPS_Transaction transaction = transactions.get(mostRecentTransaction);
            transaction.undoTransaction();
            mostRecentTransaction--;
        }
    }
    
    public String toString() {
        String text = "--Number of Transactions: " + transactions.size() + "\n";
        text += "--Current Index on Stack: " + mostRecentTransaction + "\n";
        text += "--Current Transaction Stack:\n";
        for (int i = 0; i <= mostRecentTransaction; i++) {
            jTPS_Transaction jT = transactions.get(i);
            text += "----" + jT.toString() + "\n";
        }
        return text;
    }
    
    public int getMostRecentTransaction(){
        return mostRecentTransaction;
    }
    
    public ArrayList<jTPS_Transaction> getTransactions(){
        return transactions;
    }
    
    public void clearTransaction(){
        transactions = new ArrayList();
        mostRecentTransaction = -1;
       // disableUndo();
       // disableRedo();
    }
    
    public void enableUndo(){
        app.getGUI().enableUndo(true);
    }
    
    public void enableRedo(){
        app.getGUI().enableRedo(true);
    }
    
    public void disableUndo(){
        app.getGUI().enableUndo(false);
    }
    
    public void disableRedo(){
        app.getGUI().enableUndo(false);
    }
}
