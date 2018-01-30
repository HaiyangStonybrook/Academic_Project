/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transaction;

/**
 *
 * @author hy
 */
public interface jTPS_Transaction {
     public void doTransaction();
    public void undoTransaction();;
}
