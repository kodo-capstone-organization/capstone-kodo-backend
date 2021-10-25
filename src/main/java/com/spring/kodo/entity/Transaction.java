package com.spring.kodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.spring.kodo.util.helper.Constants;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity(name="transaction")
@Table(name="transaction", uniqueConstraints = @UniqueConstraint(columnNames = {"transactionId", "stripeTransactionId"}))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Transaction
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime dateTimeOfTransaction;

    @Column(nullable = false, unique = true, length = 128)
    @NotNull
    @Size(min = 0, max = 128)
    private String stripeTransactionId;

    @Column(nullable = false, precision = 11, scale = 2)
    @DecimalMin("0.00")
    @NotNull
    @Digits(integer = 9, fraction = 2)
    private BigDecimal stripeFee;

    @Column(nullable = false, precision = 11, scale = 2)
    @DecimalMin("0.00")
    @NotNull
    @Digits(integer = 9, fraction = 2)
    private BigDecimal platformFee;

    @Column(nullable = false, precision = 11, scale = 2)
    @DecimalMin("0.00")
    @NotNull
    @Digits(integer = 9, fraction = 2)
    private BigDecimal coursePrice;

    @Column(nullable = false, precision = 11, scale = 2)
    @DecimalMin("0.00")
    @NotNull
    @Digits(integer = 9, fraction = 2)
    private BigDecimal tutorPayout;

    @ManyToOne(optional = false, targetEntity = Account.class, fetch = FetchType.LAZY)
    private Account payer;

    @ManyToOne(optional = false, targetEntity = Account.class, fetch = FetchType.LAZY)
    private Account payee;

    @ManyToOne(optional = false, targetEntity = Course.class, fetch = FetchType.LAZY)
    private Course course;

    public Transaction()
    {
        this.dateTimeOfTransaction = LocalDateTime.now();
    }

    public Transaction(String stripeTransactionId, BigDecimal coursePrice)
    {
        this();

        this.stripeTransactionId = stripeTransactionId;
        this.coursePrice = coursePrice;

        // Total application fee consisting of Kodo platform fee and Stripe fees
        BigDecimal totalApplicationFee = Constants.PLATFORM_FEE_PERCENTAGE.multiply(this.coursePrice).setScale(2, RoundingMode.DOWN);

        this.tutorPayout = this.coursePrice.subtract(totalApplicationFee).setScale(2, RoundingMode.DOWN);
        this.stripeFee = Constants.STRIPE_FEE_PERCENTAGE.multiply(this.coursePrice).add(Constants.STRIPE_FEE_ACCOUNT_BASE).setScale(2, RoundingMode.DOWN);

        // Net platform fee receivable by Kodo after paying for Stripe fees
        this.platformFee = totalApplicationFee.subtract(this.stripeFee);
    }

    public Long getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(Long transactionId)
    {
        this.transactionId = transactionId;
    }

    public LocalDateTime getDateTimeOfTransaction()
    {
        return dateTimeOfTransaction;
    }

    public void setDateTimeOfTransaction(LocalDateTime dateTimeOfTransaction)
    {
        this.dateTimeOfTransaction = dateTimeOfTransaction;
    }

    public String getStripeTransactionId()
    {
        return stripeTransactionId;
    }

    public void setStripeTransactionId(String stripeTransactionId)
    {
        this.stripeTransactionId = stripeTransactionId;
    }

    public BigDecimal getStripeFee()
    {
        return stripeFee;
    }

    public void setStripeFee(BigDecimal stripeFee)
    {
        this.stripeFee = stripeFee;
    }

    public BigDecimal getPlatformFee()
    {
        return platformFee;
    }

    public void setPlatformFee(BigDecimal platformFee)
    {
        this.platformFee = platformFee;
    }

    public BigDecimal getCoursePrice()
    {
        return coursePrice;
    }

    public void setCoursePrice(BigDecimal coursePrice)
    {
        this.coursePrice = coursePrice;
    }

    public BigDecimal getTutorPayout()
    {
        return tutorPayout;
    }

    public void setTutorPayout(BigDecimal tutorPayout)
    {
        this.tutorPayout = tutorPayout;
    }

    public Account getPayer()
    {
        return payer;
    }

    public void setPayer(Account payer)
    {
        this.payer = payer;
    }

    public Account getPayee()
    {
        return payee;
    }

    public void setPayee(Account payee)
    {
        this.payee = payee;
    }

    public Course getCourse()
    {
        return course;
    }

    public void setCourse(Course course)
    {
        this.course = course;
    }

    @Override
    public String toString()
    {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", dateTimeOfTransaction=" + dateTimeOfTransaction +
                ", stripeTransactionId='" + stripeTransactionId + '\'' +
                ", stripeFee=" + stripeFee +
                ", platformFee=" + platformFee +
                ", coursePrice=" + coursePrice +
                ", tutorPayout=" + tutorPayout +
                ", payer=" + payer +
                ", payee=" + payee +
                ", course=" + course +
                '}';
    }
}
