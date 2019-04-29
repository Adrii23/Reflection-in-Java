package passcReflection;

import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.io.*;
import java.lang.reflect.*;
import java.lang.Class;
 
public class javaReflection {
	
	public static ArrayList<String> getItemsFromJar(String JarName) {		// items from a jar are saved with this list	
		ArrayList<String> listofClasses = new ArrayList<String>();			// an list to contain only the java classes
		
		try {
			JarInputStream JarFile = new JarInputStream(new FileInputStream(JarName));
			JarEntry Jar;
			
			while (true) {
				Jar = JarFile.getNextJarEntry();					// takes all the entries of a jar
				if (Jar == null) {
					break;
				   }
				
				if ((Jar.getName().endsWith(".class"))) {						// takes only the items that are class extension
					String className = Jar.getName().replaceAll("/", ".");		// modifies the path to a class in the form we need for future reflection
					String myClass = className.substring(0,className.lastIndexOf('.'));		// we save the name as package.ClassName
					listofClasses.add(myClass);												// adds the class to the list
				   }
			   }
			JarFile.close();
		    } catch (Exception e) {
		    	System.out.println("Unfortunately, parsing this jar cannot be made possible");   // an exception if parsing failed
		    	}
		
		return listofClasses;				// returns the list with all the class names from the jar
	   }
 
	
	public static void main(String[] args) {
 
		ArrayList<String> myList = getItemsFromJar("/Users/User/Desktop/passc.jar");
		
		for(int i=0 ; i < myList.size() ; i++)
			System.out.println(myList.get(i)); 				// prints all the classes
		
		try {
			for(int i=0 ; i < myList.size() ; i++) {
			String s = myList.get(i);							// takes a class from the list
			int index = s.indexOf('.');							// saves the position of the first dot
			String name = s.substring(index+1);					// saves the class name ( without package. )
			Class<?> myClass =Class.forName(s);					// forName associates the class name with a Class object
			String className = myClass.getName();				// we use getName for the Class object to see if the connection is right
			
			System.out.println("\n* * * * * * * * * * * * * * * * * *\n" + name + " - " + className + "\n");
			
			if(myClass.isInterface())								// test if class is interface
				System.out.println(name + " is interface\n");
			
			if(!myClass.isInterface()) {
			if(Modifier.isAbstract(myClass.getModifiers()))				// test if the class is abstract
				System.out.print(name + " is an abstract class\n");
			
			if(Modifier.isPublic(myClass.getModifiers()))				// test is the class is public
				System.out.println(name + " is a public class\n");
			}
			
			Class<?>[] itfs = myClass.getInterfaces();				// gets all the interfaces a class implements
			if(itfs.length > 0) {
				System.out.print(name + " implements ");			// displays the class and the interface it implements
				for(int abc = 0; abc < itfs.length; ++abc)	
					System.out.print(itfs[abc] + " ");				
			    }
			
			if(myClass.isInterface()) 
				System.out.println("An interface does not extend a class");
			 else {
			Class<?> superClass = myClass.getSuperclass();			// gets the super class is a class extends one
			System.out.println(name + " extends " + superClass);
			}
				
			Field[] getFields = myClass.getDeclaredFields();		// gets all the fields from a class
			for (int aa = 0; aa < getFields.length; ++aa) {
					String fieldName = getFields[aa].getName();
					Class <?> typeClass = getFields[aa].getType();		// returns the field , the name of the field and the type of the field	
					System.out.println("Field: " + fieldName + " of type " + typeClass.getName());
			  }
			
			Method[] ms = myClass.getMethods();							// only public methods or those inherited from a base class
			for (int d = 0; d < ms.length; ++d)	{
				String mname = ms[d].getName();
				Class<?> retType = ms[d].getReturnType();
				if(Modifier.isAbstract(ms[d].getModifiers())) 
				System.out.print("Abstract method : " + mname + " returns " + retType.getName() + " parameters : ( ");
				else if((Modifier.isPublic(ms[d].getModifiers())))
					System.out.print("Public method : " + mname + " returns " + retType.getName() + " parameters : ( ");
				Class<?>[] params = ms[d].getParameterTypes();
			 	for (int e = 0; e < params.length; ++e)
					{
					String paramType = params[e].getName();
					System.out.print(paramType + " ");
					}
				System.out.println(") ");
				}

			Method[] ms2 = myClass.getDeclaredMethods();			//  includes all methods declared by the class itself
			for (int f = 0; f < ms2.length; ++f) {
				String mname = ms2[f].getName();
				Class<?> retType = ms2[f].getReturnType();
				System.out.print("Method : " + mname + " returns " + retType.getName() + " parameters : ( ");
				Class<?>[] params = ms2[f].getParameterTypes();
			 	for (int g = 0; g < params.length; ++g)
					{
					String paramType = params[g].getName();
					System.out.print(paramType + " ");
					}
				System.out.println(") ");
				}

			Constructor<?>[] ctors = myClass.getConstructors();				// gets all the constructors of a class
			for (int b= 0; b < ctors.length; ++b) {
				System.out.print("Constructor (");
				Class<?>[] params = ctors[b].getParameterTypes();
				for (int c = 0; c < params.length; ++c)
					{
					String paramType = params[c].getName();
					System.out.print(paramType + " ");
					}
				System.out.println(")");
				}
			
		}
	}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		    }
	    
	}
}