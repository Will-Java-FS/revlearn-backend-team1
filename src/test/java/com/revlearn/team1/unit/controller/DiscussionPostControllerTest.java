//package com.revlearn.team1.unit.controller;
//
//import com.revlearn.team1.controller.DiscussionPostController;
//import com.revlearn.team1.dto.DiscussionPostDTO;
//import com.revlearn.team1.model.Course;
//import com.revlearn.team1.model.User;
//import com.revlearn.team1.service.DiscussionPostService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.time.LocalDateTime;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//class DiscussionPostControllerTest {
//
//    @Mock
//    private DiscussionPostService disServ;
//
//    @InjectMocks
//    private DiscussionPostController disCon;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    private MockMvc mockMvc;
//
//    @Test
//    void testGetDiscussionById() throws Exception{
//        mockMvc = MockMvcBuilders.standaloneSetup(disCon).build();
//        DiscussionPostDTO disDto = new DiscussionPostDTO(
//                10L,
//                new Course(),
//                new User(),
//                "content",
//                LocalDateTime.of(2024,5,1,13,30),
//                LocalDateTime.of(2024,5,1,13,30)
//        );
//
//        when(disServ.getDiscussionById(10L)).thenReturn(disDto);
//
////        ResponseEntity<DiscussionPostDTO> disDtoResult = disCon.getDiscussionById(10L);
//
////        assertEquals(HttpStatus.OK, disDtoResult.getStatusCode());
////        assertEquals(disDto, disDtoResult.getBody());
////        verify(disServ).getDiscussionById(10L);
//
//        mockMvc.perform(get("/api/v1/discussion/{id}", "10")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.discussionId").value(disDto.discussionId()));
//    }
//
//    @Test
//    void testPostDiscussionPost(){
//        DiscussionPostDTO disDto = new DiscussionPostDTO(
//                10L,
//                new Course(),
//                new User(),
//                "content",
//                LocalDateTime.of(2024,5,1,13,30),
//                LocalDateTime.of(2024,5,1,13,30)
//        );
//
//        when(disServ.postDiscussionPost(any(DiscussionPostDTO.class))).thenReturn(disDto);
//
//        ResponseEntity<DiscussionPostDTO> disDtoResult = disCon.postDiscussionPost(disDto);
//
//        assertEquals(HttpStatus.CREATED, disDtoResult.getStatusCode());
//        assertEquals(disDto, disDtoResult.getBody());
//        verify(disServ, times(1)).postDiscussionPost(disDto);
//    }
//
//    @Test
//    void testUpdateDiscussionPost(){
//        DiscussionPostDTO disDto = new DiscussionPostDTO(
//                10L,
//                new Course(),
//                new User(),
//                "content",
//                LocalDateTime.of(2024,5,1,13,30),
//                LocalDateTime.of(2024,5,1,13,30)
//        );
//
//        when(disServ.updateDiscussionPost(eq(10L), any(DiscussionPostDTO.class))).thenReturn(disDto);
//
//        ResponseEntity<DiscussionPostDTO> disDtoResult = disCon.updateDiscussionPost(10L, disDto);
//
//        assertEquals(HttpStatus.OK, disDtoResult.getStatusCode());
//        assertEquals(disDto, disDtoResult.getBody());
//        verify(disServ, times(1)).updateDiscussionPost(10L, disDto);
//    }
//
//    @Test
//    void testDeleteDiscussionById(){
//        when(disServ.deleteDiscussionById(eq(10L))).thenReturn("Discussion post with id: 10 deleted");
//
//        ResponseEntity<String> deletedDis = disCon.deleteDiscussionById(10L);
//
//        assertEquals(HttpStatus.OK, deletedDis.getStatusCode());
//        assertEquals("Discussion post with id: 10 deleted", deletedDis.getBody());
//        verify(disServ, times(1)).deleteDiscussionById(10L);
//    }
//}