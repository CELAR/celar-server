package gr.ntua.cslab.bash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * This class contains code which is  used to execute a bash script through Java.
 * @author Giannis Giannakopoulos
 *
 */
public class Command {

	private String script;
	private Process proc;
	private int exitCode;
	
	private String stdout=null, stderr=null;
	
	/**
	 * Default constructor: doesn't do anything by default.
	 */
	public Command(){
		
	}
	
	/**
	 * Command constructor: a new process is created and is executed.
	 * @param command
	 */
	public Command(String command){
		this.execute(command);
	}
	
	/**
	 * Executes the script.
	 * @param script
	 */
	public void execute(String script){
		this.script=script;
		this.proc = this.initProcess();
	}
	
	public void waitFor(){
		if(this.proc==null)
			return;
		try {
			this.exitCode=this.proc.waitFor();
		} catch (InterruptedException e) {
			this.exitCode=-1;
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the script name.
	 * @return
	 */
	public String getScriptName(){
		return this.script;
	}
		
	/**
	 * Returns the standard output
	 * @return
	 */
	public String getStdout(){
		this.stdout=(this.stdout==null?this.stdout=this.getStream(this.proc.getInputStream()):this.stdout);
		return this.stdout;
	}
	
	/**
	 * Returns the error output.
	 * @return
	 */
	public String getStderr() {
		this.stderr=(this.stderr==null?this.stderr=this.getStream(this.proc.getErrorStream()):this.stderr);
		return this.stderr;	
	}
	
	/**
	 * Returns whether the command terminated successfully or not.
	 * @return
	 */
	public boolean terminatedSuccessfully(){
		return this.exitCode==0;
	}
	
	/**
	 * Returns exit code of the command.
	 * @return
	 */
	public int getExitCode(){
		return this.exitCode;
	}
	
	public String getOutputsAsJSONString(){
		String buffer="";
		buffer+="{";
		buffer+="\t\"stdout\": \""+this.getStdout()+"\",";
		buffer+="\t\"stderr\": \""+this.getStderr()+"\"";
		buffer+="}";
		return buffer;
	}
	
	private Process initProcess(){
		try {
			ProcessBuilder builder = new ProcessBuilder(Arrays.asList(this.script.split("\\s")));
			Process p = builder.start();
			return p;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getStream(InputStream stream){
		StringBuilder builder=new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		try {
			for(int i=0;!reader.ready() && i<1000;i++);
			while(reader.ready()){
				builder.append(reader.readLine());
				if(reader.ready())
					builder.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	public static void main(String[] args) throws IOException {
		Command com = new Command();
		com.execute(args[0]);
		com.waitFor();
		System.out.println("STDOUT");
		System.out.println(com.getStdout());
		System.out.println("STDERR");
		System.out.println(com.getStderr());
	}
}
