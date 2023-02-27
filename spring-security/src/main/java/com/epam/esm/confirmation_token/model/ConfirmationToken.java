package com.epam.esm.confirmation_token.model;

import com.epam.esm.user.model.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tokenId;

    private String confirmationToken;

    @OneToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private User user;

    @CreatedDate
    private Date createdDate;

    public boolean getStatus (){
        return new Date().before(new Date(createdDate.getTime() + (1000 * 60 * 60 * 24)));
    }
}
