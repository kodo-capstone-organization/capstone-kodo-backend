package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.restentity.CreateNewAccountReq;
import com.spring.kodo.restentity.UpdateAccountReq;
import com.spring.kodo.service.FileService;
import com.spring.kodo.util.exception.*;
import com.spring.kodo.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/account")
public class AccountController
{
    Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private FileService fileService;

    @GetMapping("/getAllAccounts")
    public List<Account> getAllAccounts()
    {
        return this.accountService.getAllAccounts();
    }

    @GetMapping("/getAccountByAccountId/{accountId}")
    public Account getAccountByAccountId(@PathVariable Long accountId)
    {
        try
        {
           return this.accountService.getAccountByAccountId(accountId);
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/createNewAccount")
    public Account createNewAccount(@RequestPart(name="account", required = true) CreateNewAccountReq createNewAccountReq,
                                    @RequestPart(name="displayPicture", required = false) MultipartFile displayPicture)
    {
        if (createNewAccountReq != null)
        {
            logger.info("HIT account/createNewAccount | POST | Received : " + createNewAccountReq);
            try
            {
                Account newAccount = new Account(createNewAccountReq.getUsername(), createNewAccountReq.getPassword(), createNewAccountReq.getName(), createNewAccountReq.getBio(), createNewAccountReq.getEmail(), "", createNewAccountReq.getIsAdmin());
                newAccount = this.accountService.createNewAccount(newAccount, createNewAccountReq.getTagTitles());

                if (displayPicture != null)
                {
                    String displayPictureURL = fileService.upload(displayPicture);
                    newAccount.setDisplayPictureUrl(displayPictureURL);
                    newAccount = accountService.updateAccount(newAccount, null, null, null, null,null,null);
                }

                return newAccount;
            }
            catch (InputDataValidationException | AccountUsernameOrEmailExistsException | FileUploadToGCSException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            catch (AccountNotFoundException | TagNotFoundException | UpdateAccountException | EnrolledCourseNotFoundException | CourseNotFoundException | StudentAttemptQuestionNotFoundException | StudentAttemptNotFoundException | ForumThreadNotFoundException | ForumPostNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (UnknownPersistenceException ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Account Request");
        }
    }

    @PutMapping("/updateAccount")
    public Account updateAccount(@RequestPart(name="account", required = true) UpdateAccountReq updateAccountReq,
                                 @RequestPart(name="displayPicture", required = false) MultipartFile updatedDisplayPicture)
    {
        if (updateAccountReq != null)
        {
            try
            {
                Account updatedAccount = this.accountService.updateAccount(
                        updateAccountReq.getAccount(),
                        updateAccountReq.getTagTitles(),
                        updateAccountReq.getEnrolledCourseIds(),
                        updateAccountReq.getCourseIds(),
                        updateAccountReq.getForumThreadIds(),
                        updateAccountReq.getForumPostIds(),
                        updateAccountReq.getStudentAttemptIds());

                if (updatedDisplayPicture != null)
                {
                    // Delete existing file in cloud
                    String currentDisplayPictureFilename = updatedAccount.getDisplayPictureFilename();
                    Boolean isDeleted = fileService.delete(currentDisplayPictureFilename);

                    if (isDeleted)
                    {
                        // Upload new file
                        String updatedDisplayPictureURL = fileService.upload(updatedDisplayPicture);
                        updatedAccount.setDisplayPictureUrl(updatedDisplayPictureURL);
                        updatedAccount = accountService.updateAccount(updatedAccount, null, null, null, null, null,null);
                    }
                    else
                    {
                        throw new UpdateAccountException("Unable to replace display picture");
                    }
                }

                return updatedAccount;
            }
            catch (AccountNotFoundException | TagNotFoundException | UpdateAccountException | EnrolledCourseNotFoundException | CourseNotFoundException | StudentAttemptQuestionNotFoundException | StudentAttemptNotFoundException | ForumThreadNotFoundException | ForumPostNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (InputDataValidationException | FileUploadToGCSException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Update Account Request");
        }
    }

    @DeleteMapping("/deactivateAccount/{deactivatingAccountId}&{requestingAccountId}")
    public ResponseEntity deactivateAccount(@PathVariable Long deactivatingAccountId, @PathVariable Long requestingAccountId)
    {
        try
        {
            Long deactivatedAccountId = this.accountService.deactivateAccount(deactivatingAccountId, requestingAccountId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deactivated account with Account ID: " + deactivatedAccountId);
        }
        catch (AccountPermissionDeniedException ex)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestPart (name="username", required=true) String username, @RequestPart (name="password", required=true) String password)
    {
        System.out.println(username);
        System.out.println(password);
        try
        {
            Account accountLoggedIn = this.accountService.login(username, password);
            return ResponseEntity.status(HttpStatus.OK).body(accountLoggedIn.getAccountId());
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
        catch (InvalidLoginCredentialsException ex)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
    }
}
