package application;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Pattern;


import javax.swing.*;

public class FirstWindow{
    static JTextField textfield1, textfield2, textfield3;
    static String emailAdress,filePathPic;
    String validFailPath,ValidEmail,Check;
  public   static int flag1=0,flag2=0;
	public static String valid="nothing";
	private static int tryAgain=1;
     public static void main(String[] args) {
       
    	 
    	 
    	 while(tryAgain==1) {
    	 @SuppressWarnings({ "unchecked", "rawtypes" })
		JList list = new JList(new String[] {"Start", "Edit data"});
    	 JOptionPane.showMessageDialog(
    	   null, list, "Welcome :)", JOptionPane.PLAIN_MESSAGE);
    

    	 
    	 if(Arrays.toString(list.getSelectedIndices()).equals("[0]")){		// choose first option from the list
		 System.out.println("Start");
		 Path path = Paths.get("E:\\a.txt");

		 if (Files.exists(path)) {
			 //emailAdress
			 
			  try {
				  int cnt=0;
			        BufferedReader inFile = new BufferedReader(new FileReader("E:\\a.txt"));
			        String str;
			        while ((str = inFile.readLine()) != null){
			        	if(cnt==0) {
			        		emailAdress=str;
				        	System.out.println("emailAdress "+ str);

			        	}
			        	else {
			        		filePathPic=str;
			        	System.out.println("filePathPic"+ str);
			        	}
			   
			   cnt++;
			        }
			        inFile.close();
			    } catch (IOException e) {
			    }
			  
		 }

		 if (Files.notExists(path)) {
			 
				JOptionPane optionPane = new JOptionPane("You need to insert where do you want save the picture ", JOptionPane.ERROR_MESSAGE);    
          		JDialog dialog = optionPane.createDialog("Failure");
          		dialog.setAlwaysOnTop(true);
          		dialog.setVisible(true);    
          		//System.exit(0);	
    	      	tryAgain=2;

          		
		 }
    	if(tryAgain==2) {//if file path doesnot exist start over again th e while loopp
    		tryAgain=1;
    	}
    	else {
	      	valid="OK";
	      	tryAgain=0;
    	}
    	 

    	 }
		 else {
    	  String m1 = JOptionPane.showInputDialog("Insert your email");
    	if(m1==null) {
    		JOptionPane optionPane = new JOptionPane("You Need to start again", JOptionPane.ERROR_MESSAGE);    
      		JDialog dialog = optionPane.createDialog("Failure");
      		dialog.setAlwaysOnTop(true);
      		dialog.setVisible(true);    
      		System.exit(0);
      		
    	}
    	 	 if((!(m1.isEmpty()))&&isValidEmail1(m1)) {
    	          	System.out.println("Text is "+m1);
    	          	emailAdress=m1;
    	          
    	          	flag1=1;
    	          
    	     	 }
    	 	 
    	  String m2 = JOptionPane.showInputDialog("Insert path to save pic");
     
    		if(m2==null) {
        		JOptionPane optionPane = new JOptionPane("You Need to start again", JOptionPane.ERROR_MESSAGE);    
          		JDialog dialog = optionPane.createDialog("Failure");
          		dialog.setAlwaysOnTop(true);
          		dialog.setVisible(true);    
          		System.exit(0);
          		
        	}
     	 if((!m2.equals(""))&&isValidFilePath(m2)) {
           	System.out.println("Text is "+m2);
     	 filePathPic=m2;
       		//f.setVisible(false);
       	//	f.dispose();
       		flag2=1;
       		
     	 }
     	 
      	 if(flag1==1&&flag2==1) {
             valid=new String();
         	flag1=0;
         	flag2=0;
         	valid="OK";
         	tryAgain=0;
       			String content =  emailAdress+"\n"+filePathPic;
            	 String path = "E:\\a.txt";
         	 try {
     			Files.write( Paths.get(path), content.getBytes());
     		} catch (IOException e) {
     			// TODO Auto-generated catch block
     			e.printStackTrace();
     		}
        }
      	 else {
      		JOptionPane optionPane = new JOptionPane("Wrong input", JOptionPane.ERROR_MESSAGE);    
      		JDialog dialog = optionPane.createDialog("Failure");
      		dialog.setAlwaysOnTop(true);
      		dialog.setVisible(true);
      		 tryAgain=1;
      	 }

		 
          System.out.println(m1+" :"+m2);
          }
    	 
    	 }
     }
     

     
     public static boolean isValidEmail1(String email) 			// check if the email is valid
     { 
         String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                             "[a-zA-Z0-9_+&*-]+)*@" + 
                             "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                             "A-Z]{2,7}$"; 
                               
         Pattern pat = Pattern.compile(emailRegex); 
         if (email == null) 
             return false; 
         return pat.matcher(email).matches(); 
     } 
     
     public static boolean isValidFilePath(String path) {			// check if the File path is valid
    	    File f = new File(path);
    	    if (!f.isDirectory()) {
	    		System.out.println("noo");
	    		return false;
    	    
    	    }
    	    	if (f.exists()){
    	    		System.out.println("exist");
    	    	}
				return true;
    	  }

}
