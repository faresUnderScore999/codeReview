package org.example.Model.Product.ClassProduct;

import org.example.Model.Product.EnumProduct.SubscriptionStatus;
import org.example.Model.Product.EnumProduct.SubscriptionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProductSubscription {

    // Attribute
    private int subscriptionId;
    private int client;
    private int product;
    private SubscriptionType type;
    private LocalDateTime subscriptionDate;
    private LocalDateTime expirationDate;
    private SubscriptionStatus status;

    //Constructors
    public ProductSubscription() {
        this.subscriptionDate = LocalDate.now().atStartOfDay();
        this.status = SubscriptionStatus.ACTIVE;
    }

    public ProductSubscription(int client, int product, SubscriptionType type) {
        this.client = client;
        this.product = product;
        this.type = type;
        this.subscriptionDate = LocalDateTime.now();
        switch (type) {
            case MONTHLY:
                this.expirationDate = subscriptionDate.plusMonths(1);
                break;

            case ANNUAL:
                this.expirationDate = subscriptionDate.plusYears(1);
                break;

            case TRANSACTION:
                this.expirationDate = subscriptionDate.plusDays(1);
                break;

            case ONE_TIME:
                this.expirationDate = subscriptionDate;
                break;

            default:
                throw new IllegalArgumentException("Unknown subscription type");
        }

        this.status = SubscriptionStatus.ACTIVE;
    }
    public ProductSubscription(int subscriptionId,int client, int product, SubscriptionType type) {
        this.subscriptionId = subscriptionId;
        this.client = client;
        this.product = product;
        this.type = type;
        this.subscriptionDate = LocalDateTime.now();
        switch (type) {
            case MONTHLY:
                this.expirationDate = subscriptionDate.plusMonths(1);
                break;

            case ANNUAL:
                this.expirationDate = subscriptionDate.plusYears(1);
                break;

            case TRANSACTION:
                this.expirationDate = subscriptionDate.plusDays(1);
                break;

            case ONE_TIME:
                this.expirationDate = subscriptionDate;
                break;

            default:
                throw new IllegalArgumentException("Unknown subscription type");
        }

        this.status = SubscriptionStatus.ACTIVE;
    }

    // ===== Getters & Setters =====


    public int getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public int getClient() {
        return client;
    }

    public void setClient(int client) {
        this.client = client;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public SubscriptionType getType() {
        return type;
    }

    public void setType(SubscriptionType type) {
        this.type = type;
    }

    public LocalDateTime getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(LocalDateTime subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    //Methods

    @Override
    public String toString() {
        return "ProductSubscription{" +
                "subscriptionId=" + subscriptionId +
                ", client=" + client +
                ", product=" + product +
                ", type=" + type +
                ", subscriptionDate=" + subscriptionDate +
                ", expirationDate=" + expirationDate +
                ", status=" + status +
                '}';
    }


}
