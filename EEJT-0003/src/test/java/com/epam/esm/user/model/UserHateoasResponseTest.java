package com.epam.esm.user.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
class UserHateoasResponseTest {

    @InjectMocks
    private UserHateoasResponse userHateoasResponse;
    @Mock
    private PagedResourcesAssembler<User> representationModelAssembler;

    @Captor
    ArgumentCaptor<Link> linkCaptor;

    @Test
    void getHateoasUserForReadingTest() {
        //given
        User user = User.builder().id(1L).name("USerPlug").orders(Set.of()).build();
        Page<User> users = new PageImpl<>(List.of(user));
        PagedModel<User> allUserModel = mock(PagedModel.class);

        when(allUserModel.add(ArgumentMatchers.<Link>any())).thenReturn(allUserModel);

        when(representationModelAssembler.toModel(eq(users), ArgumentMatchers.<RepresentationModelAssembler<User, User>>any())).thenReturn(allUserModel);

        //when
        PagedModel<User> actual = userHateoasResponse.getHateoasUserForReading(users);

        //then
        assertEquals(actual, allUserModel);
        verify(actual, times(1)).add(linkCaptor.capture());
        comparingLinks(linkCaptor.getAllValues().get(0).getRel().value());
    }

    @Test
    void getHateoasUserForReadingByIdTest() {
        //given
        User userObj = User.builder().id(1L).name("USerPlug").orders(Set.of()).build();
        User userExpected = User.builder().id(1L).name("USerPlug").orders(Set.of()).build();

        CollectionModel<User> expected = CollectionModel.of(List.of(userObj));

        //when
        CollectionModel<User> actual = userHateoasResponse.getHateoasUserForReadingById(userExpected);

        //then
        assertEquals(expected.getContent().stream().toList(), actual.getContent().stream().toList());
    }

    private void comparingLinks(String link1) {
        Assertions.assertEquals(link1, "get all orders");
    }
}