package com.HolidayTracker.fullstackbackend.controller;
import com.HolidayTracker.fullstackbackend.controller.UserController;
import com.HolidayTracker.fullstackbackend.model.User;
import com.HolidayTracker.fullstackbackend.repository.user.UserDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



/**
 * JUnit tutorial: https://www.vogella.com/tutorials/JUnit/article.html
 * Mockito tutorial: https://www.vogella.com/tutorials/Mockito/article.html
 * WebMvcTest (for mocking dao in controller): https://spring.io/guides/gs/testing-web
 */
/**
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String URL_RETRIEVE_USER_BY_ID = "/RetrieveUserbyID/%d";
    private static final String URL_RETRIEVE_ALL_USERS  = "/RetrieveAllUsers";
    private static final String URL_CREATE_NEW_USER  = "/CreateNewUser";
    private static final String URL_UPDATE_USER  = "/UpdateUser/%d";
    private static final String URL_DELETE_USER  = "/DeleteUser/%d";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDao mockUserDao;

    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    private User mockUser1;
    private User mockUser2;

    //This method is run before each test
    @BeforeEach
    void setUp() throws SQLException {
        //Create mock users
        mockUser1 = new User(7,
                "{sampleKey: \"sampleValue1\"}",
                "sample1.email@gmail.com",
                21,
                1,
                1);
        mockUser2 = new User(8,
                "{sampleKey: \"sampleValue2\"}",
                "sample2.email@gmail.com",
                23,
                3,
                2);
        //Set up the mock dao to return the mock users when their userId is requested
        Mockito.when(mockUserDao.get(7)).thenReturn(mockUser1);
        Mockito.when(mockUserDao.get(8)).thenReturn(mockUser2);
        //Set up the mock dao to return all users when getAll are requested
        List<User> allUsers = new ArrayList<>();
        allUsers.add(mockUser1);
        allUsers.add(mockUser2);
        Mockito.when(mockUserDao.getAll()).thenReturn(allUsers);
        //Set up the mock dao to insert a new user into the db when requested
        Mockito.when(mockUserDao.insert(Mockito.any(User.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                User inputUser = (User) args[0];
                int inputUserId = (int) inputUser.getUserID();
                if(checkIfEmailExists(inputUser.getEmail())){
                    //Since this email is already taken, do not insert this user
                    return 0;
                }else {
                    //Mock the db insert: so when the user requests this users id, return this user
                    Mockito.when(mockUserDao.get(inputUserId)).thenReturn(inputUser);
                    return 1;
                }
            }
        });
        //Set up the mock dao to update user when requested
        Mockito.when(mockUserDao.update(Mockito.any(User.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                User inputUser = (User) args[0];
                int inputUserId = (int) inputUser.getUserID();
                if(checkIfUserIdExists(inputUserId)){
                    //This user exists, so update
                    Mockito.when(mockUserDao.get(inputUserId)).thenReturn(inputUser);
                    return 1;
                }else {
                    //User does not exist, nothing to update
                    return 0;
                }
            }
        });
        //Set up the mock dao to delete user when requested
        Mockito.when(mockUserDao.delete(Mockito.anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                int inputUserId = (Integer) args[0];
                if(checkIfUserIdExists(inputUserId)){
                    //This user exists, so delete
                    Mockito.when(mockUserDao.get(inputUserId)).thenReturn(null);
                    return 1;
                }else {
                    //User does not exist, nothing to delete
                    return 0;
                }
            }
        });
    }

    private boolean checkIfEmailExists(String emailToCheck) throws SQLException {
        List<User> allUsers = mockUserDao.getAll();
        for(User u : allUsers){
            if(u.getEmail().equals(emailToCheck)){
                return true;
            }
        }
        return false;
    }

    private boolean checkIfUserIdExists(long userId) throws SQLException {
        List<User> allUsers = mockUserDao.getAll();
        for(User u : allUsers){
            if(u.getUserID() == userId){
                return true;
            }
        }
        return false;
    }

    @AfterEach
    void cleanUp(){
        //Reset all the mocked functionality after each test, to ensure no carry over messiness between tests
        Mockito.reset(mockUserDao);
    }

    @Test
    public void testRetrieveUserById_Success() throws Exception {
        String endpoint = String.format(URL_RETRIEVE_USER_BY_ID, mockUser1.getUserID());

        MvcResult response = mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andReturn();

        //get the json response content as a string from the endpoint response
        String jsonResponse = response.getResponse().getContentAsString();

        //convert the json response content into a User object
        User responseUser = jsonObjectMapper.readValue(jsonResponse, User.class);

        //assert the response user is the same as the mock user
        assertTrue(mockUser1.getUserID() == responseUser.getUserID());
        assertTrue(mockUser1.getData().equals(responseUser.getData()));
        assertTrue(mockUser1.getEmail().equals(responseUser.getEmail()));
        assertTrue(mockUser1.getHolidayEntitlement() == responseUser.getHolidayEntitlement());
        assertTrue(mockUser1.getDepartmentID() == responseUser.getDepartmentID());
        assertTrue(mockUser1.getRoleID() == responseUser.getRoleID());
    }

    @Test
    public void testRetrieveUserById_InvalidIdReturnsNull() throws Exception {
        int invalidId = 1234;
        String endpoint = String.format(URL_RETRIEVE_USER_BY_ID, invalidId);

        MvcResult response = mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andReturn();

        //get the json response content as a string from the endpoint response
        String jsonResponse = response.getResponse().getContentAsString();

        //assert the jsonResponse is empty
        assertTrue(jsonResponse.isEmpty());
    }

    @Test
    public void testRetrieveUserById_MalformedIdThrowsException() throws Exception {
        String malformedId = "x123";
        String endpoint = String.format("/RetrieveUserbyID/%s", malformedId);

        mockMvc.perform(get(endpoint))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRetrieveAllUsers_Success() throws Exception {
        MvcResult response = mockMvc.perform(get(URL_RETRIEVE_ALL_USERS))
                .andExpect(status().isOk())
                .andReturn();

        //get the json response content as a string from the endpoint response
        String jsonResponse = response.getResponse().getContentAsString();

        //convert the json response content into List<User>
        List<User> responseUsers = jsonObjectMapper.readValue(jsonResponse, new TypeReference<List<User>>(){});

        //assert the response user is the same as the mock user
        assertTrue(responseUsers.size() == 2);
    }

    @Test
    public void testCreateNewUser_Success() throws Exception {

        //Sample json response to create a new user
        String jsonForNewUser = """
                {
                   "holidayEntitlement": 16,
                   "departmentID": 4,
                   "roleID": 4,
                   "userID": 9,
                   "email": "sample3.email@gmail.com",
                   "data": "{sampleKey: \\"sampleValue3\\"}"
                 }""";

        //POST create new user and verify successful
        mockMvc.perform( MockMvcRequestBuilders
                        .post(URL_CREATE_NEW_USER)
                        .content(jsonForNewUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully created"));

        //GET retrieve new user by id (9)
        String endpoint = String.format(URL_RETRIEVE_USER_BY_ID, 9);

        //verify response is for the new user (9)
        MvcResult response = mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = response.getResponse().getContentAsString();
        User responseUser = jsonObjectMapper.readValue(jsonResponse, User.class);
        assertTrue(9 == responseUser.getUserID());
    }

    @Test
    public void testCreateNewUser_EmailAlreadyTakenFailure() throws Exception {

        //Sample json response to create a new user who has the same email as mockUser1
        String jsonForNewUser = String.format("""
                {
                   "holidayEntitlement": 16,
                   "departmentID": 4,
                   "roleID": 4,
                   "userID": 10,
                   "email": "%s",
                   "data": "{sampleKey: \\"sampleValue3\\"}"
                 }""", mockUser1.getEmail());

        //POST create new user and verify failure to create new user
        mockMvc.perform( MockMvcRequestBuilders
                        .post(URL_CREATE_NEW_USER)
                        .content(jsonForNewUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unable to create user"));

        //GET retrieve new user by id (10)
        String endpoint = String.format(URL_RETRIEVE_USER_BY_ID, 10);

        //verify response is empty as user was not created
        MvcResult response = mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = response.getResponse().getContentAsString();
        assertTrue(jsonResponse.isEmpty());
    }

    @Test
    public void testUpdateUser_Success() throws Exception {

        //UPDATE lets give mockUser1 a new email and data
        String newEmail = "my.new.email.address@gmail.com";
        String newData = "{sampleKey: \\\"sampleValue9\\\"}";

        String jsonForMockUser1WithNewEmail = String.format("""
                {
                   "holidayEntitlement": %d,
                   "departmentID": %d,
                   "roleID": %d,
                   "userID": %d,
                   "email": "%s",
                   "data": "%s"
                 }""",
                mockUser1.getHolidayEntitlement(),
                mockUser1.getDepartmentID(),
                mockUser1.getRoleID(),
                mockUser1.getUserID(),
                newEmail,
                newData);

        //UPDATE and verify response count is 1 updated rows
        mockMvc.perform( MockMvcRequestBuilders
                        .put(String.format(URL_UPDATE_USER, mockUser1.getUserID()))
                        .content(jsonForMockUser1WithNewEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        //GET retrieve mockUser1
        String endpoint = String.format(URL_RETRIEVE_USER_BY_ID, mockUser1.getUserID());
        //verify mockUser1 has new email and data
        MvcResult response = mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = response.getResponse().getContentAsString();
        User responseUser = jsonObjectMapper.readValue(jsonResponse, User.class);
        assertTrue(mockUser1.getUserID() == responseUser.getUserID());
        assertTrue(newEmail.equals(responseUser.getEmail()));
        assertTrue("{sampleKey: \"sampleValue9\"}".equals(responseUser.getData()));
    }



    @Test
    public void testUpdateUser_InvalidUserIdDoesNothing() throws Exception {

        //UPDATE lets update a user with an ID that does not exist (123)
        String jsonForUpdateUser = """
                {
                   "holidayEntitlement": 16,
                   "departmentID": 4,
                   "roleID": 4,
                   "userID": 123,
                   "email": "sample3.email@gmail.com",
                   "data": "{sampleKey: \\"sampleValue3\\"}"
                 }""";

        //UPDATE and verify response count is 0 updated rows
        mockMvc.perform(MockMvcRequestBuilders
                        .put(String.format(URL_UPDATE_USER, 123))
                        .content(jsonForUpdateUser)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    public void testDeleteUserById_Success() throws Exception {

        //Delete mockUser1
        String deleteEndpoint = String.format(URL_DELETE_USER, mockUser1.getUserID());

        //DELETE and verify response count is 1 deleted rows
        mockMvc.perform(delete(deleteEndpoint))
                .andExpect(status().isOk())
                .andExpect(content().string("User successfully deleted"));

        //GET retrieve mockUser1
        String getEndpoint = String.format(URL_RETRIEVE_USER_BY_ID, mockUser1.getUserID());

        //verify response is empty as user was deleted
        MvcResult response = mockMvc.perform(get(getEndpoint))
                .andExpect(status().isOk())
                .andReturn();
        String jsonResponse = response.getResponse().getContentAsString();
        assertTrue(jsonResponse.isEmpty());
    }

    @Test
    public void testDeleteUserById_NothingToDelete() throws Exception {

        //Delete invalid user id
        String deleteEndpoint = String.format(URL_DELETE_USER, 123);

        //DELETE and verify response count is 0 deleted rows
        mockMvc.perform(delete(deleteEndpoint))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unable to delete user"));
    }

}
 */