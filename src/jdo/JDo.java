/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdo;
import gnu.getopt.*;
/**
 * Main program of the JDo.
 * @author NQMTri
 */
public class JDo {
    private static final String TODO_FILE_NAME =  "TODO_FILE.json";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TodoList tdlist = new TodoList(TODO_FILE_NAME);
        
        // Get command line arguments
        Getopt getopt = new Getopt("JDo", args, "hd:c:");
        int c=0;
        boolean isChanged = false;
        String optArg = null;
        while((c=getopt.getopt()) != -1){
            switch(c){
                case 'h':
                    System.out.print("HELP");
                    break;
                case 'd':
                    optArg = getopt.getOptarg();
                    isChanged = true;
                    System.out.println("You delete item " + optArg);
                    // TODO : Implement logic here
                    break;
                case 'c':
                    optArg = getopt.getOptarg();
                    isChanged = true;
                    System.out.println("You created item with content " + optArg);
                    // TODO : Implement logic here
                    break;
                default:
                    System.out.println("Incorrect argument(s)");
            }
        } 
        // Write back to file whenever there is any change.
        if(isChanged) tdlist.writeToFile();
    }    
}
