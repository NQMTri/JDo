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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.*;

/**
 *
 * @author NQMTri
 */
public final class TodoList {
    //private final String TODO_FILE_NAME = "TODO_LIST.json";
    private ArrayList<Todo> value;
    private String fileName;
    public TodoList(String inputFile){
        this.value = parseJSONFile(inputFile);
        this.fileName = inputFile;
    }
    
    /**
     * Parse a JSONArray from a json file.
     * @param inputFile The filename of the input file.
     * @return The parsed json Array.
     */
    public ArrayList<Todo> parseJSONFile(String inputFile){
        ArrayList<Todo> result = new ArrayList<>();
        try{            
            JSONTokener tokener = new JSONTokener(new FileReader(inputFile));
            JSONArray todoArray = new JSONArray(tokener);
            for(int i=0 ; i < todoArray.length(); ++i){
                if(todoArray.isNull(i)) continue;
                JSONObject tmp = (JSONObject) todoArray.get(i);
                result.add(new Todo(tmp));
            }
        }
        catch(FileNotFoundException exc){
            System.out.println(exc);
            System.out.println("New empty list of TODO Initialized");
        }                    
        finally{
            return result;
        }
    }
    
    /**
     * Create a JSONArray of the Todo list. 
     * @return A JSONArray of the Todo list as JSONObject.
     */
    public JSONArray toJSON(){
        JSONArray result = new JSONArray();
        this.value.forEach((i) -> {
            result.put(i.toJSON());
        });
        return result;
    }
    
    /**
     * Write TODO JSON to file. 
     */
    public void writeToFile(){
        FileWriter fout = null;
        try{
            fout = new FileWriter(this.fileName);
            this.toJSON().write(fout);
        }
        catch(IOException|JSONException e){
            System.out.println(e);
        }
        finally{
            try{
                if(fout!=null) fout.close();
            }
            catch(IOException ex){
                System.out.println(ex);
            }            
        }
    }
    
    /**
     * Appends new todo to the list. 
     * @param element New Todo object to be added.
     */
    public void addNewTodo(Todo element){
        this.value.add(element);
    }
    
    /**
     * Get ith Todo in the list. If a negative in the range [-list.size(),-1]
     * is given, the element is counted from the end to the start.
     * 
     * @param i the index of the todo item.
     * @return The given todo item.
     */
    public Todo getTodo(int i){
        Todo result = null;
        try{
            if(i<0) i+=this.value.size();
            result = this.value.get(i);
        }
        catch(ArrayIndexOutOfBoundsException ex){
            System.out.println(ex);
        }
        return result;
    }
    
    /**
     * Return the size of the current todo list.
     * @return The size of the current todo list.
     */
    public int getSize(){
        return this.value.size();
    }
    
    /**
     * Remove the Todo item at index idx
     * @param idx The index of Todo item to be deleted.
     */
    public void removeTodo(int idx) throws IndexOutOfBoundsException{
        try{
            this.value.remove(idx);
        }
        catch(IndexOutOfBoundsException ex){
            throw ex;            
        }        
    }
    
    /**
     * Swap Todo item to the new position
     * @param idx Index of the Todo item to move.
     * @param position Index of the position, they will be automatically set to 
     * be the nearest possible position.
     * @return true if operation is done successfully, false otherwise.
     */
    public boolean swapTodo(int idx, int position) throws IndexOutOfBoundsException{
        try{
            position = position<0?0:Math.min(position, this.getSize()-1);
            Todo tmp = this.getTodo(idx);
            this.value.set(idx, this.getTodo(position));
            this.value.set(position,tmp);
        }
        catch(IndexOutOfBoundsException exc){
            System.out.println(exc);
            System.out.println("Given position of the todo item or its new" + 
                    " position is out of bound!");
            return false;
        }
        return true;
    }
}
