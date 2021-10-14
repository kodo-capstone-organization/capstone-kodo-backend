package com.spring.kodo.controller;

import com.spring.kodo.entity.Account;
import com.spring.kodo.restentity.request.CreateNewAccountReq;
import com.spring.kodo.restentity.request.UpdateAccountPasswordReq;
import com.spring.kodo.restentity.request.UpdateAccountReq;
import com.spring.kodo.restentity.response.AccountResp;
import com.spring.kodo.service.inter.AccountService;
import com.spring.kodo.service.inter.FileService;
import com.spring.kodo.util.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @GetMapping("/getAllAccountsWithoutEnrollment")
    public List<AccountResp> getAllAccountsWithoutEnrollment()
    {
        List<Account> accounts = this.accountService.getAllAccounts();
        List<AccountResp> accountResps = new ArrayList<>();

        for (Account account : accounts)
        {
            accountResps.add(new AccountResp(
                    account.getAccountId(),
                    account.getName(),
                    account.getUsername(),
                    account.getEmail(),
                    account.getIsAdmin(),
                    account.getDisplayPictureUrl()
            ));
        }

        return accountResps;
    }

    @GetMapping("/getAllTutors")
    public List<AccountResp> getAllTutors()
    {
        List<Account> accounts = this.accountService.getAllAccounts();
        List<AccountResp> accountResps = new ArrayList<>();

        for (Account account : accounts)
        {
            if(!(account.getCourses().isEmpty()))
            {
                accountResps.add(new AccountResp(
                        account.getAccountId(),
                        account.getName(),
                        account.getUsername(),
                        account.getEmail(),
                        account.getIsAdmin(),
                        account.getDisplayPictureUrl()
                ));
            }
        }
        return accountResps;
    }

    @PostMapping("/getSomeAccountsWithoutEnrollment")
    public List<AccountResp> getSomeAccountsWithoutEnrollment(@RequestPart(required = true, name = "accountIds") List<Long> accountIds)
    {
        try
        {
            List<Account> accounts = this.accountService.getSomeAccountsBySomeAccountIds(accountIds);
            List<AccountResp> accountResps = new ArrayList<>();

            for (Account account : accounts)
            {
                accountResps.add(new AccountResp(
                        account.getAccountId(),
                        account.getName(),
                        account.getUsername(),
                        account.getEmail(),
                        account.getIsAdmin(),
                        account.getDisplayPictureUrl()
                ));
            }

            return accountResps;
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
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

    @GetMapping("/getAccountByCourseId/{courseId}")
    public Account getAccountByCourseId(@PathVariable Long courseId)
    {
        try
        {
            Account account = this.accountService.getAccountByCourseId(courseId);

            account.setEnrolledCourses(null);
            account.setCourses(null);
            account.setInterests(null);

            return account;
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAccountByEnrolledCourseId/{enrolledCourseId}")
    public Account getAccountByEnrolledCourseId(@PathVariable Long enrolledCourseId)
    {
        try
        {
            return this.accountService.getAccountByEnrolledCourseId(enrolledCourseId);
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAccountByEnrolledLessonId/{enrolledLessonId}")
    public Account getAccountByEnrolledLessonId(@PathVariable Long enrolledLessonId)
    {
        try
        {
            return this.accountService.getAccountByEnrolledLessonId(enrolledLessonId);
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @GetMapping("/getAccountByQuizId/{quizId}")
    public Account getAccountByQuizId(@PathVariable Long quizId)
    {
        try
        {
            return this.accountService.getAccountByQuizId(quizId);
        }
        catch (AccountNotFoundException ex)
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @PostMapping("/createNewAccount")
    public Account createNewAccount(
            @RequestPart(name = "account", required = true) CreateNewAccountReq createNewAccountReq,
            @RequestPart(name = "displayPicture", required = false) MultipartFile displayPicture
    )
    {
        if (createNewAccountReq != null)
        {
            logger.info("HIT account/createNewAccount | POST | Received : " + createNewAccountReq);
            try
            {
                Account newAccount = new Account(createNewAccountReq.getUsername(), createNewAccountReq.getPassword(), createNewAccountReq.getName(), createNewAccountReq.getBio(), createNewAccountReq.getEmail(), createNewAccountReq.getIsAdmin());

                if (displayPicture == null)
                {
                    newAccount = this.accountService.createNewAccount(newAccount, createNewAccountReq.getTagTitles());
                }
                else
                {
                    newAccount = this.accountService.createNewAccount(newAccount, createNewAccountReq.getTagTitles(), displayPicture);
                }

                return newAccount;
            }
            catch (CreateNewAccountException | InputDataValidationException | AccountUsernameExistException | AccountEmailExistException | TagNameExistsException | FileUploadToGCSException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
            catch (AccountNotFoundException | TagNotFoundException | UpdateAccountException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (UnknownPersistenceException ex)
            {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Create New Account Request");
        }
    }

    @PutMapping("/updateAccount")
    public Account updateAccount(
            @RequestPart(name = "account", required = true) UpdateAccountReq updateAccountReq,
            @RequestPart(name = "displayPicture", required = false) MultipartFile updatedDisplayPicture
    )
    {
        if (updateAccountReq != null)
        {
            try
            {
                Account updatedAccount = accountService.updateAccount(
                        updateAccountReq.getAccount(),
                        updateAccountReq.getTagTitles(),
                        updateAccountReq.getEnrolledCourseIds(),
                        updateAccountReq.getCourseIds(),
                        updateAccountReq.getForumThreadIds(),
                        updateAccountReq.getForumPostIds(),
                        updateAccountReq.getStudentAttemptIds()
                );

                if (updatedDisplayPicture != null)
                {
                    // Delete existing file in cloud if exists
                    String currentDisplayPictureFilename = updatedAccount.getDisplayPictureFilename();
                    if (currentDisplayPictureFilename != "")
                    {
                        Boolean isDeleted = fileService.delete(currentDisplayPictureFilename);
                        if (!isDeleted)
                        {
                            System.err.println("Unable to delete previous display picture: " + currentDisplayPictureFilename + ". Proceeding to overwrite with new picture");
                        }
                    }

                    // Upload new file
                    String updatedDisplayPictureURL = fileService.upload(updatedDisplayPicture);
                    updatedAccount.setDisplayPictureUrl(updatedDisplayPictureURL);
                    updatedAccount = accountService.updateAccount(updatedAccount);
                }

                return updatedAccount;
            }
            catch (AccountNotFoundException | TagNotFoundException | UpdateAccountException | EnrolledCourseNotFoundException | StudentAttemptNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (UnknownPersistenceException | AccountEmailExistException | InputDataValidationException | TagNameExistsException | FileUploadToGCSException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Update Account Request");
        }
    }

    @PutMapping("/updateAccountPassword")
    Account updateAccountPassword(@RequestPart(name = "updateAccountPasswordReq", required = true) UpdateAccountPasswordReq updateAccountPasswordReq)
    {
        if (updateAccountPasswordReq != null)
        {
            try
            {
                Long accountId = updateAccountPasswordReq.getAccountId();
                String username = updateAccountPasswordReq.getUsername();
                String oldPassword = updateAccountPasswordReq.getOldPassword();
                String newPassword = updateAccountPasswordReq.getNewPassword();

                Account account = accountService.updateAccountPassword(accountId, username, oldPassword, newPassword);

                return account;
            }
            catch (AccountNotFoundException ex)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
            }
            catch (UpdateAccountException | InputDataValidationException ex)
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
            }
        }
        else
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Update Account Password Request");
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

    @DeleteMapping("/reactivateAccount/{reactivatingAccountId}&{requestingAccountId}")
    public ResponseEntity reactivateAccount(@PathVariable Long reactivatingAccountId, @PathVariable Long requestingAccountId)
    {
        try
        {
            Long reactivatedAccountId = this.accountService.reactivateAccount(reactivatingAccountId, requestingAccountId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully reactivated account with Account ID: " + reactivatedAccountId);
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

    @GetMapping("/downgradeAccount/{accountId}")
    public Account downgradeAdmin(@PathVariable Long accountId) {
        try {
            Account updatedAccount = this.accountService.downgradeAdminStatus(accountId);
            return updatedAccount;
        } catch(AccountNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
    }

    @GetMapping("/upgradeAccount/{accountId}")
    public Account upgradeAdmin(@PathVariable Long accountId) {
        try {
            Account updatedAccount = this.accountService.upgradeAdminStatus(accountId);
            return updatedAccount;
        } catch(AccountNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
    }

    @PostMapping("/login")
    public Account login(@RequestPart(name = "username", required = true) String username, @RequestPart(name = "password", required = true) String password)
    {
        try
        {
            Account accountLoggedIn = this.accountService.userLogin(username, password);

            accountLoggedIn.getInterests().clear();
            accountLoggedIn.getEnrolledCourses().clear();
            accountLoggedIn.getCourses().clear();

            return accountLoggedIn;
        }
        catch (InvalidLoginCredentialsException ex)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
    }

    @PostMapping("/adminLogin")
    public ResponseEntity adminLogin(@RequestPart(name = "username", required = true) String username, @RequestPart(name = "password", required = true) String password)
    {
        try
        {
            Account accountLoggedIn = this.accountService.adminLogin(username, password);
            return ResponseEntity.status(HttpStatus.OK).body(accountLoggedIn.getAccountId());
        }
        catch (InvalidLoginCredentialsException ex)
        {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ex.getMessage());
        }
    }

}
