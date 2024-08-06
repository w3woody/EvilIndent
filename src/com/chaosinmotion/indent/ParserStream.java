package com.chaosinmotion.indent                                               ;
import java.io.IOException                                                     ;
import java.io.InputStream                                                     ;
public class ParserStream                                                      {
	private InputStream in                                                     ;
	private byte[] pushstack                                                   ;
	private byte pushback                                                      ;
	private Token pushToken                                                    ;
	private static final int TOKEN_TOKEN = 0x110000                            ;
	private static final int TOKEN_NUMBER = 0x110001                           ;
	private static final int TOKEN_STRING = 0x110002                           ;
	private static final int TOKEN_CHAR = 0x110003                             ;
	public class Token                                                         {
		final int tokenID                                                      ;
		final String token                                                     ;
		public Token(int tokenID, String token)                                {
			this.token = token                                                 ;
			this.tokenID = tokenID                                           ;}}

	public ParserStream(InputStream in)                                        {
		this.in = in                                                           ;
		pushback = 0                                                           ;
		pushstack = new byte[8]                                               ;}

	private int readl1() throws IOException                                    {
		if (pushback > 0)                                                      {
			return 0xFF & pushstack[--pushback]                               ;}
		else                                                                   {
			return in.read()                                                 ;}}

	private void push(int ch)                                                  {
		if (ch != -1)                                                          {
			pushstack[pushback++] = (byte) ch                                ;}}

	private int readl2() throws IOException                                    {
		int r = readl1()                                                       ;
		for (;;)                                                               {
			if (r == '\\')                                                     {
				int s = readl1()                                               ;
				if (s != '\n')                                                 {
					push(s)                                                    ;
					return r                                                 ;}}
			else                                                               {
				return r                                                    ;}}}

	private int readchar() throws IOException                                  {
		int c = readl2()                                                       ;
		if (c == '/')                                                          {
			int d = readl2()                                                   ;
			if (d == '/')                                                      {
				for (;;)                                                       {
					int e = readl2()                                           ;
					if ((e == -1) || (e == '\n')) return e                   ;}}
			else if (d == '*')                                                 {
				boolean flag = false                                           ;
				for (;;)                                                       {
					c = readl2()                                               ;
					if (c == '*')                                              {
						flag = true                                           ;}
					else if (flag && (c == '/'))                               {
						return ' '                                            ;}
					else                                                       {
						flag = false                                        ;}}}
			else                                                               {
				push(d)                                                      ;}}
		return c                                                              ;}

	private static boolean isspace(int ch)                                     {
		return (ch == ' ') || (ch == '\t') || (ch == '\n') || (ch == '\r')    ;}

	private static boolean isdigit(int ch)                                     {
		return (ch >= '0') && (ch <= '9')                                     ;}

	private static boolean istokenstart(int ch)                                {
		return (ch == '_') || ((ch >= 'A') && (ch <= 'Z')) || ((ch >= 'a') && (ch <= 'z')) ;}

	private static boolean istokenbody(int ch)                                 {
		return isdigit(ch) || istokenstart(ch)                                ;}

	public void push(Token t)                                                  {
		pushToken = t                                                         ;}

	public Token read() throws IOException                                     {
		if (pushToken != null)                                                 {
			Token tmp = pushToken                                              ;
			pushToken = null                                                   ;
			return tmp                                                        ;}
		int c, d                                                               ;
		StringBuilder buffer = new StringBuilder()                             ;
		c = readchar()                                                         ;
		if (c == -1) return null                                               ;
		if (isspace(c))                                                        {
			buffer.append((char) c)                                            ;
			while (isspace(c = readchar()))                                    {
				buffer.append((char) c)                                       ;}
			push(c)                                                            ;
			return new Token(' ', buffer.toString())                          ;}
		if (istokenstart(c))                                                   {
			while (istokenbody(c))                                             {
				buffer.append((char) c)                                        ;
				c = readchar()                                                ;}
			push(c)                                                            ;
			String ret = buffer.toString()                                     ;
			return new Token(TOKEN_TOKEN, ret)                                ;}
		if (c == '.')                                                          {
			d = readchar()                                                     ;
			if (!isdigit(d))                                                   {
				push(d)                                                        ;
				return new Token('.',".")                                    ;}}
		if (isdigit(c) || (c == '.'))                                          {
			buffer.append((char) c)                                            ;
			if (c == '.')                                                      {
				buffer.append((char) readchar())                              ;}
			boolean expflag = false                                            ;
			for (;;)                                                           {
				c = readchar()                                                 ;
				if (istokenbody(c) || (c == '.'))                              {
					buffer.append((char) c)                                    ;
					expflag = ((c == 'e') || (c == 'E') || (c == 'p') || (c == 'P')) ;}
				else if (expflag && ((c == '-') || (c == '+')))                {
					buffer.append((char) c)                                    ;
					expflag = false                                           ;}
				else                                                           {
					push(c)                                                    ;
					break                                                    ;}}
			return new Token(TOKEN_NUMBER,buffer.toString())                  ;}
		if ((c == '"') || (c == '\''))                                         {
			buffer.append((char)c)                                             ;
			for (;;)                                                           {
				d = readchar()                                                 ;
				if (d == -1) break                                             ;
				buffer.append((char) d)                                        ;
				if (d == c) break                                              ;
				if (d == '\\')                                                 {
					d = readchar()                                             ;
					if (d != -1) buffer.append((char)d)                      ;}}
			return new Token((c == '"') ? TOKEN_STRING : TOKEN_CHAR, buffer.toString()) ;}
		buffer.append((char)c)                                                 ;
		return new Token(c,buffer.toString())                                ;}}
