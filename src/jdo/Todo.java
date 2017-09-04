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

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author NQMTri
 */
public class Todo {
    private static String OUTPUT_DATE_FORMAT = "dd-MM-yyyy";
    // Deadline notification line is set to 2 days before final date.
    private static int DEADLINE_NOTIFICATION = 2;
    private long timestamp;
    private String content;
    private String tags;
    private long deadline;
    
    /**
     * Constructor for Todo class.
     * @param content The content of the todo in text. 
     * @param tags Tags given to that todo, each tag start with "#".
     * @param deadline The deadline in epoch time.
     */
    public Todo(String content, String tags, long deadline){
        this.timestamp = System.currentTimeMillis()/1000;
        this.content = content;
        this.tags = tags;
        this.deadline = deadline;
    }
    
    /**
     * Constructor for Todo class without deadline.
     * @param content The content of the todo in text. 
     * @param tags Tags given to that todo, each tag start with "#".
     */
    public Todo(String content, String tags){
        this(content, tags,-1);
    }
    
    
    
    /**
     * Constructor for Todo class, parse Todo object from a JSONObject.
     * @param jsonObj The given JSONObject to parse from.
     */
    public Todo(JSONObject jsonObj){
        try{
            this.timestamp =jsonObj.getLong("timestamp");
            this.content = jsonObj.getString("content");
            this.tags = jsonObj.getString("tags");
            this.deadline = jsonObj.getLong("deadline");
        }
        // Catch and print out exception generally.
        // TODO: Bad implementation!. Fix later
        catch(NumberFormatException | JSONException e){
            System.out.print(e);
        }        
    }
    
    /**
     * Convert Todo object into JSON object.
     * @return The JSON representation of Todo object.
     */
    public JSONObject toJSON(){
        return new JSONObject().put("timestamp", this.timestamp)
                .put("tags",this.tags)
                .put("content", this.content)
                .put("deadline", this.deadline);        
    }
    
    // Getters and setters 
    // ====================
    
    public String getContent(){
        return this.content;
    }
    
    public void setContent(String content){
        this.content = content;
    }
    

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return the deadline
     */
    public long getDeadline() {
        return deadline;
    }

    /**
     * @param deadline the deadline to set
     */
    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }
    // End getter and setter section
    // ====================        
    
    private boolean isNearDeadline(){
        
        return false;
    }
    
    /**
     * @param printDeadline Print Deadline or not.
     * @param printTags Print todo tags or not
     * @param beautify Print beautify characters or not.
     */
    public String printOut( Boolean printDeadline, Boolean printTags, 
                            Boolean beautify){
        StringBuilder sb = new StringBuilder();
        String divider = " || ";
        
        if(this.isNearDeadline()){
            sb.append("!!!").append(divider);
        }
        
        if(printDeadline){
            String deadline = "    None    ";
            if(this.deadline>0){
                Date dt = new Date(this.deadline*1000L);
                SimpleDateFormat sdf = new SimpleDateFormat(this.OUTPUT_DATE_FORMAT);
                deadline=sdf.format(dt);
            }
            sb.append(deadline).append(divider);
        }
        sb.append(this.content);
        
        if(printTags){  
            sb.append(divider).append(this.tags);
        }
        return sb.toString();
    }
    
}
