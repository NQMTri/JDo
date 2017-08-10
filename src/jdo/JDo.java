/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdo;
import gnu.getopt.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Main program of the JDo.
 * @author NQMTri
 */
public class JDo {
    private static final String TODO_FILE_NAME =  "TODO_FILE.json";
       
    private static void printOut(String[] args, TodoList tdlist){
        // Get command line arguments
        //System.out.print(args);
        if(args.length<1){
            args=new String[1];
            args[0] = "-a";
        }
        
        Getopt getopt = new Getopt("JDo", args, "a");
        int c=0;        
        //String optArg = null;
        try{
            while((c=getopt.getopt()) != -1){
                switch(c){
                    case 'a':    
                        //System.out.println("haha");
                    default:
                        for(int i=0; i < tdlist.getSize(); ++i){
                            System.out.format("%1$5d : %2$s\n", i, 
                                    tdlist.getTodo(i).getContent());
                        }
                        break;               
                        //System.out.println("Incorrect argument(s)");                    
                }
            }        
        }
        catch(NullPointerException ex){
            System.out.println(ex);
            System.out.println("Input argument is false");
        }
        
    }
    
    private static boolean removeTodo(String[] args, TodoList tdlist){
        return true;
    }
    
    // TODO : Convert each action into a class
    private static boolean createTodo(String[] args, TodoList tdlist){
        // Check for error, must have at least 2 arguments
        if(args.length<1) {
            System.out.println("Error: insufficient arguments!");
            return false;
        }
        // get and check todo content;
        String content = args[0].trim();
        if(content.length()<1){
            System.out.println("TODO is empty, aborting!");
            return false;
        }           

        // Create new todo with given content;
        Todo result = new Todo(content,"");       
        
        // Get command line arguments
        Getopt getopt = new Getopt("JDo", args, "d:t:");
        int c=0;        
        String optArg = null;
        while((c=getopt.getopt()) != -1){
            // This is put here since every argopt must have given argument.
            optArg = getopt.getOptarg().trim();
            switch(c){
                // tags arguments.
                case 't':
                    result.setTags(optArg);
                    break;
                // deadline arguments.
                case 'd':
                    try{
                        // Parse given date to set deadline.
                        DateFormat df = new SimpleDateFormat("dd/mm/yyyy",Locale.ENGLISH);
                        Date deadline = df.parse(optArg);
                        result.setDeadline(deadline.getTime());
                    }
                    catch(NullPointerException | IllegalArgumentException ex){
                        System.out.println(ex);
                        System.out.println("Given argument is not correct!");
                    } catch (ParseException ex) {
                        System.out.println(ex);
                        System.out.println("Cannot parse the given date!" + 
                                " The correct format is 'dd/mm/yyyy'.");
                    }
                    break;
                default:
//                    for(int i=0; i < tdlist.getSize(); ++i){
//                        System.out.format("%1$5d : %2$s", i, tdlist.getTodo(i));                        
//                    }
                      //  System.out.println("Incorrect argument(s)");
                    break;                    
            }
        }        
        tdlist.addNewTodo(result);
        
        System.out.println("Todo added successfully!");
        
        return true;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TodoList tdlist = new TodoList(TODO_FILE_NAME);
        boolean isChanged = false;
        if(args.length < 1){
            printOut(args,tdlist);
            return;
        }
        String[] argv = args.length<1?null:new String[args.length-1];
        System.arraycopy(args, 1,argv, 0, argv.length);
        // TODO : Change this structure with state pattern.
        if(args[0].equals("new")){
            isChanged = createTodo(argv,tdlist);
        }
        else if(args[0].equals("del")){
            isChanged = removeTodo(argv,tdlist);
        }
        else if(args[0].equals("print")){
            printOut(argv,tdlist);
        }
        else{
            System.out.println("There is no argument match with the list." + 
                    " Please refers the documentation.");
        }
         
        // Write back to file whenever there is any change.
        if(isChanged) tdlist.writeToFile();
    }    
}
