package app.clip.expenses.models;

import app.clip.commons.Money;
import app.clip.converters.MoneyAttributeConverter;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "crt_id")
    private Long creatorId;

    @Column(name = "pyr_id")
    private Long payerId;

    @Convert(converter = MoneyAttributeConverter.class)
    @Column(name = "tot_amt")
    private Money totalAmount;
    private String title;

    @Column(name = "dsc")
    private String description;

    @Column(name = "grp_id")
    private Long associatedGroupId;

    @Column(name = "crt_at")
    private Long createdAt;

    @Column(name = "upd_at")
    private Long updatedAt;

    public Expense() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public void setAssociatedGroupId(Long associatedGroupId) {
        this.associatedGroupId = associatedGroupId;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getAssociatedGroupId() {
        return associatedGroupId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Objects.equals(id, expense.id) && Objects.equals(creatorId, expense.creatorId) && Objects.equals(payerId, expense.payerId) && Objects.equals(totalAmount, expense.totalAmount) && Objects.equals(title, expense.title) && Objects.equals(description, expense.description) && Objects.equals(associatedGroupId, expense.associatedGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creatorId, payerId, totalAmount, title, description, associatedGroupId);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", creatorId='" + creatorId + '\'' +
                ", payerId='" + payerId + '\'' +
                ", totalAmount=" + totalAmount +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", associatedGroupId='" + associatedGroupId + '\'' +
                '}';
    }

    @PrePersist
    public void prePersist() {
        Long systemTime = System.currentTimeMillis();
        setCreatedAt(systemTime);
        setUpdatedAt(systemTime);
    }

    @PreUpdate
    public void preUpdate() {
        setUpdatedAt(System.currentTimeMillis());
    }
}
