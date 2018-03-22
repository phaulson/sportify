/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 *
 * @author Pauli
 */
@WebService (serviceName = "test")
public class Test {
    @WebMethod (operationName = "say")
    public String sayByeOrHi(boolean bye) {
        return bye ? "Bye" : "Hai";
    }
    @WebMethod (operationName = "Hello World")
    public String helloWorld(){
        return "SCUUUUURR";
    }
}
