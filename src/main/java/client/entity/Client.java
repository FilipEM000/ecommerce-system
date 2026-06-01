package client.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter()
public class Client {
    private Long id;
    private String name;
    private String email;
    @Setter(AccessLevel.NONE)
    private Cart cart;

    public Client(String name, String email){
        this.name = name;
        this.email = email;
        this.cart = new Cart();
    }
}
