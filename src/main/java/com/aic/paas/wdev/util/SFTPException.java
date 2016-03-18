package com.aic.paas.wdev.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SFTPException extends RuntimeException implements Serializable {
	private static Log log = LogFactory.getLog(SFTPException.class);

    private static final long serialVersionUID = 1L;

    private String errorCode;

    private String errorMessage;
 
    private Exception errOri;
    
    public SFTPException(String message) {
        this.errorMessage = message;
    }

    public SFTPException(Exception oriEx) {
        super(oriEx);
        this.errOri = oriEx;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(os);
        this.errOri.printStackTrace(p);
        this.writeChcException();
    }
    
    public SFTPException(String message, Exception oriEx) {
        super(message, oriEx);
        this.errorMessage = message;
        this.errOri = oriEx;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(os);
        this.errOri.printStackTrace(p);
        this.writeChcException();
    }
    
    private void writeChcException() { 
        log.error("系统异常编码:" + this.errorCode);
        log.error("系统异常信息:" + this.errorMessage); 
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PrintStream p = new PrintStream(os);
        this.errOri.printStackTrace(p);
        log.error(os.toString());
    }
}
