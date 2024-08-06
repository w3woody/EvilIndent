# EvilIndent

What started as a terrible Facebook meme became an even worse formatting parser.

Because if you have to indent your code like this:

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
    ...

You probably hate people.

And yes, the source code for this parser is formatted this way.

