package com.revature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revature.controller.UserController;
import com.revature.controller.UserControllerImpl;
import com.revature.factory1.BeanFactory;
import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;

public class Driver {
	public  static void main(String[] args) {
		
		ObjectMapper jackson = new ObjectMapper();
		jackson.registerModule(new JavaTimeModule());
		jackson.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		JavalinJackson.configure(jackson);
		Javalin app = Javalin.create().start(8081);
		
		UserController uc = (UserController) BeanFactory.getFactory1().get(UserController.class, UserControllerImpl.class);
		

		app.get("/", (ctx)->ctx.html("Bank of US"));
		app.post("/users", uc::login);	
		app.put("/users/:username", uc::register);		
		app.delete("/users", uc::logout);		
		app.post("/users/:username/", uc::submit);		
		app.get("/users/:username/", uc::status);
	

	}
}
