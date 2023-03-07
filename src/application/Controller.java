package application;

import framework.annotation.Autowired;
import framework.annotation.GetMapping;
import framework.annotation.RequestMapping;
import framework.annotation.RestController;

@RestController
@RequestMapping(path = "/test")
public class Controller {

    @GetMapping(path = "/")
    public Message response() {
        Message message = new Message();
        message.setMessage("test1");
        return message;
    }

    @GetMapping(path = "/")
    public Message response2() {
        Message message = new Message();
        message.setMessage("test2");
        return message;
    }

    @GetMapping(path = "/")
    public Message response3() {
        Message message = new Message();
        message.setMessage("test3");
        return message;
    }
}
