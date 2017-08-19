/*
 * The MIT License
 *
 * Copyright 2017 NQMTri.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
    
    private static Integer getInteger(String param){
        Integer result = null;
        try{
            result = Integer.valueOf(param);
        }
        catch(NumberFormatException exc){
            System.out.println(exc);
            System.out.println("The passed argument is not in correct Integer format.");
        }
        return result;
    }
       
    private static void printOut(String[] args, TodoList tdlist){
        // Get command line arguments
        
        if(args==null||args.length<1){
            args=new String[1];
            args[0] = "-a";
        }
        System.out.println(args.length);
        Getopt getopt = new Getopt("JDo", args, "a");
        int c=0;        
        String optArg = null;
        try{
            while((c=getopt.getopt()) != -1){
                switch(c){
                    case 'a':    
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
        // Check for error, must have at least 2 arguments
        if(args.length<1) {
            System.out.println("Error: insufficient arguments!");
            return false;
        }
        Integer idx = null;
        try{
            idx = Integer.valueOf(args[0]);
            if(idx<0) idx+= tdlist.getSize();
            tdlist.removeTodo(idx);
        }
        catch(NumberFormatException|IndexOutOfBoundsException ex){
            System.out.println(ex);
            System.out.println("The given index is not found!");
            return false;
        }
        System.out.format("Todo at index %1$d is deleted!\n", idx);
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
    
    private static boolean moveTodo(String[] args, TodoList tdlist, char opType){
        // Check for error, must have exactly 3 arguments
        if(args.length!=2) {
            System.out.println("Error: incorrect number of arguments! Only 2 args");
            return false;
        }
        
        // Read and convert arguments.
        Integer idx = getInteger(args[0]), position = getInteger(args[1]);
        if(idx==null || position==null){
            return false;
        }
        boolean result = false;
        switch(opType){
            case 's':
                result = tdlist.swapTodo(idx,position);
                break;
            case 'm':
                result = tdlist.swapTodo(idx,idx + position);
                break;
            default:
                System.out.println("OPCODE Error: " + 
                        "Something is wrong in the given type of operation!");
                result = false;
                break;
        }
        return result;
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
        else if(args[0].equals("move")){
            isChanged = moveTodo(argv, tdlist, 'm');
            printOut(null,tdlist);
        }
        else if(args[0].equals("swap")){
            isChanged = moveTodo(argv, tdlist, 's');
            printOut(null,tdlist);
        }
        else{
            System.out.println("There is no argument match with the list." + 
                    " Please refers the documentation.");
        }
        // Write back to file whenever there is any change.
        if(isChanged) tdlist.writeToFile();
    }    
}
