import com.chaosinmotion.indent.FormatStream                                   ;
import com.chaosinmotion.indent.ParserStream                                   ;
import java.io.FileInputStream                                                 ;
import java.io.OutputStream                                                    ;
import java.util.Map                                                           ;
public class Main                                                              {
	public static void main(String[] args)                                     {
		try                                                                    {
			Map<String, String> env = System.getenv()                          ;
			String path = env.get("PWD")                                       ;
			FileInputStream fis = new FileInputStream(path + "/src/Main.java") ;
			OutputStream os = System.out                                       ;
			ParserStream ps = new ParserStream(fis)                            ;
			FormatStream fs = new FormatStream(ps,os)                          ;
			fs.format()                                                       ;}
		catch (Throwable th)                                                   {
			th.printStackTrace()                                            ;}}}
