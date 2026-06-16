package com.waxiest.spring_cli_caching_server.comamnds;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class HelloWorld {

    @ShellMethod(key = "greetings", value = "Will say hello to a name if provided")
    public String greetings(
            @ShellOption(defaultValue = "World") String arg1
    ){
    return "Hello " + arg1 + "!";
    }
}
