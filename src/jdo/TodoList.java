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
                System.out.println(tmp.toString());
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
    
    
}
