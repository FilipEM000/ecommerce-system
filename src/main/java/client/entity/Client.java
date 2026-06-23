package client.entity;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Client {
    @EqualsAndHashCode.Include
    @Setter
    private Long id;
    private String name;
    private String email;
    private Cart cart;

    public Client(String name, String email) {
        this.name = name;
        this.email = email;
        this.cart = Cart.initialize();
    }
}
