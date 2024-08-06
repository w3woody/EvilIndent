package com.chaosinmotion.indent                                               ;
import java.io.IOException                                                     ;
import java.io.OutputStream                                                    ;
import java.nio.charset.StandardCharsets                                       ;
public class CountWriter extends OutputStream                                  {
	private OutputStream out                                                   ;
	private int count                                                          ;
	private int tab                                                            ;
	public CountWriter(OutputStream out)                                       {
		this.out = out                                                         ;
		count = 0                                                              ;
		tab = 4                                                               ;}

	public int getTab()                                                        {
		return tab                                                            ;}

	public void setTab(int tab)                                                {
		this.tab = tab                                                        ;}

	@Override public void write(int b) throws IOException                      {
		if (b == '\t')                                                         {
			count += 4 - (count % tab)                                        ;}
		else if (b == '\n')                                                    {
			count = 0                                                         ;}
		else                                                                   {
			count++                                                           ;}
		out.write(b)                                                          ;}

	public int getCount()                                                      {
		return count                                                          ;}

	public void print(char c)                                                  {
		String str = String.valueOf(c)                                         ;
		print(str)                                                            ;}

	public void print(String str)                                              {
		byte[] bytes = str.getBytes(StandardCharsets.UTF_8)                    ;
		try                                                                    {
			write(bytes,0,bytes.length)                                       ;}
		catch (IOException e)                                                {}}

	@Override public void close() throws IOException                           {
		super.close()                                                          ;
		out.close()                                                           ;}

	@Override public void flush() throws IOException                           {
		super.flush()                                                          ;
		out.flush()                                                          ;}}
